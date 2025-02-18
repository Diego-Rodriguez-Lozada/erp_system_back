package com.erp.auth.event.listener;

import com.erp.auth.event.UserEvent;
import com.erp.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserEventListener {
    private final EmailService emailService;

    @EventListener
    public void onUserEvent(UserEvent event) {
        switch (event.getType()) {
            case REGISTRATION -> emailService.sendNewAccountEmail(event.getUser().getFirstName(), event.getUser().getEmail(), (String) event.getData().get("key"));
            case RESET_PASSWORD -> emailService.sendResetPasswordEmail(event.getUser().getFirstName(), event.getUser().getEmail(), (String) event.getData().get("key"));
            default -> {}
        }
    }

}
