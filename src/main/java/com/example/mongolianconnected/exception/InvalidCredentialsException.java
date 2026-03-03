package com.example.mongolianconnected.exception;

public class InvalidCredentialsException extends RuntimeException {
    private final String errorCode;

    public InvalidCredentialsException(String message) {
        super(message);
        this.errorCode = "INVALID_CREDENTIALS";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
