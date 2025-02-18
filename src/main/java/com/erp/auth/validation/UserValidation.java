package com.erp.auth.validation;

import com.erp.auth.entity.UserEntity;
import com.erp.exception.ApiException;

public class UserValidation {

    public static void verifyAccountStatus(UserEntity userEntity) {
        if(!userEntity.isEnabled()) { throw new ApiException("La cuenta está deshabilitada."); }
        if(!userEntity.isAccountNonExpired()) { throw new ApiException("La cuenta expiró."); }
        if(!userEntity.isAccountNonLocked()) { throw new ApiException("La cuenta está bloqueada."); }
    }
}
