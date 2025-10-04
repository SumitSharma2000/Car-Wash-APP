package com.carwash.service;

import com.carwash.entity.Booking;
import com.carwash.repository.BookingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for Booking entity operations
 * Contains business logic for booking management
 */
@Service
@Transactional
public class BookingService {
    
    // Constants for error messages to avoid duplication (sonar: fix S1192)
    private static final String BOOKING_NOT_FOUND_MSG = "Booking not found with id: ";
    private static final String CUSTOMER_ID_REQUIRED_MSG = "Customer ID is required";
    private static final String SERVICE_TYPE_REQUIRED_MSG = "Service type is required";
    private static final String SCHEDULED_TIME_REQUIRED_MSG = "Scheduled time is required";
    
    private final BookingRepository bookingRepository;
    
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    
    /**
     * Create a new booking
     * Validates booking data and sets default status
     */
    public Booking createBooking(Booking booking) {
        // Set default status if not provided
        if (booking.getStatus() == null) {
            booking.setStatus(Booking.BookingStatus.PENDING);
        }
        
        // Validate required fields
        if (booking.getCustomerId() == null) {
            throw new IllegalArgumentException(CUSTOMER_ID_REQUIRED_MSG);
        }
        if (booking.getServiceType() == null) {
            throw new IllegalArgumentException(SERVICE_TYPE_REQUIRED_MSG);
        }
        if (booking.getScheduledTime() == null) {
            throw new IllegalArgumentException(SCHEDULED_TIME_REQUIRED_MSG);
        }
        
        return bookingRepository.save(booking);
    }
    
    /**
     * Get booking by ID
     * Returns booking if found, empty optional otherwise
     */
    @Transactional(readOnly = true)
    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    /**
     * Get all bookings
     * Returns complete list of bookings in the system
     */
    @Transactional(readOnly = true)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    /**
     * Get bookings by customer ID
     * Returns all bookings for a specific customer
     */
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByCustomerId(Long customerId) {
        return bookingRepository.findByCustomerId(customerId);
    }
    
    /**
     * Get bookings by provider ID
     * Returns all bookings assigned to a specific service provider
     */
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByProviderId(Long providerId) {
        return bookingRepository.findByProviderId(providerId);
    }
    
    /**
     * Get bookings by status
     * Filters bookings based on their current status
     */
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
    
    /**
     * Update booking information
     * Updates existing booking data with validation
     */
    public Booking updateBooking(Long id, Booking updatedBooking) {
        return bookingRepository.findById(id)
            .map(booking -> {
                // Update booking fields while preserving ID and creation timestamp
                if (updatedBooking.getProviderId() != null) {
                    booking.setProviderId(updatedBooking.getProviderId());
                }
                if (updatedBooking.getServiceType() != null) {
                    booking.setServiceType(updatedBooking.getServiceType());
                }
                if (updatedBooking.getStatus() != null) {
                    booking.setStatus(updatedBooking.getStatus());
                }
                if (updatedBooking.getLocation() != null) {
                    booking.setLocation(updatedBooking.getLocation());
                }
                if (updatedBooking.getScheduledTime() != null) {
                    booking.setScheduledTime(updatedBooking.getScheduledTime());
                }
                if (updatedBooking.getPrice() != null) {
                    booking.setPrice(updatedBooking.getPrice());
                }
                return bookingRepository.save(booking);
            })
            .orElseThrow(() -> new IllegalArgumentException(BOOKING_NOT_FOUND_MSG + id));
    }
    
    /**
     * Update booking status
     * Specifically for status changes (accept, complete, cancel)
     */
    public Booking updateBookingStatus(Long id, Booking.BookingStatus status) {
        return bookingRepository.findById(id)
            .map(booking -> {
                booking.setStatus(status);
                return bookingRepository.save(booking);
            })
            .orElseThrow(() -> new IllegalArgumentException(BOOKING_NOT_FOUND_MSG + id));
    }
    
    /**
     * Assign provider to booking
     * Assigns a service provider to a pending booking
     */
    public Booking assignProvider(Long bookingId, Long providerId) {
        return bookingRepository.findById(bookingId)
            .map(booking -> {
                if (booking.getStatus() != Booking.BookingStatus.PENDING) {
                    throw new IllegalStateException("Can only assign provider to pending bookings");
                }
                booking.setProviderId(providerId);
                booking.setStatus(Booking.BookingStatus.ACCEPTED);
                return bookingRepository.save(booking);
            })
            .orElseThrow(() -> new IllegalArgumentException(BOOKING_NOT_FOUND_MSG + bookingId));
    }
    
    /**
     * Delete booking by ID
     * Removes booking from the system
     */
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new IllegalArgumentException(BOOKING_NOT_FOUND_MSG + id);
        }
        bookingRepository.deleteById(id);
    }
    
    /**
     * Get booking count by provider and status
     * Provides statistics for service provider dashboard
     */
    @Transactional(readOnly = true)
    public long getBookingCountByProviderAndStatus(Long providerId, Booking.BookingStatus status) {
        return bookingRepository.countByProviderIdAndStatus(providerId, status);
    }
}