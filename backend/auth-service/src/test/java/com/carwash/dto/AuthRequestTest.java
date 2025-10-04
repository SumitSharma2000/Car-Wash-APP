package com.carwash.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class AuthRequestTest {

    private AuthRequest authRequest;

    @BeforeEach
    void setUp() {
        authRequest = new AuthRequest();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(authRequest);
        assertNull(authRequest.getEmail());
        assertNull(authRequest.getPassword());
    }

    @Test
    void testGettersAndSetters() {
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");

        assertEquals("test@example.com", authRequest.getEmail());
        assertEquals("password123", authRequest.getPassword());
    }

    @Test
    void testSetEmail() {
        authRequest.setEmail("user@test.com");
        assertEquals("user@test.com", authRequest.getEmail());
    }

    @Test
    void testSetPassword() {
        authRequest.setPassword("securePassword");
        assertEquals("securePassword", authRequest.getPassword());
    }

    @Test
    void testEmailCanBeNull() {
        authRequest.setEmail(null);
        assertNull(authRequest.getEmail());
    }

    @Test
    void testPasswordCanBeNull() {
        authRequest.setPassword(null);
        assertNull(authRequest.getPassword());
    }
}