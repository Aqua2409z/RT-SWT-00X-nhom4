package com.hazelcast.jet.impl.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;

public class ReflectionUtils_RBL4_4f424f3bTest {

    @Test
    public void testLoadClass() {
        Class<?> clazz = ReflectionUtils.loadClass("java.lang.String");
        assertNotNull(clazz);
        assertEquals("java.lang.String", clazz.getName());
    }

    @Test(expected = RuntimeException.class)
    public void testLoadClassNotFound() {
        ReflectionUtils.loadClass("non.existent.ClassName");
    }

    @Test
    public void testNewInstance() {
        String instance = ReflectionUtils.newInstance(String.class.getClassLoader(), "java.lang.String");
        assertNotNull(instance);
        assertTrue(instance instanceof String);
    }

    @Test(expected = RuntimeException.class)
    public void testNewInstanceNotFound() {
        ReflectionUtils.newInstance(String.class.getClassLoader(), "non.existent.ClassName");
    }

    @Test
    public void testReadStaticFieldOrNull() {
        String value = ReflectionUtils.readStaticFieldOrNull("java.lang.Math", "PI");
        assertNotNull(value);
        assertEquals("3.141592653589793", String.valueOf(value));
    }

    @Test
    public void testReadStaticFieldOrNullNotFound() {
        Object value = ReflectionUtils.readStaticFieldOrNull("java.lang.Math", "NON_EXISTENT_FIELD");
        assertNull(value);
    }

    @Test
    public void testFindPropertySetter() throws NoSuchMethodException {
        Method setter = ReflectionUtils.findPropertySetter(TestClass.class, "name", String.class);
        assertNotNull(setter);
        assertEquals("setName", setter.getName());
    }

    @Test
    public void testFindPropertySetterNotFound() {
        Method setter = ReflectionUtils.findPropertySetter(TestClass.class, "nonExistent", String.class);
        assertNull(setter);
    }

    @Test
    public void testFindPropertyField() throws NoSuchFieldException {
        Field field = ReflectionUtils.findPropertyField(TestClass.class, "name");
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    public void testFindPropertyFieldNotFound() {
        Field field = ReflectionUtils.findPropertyField(TestClass.class, "nonExistent");
        assertNull(field);
    }

    @Test
    public void testNestedClassesOf() {
        Collection<Class<?>> nestedClasses = ReflectionUtils.nestedClassesOf(TestClass.class);
        assertNotNull(nestedClasses);
        assertTrue(nestedClasses.contains(TestClass.NestedClass.class));
    }

    @Test
    public void testResourcesOf() {
        Resources resources = ReflectionUtils.resourcesOf("java.lang");
        assertNotNull(resources);
        assertFalse(resources.classes().count() == 0);
    }

    @Test
    public void testToClassResourceId() {
        String resourceId = ReflectionUtils.toClassResourceId("java.lang.String");
        assertEquals("java/lang/String.class", resourceId);
    }

    public static class ReflectionUtils_RBL4_4f424f3bTest {
        public String name;

        public void setName(String name) {
            this.name = name;
        }

        public static class ReflectionUtils_RBL4_4f424f3bTest {
        }
    }
}
