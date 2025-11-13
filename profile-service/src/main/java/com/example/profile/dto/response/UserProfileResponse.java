package com.example.profile.dto.response;

import java.time.LocalDate;

public record UserProfileResponse(
        String id, String username, String avatar, String firstName, String lastName, LocalDate dob, String city) {
}
