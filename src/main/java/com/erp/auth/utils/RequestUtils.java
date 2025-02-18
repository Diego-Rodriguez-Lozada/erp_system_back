package com.erp.auth.utils;

import com.erp.domain.Response;
import com.erp.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class RequestUtils {

    private static final BiConsumer<HttpServletResponse, Response> writeResponse = (httpServletResponse, response) -> {
        try {
            var outputStream = httpServletResponse.getOutputStream();
            new ObjectMapper().writeValue(outputStream, response);
            outputStream.flush();
        } catch (Exception e) {
            throw new ApiException(e.getMessage());
        }
    };

    private static final BiFunction<Exception, HttpStatus, String> errorReason = (exception, httpStatus) -> {
        if(httpStatus.isSameCodeAs(HttpStatus.FORBIDDEN)) { return "No tienes suficientes permisos."; }
        if(httpStatus.isSameCodeAs(HttpStatus.UNAUTHORIZED)) { return "No tienes acceso a este recurso."; }
        if(exception instanceof DisabledException ||
            exception instanceof LockedException ||
            exception instanceof BadCredentialsException ||
            exception instanceof CredentialsExpiredException ||
            exception instanceof ApiException) {
            return exception.getMessage();
        }
        if(httpStatus.is5xxServerError()) {
            return "Se produjo un error de servidor interno.";
        } else { return "Ha ocurrido un error, por favor intente más tarde."; }
    };

    public static Response getResponse(HttpServletRequest request, Map<?, ?> data, String message, HttpStatus status) {
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), message, StringUtils.EMPTY, data);
    }

    private static Response getErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception, HttpStatus status) {
        response.setContentType("application/json");
        response.setStatus(status.value());
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), errorReason.apply(exception, status), ExceptionUtils.getRootCauseMessage(exception), Collections.emptyMap());
    }

    public static void handleErrorResponse(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        if(exception instanceof AccessDeniedException) {
            var apiResponse = getErrorResponse(request, response, exception, HttpStatus.FORBIDDEN);
            writeResponse.accept(response, apiResponse);
        } else if(exception instanceof InsufficientAuthenticationException) {
            var apiResponse = getErrorResponse(request, response, exception, HttpStatus.UNAUTHORIZED);
            writeResponse.accept(response, apiResponse);
        } else if(exception instanceof MismatchedInputException) {
            var apiResponse = getErrorResponse(request, response, exception, HttpStatus.BAD_REQUEST);
            writeResponse.accept(response, apiResponse);
        } else if(exception instanceof DisabledException ||
                exception instanceof LockedException ||
                exception instanceof BadCredentialsException ||
                exception instanceof CredentialsExpiredException ||
                exception instanceof ApiException) {
            var apiResponse = getErrorResponse(request, response, exception, HttpStatus.BAD_REQUEST);
            writeResponse.accept(response, apiResponse);
        } else {
            Response apiResponse = getErrorResponse(request, response, exception, HttpStatus.INTERNAL_SERVER_ERROR);
            writeResponse.accept(response, apiResponse);
        }
    }

    public static Response handleErrorResponse(String message, String exception, HttpServletRequest request, HttpStatusCode status) {
        return new Response(LocalDateTime.now().toString(), status.value(), request.getRequestURI(), HttpStatus.valueOf(status.value()), message, exception, Collections.emptyMap());
    }
}
