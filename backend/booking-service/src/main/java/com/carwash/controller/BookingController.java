package com.carwash.controller;

import com.carwash.entity.Booking;
import com.carwash.service.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.lang.Nullable;
import java.util.List;
import java.util.Optional;

/**
 * REST Controller for Booking management operations
 * Provides HTTP endpoints for booking CRUD operations
 */
@RestController
@RequestMapping("/api/bookings")
// CORS handled globally via CorsConfig
public class BookingController {
    
    private final BookingService bookingService;
    
    public BookingController(@Nullable BookingService bookingService) {
        this.bookingService = bookingService;
    }
    
    /**
     * Create a new booking
     * POST /api/bookings
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        try {
            Booking createdBooking = bookingService.createBooking(booking);
            return new ResponseEntity<>(createdBooking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Return bad request if validation fails
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Get all bookings
     * GET /api/bookings
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get booking by ID
     * GET /api/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id) {
        Optional<Booking> booking = bookingService.getBookingById(id);
        return booking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get bookings by customer ID
     * GET /api/bookings/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Booking>> getBookingsByCustomerId(@PathVariable Long customerId) {
        List<Booking> bookings = bookingService.getBookingsByCustomerId(customerId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get bookings by provider ID
     * GET /api/bookings/provider/{providerId}
     */
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<Booking>> getBookingsByProviderId(@PathVariable Long providerId) {
        List<Booking> bookings = bookingService.getBookingsByProviderId(providerId);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Get bookings by status
     * GET /api/bookings/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Booking>> getBookingsByStatus(@PathVariable Booking.BookingStatus status) {
        List<Booking> bookings = bookingService.getBookingsByStatus(status);
        return ResponseEntity.ok(bookings);
    }
    
    /**
     * Update booking
     * PUT /api/bookings/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long id, @RequestBody Booking booking) {
        try {
            Booking updatedBooking = bookingService.updateBooking(id, booking);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            // Return not found if booking doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Update booking status
     * PATCH /api/bookings/{id}/status
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Booking> updateBookingStatus(@PathVariable Long id, @RequestBody Booking.BookingStatus status) {
        try {
            Booking updatedBooking = bookingService.updateBookingStatus(id, status);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            // Return not found if booking doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Assign provider to booking
     * PATCH /api/bookings/{id}/assign/{providerId}
     */
    @PatchMapping("/{id}/assign/{providerId}")
    public ResponseEntity<Booking> assignProvider(@PathVariable Long id, @PathVariable Long providerId) {
        try {
            Booking updatedBooking = bookingService.assignProvider(id, providerId);
            return ResponseEntity.ok(updatedBooking);
        } catch (IllegalArgumentException e) {
            // Return not found if booking doesn't exist
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            // Return bad request if booking is not in correct state
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * Delete booking
     * DELETE /api/bookings/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        try {
            bookingService.deleteBooking(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            // Return not found if booking doesn't exist
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Get booking statistics for provider
     * GET /api/bookings/stats/provider/{providerId}/status/{status}
     */
    @GetMapping("/stats/provider/{providerId}/status/{status}")
    public ResponseEntity<Long> getBookingCountByProviderAndStatus(
            @PathVariable Long providerId, 
            @PathVariable Booking.BookingStatus status) {
        long count = bookingService.getBookingCountByProviderAndStatus(providerId, status);
        return ResponseEntity.ok(count);
    }
}