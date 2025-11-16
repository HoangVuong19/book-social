package com.example.chat.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.corundumstudio.socketio.SocketIOServer;
import com.example.chat.dto.request.ChatMessageRequest;
import com.example.chat.dto.response.ChatMessageResponse;
import com.example.chat.entity.ChatMessage;
import com.example.chat.entity.ParticipantInfo;
import com.example.chat.exception.NotFoundException;
import com.example.chat.exception.UncategorizedException;
import com.example.chat.mapper.ChatMessageMapper;
import com.example.chat.repository.ChatMessageRepository;
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
public class ChatMessageService {
    SocketIOServer socketIOServer;

    ChatMessageRepository chatMessageRepository;
    ConversationRepository conversationRepository;
    ProfileClient profileClient;

    ChatMessageMapper chatMessageMapper;
    UserContext userContext;

    public List<ChatMessageResponse> getMessages(String conversationId) {
        // Validate conversationId
        String userId = userContext.getCurrentUsername();
        conversationRepository
                .findById(conversationId)
                .orElseThrow(() -> new NotFoundException("Conversation not found"))
                .getParticipants()
                .stream()
                .filter(participantInfo -> userId.equals(participantInfo.getUserId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Conversation not found"));

        var messages = chatMessageRepository.findAllByConversationIdOrderByCreatedDateDesc(conversationId);

        return messages.stream().map(this::toChatMessageResponse).toList();
    }

    public ChatMessageResponse create(ChatMessageRequest request) {
        String userId = userContext.getCurrentUsername();
        // Validate conversationId
        conversationRepository
                .findById(request.conversationId())
                .orElseThrow(() -> new NotFoundException("Conversation not found"))
                .getParticipants()
                .stream()
                .filter(participantInfo -> userId.equals(participantInfo.getUserId()))
                .findAny()
                .orElseThrow(() -> new NotFoundException("Conversation not found"));

        // Get UserInfo from ProfileService
        var userResponse = profileClient.getProfile(userId);
        if (Objects.isNull(userResponse)) {
            throw new UncategorizedException("Uncategorized error");
        }
        var userInfo = userResponse.getData();

        // Build Chat message Info
        ChatMessage chatMessage = chatMessageMapper.toChatMessage(request);
        chatMessage.setSender(ParticipantInfo.builder()
                .userId(userInfo.userId())
                .username(userInfo.username())
                .firstName(userInfo.firstName())
                .lastName(userInfo.lastName())
                .avatar(userInfo.avatar())
                .build());
        chatMessage.setCreatedDate(Instant.now());

        // Create chat message
        chatMessage = chatMessageRepository.save(chatMessage);
        String message = chatMessage.getMessage();

        // Publish socket event to clients
        socketIOServer.getAllClients().forEach(client -> {
            client.sendEvent("message", message);
        });

        // convert to Response
        return toChatMessageResponse(chatMessage);
    }

    private ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage) {
        String userId = userContext.getCurrentUsername();
        var original = chatMessageMapper.toChatMessageResponse(chatMessage);

        boolean isMe = userId.equals(chatMessage.getSender().getUserId());

        return new ChatMessageResponse(
                original.id(),
                original.conversationId(),
                isMe,
                original.message(),
                original.sender(),
                original.createdDate());
    }
}
