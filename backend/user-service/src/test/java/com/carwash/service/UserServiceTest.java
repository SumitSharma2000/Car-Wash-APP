package com.carwash.service;

import com.carwash.entity.User;
import com.carwash.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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
    void testCreateUser_Success() {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(userRepository.save(testUser)).thenReturn(testUser);

        User result = userService.createUser(testUser);

        assertEquals(testUser, result);
        verify(userRepository).existsByEmail(testUser.getEmail());
        verify(userRepository).save(testUser);
    }

    @Test
    void testCreateUser_EmailExists() {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(testUser)
        );

        assertTrue(exception.getMessage().contains("already exists"));
        verify(userRepository).existsByEmail(testUser.getEmail());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<User> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    @Test
    void testGetUserByEmail_Found() {
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.getUserByEmail("john@test.com");

        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
        verify(userRepository).findByEmail("john@test.com");
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository).findAll();
    }

    @Test
    void testGetUsersByRole() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByRole(User.Role.CUSTOMER)).thenReturn(users);

        List<User> result = userService.getUsersByRole(User.Role.CUSTOMER);

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository).findByRole(User.Role.CUSTOMER);
    }

    @Test
    void testUpdateUser_Success() {
        User updatedUser = new User("Jane Doe", "john@test.com", "0987654321", User.Role.SERVICE_PROVIDER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User result = userService.updateUser(1L, updatedUser);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        User updatedUser = new User("Jane Doe", "jane@test.com", "0987654321", User.Role.SERVICE_PROVIDER);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.updateUser(1L, updatedUser)
        );

        assertTrue(exception.getMessage().contains("not found"));
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> userService.deleteUser(1L));

        verify(userRepository).existsById(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.deleteUser(1L)
        );

        assertTrue(exception.getMessage().contains("not found"));
        verify(userRepository).existsById(1L);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void testSearchUsersByName() {
        List<User> users = Arrays.asList(testUser);
        when(userRepository.findByNameContainingIgnoreCase("John")).thenReturn(users);

        List<User> result = userService.searchUsersByName("John");

        assertEquals(1, result.size());
        assertEquals(testUser, result.get(0));
        verify(userRepository).findByNameContainingIgnoreCase("John");
    }

    @Test
    void testGetUserCountByRole() {
        when(userRepository.countByRole(User.Role.CUSTOMER)).thenReturn(5L);

        long result = userService.getUserCountByRole(User.Role.CUSTOMER);

        assertEquals(5L, result);
        verify(userRepository).countByRole(User.Role.CUSTOMER);
    }
}