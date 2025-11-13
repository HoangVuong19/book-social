package com.example.chat.exception;

import org.springframework.http.HttpStatus;

public class UncategorizedException extends AppException {
    public UncategorizedException(String message) {
        super(500, message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
