package com.carwash.dto;

import com.carwash.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SignupRequest {
    @NotBlank
    @Email
    private String email;
    
    @NotBlank
    private String password;
    
    @NotBlank
    private String name;
    
    @NotNull
    private User.Role role;
    
    private String phone;
    private String address;
    
    public SignupRequest() {}
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public User.Role getRole() { return role; }
    public void setRole(User.Role role) { this.role = role; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}