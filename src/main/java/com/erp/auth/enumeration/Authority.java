package com.erp.auth.enumeration;

import com.erp.constants.Constants;

public enum Authority {
    SUPER_ADMIN(Constants.SUPER_ADMIN_AUTHORITIES),
    ADMIN(Constants.ADMIN_AUTHORITIES),
    MANAGER(Constants.MANAGER_AUTHORITIES),
    SALES(Constants.SALES_PERSON_AUTHORITIES),
    PURCHASING(Constants.PURCHASING_PERSON_AUTHORITIES),
    ACCOUNTANT(Constants.ACCOUNTANT_AUTHORITIES),
    WAREHOUSE(Constants.WAREHOUSE_AUTHORITIES),
    USER(Constants.USER_AUTHORITIES);

    private final String value;

    Authority(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
