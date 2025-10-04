package com.carwash.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    @Test
    void testCorsConfigBeanCreation() {
        CorsConfig corsConfig = new CorsConfig();
        assertNotNull(corsConfig);
    }

    @Test
    void testCorsConfigurationSourceBean() {
        CorsConfig corsConfig = new CorsConfig();
        assertDoesNotThrow(() -> {
            var corsConfigSource = corsConfig.corsConfigurationSource();
            assertNotNull(corsConfigSource);
        });
    }

    @Test
    void testCorsConfigurationSourceNotNull() {
        CorsConfig corsConfig = new CorsConfig();
        var corsConfigSource = corsConfig.corsConfigurationSource();
        assertNotNull(corsConfigSource);
    }
}