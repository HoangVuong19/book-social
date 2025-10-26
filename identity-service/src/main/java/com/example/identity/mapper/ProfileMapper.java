package com.example.identity.mapper;

import org.mapstruct.Mapper;

import com.example.identity.dto.request.ProfileCreationRequest;
import com.example.identity.dto.request.RegisterRequest;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(RegisterRequest request);
}
