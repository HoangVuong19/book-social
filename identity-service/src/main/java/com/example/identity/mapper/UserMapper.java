package com.example.identity.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.identity.dto.request.RegisterRequest;
import com.example.identity.dto.request.UserUpdateRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
