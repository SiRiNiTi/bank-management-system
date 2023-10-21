package com.example.bank.core.exceptions;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private int statusCode;

    public NotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public NotFoundException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
