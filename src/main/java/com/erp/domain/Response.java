package com.erp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response(
        String time,
        int code,
        String path,
        HttpStatus status,
        String message,
        String exception,
        Map<?, ?> data
) {
}
