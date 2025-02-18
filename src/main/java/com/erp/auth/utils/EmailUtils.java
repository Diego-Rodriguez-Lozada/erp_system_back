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
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #4CAF50;'>¡Bienvenido, " + name + "!</h2>" +
                "<p>Tu cuenta ha sido creada exitosamente. Para activarla, por favor verifica tu cuenta haciendo clic en el siguiente enlace:</p>" +
                "<p><a href='" + getVerificationUrl(host, key) + "' style='color: #1E90FF; font-weight: bold;'>Activar cuenta</a></p>" +
                "<p>Si no solicitaste esta cuenta, puedes ignorar este mensaje.</p>" +
                "<br>" +
                "<p>Atentamente,<br><strong>El equipo de soporte</strong></p>" +
                "</body>" +
                "</html>";
    }

    public static String getResetPasswordMessage(String name, String host, String key) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<h2 style='color: #FF5733;'>Recuperación de contraseña</h2>" +
                "<p>Hola, " + name + ".</p>" +
                "<p>Recibimos una solicitud para restablecer tu contraseña. Puedes hacerlo haciendo clic en el siguiente enlace:</p>" +
                "<p><a href='" + getResetPasswordUrl(host, key) + "' style='color: #1E90FF; font-weight: bold;'>Restablecer contraseña</a></p>" +
                "<p>Si no solicitaste un cambio de contraseña, ignora este mensaje.</p>" +
                "<br>" +
                "<p>Atentamente,<br><strong>El equipo de soporte</strong></p>" +
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
