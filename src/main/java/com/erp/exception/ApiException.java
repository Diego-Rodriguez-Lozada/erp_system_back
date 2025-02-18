package com.erp.exception;

public class ApiException extends RuntimeException{

    public ApiException(String message) {
        super(message);
    }

    public ApiException() {
        super("Ha ocurrido un error");
    }
}
