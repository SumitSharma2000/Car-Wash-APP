package com.carwash.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ForgotPasswordRequest {
    @NotBlank
    @Email
    private String email;
    
    public ForgotPasswordRequest() {
        // Default constructor required for JSON deserialization
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}