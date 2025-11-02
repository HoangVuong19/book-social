package com.example.notification.controller;

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

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EmailController {
    EmailService emailService;

    @PostMapping("/email/send")
    ApiResponse<SendEmailResponse> sendEmail(@RequestBody SendEmailRequest request) {
        return ApiResponse.success(emailService.sendEmail(request));
    }
}
