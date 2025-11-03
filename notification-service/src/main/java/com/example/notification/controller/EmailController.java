package com.example.notification.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.notification.config.serialize.ApiResponse;
import com.example.notification.dto.request.SendEmailRequest;
import com.example.notification.dto.response.SendEmailResponse;
import com.example.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;

    @PostMapping("/email/send")
    ApiResponse<SendEmailResponse> sendEmail(@RequestBody SendEmailRequest request) {
        return ApiResponse.success(emailService.sendEmail(request));
    }

    @KafkaListener(topics = "onboard-successful", groupId = "notification-group")
    public void listen(String message) {
        log.info("Message received: {}", message);
    }
}
