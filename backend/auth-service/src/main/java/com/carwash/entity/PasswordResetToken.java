package com.carwash.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String token;
    
    private String email;
    
    private LocalDateTime expiryDate;
    
    private boolean used = false;
    
    public PasswordResetToken() {}
    
    public PasswordResetToken(String token, String email) {
        this.token = token;
        this.email = email;
        this.expiryDate = LocalDateTime.now().plusHours(1);
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDateTime getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDateTime expiryDate) { this.expiryDate = expiryDate; }
    
    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}