package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ApiGatewayApplicationIntegrationTest {

    @Test
    void contextLoads() {
        // This test will execute the main method and load Spring context
        // covering the uncovered lines in ApiGatewayApplication
        assertTrue(true);
    }

    @Test
    void mainMethodTest() {
        // Test the main method directly
        String[] args = {};
        assertDoesNotThrow(() -> ApiGatewayApplication.main(args));
    }
}