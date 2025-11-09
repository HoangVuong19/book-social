package com.example.event;

public record NotificationEvent(String channel, String recipient, String subject, String body) {}
