package com.example.chat.service;

import com.example.chat.dto.request.IntrospectRequest;
import com.example.chat.dto.response.IntrospectResponse;
import com.example.chat.repository.httpclient.IdentityClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public IntrospectResponse introspect(IntrospectRequest request) {
        try {
            var result = identityClient.introspect(request).getData();
            if (Objects.isNull(result)) {
                return new IntrospectResponse(false, "");
            }
            return result;
        } catch (FeignException e) {
            log.error("Introspect failed: {}", e.getMessage(), e);
            return new IntrospectResponse(false, "");
        }
    }
}
