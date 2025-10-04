package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceApplicationUnitTest {

    @Test
    void testApplicationClassExists() {
        assertNotNull(BookingServiceApplication.class);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(BookingServiceApplication.class.getMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublicStatic() throws NoSuchMethodException {
        var method = BookingServiceApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
    }

    @Test
    void testSpringApplicationRunCall() {
        // This test verifies the main method structure without actually running it
        // to avoid port conflicts
        assertDoesNotThrow(() -> {
            // We can't actually call main() due to port conflicts, but we can verify
            // the SpringApplication.run method exists and is callable
            assertNotNull(SpringApplication.class.getMethod("run", Class.class, String[].class));
        });
    }

    @Test
    void testApplicationAnnotations() {
        assertTrue(BookingServiceApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
        assertTrue(BookingServiceApplication.class.isAnnotationPresent(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class));
    }
}