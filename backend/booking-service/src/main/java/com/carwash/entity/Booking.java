package com.carwash.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long customerId;
    private Long providerId;
    
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
    
    private String location;
    private LocalDateTime scheduledTime;
    private Double price;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    public enum ServiceType {
        BASIC_WASH, PREMIUM_WASH, FULL_DETAIL
    }
    
    public enum BookingStatus {
        PENDING, ACCEPTED, COMPLETED, CANCELLED
    }
    
    public Booking() {}
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }
    
    public ServiceType getServiceType() { return serviceType; }
    public void setServiceType(ServiceType serviceType) { this.serviceType = serviceType; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}