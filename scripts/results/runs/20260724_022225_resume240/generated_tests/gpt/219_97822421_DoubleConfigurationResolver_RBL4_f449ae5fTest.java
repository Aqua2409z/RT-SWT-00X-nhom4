
package com.pchudzik.springmock.infrastructure.definition.registry;

import com.pchudzik.springmock.infrastructure.DoubleConfigurationParser;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DoubleConfigurationResolver_RBL4_f449ae5fTest {
    private DoubleConfigurationParser<Object, Annotation> configurationParser;
    private DoubleConfigurationResolver resolver;

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {}

    @Before
    public void setUp() {
        configurationParser = mock(DoubleConfigurationParser.class);
        resolver = new DoubleConfigurationResolver(TestAnnotation.class, configurationParser);
    }

    @Test
    public void testResolveMockConfigurationWithValidField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("mockField");
        when(configurationParser.parseMockConfiguration(anyString(), any())).thenReturn("mockConfig");

        Object result = resolver.resolveMockConfiguration("doubleName", field);

        assertEquals("mockConfig", result);
        verify(configurationParser).parseMockConfiguration("doubleName", null);
    }

    @Test
    public void testResolveSpyConfigurationWithValidField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("spyField");
        when(configurationParser.parseSpyConfiguration(anyString(), any())).thenReturn("spyConfig");

        Object result = resolver.resolveSpyConfiguration("doubleName", field);

        assertEquals("spyConfig", result);
        verify(configurationParser).parseSpyConfiguration("doubleName", null);
    }

    @Test
    public void testResolveMockConfigurationWithNoField() {
        when(configurationParser.parseMockConfiguration(anyString(), any())).thenReturn("mockConfig");

        Object result = resolver.resolveMockConfiguration("doubleName", DoubleConfigurationResolver.NO_FIELD);

        assertEquals("mockConfig", result);
        verify(configurationParser).parseMockConfiguration("doubleName", null);
    }

    @Test
    public void testResolveSpyConfigurationWithNoField() {
        when(configurationParser.parseSpyConfiguration(anyString(), any())).thenReturn("spyConfig");

        Object result = resolver.resolveSpyConfiguration("doubleName", DoubleConfigurationResolver.NO_FIELD);

        assertEquals("spyConfig", result);
        verify(configurationParser).parseSpyConfiguration("doubleName", null);
    }

    private static class DoubleConfigurationResolver_RBL4_f449ae5fTest {
        @TestAnnotation
        private Object mockField;

        @TestAnnotation
        private Object spyField;
    }
}
