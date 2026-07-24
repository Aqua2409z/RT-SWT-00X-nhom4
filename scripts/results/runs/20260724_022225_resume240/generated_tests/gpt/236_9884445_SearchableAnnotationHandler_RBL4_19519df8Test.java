
package org.minnal.instrument.entity.metadata.handler;

import org.minnal.instrument.entity.Searchable;
import org.minnal.instrument.entity.metadata.EntityMetaData;
import org.minnal.instrument.entity.metadata.ParameterMetaData;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class SearchableAnnotationHandler_RBL4_19519df8Test {

    private SearchableAnnotationHandler handler;
    private EntityMetaData metaData;

    @BeforeMethod
    public void setUp() {
        handler = new SearchableAnnotationHandler();
        metaData = mock(EntityMetaData.class);
    }

    @Test
    public void testHandleMethodWithSearchableAnnotation() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("getName");
        Searchable annotation = mock(Searchable.class);
        when(annotation.value()).thenReturn("customName");

        handler.handle(metaData, annotation, method);

        verify(metaData).addSearchField(new ParameterMetaData("customName", "customName", String.class));
    }

    @Test
    public void testHandleMethodWithEmptySearchableAnnotation() throws NoSuchMethodException {
        Method method = TestClass.class.getMethod("getAge");
        Searchable annotation = mock(Searchable.class);
        when(annotation.value()).thenReturn("");

        handler.handle(metaData, annotation, method);

        verify(metaData).addSearchField(new ParameterMetaData("getAge", "getAge", Integer.class));
    }

    @Test
    public void testHandleFieldWithSearchableAnnotation() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("name");
        Searchable annotation = mock(Searchable.class);
        when(annotation.value()).thenReturn("customFieldName");

        handler.handle(metaData, annotation, field);

        verify(metaData).addSearchField(new ParameterMetaData("customFieldName", "name", String.class));
    }

    @Test
    public void testHandleFieldWithEmptySearchableAnnotation() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("age");
        Searchable annotation = mock(Searchable.class);
        when(annotation.value()).thenReturn("");

        handler.handle(metaData, annotation, field);

        verify(metaData).addSearchField(new ParameterMetaData("age", "age", Integer.class));
    }

    @Test
    public void testGetAnnotationType() {
        Class<?> annotationType = handler.getAnnotationType();
        assert annotationType.equals(Searchable.class);
    }

    private static class SearchableAnnotationHandler_RBL4_19519df8Test {
        @Searchable("customName")
        public String getName() {
            return "Test";
        }

        @Searchable
        public Integer getAge() {
            return 30;
        }

        @Searchable("customFieldName")
        private String name;

        @Searchable
        private Integer age;
    }
}
