package com.erp.dtorequest;

import com.erp.dtorequest.validationgroup.BasicValidation;
import com.erp.dtorequest.validationgroup.FullValidation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {
    @NotEmpty(message = "El nombre no puede estar vacío o nulo", groups = BasicValidation.class)
    private String firstName;
    @NotEmpty(message = "El apellido no puede estar vacío o nulo", groups = BasicValidation.class)
    private String lastName;
    @NotEmpty(message = "El correo electrónico no puede estar vacío o nulo", groups = FullValidation.class)
    @Email(message = "Dirección de correo electrónico no válida", groups = FullValidation.class)
    private String email;
    @NotEmpty(message = "La contraseña no puede estar vacía o nula", groups = FullValidation.class)
    private String password;
    private String bio;
    private String phone;
}
