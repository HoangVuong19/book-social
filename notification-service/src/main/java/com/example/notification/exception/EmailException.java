package com.example.notification.exception;

import org.springframework.http.HttpStatus;

public class EmailException extends AppException {
    public EmailException(String message) {
        super(108, message, HttpStatus.BAD_REQUEST);
    }
}
