package com.erp.constants;

public class Constants {
    // Permissions for a Super Admin with full control over the system.
    public static final String SUPER_ADMIN_AUTHORITIES =
            "users:create,users:read,users:update,users:delete," +
                    "roles:create,roles:read,roles:update,roles:delete," +
                    "sales:create,sales:read,sales:update,sales:delete," +
                    "purchases:create,purchases:read,purchases:update,purchases:delete," +
                    "products:create,products:read,products:update,products:delete," +
                    "clients:create,clients:read,clients:update,clients:delete," +
                    "suppliers:create,suppliers:read,suppliers:update,suppliers:delete," +
                    "finance:create,finance:read,finance:update,finance:delete," +
                    "reports:generate";

    // Permissions for an Admin who manages most modules except for deleting users.
    public static final String ADMIN_AUTHORITIES =
            "users:create,users:read,users:update," +
                    "roles:read,roles:update," +
                    "sales:create,sales:read,sales:update,sales:delete," +
                    "purchases:create,purchases:read,purchases:update,purchases:delete," +
                    "products:create,products:read,products:update,products:delete," +
                    "clients:create,clients:read,clients:update,clients:delete," +
                    "suppliers:create,suppliers:read,suppliers:update,suppliers:delete," +
                    "finance:read,reports:generate";

    // Permissions for a Manager who mainly reviews and updates critical information.
    public static final String MANAGER_AUTHORITIES =
            "sales:read,sales:update," +
                    "purchases:read,purchases:update," +
                    "products:read," +
                    "clients:read,suppliers:read," +
                    "finance:read,reports:generate";

    // Permissions for a Sales Person who focuses on sales and client management.
    public static final String SALES_PERSON_AUTHORITIES =
            "sales:create,sales:read,sales:update," +
                    "clients:create,clients:read,clients:update," +
                    "products:read";

    // Permissions for a Purchasing Person who handles purchases and supplier management.
    public static final String PURCHASING_PERSON_AUTHORITIES =
            "purchases:create,purchases:read,purchases:update,purchases:delete," +
                    "suppliers:create,suppliers:read,suppliers:update,suppliers:delete," +
                    "products:read";

    // Permissions for an Accountant who manages financial records and report generation.
    public static final String ACCOUNTANT_AUTHORITIES =
            "finance:create,finance:read,finance:update,finance:delete," +
                    "reports:generate";

    // Permissions for a Warehouse staff member, responsible for inventory control.
    public static final String WAREHOUSE_AUTHORITIES =
            "products:read,products:update," +
                    "sales:read,purchases:read";

    // Permissions for a Basic User, with read-only access to most modules.
    public static final String USER_AUTHORITIES =
            "sales:read,purchases:read,products:read,clients:read,suppliers:read";

    public static final String NEW_USER_ACCOUNT_VERIFICATION = "Verifica tu cuenta de usuario";
    public static final String PASSWORD_RESET = "Restablecimiento de Contraseña";
    public static final int DAYS_EXPIRED_PASSWORD = 90;
    public static final String ERP_SYSTEM_LLC = "ERP_SYSTEM_LLC";
    public static final String FILE_STORAGE = System.getProperty("user.dir") + "\\uploads\\";
    public static final String EMPTY_VALUE = "empty";
    public static final String AUTHORITIES = "authorities";
    public static final String ROLE = "role";
    public static final String AUTHORITY_DELIMITER = ",";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String PASSWORD_PROTECTED = "[PASSWORD PROTECTED]";
    public static final String EMAIL_PROTECTED = "[EMAIL PROTECTED]";
    public static final String PATH_LOGIN = "/user/login";
    public static final String[] PUBLIC_URLS = { "/user/resetpassword/reset/**", "/user/verify/resetpassword/**", "/user/resetpassword/**", "/user/verify/qrcode/**", "/user/login/**", "/user/verify/account/**", "/user/register/**", "/user/new/password/**", "/user/verify/**", "/user/resetpassword/**", "/user/image/**", "/user/verify/password/**" };
    public static final String[] PUBLIC_ROUTES = { "/user/resetpassword/reset", "/user/verify/resetpassword", "/user/resetpassword", "/user/verify/qrcode", "/user/stream", "/user/id", "/user/login", "/user/register", "/user/new/password", "/user/verify", "/user/refresh/token", "/user/resetpassword", "/user/image", "/user/verify/account", "/user/verify/password", "/user/verify/code" };
    public static final String BASE_PATH = "/**";
    public static final String FILE_NAME = "File-Name";
    public static final int STRENGTH = 12;
}
