package com.carwash.service;

import com.carwash.entity.User;
import com.carwash.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceAdditionalTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("John Doe", "john@test.com", "1234567890", User.Role.CUSTOMER);
        testUser.setId(1L);
    }

    @Test
    void testGetUserByEmail_NotFound() {
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserByEmail("notfound@test.com");

        assertFalse(result.isPresent());
        verify(userRepository).findByEmail("notfound@test.com");
    }

    @Test
    void testUpdateUser_EmailChangeSuccess() {
        User updatedUser = new User("Jane Doe", "jane@test.com", "0987654321", User.Role.SERVICE_PROVIDER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("jane@test.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("jane@test.com");
        verify(userRepository).save(testUser);
    }

    @Test
    void testUpdateUser_EmailChangeConflict() {
        User updatedUser = new User("Jane Doe", "existing@test.com", "0987654321", User.Role.SERVICE_PROVIDER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.existsByEmail("existing@test.com")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.updateUser(1L, updatedUser)
        );

        assertTrue(exception.getMessage().contains("already in use"));
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail("existing@test.com");
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUser_SameEmail() {
        User updatedUser = new User("Jane Doe", "john@test.com", "0987654321", User.Role.SERVICE_PROVIDER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository, never()).existsByEmail(anyString());
        verify(userRepository).save(testUser);
    }

    @Test
    void testGetAllUsers_EmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<User> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUsersByRole_EmptyList() {
        when(userRepository.findByRole(User.Role.SERVICE_PROVIDER)).thenReturn(Collections.emptyList());

        List<User> result = userService.getUsersByRole(User.Role.SERVICE_PROVIDER);

        assertTrue(result.isEmpty());
        verify(userRepository).findByRole(User.Role.SERVICE_PROVIDER);
    }

    @Test
    void testSearchUsersByName_EmptyList() {
        when(userRepository.findByNameContainingIgnoreCase("NonExistent")).thenReturn(Collections.emptyList());

        List<User> result = userService.searchUsersByName("NonExistent");

        assertTrue(result.isEmpty());
        verify(userRepository).findByNameContainingIgnoreCase("NonExistent");
    }

    @Test
    void testGetUserCountByRole_ZeroCount() {
        when(userRepository.countByRole(User.Role.SERVICE_PROVIDER)).thenReturn(0L);

        long result = userService.getUserCountByRole(User.Role.SERVICE_PROVIDER);

        assertEquals(0L, result);
        verify(userRepository).countByRole(User.Role.SERVICE_PROVIDER);
    }

    @Test
    void testSearchUsersByName_CaseInsensitive() {
        when(userRepository.findByNameContainingIgnoreCase("john")).thenReturn(Collections.singletonList(testUser));

        List<User> result = userService.searchUsersByName("john");

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository).findByNameContainingIgnoreCase("john");
    }

    @Test
    void testGetUsersByRole_ServiceProvider() {
        User serviceProvider = new User("Provider", "provider@test.com", "5555555555", User.Role.SERVICE_PROVIDER);
        when(userRepository.findByRole(User.Role.SERVICE_PROVIDER)).thenReturn(Collections.singletonList(serviceProvider));

        List<User> result = userService.getUsersByRole(User.Role.SERVICE_PROVIDER);

        assertEquals(1, result.size());
        assertEquals(serviceProvider, result.get(0));
        verify(userRepository).findByRole(User.Role.SERVICE_PROVIDER);
    }

    @Test
    void testCreateUser_NullEmail() {
        User userWithNullEmail = new User("Test User", null, "1234567890", User.Role.CUSTOMER);
        when(userRepository.existsByEmail(null)).thenReturn(false);
        when(userRepository.save(userWithNullEmail)).thenReturn(userWithNullEmail);

        User result = userService.createUser(userWithNullEmail);

        assertEquals(userWithNullEmail, result);
        verify(userRepository).existsByEmail(null);
        verify(userRepository).save(userWithNullEmail);
    }

    @Test
    void testGetUserCountByRole_LargeCount() {
        when(userRepository.countByRole(User.Role.CUSTOMER)).thenReturn(1000L);

        long result = userService.getUserCountByRole(User.Role.CUSTOMER);

        assertEquals(1000L, result);
        verify(userRepository).countByRole(User.Role.CUSTOMER);
    }
}