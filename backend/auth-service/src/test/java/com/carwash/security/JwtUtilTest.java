package com.carwash.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private final String testEmail = "test@example.com";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // Set test values for JWT configuration
        ReflectionTestUtils.setField(jwtUtil, "secret", "mySecretKeyForTestingPurposesOnly123456789");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 86400000L); // 24 hours
    }

    @Test
    void generateToken_ValidEmail_ShouldReturnToken() {
        String token = jwtUtil.generateToken(testEmail);
        
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void extractEmail_ValidToken_ShouldReturnEmail() {
        String token = jwtUtil.generateToken(testEmail);
        
        String extractedEmail = jwtUtil.extractEmail(token);
        
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    void isTokenValid_ValidToken_ShouldReturnTrue() {
        String token = jwtUtil.generateToken(testEmail);
        
        boolean isValid = jwtUtil.isTokenValid(token);
        
        assertTrue(isValid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid.token.here", ""})
    @NullSource
    void isTokenValid_InvalidTokens_ShouldReturnFalse(String token) {
        boolean isValid = jwtUtil.isTokenValid(token);
        
        assertFalse(isValid);
    }

    @Test
    void generateToken_NullEmail_ShouldGenerateTokenWithNullSubject() {
        // JWT library allows null subject, so this should not throw exception
        String token = jwtUtil.generateToken(null);
        assertNotNull(token);
    }

    @Test
    void generateToken_EmptyEmail_ShouldGenerateToken() {
        // Empty email should still generate a token (business logic decision)
        String token = jwtUtil.generateToken("");
        assertNotNull(token);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid.token", ""})
    @NullSource
    void extractEmail_InvalidTokens_ShouldThrowException(String token) {
        assertThrows(Exception.class, () -> jwtUtil.extractEmail(token));
    }
}