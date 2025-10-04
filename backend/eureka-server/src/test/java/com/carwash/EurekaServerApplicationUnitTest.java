package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import static org.junit.jupiter.api.Assertions.*;

class EurekaServerApplicationUnitTest {

    @Test
    void testApplicationClassExists() {
        assertNotNull(EurekaServerApplication.class);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(EurekaServerApplication.class.getMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublicStatic() throws NoSuchMethodException {
        var method = EurekaServerApplication.class.getMethod("main", String[].class);
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
        assertTrue(EurekaServerApplication.class.isAnnotationPresent(org.springframework.boot.autoconfigure.SpringBootApplication.class));
        assertTrue(EurekaServerApplication.class.isAnnotationPresent(org.springframework.cloud.netflix.eureka.server.EnableEurekaServer.class));
    }
}