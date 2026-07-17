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
        @Override
        public void testMethod() {
        }
    }

    private AnnotationRetriever retriever;

    @Before
    public void setUp() {
        retriever = new AnnotationRetriever(TestAnnotation.class, TestClass.class);
    }

    @Test
    public void testGetAnnotationFound() {
        try {
            Method method = SubClass.class.getMethod("testMethod");
            TestAnnotation annotation = retriever.getAnnotation(method);
            assertNotNull("Annotation should be found", annotation);
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        } catch (AnnotationNotFoundException e) {
            fail("Annotation not found: " + e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationNotFound() {
        retriever = new AnnotationRetriever(TestAnnotation.class, SubClass.class);
        try {
            Method method = SubClass.class.getMethod("testMethod");
            retriever.getAnnotation(method);
            fail("Expected AnnotationNotFoundException was not thrown");
        } catch (AnnotationNotFoundException e) {
            // Expected exception
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        }
    }

    @Test
    public void testGetAnnotationFromSuperClass() {
        try {
            Method method = TestClass.class.getMethod("testMethod");
            TestAnnotation annotation = retriever.getAnnotation(method);
            assertNotNull("Annotation should be found", annotation);
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + e.getMessage());
        } catch (AnnotationNotFoundException e) {
            fail("Annotation not found: " + e.getMessage());
        }
    }
}
