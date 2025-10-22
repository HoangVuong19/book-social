package com.example.identity.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AppException {
    public UnauthorizedException(String message) {
        super(401, message, HttpStatus.UNAUTHORIZED);
    }
}
