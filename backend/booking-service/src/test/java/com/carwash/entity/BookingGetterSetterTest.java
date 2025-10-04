package com.carwash.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

class BookingGetterSetterTest {

    @Test
    void testScheduledTimeGetterSetter() {
        Booking booking = new Booking();
        LocalDateTime time = LocalDateTime.now().plusDays(1);
        
        booking.setScheduledTime(time);
        assertEquals(time, booking.getScheduledTime());
    }

    @Test
    void testPriceGetterSetter() {
        Booking booking = new Booking();
        Double price = 150.0;
        
        booking.setPrice(price);
        assertEquals(price, booking.getPrice());
    }

    @Test
    void testCreatedAtGetterSetter() {
        Booking booking = new Booking();
        LocalDateTime createdAt = LocalDateTime.now().minusHours(1);
        
        booking.setCreatedAt(createdAt);
        assertEquals(createdAt, booking.getCreatedAt());
    }
}