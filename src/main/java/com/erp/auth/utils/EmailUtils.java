package com.erp.auth.utils;

public class EmailUtils {
    /*public static String getEmailMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nYour new account has been created. Please click on the link below to verify your account.\n\n" +
                getVerificationUrl(host, key) + "\n\nThe Support Team!";
    }

    public static String getResetPasswordMessage(String name, String host, String key) {
        return "Hello " + name + ",\n\nPlease use this link below to reset your password.\n\n" +
                getResetPasswordUrl(host, key) + "\n\nThe Support Team!";
    }*/
    public static String getEmailMessage(String name, String host, String key) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6;'>" +
                "<div style='max-width: 600px; margin: 40px auto; background: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +
                "    <div style='text-align: center; margin-bottom: 20px;'>" +
                "        <img src='https://cdn-icons-png.flaticon.com/512/5968/5968267.png' alt='ERP System Logo' style='max-width: 100px; margin-bottom: 10px;' />" +
                "        <h1 style='font-size: 20px; color: #4CAF50; margin: 0;'>ERP System</h1>" +
                "    </div>" +
                "    <h2 style='color: #333333; font-weight: 600; margin: 0 0 15px;'>¡Bienvenido, " + name + "!</h2>" +
                "    <p style='color: #555555; font-size: 14px; line-height: 1.6;'>Tu cuenta ha sido creada exitosamente. Para activarla, por favor verifica tu cuenta haciendo clic en el siguiente botón:</p>" +
                "    <div style='margin: 25px 0; text-align: center;'>" +
                "        <a href='" + getVerificationUrl(host, key) + "' style='display: inline-block; background-color: #4CAF50; color: #ffffff; text-decoration: none; padding: 12px 24px; border-radius: 5px; font-size: 14px; font-weight: 600;'>Activar cuenta</a>" +
                "    </div>" +
                "    <p style='color: #888888; font-size: 12px; margin-top: 20px;'>Si no solicitaste esta cuenta, puedes ignorar este mensaje.</p>" +
                "    <hr style='border: none; border-top: 1px solid #eeeeee; margin: 20px 0;'>" +
                "    <p style='color: #555555; font-size: 12px; text-align: center;'>Atentamente,<br><strong>El equipo de soporte de ERP System</strong></p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    public static String getResetPasswordMessage(String name, String host, String key) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; margin: 0; padding: 0; background-color: #f6f6f6;'>" +
                "  <div style='max-width: 600px; margin: 40px auto; background: #ffffff; padding: 30px; border-radius: 8px; box-shadow: 0 4px 12px rgba(0,0,0,0.1);'>" +
                "    <div style='text-align: center; margin-bottom: 20px;'>" +
                "      <img src='https://cdn-icons-png.flaticon.com/512/5968/5968267.png' alt='ERP System Logo' style='max-width: 100px; margin-bottom: 10px;' />" +
                "      <h1 style='font-size: 20px; color: #4CAF50; margin: 0;'>ERP System</h1>" +
                "    </div>" +
                "    <h2 style='color: #FF5733; font-weight: 600; margin: 0 0 15px;'>Recuperación de contraseña</h2>" +
                "    <p style='color: #555555; font-size: 14px; line-height: 1.6;'>Hola, " + name + ".</p>" +
                "    <p style='color: #555555; font-size: 14px; line-height: 1.6;'>Recibimos una solicitud para restablecer tu contraseña. Puedes hacerlo haciendo clic en el siguiente botón:</p>" +
                "    <div style='margin: 25px 0; text-align: center;'>" +
                "      <a href='" + getResetPasswordUrl(host, key) + "' style='display: inline-block; background-color: #FF5733; color: #ffffff; text-decoration: none; padding: 12px 24px; border-radius: 5px; font-size: 14px; font-weight: 600;'>Restablecer contraseña</a>" +
                "    </div>" +
                "    <p style='color: #888888; font-size: 12px; margin-top: 20px;'>Si no solicitaste un cambio de contraseña, ignora este mensaje.</p>" +
                "    <hr style='border: none; border-top: 1px solid #eeeeee; margin: 20px 0;'>" +
                "    <p style='color: #555555; font-size: 12px; text-align: center;'>Atentamente,<br><strong>El equipo de soporte de ERP System</strong></p>" +
                "  </div>" +
                "</body>" +
                "</html>";
    }

    public static String getVerificationUrl(String host, String key) {
        return host + "/user/verify/account?key=" + key;
    }

    public static String getResetPasswordUrl(String host, String key) {
        return host + "/user/verify/password?key=" + key;
    }
}
