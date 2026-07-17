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
        private String annotatedField;

        @Field
        private String defaultField;

        private String nonAnnotatedField;
    }

    @Test
    public void testGatherFieldIds() {
        FieldLocator fieldLocator = new FieldLocator();
        List<String> fieldIds = fieldLocator.gatherFieldIds(TestClass.class);
        
        assertEquals(2, fieldIds.size());
        assertTrue(fieldIds.contains("customName"));
        assertTrue(fieldIds.contains("defaultField"));
    }

    @Test
    public void testLocateByIdWithAnnotatedField() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "customName");
        
        assertNotNull(reflectionField);
        assertEquals("annotatedField", reflectionField.getField().getName());
    }

    @Test
    public void testLocateByIdWithDefaultField() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "defaultField");
        
        assertNotNull(reflectionField);
        assertEquals("defaultField", reflectionField.getField().getName());
    }

    @Test
    public void testLocateByIdWithNonAnnotatedField() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "nonAnnotatedField");
        
        assertNull(reflectionField);
    }

    @Test
    public void testLocateByIdWithConvertedFieldName() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "default field");
        
        assertNotNull(reflectionField);
        assertEquals("defaultField", reflectionField.getField().getName());
    }

    @Test
    public void testLocateByIdWithInvalidId() {
        FieldLocator fieldLocator = new FieldLocator();
        ReflectionField reflectionField = fieldLocator.locateById(TestClass.class, "invalidId");
        
        assertNull(reflectionField);
    }
}
