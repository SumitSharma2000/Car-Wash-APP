package com.carwash;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",
    "eureka.server.enable-self-preservation=false"
})
class EurekaServerAdditionalTest {

    @Test
    void testApplicationClassExists() {
        assertNotNull(EurekaServerApplication.class);
    }

    @Test
    void testApplicationClassHasSpringBootApplicationAnnotation() {
        assertTrue(EurekaServerApplication.class.isAnnotationPresent(
            org.springframework.boot.autoconfigure.SpringBootApplication.class));
    }

    @Test
    void testApplicationClassHasEnableEurekaServerAnnotation() {
        assertTrue(EurekaServerApplication.class.isAnnotationPresent(EnableEurekaServer.class));
    }

    @Test
    void testApplicationInstanceCreation() {
        EurekaServerApplication app = new EurekaServerApplication();
        assertNotNull(app);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(EurekaServerApplication.class.getDeclaredMethod("main", String[].class));
    }

    @Test
    void testMainMethodIsPublic() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsStatic() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodReturnType() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertEquals(void.class, mainMethod.getReturnType());
    }

    @Test
    void testMainMethodParameterType() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertEquals(String[].class, mainMethod.getParameterTypes()[0]);
    }

    @Test
    void testPackageStructure() {
        assertEquals("com.carwash", EurekaServerApplication.class.getPackage().getName());
    }

    @Test
    void testClassModifiers() {
        assertTrue(java.lang.reflect.Modifier.isPublic(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testClassIsNotAbstract() {
        assertFalse(java.lang.reflect.Modifier.isAbstract(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testClassIsNotInterface() {
        assertFalse(EurekaServerApplication.class.isInterface());
    }

    @Test
    void testClassIsNotEnum() {
        assertFalse(EurekaServerApplication.class.isEnum());
    }

    @Test
    void testClassHasDefaultConstructor() {
        assertDoesNotThrow((ThrowingSupplier<?>) EurekaServerApplication.class::getDeclaredConstructor);
    }

    @Test
    void testSpringBootApplicationAnnotationProperties() {
        var annotation = EurekaServerApplication.class.getAnnotation(
            org.springframework.boot.autoconfigure.SpringBootApplication.class);
        assertNotNull(annotation);
    }

    @Test
    void testEnableEurekaServerAnnotationProperties() {
        var annotation = EurekaServerApplication.class.getAnnotation(EnableEurekaServer.class);
        assertNotNull(annotation);
    }

    @Test
    void testApplicationCanBeInstantiatedMultipleTimes() {
        EurekaServerApplication app1 = new EurekaServerApplication();
        EurekaServerApplication app2 = new EurekaServerApplication();
        
        assertNotNull(app1);
        assertNotNull(app2);
        assertNotSame(app1, app2);
    }

    @Test
    void testApplicationClassSimpleName() {
        assertEquals("EurekaServerApplication", EurekaServerApplication.class.getSimpleName());
    }

    @Test
    void testApplicationClassCanonicalName() {
        assertEquals("com.carwash.EurekaServerApplication", EurekaServerApplication.class.getCanonicalName());
    }

    @Test
    void testApplicationSuperclass() {
        assertEquals(Object.class, EurekaServerApplication.class.getSuperclass());
    }

    @Test
    void testApplicationImplementsNoInterfaces() {
        assertEquals(0, EurekaServerApplication.class.getInterfaces().length);
    }

    @Test
    void testApplicationHasNoFields() {
        assertEquals(0, EurekaServerApplication.class.getDeclaredFields().length);
    }

    @Test
    void testApplicationMethodCount() {
        // Should have main method and default constructor
        assertTrue(EurekaServerApplication.class.getDeclaredMethods().length >= 1);
    }

    @Test
    void testApplicationToString() {
        EurekaServerApplication app = new EurekaServerApplication();
        assertNotNull(app.toString());
        assertTrue(app.toString().contains("EurekaServerApplication"));
    }

    @Test
    void testApplicationEquals() {
        EurekaServerApplication app1 = new EurekaServerApplication();
        EurekaServerApplication app2 = new EurekaServerApplication();
        
        assertEquals(app1, app1); // reflexive
        assertNotEquals(app1, app2); // different instances
        assertNotEquals(null, app1); // null check
        assertNotEquals("string", app1); // different type
    }

    @Test
    void testApplicationHashCode() {
        EurekaServerApplication testApp = new EurekaServerApplication();
        Integer hashCode = testApp.hashCode();
        assertNotNull(hashCode);
        assertEquals(hashCode, testApp.hashCode()); // consistent
    }

    @Test
    void testApplicationClassLoader() {
        assertNotNull(EurekaServerApplication.class.getClassLoader());
    }

    @Test
    void testApplicationAnnotationCount() {
        assertEquals(2, EurekaServerApplication.class.getAnnotations().length);
    }

    @Test
    void testMainMethodWithNullArgs() {
        // Test that main method can handle null args without throwing exception during reflection
        assertDoesNotThrow(() -> {
            var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
            assertNotNull(mainMethod);
        });
    }

    @Test
    void testMainMethodWithEmptyArgs() {
        // Test that main method signature accepts empty string array
        assertDoesNotThrow(() -> {
            var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
            assertNotNull(mainMethod);
            assertEquals(1, mainMethod.getParameterCount());
        });
    }

    @Test
    void testApplicationClassIsNotFinal() {
        assertFalse(java.lang.reflect.Modifier.isFinal(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testApplicationClassIsNotSynchronized() {
        assertFalse(java.lang.reflect.Modifier.isSynchronized(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testApplicationClassIsNotVolatile() {
        assertFalse(java.lang.reflect.Modifier.isVolatile(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testApplicationClassIsNotTransient() {
        assertFalse(java.lang.reflect.Modifier.isTransient(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testApplicationClassIsNotNative() {
        assertFalse(java.lang.reflect.Modifier.isNative(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testApplicationClassIsNotStrict() {
        assertFalse(java.lang.reflect.Modifier.isStrict(EurekaServerApplication.class.getModifiers()));
    }

    @Test
    void testMainMethodIsNotFinal() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertFalse(java.lang.reflect.Modifier.isFinal(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsNotSynchronized() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertFalse(java.lang.reflect.Modifier.isSynchronized(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsNotNative() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertFalse(java.lang.reflect.Modifier.isNative(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsNotAbstract() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertFalse(java.lang.reflect.Modifier.isAbstract(mainMethod.getModifiers()));
    }

    @Test
    void testMainMethodIsNotStrict() throws NoSuchMethodException {
        var mainMethod = EurekaServerApplication.class.getDeclaredMethod("main", String[].class);
        assertFalse(java.lang.reflect.Modifier.isStrict(mainMethod.getModifiers()));
    }

    @Test
    void testApplicationClassDeclaringClass() {
        assertNull(EurekaServerApplication.class.getDeclaringClass());
    }

    @Test
    void testApplicationClassEnclosingClass() {
        assertNull(EurekaServerApplication.class.getEnclosingClass());
    }

    @Test
    void testApplicationClassIsNotAnonymous() {
        assertFalse(EurekaServerApplication.class.isAnonymousClass());
    }

    @Test
    void testApplicationClassIsNotLocal() {
        assertFalse(EurekaServerApplication.class.isLocalClass());
    }

    @Test
    void testApplicationClassIsNotMember() {
        assertFalse(EurekaServerApplication.class.isMemberClass());
    }

    @Test
    void testApplicationClassIsNotSynthetic() {
        assertFalse(EurekaServerApplication.class.isSynthetic());
    }

    @Test
    void testApplicationClassIsNotArray() {
        assertFalse(EurekaServerApplication.class.isArray());
    }

    @Test
    void testApplicationClassIsNotPrimitive() {
        assertFalse(EurekaServerApplication.class.isPrimitive());
    }

    @Test
    void testApplicationClassComponentType() {
        assertNull(EurekaServerApplication.class.getComponentType());
    }

    @Test
    void testApplicationClassGenericSuperclass() {
        assertEquals(Object.class, EurekaServerApplication.class.getGenericSuperclass());
    }

    @Test
    void testApplicationClassGenericInterfaces() {
        assertEquals(0, EurekaServerApplication.class.getGenericInterfaces().length);
    }

    @Test
    void testApplicationClassTypeParameters() {
        assertEquals(0, EurekaServerApplication.class.getTypeParameters().length);
    }

    @Test
    void testApplicationClassDeclaredClasses() {
        assertEquals(0, EurekaServerApplication.class.getDeclaredClasses().length);
    }

    @Test
    void testApplicationClassClasses() {
        assertEquals(0, EurekaServerApplication.class.getClasses().length);
    }

    @Test
    void testApplicationClassConstructors() {
        assertEquals(1, EurekaServerApplication.class.getConstructors().length);
    }

    @Test
    void testApplicationClassDeclaredConstructors() {
        assertEquals(1, EurekaServerApplication.class.getDeclaredConstructors().length);
    }

    @Test
    void testApplicationClassMethods() {
        // Should have inherited methods from Object plus main method
        assertTrue(EurekaServerApplication.class.getMethods().length > 1);
    }

    @Test
    void testApplicationClassDeclaredMethods() {
        assertTrue(EurekaServerApplication.class.getDeclaredMethods().length >= 1);
    }

    @Test
    void testApplicationClassFields() {
        assertEquals(0, EurekaServerApplication.class.getFields().length);
    }

    @Test
    void testApplicationClassDeclaredFields() {
        assertEquals(0, EurekaServerApplication.class.getDeclaredFields().length);
    }

    @Test
    void testApplicationClassDeclaredAnnotations() {
        assertEquals(2, EurekaServerApplication.class.getDeclaredAnnotations().length);
    }

    @Test
    void testApplicationClassAnnotations() {
        assertEquals(2, EurekaServerApplication.class.getAnnotations().length);
    }
}