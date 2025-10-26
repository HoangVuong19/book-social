package com.example.profile.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AppException {
    public BadRequestException(String message) {
        super(400, message, HttpStatus.BAD_REQUEST);
    }
}
