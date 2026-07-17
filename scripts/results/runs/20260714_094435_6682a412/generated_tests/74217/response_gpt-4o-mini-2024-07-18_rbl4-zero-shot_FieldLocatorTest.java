package flapjack.annotation;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.List;

public class FieldLocatorTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface Field {
        String value() default "";
    }

    public static class TestClass {
        @Field("customName")
        private String field1;

        @Field
        private String field2;

        private String field3;
    }

    @Test
    public void testGatherFieldIds() {
        FieldLocator fieldLocator = new FieldLocator();
        List<String> fieldIds = fieldLocator.gatherFieldIds(TestClass.class);
        assertEquals(3, fieldIds.size());
        assertTrue(fieldIds.contains("customName"));
        assertTrue(fieldIds.contains("field2"));
        assertTrue(fieldIds.contains("field3"));
    }

    @Test
    public void testLocateByIdWithAnnotationValue() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "customName");
        assertNotNull(reflectionField);
        assertEquals("field1", reflectionField.getField().getName());
    }

    @Test
    public void testLocateByIdWithFieldName() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "field2");
        assertNotNull(reflectionField);
        assertEquals("field2", reflectionField.getField().getName());
    }

    @Test
    public void testLocateByIdWithConvertedFieldName() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "field 3");
        assertNotNull(reflectionField);
        assertEquals("field3", reflectionField.getField().getName());
    }

    @Test
    public void testLocateByIdNotFound() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "nonExistentField");
        assertNull(reflectionField);
    }
}
