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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerAdditionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@test.com", "1234567890", User.Role.CUSTOMER);
        testUser.setId(1L);
    }

    @Test
    void testCreateUser_ValidationError() throws Exception {
        when(userService.createUser(any(User.class)))
            .thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isBadRequest());

        verify(userService).createUser(any(User.class));
    }

    @Test
    void testGetAllUsers_EmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService).getUserById(1L);
    }

    @Test
    void testGetUserByEmail_Success() throws Exception {
        when(userService.getUserByEmail("test@test.com")).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/email/test@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@test.com"));

        verify(userService).getUserByEmail("test@test.com");
    }

    @Test
    void testGetUserByEmail_NotFound() throws Exception {
        when(userService.getUserByEmail("notfound@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/email/notfound@test.com"))
                .andExpect(status().isNotFound());

        verify(userService).getUserByEmail("notfound@test.com");
    }

    @Test
    void testGetUsersByRole_Success() throws Exception {
        when(userService.getUsersByRole(User.Role.CUSTOMER))
            .thenReturn(List.of(testUser));

        mockMvc.perform(get("/api/users/role/CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].role").value("CUSTOMER"));

        verify(userService).getUsersByRole(User.Role.CUSTOMER);
    }

    @Test
    void testGetUsersByRole_EmptyList() throws Exception {
        when(userService.getUsersByRole(User.Role.SERVICE_PROVIDER))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/role/SERVICE_PROVIDER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).getUsersByRole(User.Role.SERVICE_PROVIDER);
    }

    @Test
    void testUpdateUser_NotFound() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class)))
            .thenThrow(new IllegalArgumentException("User not found"));

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
        doThrow(new IllegalArgumentException("User not found"))
            .when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNotFound());

        verify(userService).deleteUser(1L);
    }

    @Test
    void testSearchUsersByName_Success() throws Exception {
        when(userService.searchUsersByName("Test")).thenReturn(List.of(testUser));

        mockMvc.perform(get("/api/users/search?name=Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Test User"));

        verify(userService).searchUsersByName("Test");
    }

    @Test
    void testSearchUsersByName_EmptyList() throws Exception {
        when(userService.searchUsersByName("NonExistent")).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/search?name=NonExistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(userService).searchUsersByName("NonExistent");
    }

    @Test
    void testGetUserCountByRole() throws Exception {
        when(userService.getUserCountByRole(User.Role.CUSTOMER)).thenReturn(10L);

        mockMvc.perform(get("/api/users/stats/CUSTOMER"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(userService).getUserCountByRole(User.Role.CUSTOMER);
    }

    @Test
    void testCreateUser_Success() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"));

        verify(userService).createUser(any(User.class));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById(1L)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@test.com"));

        verify(userService).getUserById(1L);
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(testUser);

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].email").value("test@test.com"));

        verify(userService).getAllUsers();
    }
}