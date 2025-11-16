package com.example.chat.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.chat.dto.request.ChatMessageRequest;
import com.example.chat.dto.response.ChatMessageResponse;
import com.example.chat.entity.ChatMessage;

@Mapper(componentModel = "spring")
public interface ChatMessageMapper {
    ChatMessageResponse toChatMessageResponse(ChatMessage chatMessage);

    ChatMessage toChatMessage(ChatMessageRequest request);

    List<ChatMessageResponse> toChatMessageResponses(List<ChatMessage> chatMessages);
}
