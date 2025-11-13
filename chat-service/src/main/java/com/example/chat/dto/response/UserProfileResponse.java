package com.example.chat.dto.response;

public record UserProfileResponse(
        String id, String userId, String username, String firstName, String lastName, String avatar) {}
