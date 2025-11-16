package com.example.chat.dto.response;

import java.time.Instant;

import com.example.chat.entity.ParticipantInfo;

public record ChatMessageResponse(
        String id, String conversationId, boolean me, String message, ParticipantInfo sender, Instant createdDate) {}
