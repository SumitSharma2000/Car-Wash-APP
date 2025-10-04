package com.carwash;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceApplicationAdditionalTest {

    @Test
    void testApplicationClassExists() {
        assertNotNull(UserServiceApplication.class);
    }

    @Test
    void testApplicationHasSpringBootApplicationAnnotation() {
        assertTrue(UserServiceApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testApplicationCanBeInstantiated() {
        UserServiceApplication app = new UserServiceApplication();
        assertNotNull(app);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(UserServiceApplication.class.getDeclaredMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublicStatic() throws NoSuchMethodException {
        var mainMethod = UserServiceApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void testApplicationPackage() {
        assertEquals("com.carwash", UserServiceApplication.class.getPackage().getName());
    }

    @Test
    void testApplicationClassModifiers() {
        assertTrue(java.lang.reflect.Modifier.isPublic(UserServiceApplication.class.getModifiers()));
        assertFalse(java.lang.reflect.Modifier.isAbstract(UserServiceApplication.class.getModifiers()));
    }

    @Test
    void testApplicationToString() {
        UserServiceApplication app = new UserServiceApplication();
        assertNotNull(app.toString());
        assertTrue(app.toString().contains("UserServiceApplication"));
    }
}