package com.example.profile.service;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.profile.dto.request.ProfileCreationRequest;
import com.example.profile.dto.request.SearchUserRequest;
import com.example.profile.dto.request.UpdateProfileRequest;
import com.example.profile.dto.response.UserProfileResponse;
import com.example.profile.entity.UserProfile;
import com.example.profile.exception.NotFoundException;
import com.example.profile.mapper.UserProfileMapper;
import com.example.profile.repository.UserProfileRepository;
import com.example.profile.repository.httpclient.FileClient;
import com.example.profile.utils.UserContext;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserProfileService {
    UserProfileRepository userProfileRepository;
    UserProfileMapper userProfileMapper;
    UserContext userContext;
    FileClient fileClient;

    public UserProfileResponse createProfile(ProfileCreationRequest request) {
        UserProfile userProfile = userProfileMapper.toUserProfile(request);
        userProfile = userProfileRepository.save(userProfile);

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getProfile(String id) {
        UserProfile userProfile =
                userProfileRepository.findById(id).orElseThrow(() -> new NotFoundException("Profile not found"));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getAllProfiles() {
        var profiles = userProfileRepository.findAll();

        return profiles.stream().map(userProfileMapper::toUserProfileResponse).toList();
    }

    public UserProfileResponse getByUserId(String userId) {
        UserProfile userProfile =
                userProfileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User not existed"));

        return userProfileMapper.toUserProfileResponse(userProfile);
    }

    public UserProfileResponse getMyProfile() {
        String userId = userContext.getCurrentUsername();

        var profile =
                userProfileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User not existed"));

        return userProfileMapper.toUserProfileResponse(profile);
    }

    public UserProfileResponse updateMyProfile(UpdateProfileRequest request) {
        String userId = userContext.getCurrentUsername();

        var profile =
                userProfileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User not existed"));

        userProfileMapper.update(profile, request);

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(profile));
    }

    public UserProfileResponse updateAvatar(MultipartFile file) {
        String userId = userContext.getCurrentUsername();

        var profile =
                userProfileRepository.findByUserId(userId).orElseThrow(() -> new NotFoundException("User not existed"));

        var response = fileClient.uploadMedia(file);

        profile.setAvatar(response.getData().getUrl());

        return userProfileMapper.toUserProfileResponse(userProfileRepository.save(profile));
    }

    public List<UserProfileResponse> search(SearchUserRequest request) {
        var userId = userContext.getCurrentUsername();
        List<UserProfile> userProfiles = userProfileRepository.findAllByUsernameLike(request.keyword());
        return userProfiles.stream()
                .filter(userProfile -> !userId.equals(userProfile.getUserId()))
                .map(userProfileMapper::toUserProfileResponse)
                .toList();
    }
}
