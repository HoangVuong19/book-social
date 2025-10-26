package com.example.gateway.config.serialize;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    boolean success;
    T data;
    ErrorResponse errors;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder().success(true).data(data).errors(null).build();
    }

    public static ApiResponse<?> error(int code, String message) {
        return ApiResponse.builder()
                .success(false)
                .data(null)
                .errors(new ErrorResponse(code, message))
                .build();
    }
}
