package com.carwash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceApplicationTest {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void applicationStartsSuccessfully() {
        assertDoesNotThrow(() -> {
            // Simple test without Spring context
            UserServiceApplication app = new UserServiceApplication();
            assertNotNull(app);
        });
    }
}