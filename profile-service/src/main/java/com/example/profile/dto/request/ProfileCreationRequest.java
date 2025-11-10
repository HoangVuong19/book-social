package com.example.profile.dto.request;

import java.time.LocalDate;

public record ProfileCreationRequest(
        String userId, String username, String firstName, String lastName, LocalDate dob, String city) {}
