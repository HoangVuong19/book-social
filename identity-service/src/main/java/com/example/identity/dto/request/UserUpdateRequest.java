package com.example.identity.dto.request;

import java.time.LocalDate;

public record UserUpdateRequest(
        String password,
        String firstName,
        String lastName,
        LocalDate dob
) {
}