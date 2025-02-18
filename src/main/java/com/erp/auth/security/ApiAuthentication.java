package com.erp.auth.security;

import com.erp.auth.domain.User;
import com.erp.constants.Constants;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class ApiAuthentication extends AbstractAuthenticationToken {
    private User user;
    private String email;
    private String password;
    private boolean authenticated;

    public ApiAuthentication(String email, String password) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.email = email;
        this.password = password;
        this.authenticated = false;
    }

    private ApiAuthentication(User user, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.user = user;
        this.password = Constants.PASSWORD_PROTECTED;
        this.email = Constants.EMAIL_PROTECTED;
        this.authenticated = true;
    }

    public static ApiAuthentication unauthenticated(String email, String password) {
        return new ApiAuthentication(email, password);
    }

    public static ApiAuthentication authenticate(User user, Collection<? extends GrantedAuthority> authorities) {
        return new ApiAuthentication(user, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        super.setAuthenticated(authenticated);
    }

    @Override
    public boolean isAuthenticated() {
        return super.isAuthenticated();
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

}
