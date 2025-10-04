package com.carwash.controller;

import com.carwash.dto.AuthRequest;
import com.carwash.dto.AuthResponse;
import com.carwash.dto.SignupRequest;
import com.carwash.entity.User;
import com.carwash.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(AuthController.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupRequest signupRequest;
    private AuthRequest authRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail("test@test.com");
        signupRequest.setName("Test User");
        signupRequest.setPassword("password");
        signupRequest.setRole(User.Role.CUSTOMER);

        authRequest = new AuthRequest();
        authRequest.setEmail("test@test.com");
        authRequest.setPassword("password");

        authResponse = new AuthResponse();
        authResponse.setToken("test-token");
        authResponse.setEmail("test@test.com");
        authResponse.setName("Test User");
        authResponse.setRole(User.Role.CUSTOMER);
    }

    @Test
    @WithMockUser
    void signup_ShouldReturnAuthResponse_WhenValidRequest() throws Exception {
        when(authService.signup(any(SignupRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    @WithMockUser
    void signup_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        SignupRequest invalidRequest = new SignupRequest();
        // Missing required fields

        mockMvc.perform(post("/api/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void signup_ShouldReturnBadRequest_WhenServiceThrowsException() throws Exception {
        when(authService.signup(any(SignupRequest.class)))
                .thenThrow(new RuntimeException("Email already exists"));

        mockMvc.perform(post("/api/auth/signup")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void login_ShouldReturnAuthResponse_WhenValidCredentials() throws Exception {
        when(authService.login(any(AuthRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));
    }

    @Test
    @WithMockUser
    void login_ShouldReturnBadRequest_WhenInvalidCredentials() throws Exception {
        when(authService.login(any(AuthRequest.class)))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void login_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        AuthRequest invalidRequest = new AuthRequest();
        // Missing required fields

        mockMvc.perform(post("/api/auth/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}