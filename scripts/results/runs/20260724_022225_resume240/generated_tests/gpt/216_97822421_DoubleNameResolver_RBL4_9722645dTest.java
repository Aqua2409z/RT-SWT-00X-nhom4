
package com.pchudzik.springmock.infrastructure.definition.registry;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DoubleNameResolver_RBL4_9722645dTest {

    private final DoubleNameResolver resolver = new DoubleNameResolver();

    @Test
    public void testResolveDoubleName_WithName() {
        AnnotationDetails details = new AnnotationDetails("testName", Optional.empty());
        String result = resolver.resolveDoubleName(details);
        assertEquals("testName", result);
    }

    @Test
    public void testResolveDoubleName_WithFieldName() {
        AnnotationDetails details = new AnnotationDetails("testName", Optional.empty());
        String result = resolver.resolveDoubleName(details, "fieldName");
        assertEquals("fieldName", result);
    }

    @Test
    public void testResolveDoubleName_WithDoubleClass() {
        AnnotationDetails details = new AnnotationDetails("testName", Optional.of(TestClass.class));
        String result = resolver.resolveDoubleName(details);
        assertEquals("testClass", result);
    }

    @Test
    public void testResolveDoubleName_WithAllNulls() {
        AnnotationDetails details = new AnnotationDetails(null, Optional.empty());
        assertThrows(IllegalStateException.class, () -> resolver.resolveDoubleName(details));
    }

    @Test
    public void testResolveDoubleName_WithFieldNameAndDoubleClass() {
        AnnotationDetails details = new AnnotationDetails(null, Optional.of(TestClass.class));
        String result = resolver.resolveDoubleName(details, "fieldName");
        assertEquals("fieldName", result);
    }

    @Test
    public void testResolveDoubleName_WithDoubleClassAndNullFieldName() {
        AnnotationDetails details = new AnnotationDetails(null, Optional.of(TestClass.class));
        String result = resolver.resolveDoubleName(details, null);
        assertEquals("testClass", result);
    }

    private static class DoubleNameResolver_RBL4_9722645dTest {
    }
}
