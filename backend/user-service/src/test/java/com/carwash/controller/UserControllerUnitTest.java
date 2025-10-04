package com.carwash.controller;

import com.carwash.entity.User;
import com.carwash.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserControllerUnitTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
    }

    @Test
    void testConstructorWithNullService() {
        UserController controller = new UserController(null);
        assertNotNull(controller);
    }

    @Test
    void createUser_Success() {
        User user = createTestUser();
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void createUser_ValidationError() {
        when(userService.createUser(any(User.class)))
                .thenThrow(new IllegalArgumentException("User already exists"));

        User user = createTestUser();
        ResponseEntity<User> response = userController.createUser(user);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllUsers_Success() {
        List<User> users = Arrays.asList(createTestUser());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getUserById_Found() {
        User user = createTestUser();
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserById_NotFound() {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getUserByEmail_Found() {
        User user = createTestUser();
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.of(user));

        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void getUserByEmail_NotFound() {
        when(userService.getUserByEmail("test@example.com")).thenReturn(Optional.empty());

        ResponseEntity<User> response = userController.getUserByEmail("test@example.com");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getUsersByRole_Success() {
        List<User> users = Arrays.asList(createTestUser());
        when(userService.getUsersByRole(User.Role.CUSTOMER)).thenReturn(users);

        ResponseEntity<List<User>> response = userController.getUsersByRole(User.Role.CUSTOMER);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void updateUser_Success() {
        User user = createTestUser();
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(user);

        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
    }

    @Test
    void updateUser_NotFound() {
        when(userService.updateUser(eq(1L), any(User.class)))
                .thenThrow(new IllegalArgumentException("User not found"));

        User user = createTestUser();
        ResponseEntity<User> response = userController.updateUser(1L, user);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteUser_Success() {
        doNothing().when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteUser_NotFound() {
        doThrow(new IllegalArgumentException("User not found"))
                .when(userService).deleteUser(1L);

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void searchUsers_Success() {
        List<User> users = Arrays.asList(createTestUser());
        when(userService.searchUsersByName("John")).thenReturn(users);

        ResponseEntity<List<User>> response = userController.searchUsers("John");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void getUserCountByRole_Success() {
        when(userService.getUserCountByRole(User.Role.CUSTOMER)).thenReturn(5L);

        ResponseEntity<Long> response = userController.getUserCountByRole(User.Role.CUSTOMER);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setName("Test User");
        user.setRole(User.Role.CUSTOMER);
        user.setPhone("1234567890");
        user.setCreatedAt(LocalDateTime.now());
        return user;
    }
}