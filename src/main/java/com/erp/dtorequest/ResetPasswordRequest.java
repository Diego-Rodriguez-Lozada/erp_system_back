package com.erp.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResetPasswordRequest {
    @NotEmpty(message = "El ID no puede estar vacío o nulo")
    private String userId;
    @NotEmpty(message = "La nueva contraseña no puede estar vacía o nula")
    private String newPassword;
    @NotEmpty(message = "La confirmación de la nueva contraseña no puede estar vacía o nula")
    private String confirmNewPassword;
}
