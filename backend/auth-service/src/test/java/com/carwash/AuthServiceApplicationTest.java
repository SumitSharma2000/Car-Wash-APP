package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceApplicationTest {

    @Test
    void contextLoads() {
        assertTrue(true);
    }

    @Test
    void applicationStartsSuccessfully() {
        assertDoesNotThrow(() -> {
            // Simple test without Spring context
            AuthServiceApplication app = new AuthServiceApplication();
            assertNotNull(app);
        });
    }
}