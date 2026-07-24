
package ch.jalu.injector.utils;

import ch.jalu.injector.exceptions.InjectorException;
import ch.jalu.injector.exceptions.InjectorReflectionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionUtils_RBL4_00880516Test {

    private TestClass testInstance;

    @BeforeEach
    void setUp() throws Exception {
        testInstance = new TestClass();
    }

    @Test
    void testGetFieldValue() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("privateField");
        Object value = ReflectionUtils.getFieldValue(field, testInstance);
        assertEquals("initialValue", value);
    }

    @Test
    void testSetField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("privateField");
        ReflectionUtils.setField(field, testInstance, "newValue");
        assertEquals("newValue", ReflectionUtils.getFieldValue(field, testInstance));
    }

    @Test
    void testInvokeMethod() throws NoSuchMethodException {
        Method method = TestClass.class.getDeclaredMethod("privateMethod");
        Object result = ReflectionUtils.invokeMethod(method, testInstance);
        assertEquals("Hello, World!", result);
    }

    @Test
    void testNewInstance() throws NoSuchMethodException {
        Constructor<TestClass> constructor = TestClass.class.getDeclaredConstructor(String.class);
        TestClass newInstance = ReflectionUtils.newInstance(constructor, "newInstance");
        assertEquals("newInstance", ReflectionUtils.getFieldValue(TestClass.class.getDeclaredField("privateField"), newInstance));
    }

    @Test
    void testGetGenericType() {
        Class<?> genericType = ReflectionUtils.getGenericType(TestClass.class.getGenericSuperclass());
        assertEquals(String.class, genericType);
    }

    @Test
    void testGetCollectionType() {
        Class<?> collectionType = ReflectionUtils.getCollectionType(Set.class, TestClass.class.getGenericSuperclass());
        assertEquals(String.class, collectionType);
    }

    @Test
    void testToSuitableCollectionType() {
        Set<String> resultSet = new HashSet<>();
        resultSet.add("one");
        resultSet.add("two");

        Object listResult = ReflectionUtils.toSuitableCollectionType(List.class, resultSet);
        assertTrue(listResult instanceof java.util.List);
        assertEquals(2, ((java.util.List<?>) listResult).size());
    }

    @Test
    void testSafeGetDeclaredMethods() {
        Method[] methods = ReflectionUtils.safeGetDeclaredMethods(TestClass.class);
        assertEquals(1, methods.length);
    }

    @Test
    void testSafeGetDeclaredFields() {
        Field[] fields = ReflectionUtils.safeGetDeclaredFields(TestClass.class);
        assertEquals(1, fields.length);
    }

    private static class ReflectionUtils_RBL4_00880516Test {
        private String privateField = "initialValue";

        private String privateMethod() {
            return "Hello, World!";
        }

        public TestClass(String privateField) {
            this.privateField = privateField;
        }
    }
}
