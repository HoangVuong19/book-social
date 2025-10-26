package com.example.identity.dto.response;

import java.time.LocalDate;

public record UserProfileResponse(
        String id, String userId, String firstName, String lastName, LocalDate dob, String city) {}
