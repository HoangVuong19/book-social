package com.example.identity.dto.request;

public record AuthenticationRequest(
        String username,
        String password
) {
}
