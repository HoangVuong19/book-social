package com.example.profile.controller;

import org.springframework.web.bind.annotation.*;

import com.example.profile.config.serialize.ApiResponse;
import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.service.UserProfileService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserProfileController {
    UserProfileService userProfileService;

    @PostMapping("/users")
    ApiResponse<UserProfileResponse> createProfile(@RequestBody ProfileCreationRequest request) {
        return ApiResponse.success(userProfileService.createProfile(request));
    }

    @GetMapping("/users/{profileId}")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String profileId) {
        return ApiResponse.success(userProfileService.getProfile(profileId));
    }
}
