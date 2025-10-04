package com.carwash.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(user);
        assertNull(user.getId());
        assertNull(user.getName());
        assertNull(user.getEmail());
        assertNull(user.getPhone());
        assertNull(user.getRole());
    }

    @Test
    void testParameterizedConstructor() {
        User testUser = new User("John Doe", "john@test.com", "1234567890", User.Role.CUSTOMER);
        
        assertEquals("John Doe", testUser.getName());
        assertEquals("john@test.com", testUser.getEmail());
        assertEquals("1234567890", testUser.getPhone());
        assertEquals(User.Role.CUSTOMER, testUser.getRole());
        assertNotNull(testUser.getCreatedAt());
        assertNotNull(testUser.getUpdatedAt());
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        user.setId(1L);
        user.setName("Jane Doe");
        user.setEmail("jane@test.com");
        user.setPhone("0987654321");
        user.setRole(User.Role.SERVICE_PROVIDER);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        assertEquals(1L, user.getId());
        assertEquals("Jane Doe", user.getName());
        assertEquals("jane@test.com", user.getEmail());
        assertEquals("0987654321", user.getPhone());
        assertEquals(User.Role.SERVICE_PROVIDER, user.getRole());
        assertEquals(now, user.getCreatedAt());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void testRoleEnum() {
        assertEquals(2, User.Role.values().length);
        assertEquals(User.Role.CUSTOMER, User.Role.valueOf("CUSTOMER"));
        assertEquals(User.Role.SERVICE_PROVIDER, User.Role.valueOf("SERVICE_PROVIDER"));
    }
}