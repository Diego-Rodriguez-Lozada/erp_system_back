package com.erp.auth.service;

public interface EmailService {
    void sendNewAccountEmail(String name, String email, String token);
    void sendResetPasswordEmail(String name, String email, String token);
}
