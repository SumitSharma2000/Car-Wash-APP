package com.carwash.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendPasswordResetEmail(String email, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("CarWash Pro - Password Reset Request");
        message.setText("Click the link below to reset your password:\n\n" +
                "http://localhost:4200/reset-password?token=" + token + "\n\n" +
                "This link will expire in 1 hour.\n\n" +
                "If you didn't request this, please ignore this email.");
        
        mailSender.send(message);
    }
}