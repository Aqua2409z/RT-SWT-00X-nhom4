
package ch.jalu.injector.handlers.instantiation;

import ch.jalu.injector.exceptions.InjectorException;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultInjectionProvider_RBL4_b7272345Test {

    private final DefaultInjectionProvider provider = new DefaultInjectionProvider("ch.jalu.injector");

    @Test
    void testSafeGet_withAllowedClass_shouldReturnResolution() {
        Resolution<TestClass> resolution = provider.safeGet(TestClass.class);
        assertNotNull(resolution);
    }

    @Test
    void testSafeGet_withDisallowedClass_shouldThrowInjectorException() {
        assertThrows(InjectorException.class, () -> provider.safeGet(DisallowedClass.class));
    }

    @Test
    void testValidateInjection_withInjectConstructorAndFields_shouldThrowInjectorException() {
        assertThrows(InjectorException.class, () -> {
            Constructor<InvalidClass> constructor = InvalidClass.class.getConstructor();
            List<Field> fields = new ArrayList<>();
            fields.add(InvalidClass.class.getDeclaredField("field"));
            provider.validateInjection(InvalidClass.class, constructor, fields);
        });
    }

    @Test
    void testValidateInjection_withStaticField_shouldThrowInjectorException() {
        assertThrows(InjectorException.class, () -> {
            Constructor<InvalidStaticFieldClass> constructor = InvalidStaticFieldClass.class.getConstructor();
            List<Field> fields = new ArrayList<>();
            fields.add(InvalidStaticFieldClass.class.getDeclaredField("staticField"));
            provider.validateInjection(InvalidStaticFieldClass.class, constructor, fields);
        });
    }

    @Test
    void testValidateInjection_withValidClass_shouldNotThrow() {
        assertDoesNotThrow(() -> {
            Constructor<ValidClass> constructor = ValidClass.class.getConstructor();
            List<Field> fields = new ArrayList<>();
            provider.validateInjection(ValidClass.class, constructor, fields);
        });
    }

    static class DefaultInjectionProvider_RBL4_b7272345Test {}

    static class DefaultInjectionProvider_RBL4_b7272345Test {}

    static class DefaultInjectionProvider_RBL4_b7272345Test {
        @Inject
        private String field;

        @Inject
        public InvalidClass() {}
    }

    static class DefaultInjectionProvider_RBL4_b7272345Test {
        @Inject
        private static String staticField;

        public InvalidStaticFieldClass() {}
    }

    static class DefaultInjectionProvider_RBL4_b7272345Test {
        @Inject
        public ValidClass() {}
    }
}
