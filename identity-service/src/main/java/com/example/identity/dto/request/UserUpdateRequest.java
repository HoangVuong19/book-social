package com.example.identity.dto.request;

import java.time.LocalDate;
import java.util.List;

public record UserUpdateRequest(
        String password,
        String firstName,
        String lastName,
        LocalDate dob,
        List<String> roles
) {
}