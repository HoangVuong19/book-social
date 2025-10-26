package com.example.profile.dto.request;

import java.time.LocalDate;

public record ProfileCreationRequest(String firstName, String lastName, LocalDate dob, String city) {}
