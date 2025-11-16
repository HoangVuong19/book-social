package com.example.identity.service;

import com.example.event.NotificationEvent;
import com.example.identity.constant.PredefinedRole;
import com.example.identity.dto.request.*;
import com.example.identity.dto.response.IntrospectResponse;
import com.example.identity.dto.response.UserResponse;
import com.example.identity.entity.InvalidatedToken;
import com.example.identity.entity.Role;
import com.example.identity.entity.User;
import com.example.identity.exception.NotFoundException;
import com.example.identity.exception.UnauthorizedException;
import com.example.identity.mapper.ProfileMapper;
import com.example.identity.mapper.UserMapper;
import com.example.identity.repository.InvalidatedTokenRepository;
import com.example.identity.repository.RoleRepository;
import com.example.identity.repository.UserRepository;
import com.example.identity.repository.httpclient.ProfileClient;
import com.example.identity.utils.JwtUtils;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    RoleRepository roleRepository;
    ProfileClient profileClient;
    InvalidatedTokenRepository invalidatedTokenRepository;
    UserMapper userMapper;
    ProfileMapper profileMapper;
    PasswordEncoder passwordEncoder;
    JwtUtils jwtUtils;
    KafkaTemplate<String, Object> kafkaTemplate;

    public String login(LoginRequest request) {
        var user = userRepository
                .findByUsername(request.username())
                .orElseThrow(() -> new NotFoundException("USER_NOT_EXISTED"));

        boolean authenticated = passwordEncoder.matches(request.password(), user.getPassword());

        if (!authenticated) throw new UnauthorizedException("Unauthorized!");
        return jwtUtils.generateToken(user);
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) throw new NotFoundException("User is exist");

        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.password()));

        HashSet<Role> roles = new HashSet<>();
        roleRepository.findById(PredefinedRole.USER_ROLE).ifPresent(roles::add);

        user.setRoles(roles);
        user = userRepository.save(user);

        var profileRequest = profileMapper.toProfileCreationRequest(request);
        profileRequest = new ProfileCreationRequest(
                user.getId(),
                profileRequest.username(),
                profileRequest.firstName(),
                profileRequest.lastName(),
                profileRequest.dob(),
                profileRequest.city());
        profileClient.createProfile(profileRequest);

        NotificationEvent notificationEvent = new NotificationEvent(
                "EMAIL", request.username(), "Welcome to book-social", "Hello, " + request.username());
        // Publish message to kafka
        kafkaTemplate.send("notification-delivery", notificationEvent);

        return userMapper.toUserResponse(user);
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        var signToken = jwtUtils.verifyToken(request.token());

        String jit = signToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.token();
        boolean isValid = true;
        var jwt = jwtUtils.verifyToken(token);
        return new IntrospectResponse(isValid, jwt.getJWTClaimsSet().getSubject());
    }
}
