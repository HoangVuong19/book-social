package com.example.chat.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.example.chat.dto.response.ConversationResponse;
import com.example.chat.entity.Conversation;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse(Conversation conversation);

    List<ConversationResponse> toConversationResponseList(List<Conversation> conversations);
}
