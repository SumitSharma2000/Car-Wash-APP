package com.carwash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingServiceApplicationAdditionalTest {

    @Test
    void testApplicationClassExists() {
        assertNotNull(BookingServiceApplication.class);
    }

    @Test
    void testApplicationHasSpringBootApplicationAnnotation() {
        assertTrue(BookingServiceApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testApplicationCanBeInstantiated() {
        BookingServiceApplication app = new BookingServiceApplication();
        assertNotNull(app);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(BookingServiceApplication.class.getDeclaredMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublicStatic() throws NoSuchMethodException {
        var mainMethod = BookingServiceApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void testApplicationPackage() {
        assertEquals("com.carwash", BookingServiceApplication.class.getPackage().getName());
    }

    @Test
    void testApplicationClassModifiers() {
        assertTrue(java.lang.reflect.Modifier.isPublic(BookingServiceApplication.class.getModifiers()));
        assertFalse(java.lang.reflect.Modifier.isAbstract(BookingServiceApplication.class.getModifiers()));
    }

    @Test
    void testApplicationToString() {
        BookingServiceApplication app = new BookingServiceApplication();
        assertNotNull(app.toString());
        assertTrue(app.toString().contains("BookingServiceApplication"));
    }
}