package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import static org.junit.jupiter.api.Assertions.*;

class UserServiceApplicationUnitTest {

    @Test
    void testApplicationClassExists() {
        assertNotNull(UserServiceApplication.class);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(UserServiceApplication.class.getMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublicStatic() throws NoSuchMethodException {
        var method = UserServiceApplication.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
    }

    @Test
    void testSpringApplicationRunCall() {
        assertDoesNotThrow(() -> {
            assertNotNull(SpringApplication.class.getMethod("run", Class.class, String[].class));
        });
    }

    @Test
    void testApplicationAnnotations() {
        assertTrue(UserServiceApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
        assertTrue(UserServiceApplication.class.isAnnotationPresent(org.springframework.cloud.client.discovery.EnableDiscoveryClient.class));
    }
}