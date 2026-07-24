
package ch.jalu.injector.context;

import ch.jalu.injector.exceptions.InjectorException;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObjectIdentifier_RBL4_7460272fTest {

    @Retention(RetentionPolicy.RUNTIME)
    @interface TestAnnotation {}

    @Test
    void testGetResolutionType() {
        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, String.class);
        assertEquals(ResolutionType.SINGLETON, identifier.getResolutionType());
    }

    @Test
    void testGetType() {
        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, List.class);
        assertEquals(List.class, identifier.getType());
    }

    @Test
    void testGetTypeAsClassWithClassType() {
        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, String.class);
        assertEquals(String.class, identifier.getTypeAsClass());
    }

    @Test
    void testGetTypeAsClassWithParameterizedType() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            @Override
            public Type getRawType() {
                return List.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, type);
        assertEquals(List.class, identifier.getTypeAsClass());
    }

    @Test
    void testGetTypeAsClassThrowsExceptionForInvalidParameterizedType() {
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{};
            }

            @Override
            public Type getRawType() {
                return Object.class; // Not a Class type
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, type);
        assertThrows(InjectorException.class, identifier::getTypeAsClass);
    }

    @Test
    void testGetAnnotations() {
        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, String.class, new TestAnnotation() {});
        List<Annotation> annotations = identifier.getAnnotations();
        assertEquals(1, annotations.size());
        assertTrue(annotations.get(0) instanceof TestAnnotation);
    }

    @Test
    void testToString() {
        ObjectIdentifier identifier = new ObjectIdentifier(ResolutionType.SINGLETON, String.class);
        String expected = "ObjId[type=class ObjectIdentifier_RBL4_7460272fTest.lang.String, annotations=[]]";
        assertEquals(expected, identifier.toString());
    }
}
