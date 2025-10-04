package com.carwash.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class BookingTest {

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(booking);
        assertNull(booking.getId());
        assertNull(booking.getCustomerId());
        assertNull(booking.getProviderId());
        assertNull(booking.getServiceType());
        assertEquals(Booking.BookingStatus.PENDING, booking.getStatus());
        assertNull(booking.getLocation());
        assertNull(booking.getScheduledTime());
        assertNull(booking.getPrice());
        assertNotNull(booking.getCreatedAt());
    }

    @Test
    void testGettersAndSetters() {
        LocalDateTime now = LocalDateTime.now();
        
        booking.setId(1L);
        booking.setCustomerId(100L);
        booking.setProviderId(200L);
        booking.setServiceType(Booking.ServiceType.PREMIUM_WASH);
        booking.setStatus(Booking.BookingStatus.ACCEPTED);
        booking.setLocation("123 Main St");
        booking.setScheduledTime(now);
        booking.setPrice(50.0);
        booking.setCreatedAt(now);

        assertEquals(1L, booking.getId());
        assertEquals(100L, booking.getCustomerId());
        assertEquals(200L, booking.getProviderId());
        assertEquals(Booking.ServiceType.PREMIUM_WASH, booking.getServiceType());
        assertEquals(Booking.BookingStatus.ACCEPTED, booking.getStatus());
        assertEquals("123 Main St", booking.getLocation());
        assertEquals(now, booking.getScheduledTime());
        assertEquals(50.0, booking.getPrice());
        assertEquals(now, booking.getCreatedAt());
    }

    @Test
    void testServiceTypeEnum() {
        assertEquals(3, Booking.ServiceType.values().length);
        assertEquals(Booking.ServiceType.BASIC_WASH, Booking.ServiceType.valueOf("BASIC_WASH"));
        assertEquals(Booking.ServiceType.PREMIUM_WASH, Booking.ServiceType.valueOf("PREMIUM_WASH"));
        assertEquals(Booking.ServiceType.FULL_DETAIL, Booking.ServiceType.valueOf("FULL_DETAIL"));
    }

    @Test
    void testBookingStatusEnum() {
        assertEquals(4, Booking.BookingStatus.values().length);
        assertEquals(Booking.BookingStatus.PENDING, Booking.BookingStatus.valueOf("PENDING"));
        assertEquals(Booking.BookingStatus.ACCEPTED, Booking.BookingStatus.valueOf("ACCEPTED"));
        assertEquals(Booking.BookingStatus.COMPLETED, Booking.BookingStatus.valueOf("COMPLETED"));
        assertEquals(Booking.BookingStatus.CANCELLED, Booking.BookingStatus.valueOf("CANCELLED"));
    }

    @Test
    void testDefaultStatus() {
        Booking newBooking = new Booking();
        assertEquals(Booking.BookingStatus.PENDING, newBooking.getStatus());
    }

    @Test
    void testCreatedAtDefaultValue() {
        Booking newBooking = new Booking();
        assertNotNull(newBooking.getCreatedAt());
        assertTrue(newBooking.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        assertTrue(newBooking.getCreatedAt().isAfter(LocalDateTime.now().minusSeconds(1)));
    }
}