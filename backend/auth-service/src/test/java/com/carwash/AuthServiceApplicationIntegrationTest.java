package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthServiceApplicationIntegrationTest {

    @Test
    void contextLoads() {
        // This test will execute the main method and load Spring context
        assertTrue(true);
    }

    @Test
    void mainMethodTest() {
        String[] args = {};
        assertDoesNotThrow(() -> AuthServiceApplication.main(args));
    }
}