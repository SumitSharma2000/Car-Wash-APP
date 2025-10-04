package com.carwash.controller;

import com.carwash.entity.User;
import com.carwash.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("John Doe", "john@test.com", "1234567890", User.Role.CUSTOMER);
        testUser.setId(1L);
    }

    @Test
    void testCreateUser_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@test.com"));

        verify(userService).createUser(any(User.class));
    }

    @Test
    void testCreateUser_BadRequest() throws Exception {
        when(userService.createUser(any(User.class))).thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService).createUser(any(User.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById_Found() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(userService).getUserById(1L);
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(1L);
    }

    @Test
    void testGetUserByEmail_Found() throws Exception {
        when(userService.getUserByEmail("john@test.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/email/john@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("john@test.com"));

        verify(userService).getUserByEmail("john@test.com");
    }

    @Test
    void testGetUsersByRole() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.getUsersByRole(User.Role.CUSTOMER)).thenReturn(users);

        mockMvc.perform(get("/api/users/role/CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService).getUsersByRole(User.Role.CUSTOMER);
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenThrow(new IllegalArgumentException("User not found"));

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isNotFound());

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).deleteUser(1L);
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("User not found")).when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(1L);
    }

    @Test
    void testSearchUsers() throws Exception {
        List<User> users = Arrays.asList(testUser);
        when(userService.searchUsersByName("John")).thenReturn(users);

        mockMvc.perform(get("/api/users/search").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());

        verify(userService).searchUsersByName("John");
    }

    @Test
    void testGetUserCountByRole() throws Exception {
        when(userService.getUserCountByRole(User.Role.CUSTOMER)).thenReturn(5L);

        mockMvc.perform(get("/api/users/stats/CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(userService).getUserCountByRole(User.Role.CUSTOMER);
    }
}