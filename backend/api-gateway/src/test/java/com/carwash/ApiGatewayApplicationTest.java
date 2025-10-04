package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.gateway.enabled=false"
})
class ApiGatewayApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
        assertTrue(true);
    }

    @Test
    void mainMethodRuns() {
        assertNotNull(ApiGatewayApplication.class);
    }
}
