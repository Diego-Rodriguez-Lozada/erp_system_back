package com.erp.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginRequest {
    @NotEmpty(message = "El correo electrónico no puede estar vacío o nulo")
    @Email(message = "Dirección de correo electrónico no válida")
    private String email;
    @NotEmpty(message = "La contraseña no puede estar vacía o nula")
    private String password;
}
