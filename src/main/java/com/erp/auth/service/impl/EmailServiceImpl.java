package com.erp.auth.service.impl;

import com.erp.auth.service.EmailService;
import com.erp.auth.utils.EmailUtils;
import com.erp.constants.Constants;
import com.erp.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender sender;

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    @Async
    public void sendNewAccountEmail(String name, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(Constants.NEW_USER_ACCOUNT_VERIFICATION);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(EmailUtils.getEmailMessage(name, host, token));
            sender.send(message);
        } catch (Exception e) {
            throw new ApiException("No se puede enviar el correo electrónico");
        }
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String name, String email, String token) {
        try {
            var message = new SimpleMailMessage();
            message.setSubject(Constants.PASSWORD_RESET);
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setText(EmailUtils.getResetPasswordMessage(name, host, token));
            sender.send(message);
        } catch (Exception e) {
            throw new ApiException("No se puede enviar el correo electrónico");
        }
    }
}
