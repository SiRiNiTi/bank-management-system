package com.example.bank.core.exceptions;

import lombok.Getter;

@Getter
public class TransferMoneyException extends RuntimeException {

    private int statusCode;

    public TransferMoneyException(String message) {
        super(message);
        this.statusCode = 500;
    }

    public TransferMoneyException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }


    public TransferMoneyException(String message, int statusCode, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
}
