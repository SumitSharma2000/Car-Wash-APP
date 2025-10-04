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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

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
    }

    @Test
    void testCreateBooking_Success() {
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.createBooking(testBooking);

        assertEquals(testBooking, result);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testCreateBooking_MissingCustomerId() {
        testBooking.setCustomerId(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(testBooking));

        assertTrue(exception.getMessage().contains("Customer ID is required"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCreateBooking_MissingServiceType() {
        testBooking.setServiceType(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(testBooking));

        assertTrue(exception.getMessage().contains("Service type is required"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCreateBooking_MissingScheduledTime() {
        testBooking.setScheduledTime(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.createBooking(testBooking));

        assertTrue(exception.getMessage().contains("Scheduled time is required"));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testCreateBooking_DefaultStatus() {
        testBooking.setStatus(null);
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.createBooking(testBooking);

        assertEquals(Booking.BookingStatus.PENDING, result.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testGetBookingById_Found() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        Optional<Booking> result = bookingService.getBookingById(1L);

        assertTrue(result.isPresent());
        assertEquals(testBooking, result.get());
        verify(bookingRepository).findById(1L);
    }

    @Test
    void testGetBookingById_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Booking> result = bookingService.getBookingById(1L);

        assertFalse(result.isPresent());
        verify(bookingRepository).findById(1L);
    }

    @Test
    void testGetAllBookings() {
        List<Booking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findAll()).thenReturn(bookings);

        List<Booking> result = bookingService.getAllBookings();

        assertEquals(1, result.size());
        assertEquals(testBooking, result.get(0));
        verify(bookingRepository).findAll();
    }

    @Test
    void testGetBookingsByCustomerId() {
        List<Booking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findByCustomerId(100L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByCustomerId(100L);

        assertEquals(1, result.size());
        assertEquals(testBooking, result.get(0));
        verify(bookingRepository).findByCustomerId(100L);
    }

    @Test
    void testGetBookingsByProviderId() {
        List<Booking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findByProviderId(200L)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByProviderId(200L);

        assertEquals(1, result.size());
        assertEquals(testBooking, result.get(0));
        verify(bookingRepository).findByProviderId(200L);
    }

    @Test
    void testGetBookingsByStatus() {
        List<Booking> bookings = Arrays.asList(testBooking);
        when(bookingRepository.findByStatus(Booking.BookingStatus.PENDING)).thenReturn(bookings);

        List<Booking> result = bookingService.getBookingsByStatus(Booking.BookingStatus.PENDING);

        assertEquals(1, result.size());
        assertEquals(testBooking, result.get(0));
        verify(bookingRepository).findByStatus(Booking.BookingStatus.PENDING);
    }

    @Test
    void testUpdateBooking_Success() {
        Booking updatedBooking = new Booking();
        updatedBooking.setProviderId(300L);
        updatedBooking.setStatus(Booking.BookingStatus.ACCEPTED);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBooking(1L, updatedBooking);

        assertNotNull(result);
        verify(bookingRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testUpdateBooking_NotFound() {
        Booking updatedBooking = new Booking();
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.updateBooking(1L, updatedBooking));

        assertTrue(exception.getMessage().contains("not found"));
        verify(bookingRepository).findById(1L);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testUpdateBookingStatus_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.updateBookingStatus(1L, Booking.BookingStatus.COMPLETED);

        assertNotNull(result);
        verify(bookingRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testAssignProvider_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(testBooking);

        Booking result = bookingService.assignProvider(1L, 200L);

        assertNotNull(result);
        verify(bookingRepository).findById(1L);
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void testAssignProvider_NotPending() {
        testBooking.setStatus(Booking.BookingStatus.COMPLETED);
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(testBooking));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> bookingService.assignProvider(1L, 200L));

        assertTrue(exception.getMessage().contains("pending"));
        verify(bookingRepository).findById(1L);
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void testDeleteBooking_Success() {
        when(bookingRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> bookingService.deleteBooking(1L));

        verify(bookingRepository).existsById(1L);
        verify(bookingRepository).deleteById(1L);
    }

    @Test
    void testDeleteBooking_NotFound() {
        when(bookingRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> bookingService.deleteBooking(1L));

        assertTrue(exception.getMessage().contains("not found"));
        verify(bookingRepository).existsById(1L);
        verify(bookingRepository, never()).deleteById(any());
    }

    @Test
    void testGetBookingCountByProviderAndStatus() {
        when(bookingRepository.countByProviderIdAndStatus(200L, Booking.BookingStatus.COMPLETED)).thenReturn(3L);

        long result = bookingService.getBookingCountByProviderAndStatus(200L, Booking.BookingStatus.COMPLETED);

        assertEquals(3L, result);
        verify(bookingRepository).countByProviderIdAndStatus(200L, Booking.BookingStatus.COMPLETED);
    }

    @Test
    void testUpdateBookingStatus_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED));
    }

    @Test
    void testAssignProvider_NotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> bookingService.assignProvider(1L, 10L));
    }

    @Test
    void testUpdateBooking_MultipleFields() {
        Booking existingBooking = new Booking();
        existingBooking.setId(1L);
        existingBooking.setLocation("Old Location");
        existingBooking.setProviderId(5L);
        existingBooking.setPrice(200.0);

        Booking updates = new Booking();
        updates.setLocation("New Location");
        updates.setProviderId(10L);
        updates.setPrice(500.0);

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(existingBooking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> i.getArguments()[0]);

        Booking updated = bookingService.updateBooking(1L, updates);

        assertEquals("New Location", updated.getLocation());
        assertEquals(10L, updated.getProviderId());
        assertEquals(500.0, updated.getPrice());
    }

    @Test
    void testCreateBooking_NullPriceAllowed() {
        Booking booking = new Booking();
        booking.setCustomerId(1L);
        booking.setProviderId(2L);
        booking.setServiceType(Booking.ServiceType.BASIC_WASH);
        booking.setScheduledTime(LocalDateTime.now().plusDays(1));
        booking.setLocation("Delhi");
        booking.setPrice(null); // price is null

        when(bookingRepository.save(any(Booking.class))).thenAnswer(i -> {
            Booking b = i.getArgument(0);
            b.setId(1L);
            return b;
        });

        Booking saved = bookingService.createBooking(booking);

        assertNotNull(saved.getId());
        assertEquals(Booking.BookingStatus.PENDING, saved.getStatus()); // default status
        assertNull(saved.getPrice()); // should be null
    }

}