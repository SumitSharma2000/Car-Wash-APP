package com.carwash.service;

import com.carwash.entity.Booking;
import com.carwash.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceAdditionalTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    private Booking testBooking;

    @BeforeEach
    void setUp() {
        testBooking = new Booking();
        testBooking.setId(1L);
        testBooking.setCustomerId(100L);
        testBooking.setServiceType(Booking.ServiceType.BASIC_WASH);
        testBooking.setScheduledTime(LocalDateTime.now().plusDays(1));
        testBooking.setStatus(Booking.BookingStatus.PENDING);
        testBooking.setLocation("Test Location");
        testBooking.setPrice(50.0);
    }

    @Test
    void testUpdateBooking_OnlyProviderId() {
        Booking updateData = new Booking();
        updateData.setProviderId(200L);
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBooking(1L, updateData);

        assertNotNull(result);
        verify(bookingRepository).findById(1L);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void testUpdateBooking_OnlyLocation() {
        Booking updateData = new Booking();
        updateData.setLocation("New Location");
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBooking(1L, updateData);

        assertNotNull(result);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void testUpdateBooking_OnlyPrice() {
        Booking updateData = new Booking();
        updateData.setPrice(75.0);
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBooking(1L, updateData);

        assertNotNull(result);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void testUpdateBooking_OnlyScheduledTime() {
        Booking updateData = new Booking();
        updateData.setScheduledTime(LocalDateTime.now().plusDays(2));
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBooking(1L, updateData);

        assertNotNull(result);
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void testUpdateBookingStatus_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookingService.updateBookingStatus(1L, Booking.BookingStatus.COMPLETED)
        );

        assertTrue(exception.getMessage().contains("not found"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testAssignProvider_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> bookingService.assignProvider(1L, 200L)
        );

        assertTrue(exception.getMessage().contains("not found"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testGetAllBookings_EmptyList() {
        when(bookingRepository.findAll()).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.getAllBookings();

        assertTrue(result.isEmpty());
        verify(bookingRepository).findAll();
    }

    @Test
    void testGetBookingsByCustomerId_EmptyList() {
        when(bookingRepository.findByCustomerId(100L)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.getBookingsByCustomerId(100L);

        assertTrue(result.isEmpty());
        verify(bookingRepository).findByCustomerId(100L);
    }

    @Test
    void testGetBookingsByProviderId_EmptyList() {
        when(bookingRepository.findByProviderId(200L)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.getBookingsByProviderId(200L);

        assertTrue(result.isEmpty());
        verify(bookingRepository).findByProviderId(200L);
    }

    @Test
    void testGetBookingsByStatus_EmptyList() {
        when(bookingRepository.findByStatus(Booking.BookingStatus.CANCELLED)).thenReturn(Collections.emptyList());

        List<Booking> result = bookingService.getBookingsByStatus(Booking.BookingStatus.CANCELLED);

        assertTrue(result.isEmpty());
        verify(bookingRepository).findByStatus(Booking.BookingStatus.CANCELLED);
    }

    @Test
    void testGetBookingCountByProviderAndStatus_ZeroCount() {
        when(bookingRepository.countByProviderIdAndStatus(200L, Booking.BookingStatus.CANCELLED)).thenReturn(0L);

        long result = bookingService.getBookingCountByProviderAndStatus(200L, Booking.BookingStatus.CANCELLED);

        assertEquals(0L, result);
        verify(bookingRepository).countByProviderIdAndStatus(200L, Booking.BookingStatus.CANCELLED);
    }

    @Test
    void testCreateBooking_WithExistingStatus() {
        testBooking.setStatus(Booking.BookingStatus.ACCEPTED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.createBooking(testBooking);

        assertEquals(Booking.BookingStatus.ACCEPTED, result.getStatus());
        verify(bookingRepository).save(testBooking);
    }

    @Test
    void testAssignProvider_AllStatusTransitions() {
        testBooking.setStatus(Booking.BookingStatus.ACCEPTED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> bookingService.assignProvider(1L, 200L)
        );

        assertTrue(exception.getMessage().contains("pending"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testUpdateBooking_AllFieldsNull() {
        Booking updateData = new Booking();
        
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBooking(1L, updateData);

        assertNotNull(result);
        verify(bookingRepository).save(testBooking);
    }
}