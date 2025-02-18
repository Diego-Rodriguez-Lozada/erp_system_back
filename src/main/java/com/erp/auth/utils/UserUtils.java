package com.erp.auth.utils;

import com.erp.auth.domain.User;
import com.erp.auth.entity.CredentialEntity;
import com.erp.auth.entity.RoleEntity;
import com.erp.auth.entity.UserEntity;
import com.erp.constants.Constants;
import com.erp.exception.ApiException;
import dev.samstevens.totp.code.HashingAlgorithm;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class UserUtils {

    public static UserEntity createUserEntity(String firstName, String lastName, String email, RoleEntity role) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .lastLogin(LocalDateTime.now())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(false)
                .mfa(false)
                .loginAttempts(0)
                .qrCodeSecret(StringUtils.EMPTY)
                .phone(StringUtils.EMPTY)
                .bio(StringUtils.EMPTY)
                .imageUrl("https://cdn-icons-png.flaticon.com/512/149/149071.png")
                .role(role)
                .build();
    }

    public static User fromUserEntity(UserEntity userEntity, RoleEntity roleEntity, CredentialEntity credentialEntity) {
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setLastLogin(userEntity.getLastLogin().toString());
        user.setCredentialsNonExpired(isCredentialsNonExpired(credentialEntity));
        user.setCreatedAt(userEntity.getCreatedAt().toString());
        user.setUpdatedAt(userEntity.getUpdatedAt().toString());
        user.setRole(roleEntity.getName());
        user.setAuthorities(roleEntity.getAuthorities().getValue());
        return user;
    }

    public static boolean isCredentialsNonExpired(CredentialEntity credentialEntity) {
        return credentialEntity.getUpdatedAt().plusDays(Constants.DAYS_EXPIRED_PASSWORD).isAfter(LocalDateTime.now());
    }

    public static BiFunction<String, String, QrData> qrDataFunction = (email, qrCodeSecret) -> new QrData.Builder()
            .issuer(Constants.ERP_SYSTEM_LLC)
            .label(email)
            .secret(qrCodeSecret)
            .algorithm(HashingAlgorithm.SHA1)
            .digits(6)
            .period(30)
            .build();

    public static BiFunction<String, String, String> qrCodeImageUri = (email, qrCodeSecret) -> {
        var data = qrDataFunction.apply(email, qrCodeSecret);
        var generator = new ZxingPngQrGenerator();
        byte[] imageData;
        try {
            imageData = generator.generate(data);
        } catch (Exception e) {
            throw new ApiException("No se puede crear el código QR URI");
        }
        return Utils.getDataUriForImage(imageData, generator.getImageMimeType());
    };

    public static Supplier<String> qrCodeSecret = () -> new DefaultSecretGenerator().generate();
}
