package com.example.identity.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

import com.example.identity.validator.DobConstraint;

public record RegisterRequest(
        @Size(min = 3, message = "Username must be at least 3 characters") String username,
        @Size(min = 8, message = "Password must be at least 8 characters") String password,
        String firstName,
        String lastName,
        @DobConstraint(min = 18) LocalDate dob,
        String city) {}
