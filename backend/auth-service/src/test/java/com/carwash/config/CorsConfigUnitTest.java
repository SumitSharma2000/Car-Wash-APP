package com.carwash.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.junit.jupiter.api.Assertions.*;

class CorsConfigUnitTest {

    @Test
    void testCorsConfigurationSource() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        assertNotNull(source);
        assertTrue(source instanceof UrlBasedCorsConfigurationSource);
    }

    @Test
    void testCorsConfigurationSettings() {
        CorsConfig corsConfig = new CorsConfig();
        CorsConfigurationSource source = corsConfig.corsConfigurationSource();
        
        UrlBasedCorsConfigurationSource urlSource = (UrlBasedCorsConfigurationSource) source;
        assertNotNull(urlSource.getCorsConfigurations());
        assertTrue(urlSource.getCorsConfigurations().containsKey("/api/**"));
    }

    @Test
    void testCorsConfigBean() {
        CorsConfig corsConfig = new CorsConfig();
        assertNotNull(corsConfig);
        assertNotNull(corsConfig.corsConfigurationSource());
    }
}