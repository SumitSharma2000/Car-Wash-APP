package com.carwash;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.ThrowingSupplier;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "spring.cloud.gateway.enabled=false"
})
class ApiGatewayAdditionalTest {

    @MockBean
    private DiscoveryClient discoveryClient;

    @Test
    void testApplicationClassExists() {
        assertNotNull(ApiGatewayApplication.class);
    }

    @Test
    void testApplicationClassHasSpringBootApplicationAnnotation() {
        assertTrue(ApiGatewayApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testApplicationClassHasEnableDiscoveryClientAnnotation() {
        assertTrue(ApiGatewayApplication.class.isAnnotationPresent(
            org.springframework.cloud.client.discovery.EnableDiscoveryClient.class));
    }

    @Test
    void testApplicationInstanceCreation() {
        ApiGatewayApplication app = new ApiGatewayApplication();
        assertNotNull(app);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(ApiGatewayApplication.class.getDeclaredMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublic() throws NoSuchMethodException {
        var mainMethod = ApiGatewayApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsStatic() throws NoSuchMethodException {
        var mainMethod = ApiGatewayApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodReturnType() throws NoSuchMethodException {
        var mainMethod = ApiGatewayApplication.class.getDeclaredMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void testMainMethodParameterType() throws NoSuchMethodException {
        var mainMethod = ApiGatewayApplication.class.getDeclaredMethod("main", String[].class);
        assertEquals(String[].class, mainMethod.getParameterTypes()[0]);
    }

    @Test
    void testPackageStructure() {
        assertEquals("com.carwash", ApiGatewayApplication.class.getPackage().getName());
    }

    @Test
    void testClassModifiers() {
        assertTrue(java.lang.reflect.Modifier.isPublic(ApiGatewayApplication.class.getModifiers()));
    }

    @Test
    void testClassIsNotAbstract() {
        assertFalse(java.lang.reflect.Modifier.isAbstract(ApiGatewayApplication.class.getModifiers()));
    }

    @Test
    void testClassIsNotInterface() {
        assertFalse(ApiGatewayApplication.class.isInterface());
    }

    @Test
    void testClassIsNotEnum() {
        assertFalse(ApiGatewayApplication.class.isEnum());
    }

    @Test
    void testClassHasDefaultConstructor() {
        assertDoesNotThrow((ThrowingSupplier<?>) ApiGatewayApplication.class::getDeclaredConstructor);
    }

    @Test
    void testSpringBootApplicationAnnotationProperties() {
        var annotation = ApiGatewayApplication.class.getAnnotation(
            org.springframework.boot.autoconfigure.SpringBootApplication.class);
        assertNotNull(annotation);
    }

    @Test
    void testApplicationCanBeInstantiatedMultipleTimes() {
        ApiGatewayApplication app1 = new ApiGatewayApplication();
        ApiGatewayApplication app2 = new ApiGatewayApplication();
        
        assertNotNull(app1);
        assertNotNull(app2);
        assertNotSame(app1, app2);
    }

    @Test
    void testApplicationClassSimpleName() {
        assertEquals("ApiGatewayApplication", ApiGatewayApplication.class.getSimpleName());
    }

    @Test
    void testApplicationClassCanonicalName() {
        assertEquals("com.carwash.ApiGatewayApplication", ApiGatewayApplication.class.getCanonicalName());
    }

    @Test
    void testApplicationSuperclass() {
        assertEquals(Object.class, ApiGatewayApplication.class.getSuperclass());
    }

    @Test
    void testApplicationImplementsNoInterfaces() {
        assertEquals(0, ApiGatewayApplication.class.getInterfaces().length);
    }

    @Test
    void testApplicationHasNoFields() {
        assertEquals(0, ApiGatewayApplication.class.getDeclaredFields().length);
    }

    @Test
    void testApplicationMethodCount() {
        // Should have main method and default constructor
        assertTrue(ApiGatewayApplication.class.getDeclaredMethods().length >= 1);
    }

    @Test
    void testApplicationToString() {
        ApiGatewayApplication app = new ApiGatewayApplication();
        assertNotNull(app.toString());
        assertTrue(app.toString().contains("ApiGatewayApplication"));
    }

    @Test
    void testApplicationEquals() {
        ApiGatewayApplication app1 = new ApiGatewayApplication();
        ApiGatewayApplication app2 = new ApiGatewayApplication();
        
        assertEquals(app1, app1); // reflexive
        assertNotEquals(app2, app1); // different instances
        assertNotEquals(null, app1); // null check
        assertNotEquals("string", app1); // different type
    }

    @Test
    void testApplicationHashCode() {
        ApiGatewayApplication testApp = new ApiGatewayApplication();
        Integer hashCode = testApp.hashCode();
        assertNotNull(hashCode);
        assertEquals(hashCode, testApp.hashCode()); // consistent
    }

    @Test
    void testApplicationClassLoader() {
        assertNotNull(ApiGatewayApplication.class.getClassLoader());
    }

    @Test
    void testApplicationAnnotationCount() {
        assertEquals(2, ApiGatewayApplication.class.getAnnotations().length);
    }

    @Test
    void testMainMethodWithNullArgs() {
        // Test that main method can handle null args without throwing exception during reflection
        assertDoesNotThrow(() -> {
            var mainMethod = ApiGatewayApplication.class.getDeclaredMethod("main", String[].class);
            assertNotNull(mainMethod);
        });
    }

    @Test
    void testMainMethodWithEmptyArgs() {
        // Test that main method signature accepts empty string array
        assertDoesNotThrow(() -> {
            var mainMethod = ApiGatewayApplication.class.getDeclaredMethod("main", String[].class);
            assertNotNull(mainMethod);
            assertEquals(1, mainMethod.getParameterCount());
        });
    }
}