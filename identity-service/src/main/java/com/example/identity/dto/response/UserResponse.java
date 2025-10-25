package com.example.identity.dto.response;

import java.time.LocalDate;
import java.util.Set;

public record UserResponse(
        String id, String username, String firstName, String lastName, LocalDate dob, Set<RoleResponse> roles) {}
