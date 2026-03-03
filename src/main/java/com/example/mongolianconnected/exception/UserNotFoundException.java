package com.example.mongolianconnected.exception;

public class UserNotFoundException extends RuntimeException {
    private final String errorCode;

    public UserNotFoundException(String message) {
        super(message);
        this.errorCode = "USER_NOT_FOUND";
    }

    public String getErrorCode() {
        return errorCode;
    }
}