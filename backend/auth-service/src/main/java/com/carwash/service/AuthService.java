package com.carwash.service;

import com.carwash.dto.AuthRequest;
import com.carwash.dto.AuthResponse;
import com.carwash.dto.SignupRequest;
import com.carwash.dto.ForgotPasswordRequest;
import com.carwash.dto.ResetPasswordRequest;
import com.carwash.entity.User;
import com.carwash.entity.PasswordResetToken;
import com.carwash.repository.UserRepository;
import com.carwash.repository.PasswordResetTokenRepository;
import com.carwash.security.JwtUtil;
import com.carwash.exception.AuthException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, 
                      JwtUtil jwtUtil, PasswordResetTokenRepository tokenRepository, 
                      EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.tokenRepository = tokenRepository;
        this.emailService = emailService;
    }
    
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        
        userRepository.save(user);
        
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
    }
    
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Invalid credentials"));
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Invalid credentials");
        }
        
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getName(), user.getRole());
    }
    
    public void forgotPassword(ForgotPasswordRequest request) {
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("User not found"));
        
        tokenRepository.deleteByEmail(request.getEmail());
        
        String token = java.util.UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken(token, request.getEmail());
        tokenRepository.save(resetToken);
        
        emailService.sendPasswordResetEmail(request.getEmail(), token);
    }
    
    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new AuthException("Invalid token"));
        
        if (resetToken.isExpired() || resetToken.isUsed()) {
            throw new AuthException("Token expired or already used");
        }
        
        User user = userRepository.findByEmail(resetToken.getEmail())
                .orElseThrow(() -> new AuthException("User not found"));
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
    }
}