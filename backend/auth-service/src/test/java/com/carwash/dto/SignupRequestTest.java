package com.carwash.dto;

import com.carwash.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    private SignupRequest signupRequest;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(signupRequest);
        assertNull(signupRequest.getEmail());
        assertNull(signupRequest.getPassword());
        assertNull(signupRequest.getName());
        assertNull(signupRequest.getRole());
        assertNull(signupRequest.getPhone());
        assertNull(signupRequest.getAddress());
    }

    @Test
    void testGettersAndSetters() {
        signupRequest.setEmail("test@example.com");
        signupRequest.setPassword("password123");
        signupRequest.setName("John Doe");
        signupRequest.setRole(User.Role.CUSTOMER);
        signupRequest.setPhone("1234567890");
        signupRequest.setAddress("123 Main St");

        assertEquals("test@example.com", signupRequest.getEmail());
        assertEquals("password123", signupRequest.getPassword());
        assertEquals("John Doe", signupRequest.getName());
        assertEquals(User.Role.CUSTOMER, signupRequest.getRole());
        assertEquals("1234567890", signupRequest.getPhone());
        assertEquals("123 Main St", signupRequest.getAddress());
    }

    @Test
    void testSetEmail() {
        signupRequest.setEmail("user@test.com");
        assertEquals("user@test.com", signupRequest.getEmail());
    }

    @Test
    void testSetPassword() {
        signupRequest.setPassword("securePassword");
        assertEquals("securePassword", signupRequest.getPassword());
    }

    @Test
    void testSetName() {
        signupRequest.setName("Jane Smith");
        assertEquals("Jane Smith", signupRequest.getName());
    }

    @Test
    void testSetRoleCustomer() {
        signupRequest.setRole(User.Role.CUSTOMER);
        assertEquals(User.Role.CUSTOMER, signupRequest.getRole());
    }

    @Test
    void testSetRoleServiceProvider() {
        signupRequest.setRole(User.Role.SERVICE_PROVIDER);
        assertEquals(User.Role.SERVICE_PROVIDER, signupRequest.getRole());
    }

    @Test
    void testSetPhone() {
        signupRequest.setPhone("0987654321");
        assertEquals("0987654321", signupRequest.getPhone());
    }

    @Test
    void testSetAddress() {
        signupRequest.setAddress("456 Oak Ave");
        assertEquals("456 Oak Ave", signupRequest.getAddress());
    }

    @Test
    void testOptionalFieldsCanBeNull() {
        signupRequest.setPhone(null);
        signupRequest.setAddress(null);
        
        assertNull(signupRequest.getPhone());
        assertNull(signupRequest.getAddress());
    }
}