package com.carwash.dto;

import jakarta.validation.constraints.NotBlank;

public class ResetPasswordRequest {
    @NotBlank
    private String token;
    
    @NotBlank
    private String newPassword;
    
    public ResetPasswordRequest() {
        // Default constructor required for JSON deserialization
    }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}