package com.erp.controller;

import com.erp.auth.security.handler.ApiLogoutHandler;
import com.erp.auth.service.JwtService;
import com.erp.auth.service.UserService;
import com.erp.auth.utils.RequestUtils;
import com.erp.domain.Response;
import com.erp.dtorequest.UserRequest;
import com.erp.dtorequest.validationgroup.FullValidation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = { "/user" })
@Validated
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final ApiLogoutHandler apiLogoutHandler;

    @PostMapping("/register")
    public ResponseEntity<Response> createUser(@Validated(FullValidation.class) @RequestBody UserRequest user, HttpServletRequest request) {
        userService.createUser(user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
        return ResponseEntity.created(URI.create("")).body(RequestUtils.getResponse(request, Collections.emptyMap(), "Cuenta creada. Verifique su correo electrónico para activar su cuenta.", HttpStatus.CREATED));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<Response> verifyAccount(@RequestParam("key") String key, HttpServletRequest request) throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        userService.verifyAccountKey(key);
        return ResponseEntity.ok(RequestUtils.getResponse(request, Collections.emptyMap(), "La cuenta ha sido verificada. Ahora puede iniciar sesión.", HttpStatus.OK));
    }
}
