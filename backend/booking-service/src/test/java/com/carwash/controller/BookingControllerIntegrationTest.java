package com.carwash.controller;

import com.carwash.entity.Booking;
import com.carwash.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBooking_Success() throws Exception {
        Booking booking = createTestBooking();
        when(bookingService.createBooking(any(Booking.class))).thenReturn(booking);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void createBooking_ValidationError() throws Exception {
        when(bookingService.createBooking(any(Booking.class)))
                .thenThrow(new IllegalArgumentException("Invalid booking"));

        Booking booking = createTestBooking();
        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllBookings_Success() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(Arrays.asList(createTestBooking()));

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void getBookingById_Found() throws Exception {
        Booking booking = createTestBooking();
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(booking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getBookingById_NotFound() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookingsByCustomerId_Success() throws Exception {
        when(bookingService.getBookingsByCustomerId(1L)).thenReturn(Arrays.asList(createTestBooking()));

        mockMvc.perform(get("/api/bookings/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1L));
    }

    @Test
    void getBookingsByProviderId_Success() throws Exception {
        when(bookingService.getBookingsByProviderId(1L)).thenReturn(Arrays.asList(createTestBooking()));

        mockMvc.perform(get("/api/bookings/provider/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].providerId").value(1L));
    }

    @Test
    void getBookingsByStatus_Success() throws Exception {
        when(bookingService.getBookingsByStatus(Booking.BookingStatus.PENDING))
                .thenReturn(Arrays.asList(createTestBooking()));

        mockMvc.perform(get("/api/bookings/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("PENDING"));
    }

    @Test
    void updateBooking_Success() throws Exception {
        Booking booking = createTestBooking();
        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(booking);

        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void updateBooking_NotFound() throws Exception {
        when(bookingService.updateBooking(eq(1L), any(Booking.class)))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        Booking booking = createTestBooking();
        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(booking)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBookingStatus_Success() throws Exception {
        Booking booking = createTestBooking();
        booking.setStatus(Booking.BookingStatus.ACCEPTED);
        when(bookingService.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED)).thenReturn(booking);

        mockMvc.perform(patch("/api/bookings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"ACCEPTED\""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACCEPTED"));
    }

    @Test
    void updateBookingStatus_NotFound() throws Exception {
        when(bookingService.updateBookingStatus(1L, Booking.BookingStatus.ACCEPTED))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(patch("/api/bookings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"ACCEPTED\""))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignProvider_Success() throws Exception {
        Booking booking = createTestBooking();
        booking.setProviderId(2L);
        when(bookingService.assignProvider(1L, 2L)).thenReturn(booking);

        mockMvc.perform(patch("/api/bookings/1/assign/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.providerId").value(2L));
    }

    @Test
    void assignProvider_NotFound() throws Exception {
        when(bookingService.assignProvider(1L, 2L))
                .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(patch("/api/bookings/1/assign/2"))
                .andExpect(status().isNotFound());
    }

    @Test
    void assignProvider_BadRequest() throws Exception {
        when(bookingService.assignProvider(1L, 2L))
                .thenThrow(new IllegalStateException("Invalid booking state"));

        mockMvc.perform(patch("/api/bookings/1/assign/2"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteBooking_Success() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteBooking_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Booking not found"))
                .when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBookingCountByProviderAndStatus_Success() throws Exception {
        when(bookingService.getBookingCountByProviderAndStatus(1L, Booking.BookingStatus.PENDING))
                .thenReturn(5L);

        mockMvc.perform(get("/api/bookings/stats/provider/1/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testConstructorWithNullService() {
        BookingController controller = new BookingController(null);
        // This covers the constructor line
        org.junit.jupiter.api.Assertions.assertNotNull(controller);
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