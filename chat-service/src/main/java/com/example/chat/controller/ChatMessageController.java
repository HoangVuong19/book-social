package com.example.chat.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.chat.config.serialize.ApiResponse;
import com.example.chat.dto.request.ChatMessageRequest;
import com.example.chat.dto.response.ChatMessageResponse;
import com.example.chat.service.ChatMessageService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequiredArgsConstructor
@RequestMapping("messages")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatMessageController {
    ChatMessageService chatMessageService;

    @PostMapping("/create")
    ApiResponse<ChatMessageResponse> create(@RequestBody @Valid ChatMessageRequest request) {
        return ApiResponse.success(chatMessageService.create(request));
    }

    @GetMapping
    ApiResponse<List<ChatMessageResponse>> getMessages(@RequestParam("conversationId") String conversationId) {
        return ApiResponse.success(chatMessageService.getMessages(conversationId));
    }
}
