package com.carwash.controller;

import com.carwash.entity.Booking;
import com.carwash.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookingController.class)
class BookingControllerAdditionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testCreateBooking_ValidationError() throws Exception {
        when(bookingService.createBooking(any(Booking.class)))
            .thenThrow(new IllegalArgumentException("Validation failed"));

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBooking)))
                .andExpect(status().isBadRequest());

        verify(bookingService).createBooking(any(Booking.class));
    }

    @Test
    void testGetAllBookings_EmptyList() throws Exception {
        when(bookingService.getAllBookings()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/bookings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookingService).getAllBookings();
    }

    @Test
    void testGetBookingById_NotFound() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isNotFound());

        verify(bookingService).getBookingById(1L);
    }

    @Test
    void testGetBookingsByCustomerId_Success() throws Exception {
        when(bookingService.getBookingsByCustomerId(100L))
            .thenReturn(List.of(testBooking));

        mockMvc.perform(get("/api/bookings/customer/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].customerId").value(100));

        verify(bookingService).getBookingsByCustomerId(100L);
    }

    @Test
    void testGetBookingsByProviderId_Success() throws Exception {
        testBooking.setProviderId(200L);
        when(bookingService.getBookingsByProviderId(200L))
            .thenReturn(List.of(testBooking));

        mockMvc.perform(get("/api/bookings/provider/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].providerId").value(200));

        verify(bookingService).getBookingsByProviderId(200L);
    }

    @Test
    void testGetBookingsByStatus_Success() throws Exception {
        when(bookingService.getBookingsByStatus(Booking.BookingStatus.PENDING))
            .thenReturn(List.of(testBooking));

        mockMvc.perform(get("/api/bookings/status/PENDING"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(bookingService).getBookingsByStatus(Booking.BookingStatus.PENDING);
    }

    @Test
    void testUpdateBooking_NotFound() throws Exception {
        when(bookingService.updateBooking(eq(1L), any(Booking.class)))
            .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBooking)))
                .andExpect(status().isNotFound());

        verify(bookingService).updateBooking(eq(1L), any(Booking.class));
    }

    @Test
    void testUpdateBookingStatus_Success() throws Exception {
        when(bookingService.updateBookingStatus(1L, Booking.BookingStatus.COMPLETED))
            .thenReturn(testBooking);

        mockMvc.perform(patch("/api/bookings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"COMPLETED\""))
                .andExpect(status().isOk());

        verify(bookingService).updateBookingStatus(1L, Booking.BookingStatus.COMPLETED);
    }

    @Test
    void testUpdateBookingStatus_NotFound() throws Exception {
        when(bookingService.updateBookingStatus(1L, Booking.BookingStatus.COMPLETED))
            .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(patch("/api/bookings/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"COMPLETED\""))
                .andExpect(status().isNotFound());

        verify(bookingService).updateBookingStatus(1L, Booking.BookingStatus.COMPLETED);
    }

    @Test
    void testAssignProvider_Success() throws Exception {
        when(bookingService.assignProvider(1L, 200L)).thenReturn(testBooking);

        mockMvc.perform(patch("/api/bookings/1/assign/200"))
                .andExpect(status().isOk());

        verify(bookingService).assignProvider(1L, 200L);
    }

    @Test
    void testAssignProvider_NotFound() throws Exception {
        when(bookingService.assignProvider(1L, 200L))
            .thenThrow(new IllegalArgumentException("Booking not found"));

        mockMvc.perform(patch("/api/bookings/1/assign/200"))
                .andExpect(status().isNotFound());

        verify(bookingService).assignProvider(1L, 200L);
    }

    @Test
    void testAssignProvider_BadRequest() throws Exception {
        when(bookingService.assignProvider(1L, 200L))
            .thenThrow(new IllegalStateException("Invalid state"));

        mockMvc.perform(patch("/api/bookings/1/assign/200"))
                .andExpect(status().isBadRequest());

        verify(bookingService).assignProvider(1L, 200L);
    }

    @Test
    void testDeleteBooking_Success() throws Exception {
        doNothing().when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNoContent());

        verify(bookingService).deleteBooking(1L);
    }

    @Test
    void testDeleteBooking_NotFound() throws Exception {
        doThrow(new IllegalArgumentException("Booking not found"))
            .when(bookingService).deleteBooking(1L);

        mockMvc.perform(delete("/api/bookings/1"))
                .andExpect(status().isNotFound());

        verify(bookingService).deleteBooking(1L);
    }

    @Test
    void testGetBookingCountByProviderAndStatus() throws Exception {
        when(bookingService.getBookingCountByProviderAndStatus(200L, Booking.BookingStatus.COMPLETED))
            .thenReturn(5L);

        mockMvc.perform(get("/api/bookings/stats/provider/200/status/COMPLETED"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(bookingService).getBookingCountByProviderAndStatus(200L, Booking.BookingStatus.COMPLETED);
    }

    @Test
    void testGetBookingsByCustomerId_EmptyList() throws Exception {
        when(bookingService.getBookingsByCustomerId(100L))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/bookings/customer/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookingService).getBookingsByCustomerId(100L);
    }

    @Test
    void testGetBookingsByProviderId_EmptyList() throws Exception {
        when(bookingService.getBookingsByProviderId(200L))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/bookings/provider/200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookingService).getBookingsByProviderId(200L);
    }

    @Test
    void testGetBookingsByStatus_EmptyList() throws Exception {
        when(bookingService.getBookingsByStatus(Booking.BookingStatus.CANCELLED))
            .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/bookings/status/CANCELLED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(bookingService).getBookingsByStatus(Booking.BookingStatus.CANCELLED);
    }

    @Test
    void testCreateBooking_Success() throws Exception {
        when(bookingService.createBooking(any(Booking.class))).thenReturn(testBooking);

        mockMvc.perform(post("/api/bookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBooking)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(100));

        verify(bookingService).createBooking(any(Booking.class));
    }

    @Test
    void testGetBookingById_Success() throws Exception {
        when(bookingService.getBookingById(1L)).thenReturn(Optional.of(testBooking));

        mockMvc.perform(get("/api/bookings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerId").value(100));

        verify(bookingService).getBookingById(1L);
    }

    @Test
    void testUpdateBooking_Success() throws Exception {
        when(bookingService.updateBooking(eq(1L), any(Booking.class))).thenReturn(testBooking);

        mockMvc.perform(put("/api/bookings/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBooking)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(bookingService).updateBooking(eq(1L), any(Booking.class));
    }
}