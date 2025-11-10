package com.example.post.dto.response;

import java.time.LocalDate;

public record UserProfileResponse(
        String id, String username, String email, String firstName, String lastName, LocalDate dob, String city) {}
