package com.googlecode.fitchy.resolver;

import com.googlecode.fitchy.exception.AnnotationNotFoundException;
import com.googlecode.fitchy.resolver.AnnotationRetriever;
import org.junit.Before;
import org.junit.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class AnnotationRetrieverTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
    }

    public class TestClass {
        @TestAnnotation
        public void testMethod() {
        }
    }

    public class SubClass extends TestClass {
        public void testMethod() {
        }
    }

    private AnnotationRetriever retriever;

    @Before
    public void setUp() {
        retriever = new AnnotationRetriever(TestAnnotation.class, TestClass.class);
    }

    @Test
    public void testGetAnnotationFromMethod() {
        try {
            Method method = TestClass.class.getMethod("testMethod");
            TestAnnotation annotation = retriever.getAnnotation(method);
            assertNotNull(annotation);
        } catch (NoSuchMethodException | AnnotationNotFoundException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationFromImplementedMethod() {
        try {
            Method method = SubClass.class.getMethod("testMethod");
            TestAnnotation annotation = retriever.getAnnotation(method);
            assertNotNull(annotation);
        } catch (NoSuchMethodException | AnnotationNotFoundException e) {
            fail("Exception should not have been thrown: " + e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationNotFound() {
        try {
            Method method = SubClass.class.getMethod("toString");
            retriever.getAnnotation(method);
            fail("Expected AnnotationNotFoundException to be thrown");
        } catch (AnnotationNotFoundException e) {
            // Expected exception
        } catch (NoSuchMethodException e) {
            fail("NoSuchMethodException should not have been thrown: " + e.getMessage());
        }
    }
}
