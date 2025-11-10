package com.example.post.dto.response;

import java.time.Instant;

public record PostResponse(
        String id,
        String content,
        String userId,
        String username,
        String created,
        Instant createdDate,
        Instant modifiedDate) {}
