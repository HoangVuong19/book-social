package com.example.identity.controller;

import com.example.identity.config.serialize.ApiResponse;
import com.example.identity.dto.request.LoginRequest;
import com.example.identity.dto.request.RegisterRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping("/login")
    ApiResponse<String> login(@RequestBody LoginRequest request) {
        String token = authenticationService.login(request);
        return ApiResponse.success(token);
    }

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody @Valid RegisterRequest request) {
        UserResponse user = authenticationService.register(request);
        return ApiResponse.success(user);
    }
}
