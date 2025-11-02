package com.example.notification.dto.request;

public record SendEmailRequest(String to, String subject, String htmlContent) {}
