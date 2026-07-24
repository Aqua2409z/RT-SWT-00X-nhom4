
package ch.jalu.injector.handlers.instantiation;

import ch.jalu.injector.context.ObjectIdentifier;
import ch.jalu.injector.utils.InjectorUtils;
import ch.jalu.injector.utils.ReflectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StandardInjection_RBL4_3a4b44fbTest {

    private Constructor<TestClass> constructor;
    private List<Field> fields;
    private StandardInjection<TestClass> standardInjection;

    @BeforeEach
    void setUp() throws NoSuchMethodException, NoSuchFieldException {
        constructor = TestClass.class.getConstructor(String.class);
        fields = new ArrayList<>();
        fields.add(TestClass.class.getDeclaredField("numberField"));
        standardInjection = new StandardInjection<>(constructor, fields);
    }

    @Test
    void testGetDependencies() {
        List<ObjectIdentifier> dependencies = standardInjection.getDependencies();
        assertEquals(2, dependencies.size());
        assertEquals(String.class, dependencies.get(0).getType());
        assertEquals(int.class, dependencies.get(1).getType());
    }

    @Test
    void testInstantiateWith() {
        TestClass instance = standardInjection.instantiateWith("Test", 42);
        assertNotNull(instance);
        assertEquals("Test", instance.getStringField());
        assertEquals(42, instance.getNumberField());
    }

    @Test
    void testInstantiateWithIncorrectNumberOfValues() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            standardInjection.instantiateWith("Test");
        });
        assertEquals("Number of values does not correspond to the expected number", exception.getMessage());
    }

    @Test
    void testIsInstantiation() {
        assertTrue(standardInjection.isInstantiation());
    }

    private static class StandardInjection_RBL4_3a4b44fbTest {
        private final String stringField;
        private int numberField;

        public TestClass(String stringField) {
            this.stringField = stringField;
        }

        public String getStringField() {
            return stringField;
        }

        public int getNumberField() {
            return numberField;
        }

        public void setNumberField(int numberField) {
            this.numberField = numberField;
        }
    }
}
