package com.example.bank.controllers.exceptionhandler;

import com.example.bank.core.exceptions.NotFoundException;
import com.example.bank.core.exceptions.TransferMoneyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorMessage> argumentNotValidException(MethodArgumentNotValidException exception) {
        LOGGER.error("Error: ", exception);
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorMessage.builder()
                        .status(400)
                        .timestamp(LocalDateTime.now())
                        .message(errors.toString())
                        .detailedMessage(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorMessage> notFoundException(NotFoundException exception) {
        LOGGER.error("Error: ", exception);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ErrorMessage.builder()
                        .status(exception.getStatusCode())
                        .timestamp(LocalDateTime.now())
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(TransferMoneyException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> transferMoneyException(TransferMoneyException exception) {
        LOGGER.error("Error: ", exception);
        return ResponseEntity
                .status(HttpStatus.valueOf(exception.getStatusCode()))
                .body(ErrorMessage.builder()
                        .status(exception.getStatusCode())
                        .timestamp(LocalDateTime.now())
                        .message(exception.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> unexpectedException(Exception exception) {
        LOGGER.error("Error: ", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorMessage.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .timestamp(LocalDateTime.now())
                        .message("An unexpected error occurred on the server. Please report to us.")
                        .detailedMessage(exception.getMessage())
                        .build());
    }
}
