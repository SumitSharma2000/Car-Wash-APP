package com.carwash.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigTest {

    @Test
    void testCorsWebFilterBean() {
        CorsConfig corsConfig = new CorsConfig();
        CorsWebFilter corsWebFilter = corsConfig.corsWebFilter();
        
        assertNotNull(corsWebFilter);
    }

    @Test
    void testCorsConfigurationCreation() {
        CorsConfig corsConfig = new CorsConfig();
        
        // Test that the configuration class can be instantiated
        assertNotNull(corsConfig);
    }
}
