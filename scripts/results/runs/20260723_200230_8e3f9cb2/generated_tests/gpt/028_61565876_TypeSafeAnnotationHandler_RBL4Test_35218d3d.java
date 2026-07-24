
package ch.jalu.injector.handlers.dependency;

import ch.jalu.injector.context.ResolutionContext;
import ch.jalu.injector.handlers.instantiation.Resolution;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TypeSafeAnnotationHandler_RBL4Test_35218d3d {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface TestAnnotation {}

    private class TypeSafeAnnotationHandler_RBL4Test_35218d3d extends TypeSafeAnnotationHandler<TestAnnotation> {
        @Override
        protected Class<TestAnnotation> getAnnotationType() {
            return TestAnnotation.class;
        }

        @Override
        protected Resolution<?> resolveValueSafely(ResolutionContext context, TestAnnotation annotation) {
            return new Resolution<>(String.class, "Resolved Value");
        }
    }

    private ResolutionContext context;
    private TestHandler handler;

    @BeforeEach
    void setUp() {
        context = mock(ResolutionContext.class);
        handler = new TestHandler();
    }

    @Test
    void testResolveWithAnnotationPresent() throws Exception {
        when(context.getIdentifier().getAnnotations()).thenReturn(new Annotation[]{new TestAnnotation() {}});

        Resolution<?> resolution = handler.resolve(context);

        assertNotNull(resolution);
        assertEquals("Resolved Value", resolution.getValue());
    }

    @Test
    void testResolveWithNoAnnotationPresent() throws Exception {
        when(context.getIdentifier().getAnnotations()).thenReturn(new Annotation[]{});

        Resolution<?> resolution = handler.resolve(context);

        assertNull(resolution);
    }

    @Test
    void testResolveWithDifferentAnnotationPresent() throws Exception {
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.TYPE)
        @interface AnotherAnnotation {}

        when(context.getIdentifier().getAnnotations()).thenReturn(new Annotation[]{new AnotherAnnotation() {}});

        Resolution<?> resolution = handler.resolve(context);

        assertNull(resolution);
    }

    @Test
    void testResolveThrowsException() {
        when(context.getIdentifier().getAnnotations()).thenReturn(new Annotation[]{new TestAnnotation() {}});

        handler = new TypeSafeAnnotationHandler<TestAnnotation>() {
            @Override
            protected Class<TestAnnotation> getAnnotationType() {
                return TestAnnotation.class;
            }

            @Override
            protected Resolution<?> resolveValueSafely(ResolutionContext context, TestAnnotation annotation) throws Exception {
                throw new Exception("Test Exception");
            }
        };

        Exception exception = assertThrows(Exception.class, () -> handler.resolve(context));
        assertEquals("Test Exception", exception.getMessage());
    }
}
