package com.example.profile.exception;

import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.profile.config.serialize.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ApiResponse<?> handleAppException(AppException ex) {
        return ApiResponse.error(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    ApiResponse<?> handlingAccessDeniedException(AccessDeniedException ex) {
        return ApiResponse.error(403, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ApiResponse<?> handlingValidation(MethodArgumentNotValidException exception) {
        String message = Objects.requireNonNull(exception.getFieldError()).getDefaultMessage();
        return ApiResponse.error(HttpStatus.BAD_REQUEST.value(), message);
    }
}
