
package ch.jalu.injector.utils;

import ch.jalu.injector.exceptions.InjectorException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InjectorUtils_RBL4Test_32d528d0 {

    @Test
    void testCheckNotNull_withNonNullObject() {
        assertDoesNotThrow(() -> InjectorUtils.checkNotNull(new Object()));
    }

    @Test
    void testCheckNotNull_withNullObject() {
        InjectorException exception = assertThrows(InjectorException.class, () -> InjectorUtils.checkNotNull(null));
        assertEquals("Object may not be null", exception.getMessage());
    }

    @Test
    void testCheckNotNull_withCustomErrorMessage() {
        InjectorException exception = assertThrows(InjectorException.class, () -> InjectorUtils.checkNotNull(null, "Custom error"));
        assertEquals("Custom error", exception.getMessage());
    }

    @Test
    void testCheckNoNullValues_withNonNullValues() {
        assertDoesNotThrow(() -> InjectorUtils.checkNoNullValues(1, "test", new Object()));
    }

    @Test
    void testCheckNoNullValues_withNullValue() {
        InjectorException exception = assertThrows(InjectorException.class, () -> InjectorUtils.checkNoNullValues(1, null, new Object()));
        assertEquals("Object may not be null", exception.getMessage());
    }

    @Test
    void testContainsNullValue_withNoNullValues() {
        assertFalse(InjectorUtils.containsNullValue(1, "test", new Object()));
    }

    @Test
    void testContainsNullValue_withNullValue() {
        assertTrue(InjectorUtils.containsNullValue(1, null, new Object()));
    }

    @Test
    void testCheckArgument_withTrueExpression() {
        assertDoesNotThrow(() -> InjectorUtils.checkArgument(true, "Should not throw"));
    }

    @Test
    void testCheckArgument_withFalseExpression() {
        InjectorException exception = assertThrows(InjectorException.class, () -> InjectorUtils.checkArgument(false, "Argument is false"));
        assertEquals("Argument is false", exception.getMessage());
    }

    @Test
    void testRethrowException_withInjectorException() {
        InjectorException originalException = new InjectorException("Original exception");
        InjectorException exception = assertThrows(InjectorException.class, () -> InjectorUtils.rethrowException(originalException));
        assertEquals("Original exception", exception.getMessage());
    }

    @Test
    void testRethrowException_withNonInjectorException() {
        Exception originalException = new Exception("Non-injector exception");
        InjectorException exception = assertThrows(InjectorException.class, () -> InjectorUtils.rethrowException(originalException));
        assertEquals("An error occurred (see cause)", exception.getMessage());
        assertEquals(originalException, exception.getCause());
    }

    @Test
    void testFirstNotNull_withBothNonNull() {
        assertEquals("first", InjectorUtils.firstNotNull("first", "second"));
    }

    @Test
    void testFirstNotNull_withFirstNull() {
        assertEquals("second", InjectorUtils.firstNotNull(null, "second"));
    }

    @Test
    void testFirstNotNull_withBothNull() {
        assertNull(InjectorUtils.firstNotNull(null, null));
    }

    @Test
    void testCanInstantiate_withConcreteClass() {
        assertTrue(InjectorUtils.canInstantiate(String.class));
    }

    @Test
    void testCanInstantiate_withAbstractClass() {
        assertFalse(InjectorUtils.canInstantiate(java.util.AbstractList.class));
    }

    @Test
    void testCanInstantiate_withInterface() {
        assertFalse(InjectorUtils.canInstantiate(java.util.List.class));
    }

    @Test
    void testCanInstantiate_withEnum() {
        assertFalse(InjectorUtils.canInstantiate(Thread.State.class));
    }

    @Test
    void testCanInstantiate_withArray() {
        assertFalse(InjectorUtils.canInstantiate(String[].class));
    }
}
