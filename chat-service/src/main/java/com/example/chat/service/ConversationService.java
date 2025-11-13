package com.example.chat.service;

import java.util.List;
import java.util.StringJoiner;

import org.springframework.stereotype.Service;

import com.example.chat.dto.request.ConversationRequest;
import com.example.chat.dto.response.ConversationResponse;
import com.example.chat.entity.Conversation;
import com.example.chat.mapper.ConversationMapper;
import com.example.chat.repository.ConversationRepository;
import com.example.chat.repository.httpclient.ProfileClient;
import com.example.chat.utils.UserContext;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConversationService {
    ConversationRepository conversationRepository;
    ProfileClient profileClient;

    ConversationMapper conversationMapper;
    UserContext userContext;

    public List<ConversationResponse> myConversations() {
        return null;
    }

    public ConversationResponse create(ConversationRequest request) {
        return null;
    }

    private String generateParticipantHash(List<String> ids) {
        StringJoiner stringJoiner = new StringJoiner("_");
        ids.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private ConversationResponse toConversationResponse(Conversation conversation) {
        String currentUserId = userContext.getCurrentUsername();

        ConversationResponse base = conversationMapper.toConversationResponse(conversation);

        return conversation.getParticipants().stream()
                .filter(participant -> !participant.getUserId().equals(currentUserId))
                .findFirst()
                .map(participant -> new ConversationResponse(
                        base.id(),
                        base.type(),
                        base.participantsHash(),
                        participant.getAvatar(),
                        participant.getUsername(),
                        base.participants(),
                        base.createdDate(),
                        base.modifiedDate()))
                .orElse(base);
    }
}
