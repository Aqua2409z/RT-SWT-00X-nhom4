
package com.ebayopensource.webrex.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.reflect.Method;

public class Reflects_RBL4_ebe207aeTest {

    @Test
    public void testGetClass() {
        Class<?> clazz = Reflects.ClassReflector.INSTANCE.getClass("java.lang.String");
        assertNotNull(clazz);
        assertEquals("java.lang.String", clazz.getName());
    }

    @Test
    public void testGetClassWithInvalidName() {
        Class<?> clazz = Reflects.ClassReflector.INSTANCE.getClass("invalid.ClassName");
        assertNull(clazz);
    }

    @Test
    public void testGetClass2() {
        Class<?> clazz = Reflects.ClassReflector.INSTANCE.getClass2("java.util.List");
        assertNotNull(clazz);
        assertEquals("java.util.List", clazz.getName());
    }

    @Test
    public void testGetNestedClass() {
        Class<?> nestedClass = Reflects.ClassReflector.INSTANCE.getNestedClass("com.ebayopensource.webrex.util.Reflects", "ClassReflector");
        assertNotNull(nestedClass);
        assertEquals("ClassReflector", nestedClass.getSimpleName());
    }

    @Test
    public void testGetNestedClassWithInvalidName() {
        Class<?> nestedClass = Reflects.ClassReflector.INSTANCE.getNestedClass("com.ebayopensource.webrex.util.Reflects", "InvalidClassName");
        assertNull(nestedClass);
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        MethodReflector methodReflector = new Reflects.MethodReflector();
        Method method = methodReflector.getMethod(String.class, "length");
        assertNotNull(method);
        assertEquals("length", method.getName());
        assertEquals(0, method.getParameterCount());
    }

    @Test
    public void testInvokeMethod() {
        Reflects.MethodReflector methodReflector = new Reflects.MethodReflector();
        String testString = "Hello, World!";
        Integer length = methodReflector.invokeMethod(testString, "length");
        assertEquals(Integer.valueOf(13), length);
    }

    @Test
    public void testInvokeStaticMethod() {
        Reflects.MethodReflector methodReflector = new Reflects.MethodReflector();
        Integer result = methodReflector.invokeStaticMethod(Math.class, "abs", -10);
        assertEquals(Integer.valueOf(10), result);
    }

    @Test
    public void testGetPropertyValue() {
        Reflects.MethodReflector methodReflector = new Reflects.MethodReflector();
        TestClass testInstance = new TestClass();
        String value = methodReflector.getPropertyValue(testInstance, "name");
        assertEquals("Test", value);
    }

    private static class Reflects_RBL4_ebe207aeTest {
        public String getName() {
            return "Test";
        }
    }
}
