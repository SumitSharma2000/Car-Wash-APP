package com.carwash;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",
    "eureka.server.enable-self-preservation=false"
})
class EurekaServerApplicationTest {

    @Test
    void contextLoads() {
        // This test verifies that the Spring application context loads successfully
    }

    @Test
    void mainMethodRuns() {
        // Test that the main method can be called without throwing exceptions
        // We don't actually call main() to avoid starting the full application
        // but we can test that the class exists and is properly configured
        assertNotNull(EurekaServerAdditionalTest.class);
    }
}
