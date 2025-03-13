package com.erp.dtorequest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QrCodeRequest {
    @NotEmpty(message = "El ID no puede estar vacío o nulo")
    private String userId;
    @NotEmpty(message = "El código QR no puede estar vacío o nulo")
    private String qrCode;
}
