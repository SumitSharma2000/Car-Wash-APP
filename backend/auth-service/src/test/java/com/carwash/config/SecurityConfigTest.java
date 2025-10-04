package com.carwash.config;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;

class SecurityConfigTest {

    @Test
    void testSecurityConfigBeanCreation() {
        SecurityConfig securityConfig = new SecurityConfig();
        assertNotNull(securityConfig);
    }

    @Test
    void testPasswordEncoderBean() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        assertNotNull(passwordEncoder);
        
        String rawPassword = "testPassword";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        assertNotNull(encodedPassword);
        assertNotEquals(rawPassword, encodedPassword);
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }

    @Test
    void testPasswordEncoderConsistency() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String password = "samePassword";
        String encoded1 = passwordEncoder.encode(password);
        String encoded2 = passwordEncoder.encode(password);
        
        // BCrypt generates different hashes for same input
        assertNotEquals(encoded1, encoded2);
        
        // But both should match the original password
        assertTrue(passwordEncoder.matches(password, encoded1));
        assertTrue(passwordEncoder.matches(password, encoded2));
    }

    @Test
    void testPasswordEncoderWithDifferentPasswords() {
        SecurityConfig securityConfig = new SecurityConfig();
        PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
        
        String password1 = "password1";
        String password2 = "password2";
        
        String encoded1 = passwordEncoder.encode(password1);
        String encoded2 = passwordEncoder.encode(password2);
        
        assertTrue(passwordEncoder.matches(password1, encoded1));
        assertTrue(passwordEncoder.matches(password2, encoded2));
        assertFalse(passwordEncoder.matches(password1, encoded2));
        assertFalse(passwordEncoder.matches(password2, encoded1));
    }
}