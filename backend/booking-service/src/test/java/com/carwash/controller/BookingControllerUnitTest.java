package com.carwash.controller;

import com.carwash.entity.Booking;
import com.carwash.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class BookingControllerUnitTest {

    @Mock
    private BookingService bookingService;

    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingController = new BookingController(bookingService);
    }

    @Test
    void testConstructorWithNullService() {
        BookingController controller = new BookingController(null);
        assertNotNull(controller);
    }

    @Test
    void createBooking_Success() {
        Booking booking = createTestBooking();
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void createBooking_ValidationError() {
        when(bookingService.createBooking(any(Booking.class)))
                .thenThrow(new IllegalArgumentException("Invalid booking"));

        Booking booking = createTestBooking();
        ResponseEntity<Booking> response = bookingController.createBooking(booking);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getAllBookings_Success() {
        List<Booking> bookings = Arrays.asList(createTestBooking());
        when(bookingService.getAllBookings()).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getAllBookings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void getBookingById_Found() {
        Booking booking = createTestBooking();
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        ResponseEntity<Booking> response = bookingController.getBookingById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void getBookingById_NotFound() {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Booking> response = bookingController.getBookingById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getBookingsByCustomerId_Success() {
        List<Booking> bookings = Arrays.asList(createTestBooking());
        when(bookingService.getBookingsByCustomerId(1L)).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getBookingsByCustomerId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void getBookingsByProviderId_Success() {
        List<Booking> bookings = Arrays.asList(createTestBooking());
        when(bookingService.getBookingsByProviderId(1L)).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getBookingsByProviderId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void getBookingsByStatus_Success() {
        List<Booking> bookings = Arrays.asList(createTestBooking());
        when(bookingService.getBookingsByStatus(Booking.BookingStatus.PENDING)).thenReturn(bookings);

        ResponseEntity<List<Booking>> response = bookingController.getBookingsByStatus(Booking.BookingStatus.PENDING);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(bookings, response.getBody());
    }

    @Test
    void updateBooking_Success() {
        Booking booking = createTestBooking();
        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.updateBooking(1L, booking);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void updateBooking_NotFound() {
        when(bookingService.updateBooking(eq(1L), any(Booking.class)))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        Booking booking = createTestBooking();
        ResponseEntity<Booking> response = bookingController.updateBooking(1L, booking);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void updateBookingStatus_Success() {
        Booking booking = createTestBooking();
        booking.setStatus(Booking.BookingStatus.ACCEPTED);
        when(bookingService.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED)).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void updateBookingStatus_NotFound() {
        when(bookingService.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        ResponseEntity<Booking> response = bookingController.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void assignProvider_Success() {
        Booking booking = createTestBooking();
        booking.setProviderId(2L);
        when(bookingService.assignProvider(1L, 2L)).thenReturn(booking);

        ResponseEntity<Booking> response = bookingController.assignProvider(1L, 2L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(booking, response.getBody());
    }

    @Test
    void assignProvider_NotFound() {
        when(bookingService.assignProvider(1L, 2L))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        ResponseEntity<Booking> response = bookingController.assignProvider(1L, 2L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void assignProvider_BadRequest() {
        when(bookingService.assignProvider(1L, 2L))
                .thenThrow(new IllegalStateException("Invalid booking state"));

        ResponseEntity<Booking> response = bookingController.assignProvider(1L, 2L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteBooking_Success() {
        doNothing().when(bookingService).deleteBooking(1L);

        ResponseEntity<Void> response = bookingController.deleteBooking(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void deleteBooking_NotFound() {
        doThrow(new IllegalArgumentException("Booking not found"))
                .when(bookingService).deleteBooking(1L);

        ResponseEntity<Void> response = bookingController.deleteBooking(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getBookingCountByProviderAndStatus_Success() {
        when(bookingService.getBookingCountByProviderAndStatus(1L, Booking.BookingStatus.PENDING))
                .thenReturn(5L);

        ResponseEntity<Long> response = bookingController.getBookingCountByProviderAndStatus(1L, Booking.BookingStatus.PENDING);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5L, response.getBody());
    }

    private Booking createTestBooking() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setCustomerId(1L);
        booking.setProviderId(1L);
        booking.setServiceType(Booking.ServiceType.BASIC_WASH);
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setScheduledTime(LocalDateTime.now().plusDays(1));
        booking.setLocation("Test Location");
        booking.setPrice(100.0);
        return booking;
    }
}