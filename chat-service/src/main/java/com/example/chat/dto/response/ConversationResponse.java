package com.example.chat.dto.response;

import java.time.Instant;
import java.util.List;

import com.example.chat.entity.ParticipantInfo;

public record ConversationResponse(
        String id,
        String type, // GROUP, DIRECT
        String participantsHash,
        String conversationAvatar,
        String conversationName,
        List<ParticipantInfo> participants,
        Instant createdDate,
        Instant modifiedDate) {}
