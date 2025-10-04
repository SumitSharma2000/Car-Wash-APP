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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceAdditionalTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordResetTokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private AuthService authService;

    private SignupRequest signupRequest;
    private AuthRequest authRequest;
    private User testUser;
    private ForgotPasswordRequest forgotPasswordRequest;
    private ResetPasswordRequest resetPasswordRequest;
    private PasswordResetToken resetToken;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("Test User");
        signupRequest.setRole(User.Role.CUSTOMER);
        signupRequest.setPhone("1234567890");
        signupRequest.setAddress("123 Test St");

        authRequest = new AuthRequest();
        authRequest.setEmail("test@test.com");
        authRequest.setPassword("password123");

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@test.com");
        testUser.setPassword("encodedPassword");
        testUser.setName("Test User");
        testUser.setRole(User.Role.CUSTOMER);

        forgotPasswordRequest = new ForgotPasswordRequest();
        forgotPasswordRequest.setEmail("test@test.com");

        resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setToken("reset-token-123");
        resetPasswordRequest.setNewPassword("newPassword123");

        resetToken = new PasswordResetToken("reset-token-123", "test@test.com");
    }

    // Signup Tests
    @Test
    void testSignup_EmailAlreadyExists() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(true);

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.signup(signupRequest)
        );

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository).existsByEmail("test@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testSignup_ServiceProviderRole() {
        signupRequest.setRole(User.Role.SERVICE_PROVIDER);
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.signup(signupRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals("test@test.com", response.getEmail());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSignup_WithNullPhone() {
        signupRequest.setPhone(null);
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.signup(signupRequest);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSignup_WithNullAddress() {
        signupRequest.setAddress(null);
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.signup(signupRequest);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSignup_PasswordEncodingCalled() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        authService.signup(signupRequest);

        verify(passwordEncoder).encode("password123");
    }

    // Login Tests
    @Test
    void testLogin_UserNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.login(authRequest)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(userRepository).findByEmail("test@test.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testLogin_WrongPassword() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.login(authRequest)
        );

        assertEquals("Invalid credentials", exception.getMessage());
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtUtil, never()).generateToken(anyString());
    }

    @Test
    void testLogin_ServiceProviderSuccess() {
        testUser.setRole(User.Role.SERVICE_PROVIDER);
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(authRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.getToken());
        assertEquals(User.Role.SERVICE_PROVIDER, response.getRole());
    }

    @Test
    void testLogin_TokenGeneration() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("generated-token");

        AuthResponse response = authService.login(authRequest);

        assertEquals("generated-token", response.getToken());
        verify(jwtUtil).generateToken("test@test.com");
    }

    // Forgot Password Tests
    @Test
    void testForgotPassword_UserNotFound() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.forgotPassword(forgotPasswordRequest)
        );

        assertEquals("User not found", exception.getMessage());
        verify(tokenRepository, never()).deleteByEmail(anyString());
        verify(tokenRepository, never()).save(any());
    }

    @Test
    void testForgotPassword_Success() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        doNothing().when(tokenRepository).deleteByEmail("test@test.com");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        assertDoesNotThrow(() -> authService.forgotPassword(forgotPasswordRequest));

        verify(tokenRepository).deleteByEmail("test@test.com");
        verify(tokenRepository).save(any(PasswordResetToken.class));
        verify(emailService).sendPasswordResetEmail(eq("test@test.com"), anyString());
    }

    @Test
    void testForgotPassword_DeleteExistingTokens() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        doNothing().when(tokenRepository).deleteByEmail("test@test.com");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        authService.forgotPassword(forgotPasswordRequest);

        verify(tokenRepository).deleteByEmail("test@test.com");
    }

    @Test
    void testForgotPassword_EmailServiceCalled() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        doNothing().when(tokenRepository).deleteByEmail("test@test.com");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        authService.forgotPassword(forgotPasswordRequest);

        verify(emailService).sendPasswordResetEmail(eq("test@test.com"), anyString());
    }

    // Reset Password Tests
    @Test
    void testResetPassword_InvalidToken() {
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.resetPassword(resetPasswordRequest)
        );

        assertEquals("Invalid token", exception.getMessage());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void testResetPassword_ExpiredToken() {
        resetToken.setExpiryDate(LocalDateTime.now().minusHours(2));
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.resetPassword(resetPasswordRequest)
        );

        assertEquals("Token expired or already used", exception.getMessage());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void testResetPassword_UsedToken() {
        resetToken.setUsed(true);
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.resetPassword(resetPasswordRequest)
        );

        assertEquals("Token expired or already used", exception.getMessage());
        verify(userRepository, never()).findByEmail(anyString());
    }

    @Test
    void testResetPassword_UserNotFound() {
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.resetPassword(resetPasswordRequest)
        );

        assertEquals("User not found", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPassword_Success() {
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(tokenRepository.save(resetToken)).thenReturn(resetToken);

        assertDoesNotThrow(() -> authService.resetPassword(resetPasswordRequest));

        verify(passwordEncoder).encode("newPassword123");
        verify(userRepository).save(testUser);
        verify(tokenRepository).save(resetToken);
        assertTrue(resetToken.isUsed());
    }

    @Test
    void testResetPassword_TokenMarkedAsUsed() {
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(tokenRepository.save(resetToken)).thenReturn(resetToken);

        authService.resetPassword(resetPasswordRequest);

        assertTrue(resetToken.isUsed());
        verify(tokenRepository).save(resetToken);
    }

    @Test
    void testResetPassword_PasswordEncoded() {
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(tokenRepository.save(resetToken)).thenReturn(resetToken);

        authService.resetPassword(resetPasswordRequest);

        verify(passwordEncoder).encode("newPassword123");
    }

    // Edge Cases and Additional Scenarios
    @Test
    void testSignup_EmptyPhoneAndAddress() {
        signupRequest.setPhone("");
        signupRequest.setAddress("");
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.signup(signupRequest);

        assertNotNull(response);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testLogin_DifferentEmailCase() {
        authRequest.setEmail("TEST@TEST.COM");
        when(userRepository.findByEmail("TEST@TEST.COM")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(authRequest);

        assertNotNull(response);
        verify(userRepository).findByEmail("TEST@TEST.COM");
    }

    @Test
    void testForgotPassword_DifferentEmailCase() {
        forgotPasswordRequest.setEmail("TEST@TEST.COM");
        when(userRepository.findByEmail("TEST@TEST.COM")).thenReturn(Optional.of(testUser));
        doNothing().when(tokenRepository).deleteByEmail("TEST@TEST.COM");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        assertDoesNotThrow(() -> authService.forgotPassword(forgotPasswordRequest));

        verify(tokenRepository).deleteByEmail("TEST@TEST.COM");
    }

    @Test
    void testResetPassword_ValidTokenNotExpiredNotUsed() {
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword123")).thenReturn("newEncodedPassword");
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(tokenRepository.save(resetToken)).thenReturn(resetToken);

        assertDoesNotThrow(() -> authService.resetPassword(resetPasswordRequest));

        verify(userRepository).save(testUser);
        assertTrue(resetToken.isUsed());
    }

    @Test
    void testSignup_AuthResponseFields() {
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.signup(signupRequest);

        assertEquals("jwt-token", response.getToken());
        assertEquals("test@test.com", response.getEmail());
        assertEquals("Test User", response.getName());
        assertEquals(User.Role.CUSTOMER, response.getRole());
    }

    @Test
    void testLogin_AuthResponseFields() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtUtil.generateToken("test@test.com")).thenReturn("jwt-token");

        AuthResponse response = authService.login(authRequest);

        assertEquals("jwt-token", response.getToken());
        assertEquals("test@test.com", response.getEmail());
        assertEquals("Test User", response.getName());
        assertEquals(User.Role.CUSTOMER, response.getRole());
    }

    @Test
    void testResetPassword_BothExpiredAndUsed() {
        resetToken.setExpiryDate(LocalDateTime.now().minusHours(2));
        resetToken.setUsed(true);
        when(tokenRepository.findByToken("reset-token-123")).thenReturn(Optional.of(resetToken));

        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> authService.resetPassword(resetPasswordRequest)
        );

        assertEquals("Token expired or already used", exception.getMessage());
    }

    @Test
    void testForgotPassword_TokenSaveVerification() {
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(testUser));
        doNothing().when(tokenRepository).deleteByEmail("test@test.com");
        when(tokenRepository.save(any(PasswordResetToken.class))).thenReturn(resetToken);
        doNothing().when(emailService).sendPasswordResetEmail(anyString(), anyString());

        authService.forgotPassword(forgotPasswordRequest);

        verify(tokenRepository).save(argThat(token -> 
            token.getEmail().equals("test@test.com") && 
            token.getToken() != null &&
            !token.isUsed()
        ));
    }
}