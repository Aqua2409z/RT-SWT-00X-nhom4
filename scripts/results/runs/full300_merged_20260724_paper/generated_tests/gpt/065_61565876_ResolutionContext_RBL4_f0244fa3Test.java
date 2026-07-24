
package ch.jalu.injector.context;

import ch.jalu.injector.Injector;
import ch.jalu.injector.exceptions.InjectorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResolutionContext_RBL4_f0244fa3Test {

    private Injector injector;
    private ObjectIdentifier originalIdentifier;
    private ResolutionContext resolutionContext;

    @BeforeEach
    void setUp() {
        injector = new Injector(); // Assuming a default constructor exists
        originalIdentifier = new ObjectIdentifier(SomeClass.class); // Assuming SomeClass is a valid class
        resolutionContext = new ResolutionContext(injector, originalIdentifier);
    }

    @Test
    void testGetInjector() {
        assertEquals(injector, resolutionContext.getInjector());
    }

    @Test
    void testGetOriginalIdentifier() {
        assertEquals(originalIdentifier, resolutionContext.getOriginalIdentifier());
    }

    @Test
    void testGetIdentifier() {
        assertEquals(originalIdentifier, resolutionContext.getIdentifier());
    }

    @Test
    void testGetParents() {
        assertTrue(resolutionContext.getParents().isEmpty());
    }

    @Test
    void testSetIdentifierValid() {
        ObjectIdentifier childIdentifier = new ObjectIdentifier(ChildClass.class); // Assuming ChildClass is a valid subclass ResolutionContext_RBL4_f0244fa3Test SomeClass
        resolutionContext.setIdentifier(childIdentifier);
        assertEquals(childIdentifier, resolutionContext.getIdentifier());
    }

    @Test
    void testSetIdentifierInvalid() {
        ObjectIdentifier invalidIdentifier = new ObjectIdentifier(InvalidClass.class); // Assuming InvalidClass is not a subclass ResolutionContext_RBL4_f0244fa3Test SomeClass
        InjectorException exception = assertThrows(InjectorException.class, () -> {
            resolutionContext.setIdentifier(invalidIdentifier);
        });
        assertEquals("New mapped class '" + InvalidClass.class + "' is not a child of original class '" + SomeClass.class + "'", exception.getMessage());
    }

    @Test
    void testCreateChildContext() {
        ObjectIdentifier childIdentifier = new ObjectIdentifier(ChildClass.class);
        ResolutionContext childContext = resolutionContext.createChildContext(childIdentifier);
        assertNotNull(childContext);
        assertEquals(childIdentifier, childContext.getIdentifier());
        assertEquals(1, childContext.getParents().size());
        assertEquals(resolutionContext, childContext.getParents().get(0));
    }
}
