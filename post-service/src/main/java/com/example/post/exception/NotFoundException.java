package com.example.post.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(404, message, HttpStatus.NOT_FOUND);
    }
}
