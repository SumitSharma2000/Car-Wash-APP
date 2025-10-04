package com.carwash.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceAdditionalTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        // Setup is handled by Mockito annotations
    }

    @Test
    void testSendPasswordResetEmail_Success() {
        String email = "test@test.com";
        String token = "reset-token-123";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(email, token));

        verify(mailSender).send((SimpleMailMessage) argThat(message -> {
            SimpleMailMessage msg = (SimpleMailMessage) message;
            String text = msg.getText();
            String[] recipients = msg.getTo();
            return recipients != null && recipients.length > 0 && email.equals(recipients[0]) &&
                   "CarWash Pro - Password Reset Request".equals(msg.getSubject()) &&
                   text != null && text.contains(token) &&
                   text.contains("http://localhost:4200/reset-password?token=") &&
                   text.contains("This link will expire in 1 hour");
        }));
    }

    @Test
    void testSendPasswordResetEmail_WithDifferentEmail() {
        String email = "another@test.com";
        String token = "different-token";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender).send((SimpleMailMessage) argThat(message -> {
            SimpleMailMessage msg = (SimpleMailMessage) message;
            String text = msg.getText();
            String[] recipients = msg.getTo();
            return recipients != null && recipients.length > 0 && email.equals(recipients[0]) && 
                   text != null && text.contains(token);
        }));
    }

    @Test
    void testSendPasswordResetEmail_MessageContent() {
        String email = "user@example.com";
        String token = "abc123";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender).send((SimpleMailMessage) argThat(message -> {
            SimpleMailMessage msg = (SimpleMailMessage) message;
            String text = msg.getText();
            return text != null &&
                   text.contains("Click the link below to reset your password:") &&
                   text.contains("http://localhost:4200/reset-password?token=" + token) &&
                   text.contains("If you didn't request this, please ignore this email.");
        }));
    }

    @Test
    void testSendPasswordResetEmail_MailSenderCalled() {
        String email = "test@domain.com";
        String token = "token456";

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendPasswordResetEmail_MessageStructure() {
        String email = "structure@test.com";
        String token = "struct-token";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender).send((SimpleMailMessage) argThat(message -> {
            SimpleMailMessage msg = (SimpleMailMessage) message;
            String[] recipients = msg.getTo();
            String text = msg.getText();
            return recipients != null &&
                   recipients.length == 1 &&
                   msg.getSubject() != null &&
                   text != null &&
                   !text.isEmpty();
        }));
    }

    @Test
    void testSendPasswordResetEmail_NullToken() {
        String email = "test@test.com";
        String token = null;

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail(email, token));

        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendPasswordResetEmail_EmptyToken() {
        String email = "test@test.com";
        String token = "";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailService.sendPasswordResetEmail(email, token);

        verify(mailSender).send((SimpleMailMessage) argThat(message -> {
            SimpleMailMessage msg = (SimpleMailMessage) message;
            String text = msg.getText();
            return text != null && text.contains("http://localhost:4200/reset-password?token=");
        }));
    }

    @Test
    void testEmailService_InstanceCreation() {
        EmailService service = new EmailService();
        assertNotNull(service);
    }
}