package com.example.notification.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.notification.dto.request.SendEmailRequest;
import com.example.notification.dto.response.SendEmailResponse;
import com.example.notification.exception.EmailException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    JavaMailSender mailSender;

    public SendEmailResponse sendEmail(SendEmailRequest request) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("no-reply@example.com");
            helper.setTo(request.to());
            helper.setSubject(request.subject());
            helper.setText(request.htmlContent(), true);

            mailSender.send(message);

            log.info("Email sent successfully to {}", request.to());
            return new SendEmailResponse("Email sent successfully to " + request.to());
        } catch (MessagingException e) {
            log.error("Failed to send email: {}", e.getMessage());
            throw new EmailException("Failed to send email");
        }
    }
}
