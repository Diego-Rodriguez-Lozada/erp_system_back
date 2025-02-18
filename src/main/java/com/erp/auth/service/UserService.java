package com.erp.auth.service;

import com.erp.auth.domain.User;
import com.erp.auth.entity.CredentialEntity;
import com.erp.auth.entity.RoleEntity;
import com.erp.auth.enumeration.LoginType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    void createUser(String firstName, String lastName, String email, String password);
    RoleEntity getRoleName(String roleName);
    void verifyAccountKey(String key);
    void updatedLoginAttempt(String email, LoginType loginType);
    User getUserByUserId(String userId);
    User getUserByEmail(String email);
    CredentialEntity getUserCredentialById(Long userId);
    User setUpMfa(Long id);
    User cancelMfa(Long id);
    User verifyQrCode(String userId, String qrCode);
    void resetPassword(String email);
    User verifyPasswordKey(String key);
    void updatePassword(String userId, String newPassword, String confirmNewPassword); //When user Not log in
    void updatePassword(String userId, String currentPassword, String newPassword, String confirmNewPassword); //When user log in
    User updateUser(String userId, String firstName, String lastName, String email, String phone, String bio);
    void updateRole(String userId, String role);
    void toggleAccountExpired(String userId);
    void toggleAccountLocked(String userId);
    void toggleAccountEnabled(String userId);
    void toggleCredentialsExpired(String userId);
    String uploadPhoto(String userId, MultipartFile photo);
    List<User> getUsers();
    User getUserById(Long id);
    boolean isUserExistsByEmail(String email);
}
