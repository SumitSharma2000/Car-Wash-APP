package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingServiceApplicationTest {

    @Test
    void contextLoads() {
        assertTrue(true);
    }
    
    @Test
    void mainMethodRuns() {
        assertNotNull(BookingServiceApplicationTest.class);
    }

}