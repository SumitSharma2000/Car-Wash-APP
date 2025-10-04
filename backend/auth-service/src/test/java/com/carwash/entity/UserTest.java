package com.carwash.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class UserTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(testUser);
        assertNull(testUser.getId());
        assertNull(testUser.getEmail());
        assertNull(testUser.getPassword());
        assertNull(testUser.getName());
        assertNull(testUser.getRole());
        assertNull(testUser.getPhone());
        assertNull(testUser.getAddress());
        assertEquals(LocalDateTime.now().toLocalDate(), testUser.getCreatedAt().toLocalDate());
    }

    @Test
    void testParameterizedConstructor() {
        User paramUser = new User("john@test.com", "password123", "John Doe", User.Role.CUSTOMER);
        
        assertEquals("john@test.com", paramUser.getEmail());
        assertEquals("password123", paramUser.getPassword());
        assertEquals("John Doe", paramUser.getName());
        assertEquals(User.Role.CUSTOMER, paramUser.getRole());
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        testUser.setId(1L);
        testUser.setEmail("test@example.com");
        testUser.setPassword("hashedPassword");
        testUser.setName("Test User");
        testUser.setRole(User.Role.SERVICE_PROVIDER);
        testUser.setPhone("1234567890");
        testUser.setAddress("123 Test St");
        testUser.setCreatedAt(now);

        assertEquals(1L, testUser.getId());
        assertEquals("test@example.com", testUser.getEmail());
        assertEquals("hashedPassword", testUser.getPassword());
        assertEquals("Test User", testUser.getName());
        assertEquals(User.Role.SERVICE_PROVIDER, testUser.getRole());
        assertEquals("1234567890", testUser.getPhone());
        assertEquals("123 Test St", testUser.getAddress());
        assertEquals(now, testUser.getCreatedAt());
    }

    @Test
    void testRoleEnum() {
        assertEquals(2, User.Role.values().length);
        assertEquals(User.Role.CUSTOMER, User.Role.valueOf("CUSTOMER"));
        assertEquals(User.Role.SERVICE_PROVIDER, User.Role.valueOf("SERVICE_PROVIDER"));
    }

    @Test
    void testCreatedAtDefaultValue() {
        User newUser = new User();
        assertNotNull(newUser.getCreatedAt());
        assertTrue(newUser.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(newUser.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }

    @Test
    void testSetEmail() {
        testUser.setEmail("newemail@test.com");
        assertEquals("newemail@test.com", testUser.getEmail());
    }

    @Test
    void testSetPassword() {
        testUser.setPassword("newPassword");
        assertEquals("newPassword", testUser.getPassword());
    }

    @Test
    void testSetName() {
        testUser.setName("New Name");
        assertEquals("New Name", testUser.getName());
    }

    @Test
    void testSetRole() {
        testUser.setRole(User.Role.CUSTOMER);
        assertEquals(User.Role.CUSTOMER, testUser.getRole());
        
        testUser.setRole(User.Role.SERVICE_PROVIDER);
        assertEquals(User.Role.SERVICE_PROVIDER, testUser.getRole());
    }

    @Test
    void testSetPhone() {
        testUser.setPhone("9876543210");
        assertEquals("9876543210", testUser.getPhone());
    }

    @Test
    void testSetAddress() {
        testUser.setAddress("456 New Address");
        assertEquals("456 New Address", testUser.getAddress());
    }
}