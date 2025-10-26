package com.example.identity.dto.request;

import java.time.LocalDate;

public record ProfileCreationRequest(String userId, String firstName, String lastName, LocalDate dob, String city) {}
