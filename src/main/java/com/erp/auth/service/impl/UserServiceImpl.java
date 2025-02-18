package com.erp.auth.service.impl;

import com.erp.auth.cache.CacheStore;
import com.erp.auth.domain.RequestContext;
import com.erp.auth.domain.User;
import com.erp.auth.entity.ConfirmationEntity;
import com.erp.auth.entity.CredentialEntity;
import com.erp.auth.entity.RoleEntity;
import com.erp.auth.entity.UserEntity;
import com.erp.auth.enumeration.Authority;
import com.erp.auth.enumeration.EventType;
import com.erp.auth.enumeration.LoginType;
import com.erp.auth.event.UserEvent;
import com.erp.auth.repository.ConfirmationRepository;
import com.erp.auth.repository.CredentialRepository;
import com.erp.auth.repository.RoleRepository;
import com.erp.auth.repository.UserRepository;
import com.erp.auth.service.UserService;
import com.erp.auth.utils.UserUtils;
import com.erp.auth.validation.UserValidation;
import com.erp.constants.Constants;
import com.erp.exception.ApiException;
import dev.samstevens.totp.code.CodeGenerator;
import dev.samstevens.totp.code.CodeVerifier;
import dev.samstevens.totp.code.DefaultCodeGenerator;
import dev.samstevens.totp.code.DefaultCodeVerifier;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CredentialRepository credentialRepository;
    private final ConfirmationRepository confirmationRepository;
    private final BCryptPasswordEncoder encoder;
    private final CacheStore<String, Integer> userCache;
    private final ApplicationEventPublisher publisher;

    @Override
    public void createUser(String firstName, String lastName, String email, String password) {
        if(isUserExistsByEmail(email)) {
            throw new ApiException("El correo ingresado no está disponible, Usa uno diferente e inténtalo nuevamente.");
        }
        var userEntity = userRepository.save(createNewUser(firstName, lastName, email));
        var credentialEntity = new CredentialEntity(userEntity, encoder.encode(password));
        credentialRepository.save(credentialEntity);
        var confirmationEntity = new ConfirmationEntity(userEntity);
        confirmationRepository.save(confirmationEntity);
        publisher.publishEvent(new UserEvent(userEntity, EventType.REGISTRATION, Map.of("key", confirmationEntity.getKey())));
    }

    @Override
    public RoleEntity getRoleName(String roleName) {
        var role = roleRepository.findByNameIgnoreCase(roleName);
        return role.orElseThrow(() -> new ApiException("El rol no existe"));
    }

    @Override
    public void verifyAccountKey(String key) {
        var confirmationEntity = getUserConfirmation(key);
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        userEntity.setEnabled(true);
        userRepository.save(userEntity);
        confirmationRepository.delete(confirmationEntity);
    }

    @Override
    public void updatedLoginAttempt(String email, LoginType loginType) {
        var userEntity = getUserEntityByEmail(email);
        RequestContext.setUserId(userEntity.getId());
        switch (loginType) {
            case LOGIN_ATTEMPT -> {
                if(userCache.get(userEntity.getEmail()) == null) {
                    userEntity.setLoginAttempts(0);
                    userEntity.setAccountNonLocked(true);
                }
                userEntity.setLoginAttempts(userEntity.getLoginAttempts() + 1);
                userCache.put(userEntity.getEmail(), userEntity.getLoginAttempts());
                if(userCache.get(userEntity.getEmail()) > 5) {
                    userEntity.setAccountNonLocked(false);
                }
            }
            case LOGIN_SUCCESS -> {
                userEntity.setAccountNonLocked(true);
                userEntity.setLoginAttempts(0);
                userEntity.setLastLogin(LocalDateTime.now());
                userCache.evict(userEntity.getEmail());
            }
        }
        userRepository.save(userEntity);
    }

    @Override
    public User getUserByUserId(String userId) {
        var userEntity = userRepository.findUserByUserId(userId).orElseThrow(() -> new ApiException("Usuario no encontrado."));
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User getUserByEmail(String email) {
        UserEntity userEntity = getUserEntityByEmail(email);
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public CredentialEntity getUserCredentialById(Long userId) {
        var credentialById = credentialRepository.getCredentialByUserEntityId(userId);
        return credentialById.orElseThrow(() -> new ApiException("Credencial no encontrada."));
    }

    @Override
    public User setUpMfa(Long id) {
        var userEntity = getUserEntityById(id);
        var codeSecret = UserUtils.qrCodeSecret.get();
        userEntity.setQrCodeImageUri(UserUtils.qrCodeImageUri.apply(userEntity.getEmail(), codeSecret));
        userEntity.setQrCodeSecret(codeSecret);
        userEntity.setMfa(true);
        userRepository.save(userEntity);
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User cancelMfa(Long id) {
        var userEntity = getUserEntityById(id);
        userEntity.setMfa(false);
        userEntity.setQrCodeSecret(StringUtils.EMPTY);
        userEntity.setQrCodeImageUri(StringUtils.EMPTY);
        userRepository.save(userEntity);
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public User verifyQrCode(String userId, String qrCode) {
        var userEntity = getUserEntityByUserId(userId);
        verifyCode(qrCode, userEntity.getQrCodeSecret());
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public void resetPassword(String email) {
        var userEntity = getUserEntityByEmail(email);
        var confirmation = getUserConfirmation(userEntity);
        if(confirmation != null) {
            publisher.publishEvent(new UserEvent(userEntity, EventType.RESET_PASSWORD, Map.of("key", confirmation.getKey())));
        } else {
            var confirmationEntity = new ConfirmationEntity(userEntity);
            confirmationRepository.save(confirmationEntity);
            publisher.publishEvent(new UserEvent(userEntity, EventType.RESET_PASSWORD, Map.of("key", confirmationEntity.getKey())));
        }
    }

    @Override
    public User verifyPasswordKey(String key) {
        var confirmationEntity = getUserConfirmation(key);
        if(confirmationEntity == null) { throw new ApiException("No fue posible encontrar el token."); }
        var userEntity = getUserEntityByEmail(confirmationEntity.getUserEntity().getEmail());
        if(userEntity == null) { throw new ApiException("Token incorrecto."); }
        UserValidation.verifyAccountStatus(userEntity);
        confirmationRepository.delete(confirmationEntity);
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public void updatePassword(String userId, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)) { throw new ApiException("Las contraseñas no coinciden, por favor intente de nuevo."); }
        var user = getUserByUserId(userId);
        var credentials = getUserCredentialById(user.getId());
        credentials.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credentials);
    }

    @Override
    public void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword) {
        if(!confirmNewPassword.equals(newPassword)) { throw new ApiException("Las contraseñas no coinciden, por favor intente de nuevo."); }
        var userEntity = getUserEntityByUserId(userId);
        UserValidation.verifyAccountStatus(userEntity);
        var credentials = getUserCredentialById(userEntity.getId());
        if(!encoder.matches(currentPassword, credentials.getPassword())) { throw new ApiException("La contraseña existente es incorrecta, intente nuevamente."); }
        credentials.setPassword(encoder.encode(newPassword));
        credentialRepository.save(credentials);
    }

    @Override
    public User updateUser(String userId, String firstName, String lastName, String email, String phone, String bio) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setFirstName(firstName);
        userEntity.setLastName(lastName);
        userEntity.setEmail(email);
        userEntity.setPhone(phone);
        userEntity.setBio(bio);
        userRepository.save(userEntity);
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public void updateRole(String userId, String role) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setRole(getRoleName(role));
        userRepository.save(userEntity);
    }

    @Override
    public void toggleAccountExpired(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setAccountNonExpired(!userEntity.isAccountNonExpired());
        userRepository.save(userEntity);
    }

    @Override
    public void toggleAccountLocked(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setAccountNonLocked(!userEntity.isAccountNonLocked());
        userRepository.save(userEntity);
    }

    @Override
    public void toggleAccountEnabled(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        userEntity.setEnabled(!userEntity.isEnabled());
        userRepository.save(userEntity);
    }

    @Override
    public void toggleCredentialsExpired(String userId) {
        var userEntity = getUserEntityByUserId(userId);
        var credentials = getUserCredentialById(userEntity.getId());
        credentials.setUpdatedAt(LocalDateTime.of(1990, 7, 12, 11, 11));
        credentialRepository.save(credentials);
    }

    @Override
    public String uploadPhoto(String userId, MultipartFile photo) {
        var userEntity = getUserEntityByUserId(userId);
        var photoUrl = photoFunction.apply(userId, photo);
        userEntity.setImageUrl(photoUrl + "?timestamp=" + System.currentTimeMillis());
        userRepository.save(userEntity);
        return photoUrl;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll()
                .stream()
                .filter(userEntity ->  !"system@gmail.com".equalsIgnoreCase(userEntity.getEmail()))
                .map(userEntity -> UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public User getUserById(Long id) {
        var userEntity = userRepository.findById(id).orElseThrow(() -> new ApiException("Usuario no encontrado."));
        return UserUtils.fromUserEntity(userEntity, userEntity.getRole(), getUserCredentialById(userEntity.getId()));
    }

    @Override
    public boolean isUserExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private UserEntity createNewUser(String firstName, String lastName, String email) {
        var role = getRoleName(Authority.USER.name());
        return UserUtils.createUserEntity(firstName, lastName, email, role);
    }

    private ConfirmationEntity getUserConfirmation(String key) {
        return confirmationRepository.findByKey(key).orElseThrow(() -> new ApiException("Clave de confirmación no encontrada."));
    }

    private ConfirmationEntity getUserConfirmation(UserEntity userEntity) {
        return confirmationRepository.findByUserEntity(userEntity).orElse(null);
    }

    private UserEntity getUserEntityByEmail(String email) {
        var userByEmail = userRepository.findByEmailIgnoreCase(email);
        return userByEmail.orElseThrow(() -> new ApiException("Usuario no encontrado."));
    }

    private UserEntity getUserEntityById(Long id) {
        var userById = userRepository.findById(id);
        return userById.orElseThrow(() -> new ApiException("Usuario no encontrado."));
    }

    private UserEntity getUserEntityByUserId(String userId) {
        var userByUserId = userRepository.findUserByUserId(userId);
        return userByUserId.orElseThrow(() -> new ApiException("Usuario no encontrado."));
    }

    private boolean verifyCode(String qrCode, String qrCodeSecret) {
        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        if (codeVerifier.isValidCode(qrCodeSecret, qrCode)) {
            return true;
        } else {
            throw new ApiException("Código QR no válido. Por favor intente de nuevo.");
        }
    }

    private final BiFunction<String, MultipartFile, String> photoFunction = (id, file) -> {
        var filename = id + ".png";
        try {
            var fileStorageLocation = Paths.get(Constants.FILE_STORAGE).toAbsolutePath().normalize();
            if(!Files.exists(fileStorageLocation)) {
                Files.createDirectories(fileStorageLocation);
            }
            Files.copy(file.getInputStream(), fileStorageLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/user/image/" + filename).toUriString();
        } catch (Exception e) {
            throw new ApiException("No fue posible guardar la imagen.");
        }
    };

}
