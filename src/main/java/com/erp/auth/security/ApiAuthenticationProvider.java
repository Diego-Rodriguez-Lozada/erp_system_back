package com.erp.auth.security;

import com.erp.auth.service.UserService;
import com.erp.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;
    private final BCryptPasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var apiAuthentication = authenticationFunction.apply(authentication);
        var user = userService.getUserByEmail(apiAuthentication.getEmail());
        if(user != null) {
            var userCredential = userService.getUserCredentialById(user.getId());
            var userPrincipal = new UserPrincipal(user, userCredential);
            validAccount.accept(userPrincipal);
            if(encoder.matches(apiAuthentication.getPassword(), userCredential.getPassword())) {
                return ApiAuthentication.authenticate(user, userPrincipal.getAuthorities());
            } else throw new BadCredentialsException("Correo electrónico y/o contraseña incorrecta. Por favor intente de nuevo.");

        } throw new ApiException("No fue posible autenticar su cuenta. Por favor intente más tarde.");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthentication.class.isAssignableFrom(authentication);
    }

    private final Function<Authentication, ApiAuthentication> authenticationFunction = authentication -> (ApiAuthentication) authentication;

    private final Consumer<UserPrincipal> validAccount = userPrincipal -> {
        if(!userPrincipal.isAccountNonLocked()) { throw new LockedException("Su cuenta está actualmente bloqueada. Póngase en contacto con su administrador para obtener ayuda."); }
        if(!userPrincipal.isEnabled()) { throw new DisabledException("Su cuenta está actualmente deshabilitada. Póngase en contacto con su administrador para obtener ayuda."); }
        if(!userPrincipal.isCredentialsNonExpired()) { throw new CredentialsExpiredException("Su contraseña ha expirado. Por favor actualice su contraseña."); }
        if(!userPrincipal.isAccountNonExpired()) { throw new DisabledException("Su cuenta ha expirado. Póngase en contacto con su administrador para obtener asistencia."); }
    };

}
