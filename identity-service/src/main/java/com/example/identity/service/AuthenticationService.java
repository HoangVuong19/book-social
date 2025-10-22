package com.example.identity.service;

import com.example.identity.dto.request.LoginRequest;
import com.example.identity.dto.request.RegisterRequest;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.User;
import com.example.identity.enums.Role;
import com.example.identity.exception.NotFoundException;
import com.example.identity.exception.UnauthorizedException;
import com.example.identity.mapper.UserMapper;
import com.example.identity.repository.UserRepository;
import com.example.identity.utils.JwtUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;

    public String login(LoginRequest request) {
        var user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException("USER_NOT_EXISTED"));

        boolean authenticated = passwordEncoder.matches(request.password(), user.getPassword());

        if (!authenticated)
            throw new UnauthorizedException("Unauthorized!");
        return jwtUtils.generateToken(user);
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username()))
            throw new NotFoundException("User is exist");

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
