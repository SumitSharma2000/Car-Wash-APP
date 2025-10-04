package com.carwash.service;

import com.carwash.dto.AuthRequest;
import com.carwash.dto.SignupRequest;
import com.carwash.entity.User;
import com.carwash.repository.UserRepository;
import com.carwash.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void signup_Success() {
        SignupRequest request = new SignupRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(new User());

        assertDoesNotThrow(() -> authService.signup(request));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_Success() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("password");
        
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("encoded");
        user.setName("Test User");
        user.setRole(User.Role.CUSTOMER);
        
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encoded")).thenReturn(true);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("token");

        var result = authService.login(request);

        assertNotNull(result);
        assertEquals("token", result.getToken());
        verify(jwtUtil).generateToken("test@test.com");
    }

    @Test
    void login_InvalidCredentials() {
        AuthRequest request = new AuthRequest();
        request.setEmail("test@test.com");
        request.setPassword("wrong");
        
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }
}