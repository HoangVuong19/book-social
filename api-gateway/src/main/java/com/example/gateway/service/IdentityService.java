package com.example.gateway.service;

import org.springframework.stereotype.Service;

import com.example.gateway.config.serialize.ApiResponse;
import com.example.gateway.dto.request.IntrospectRequest;
import com.example.gateway.dto.response.IntrospectResponse;
import com.example.gateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(IntrospectRequest introspectRequest) {
        return identityClient.introspect(introspectRequest);
    }
}
