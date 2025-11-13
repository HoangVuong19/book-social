package com.example.chat.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ConversationRequest(String type, @Size(min = 1) @NotNull List<String> participantIds) {}
