
package me.tomassetti.turin.typesystem;

import com.google.common.collect.ImmutableList;
import me.tomassetti.jvm.JvmType;
import me.tomassetti.turin.resolvers.SymbolResolver;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class PrimitiveTypeUsage_RBL4_d8ff3e11Test {

    @Test
    public void testIsPrimitive() {
        assertTrue(PrimitiveTypeUsage.INT.isPrimitive());
        assertTrue(PrimitiveTypeUsage.BOOLEAN.isPrimitive());
    }

    @Test
    public void testReplaceTypeVariables() {
        assertEquals(PrimitiveTypeUsage.INT, PrimitiveTypeUsage.INT.replaceTypeVariables(ImmutableList.of()));
    }

    @Test
    public void testFindByJvmType() {
        assertEquals(Optional.of(PrimitiveTypeUsage.INT), PrimitiveTypeUsage.findByJvmType(new JvmType("I")));
        assertEquals(Optional.empty(), PrimitiveTypeUsage.findByJvmType(new JvmType("X")));
    }

    @Test
    public void testCanBeAssignedTo() {
        assertTrue(PrimitiveTypeUsage.INT.canBeAssignedTo(PrimitiveTypeUsage.LONG));
        assertFalse(PrimitiveTypeUsage.BYTE.canBeAssignedTo(PrimitiveTypeUsage.CHAR));
        assertTrue(PrimitiveTypeUsage.INT.canBeAssignedTo(PrimitiveTypeUsage.INT));
        assertTrue(PrimitiveTypeUsage.INT.canBeAssignedTo(PrimitiveTypeUsage.BYTE));
    }

    @Test
    public void testGetByName() {
        assertEquals(PrimitiveTypeUsage.INT, PrimitiveTypeUsage.getByName("int"));
        assertEquals(PrimitiveTypeUsage.INT, PrimitiveTypeUsage.getByName("Int"));
        assertThrows(IllegalArgumentException.class, () -> PrimitiveTypeUsage.getByName("nonexistent"));
    }

    @Test
    public void testTurinName() {
        assertEquals("Int", PrimitiveTypeUsage.INT.turinName());
        assertEquals("Boolean", PrimitiveTypeUsage.BOOLEAN.turinName());
    }

    @Test
    public void testEqualsAndHashCode() {
        assertEquals(PrimitiveTypeUsage.INT, PrimitiveTypeUsage.INT);
        assertNotEquals(PrimitiveTypeUsage.INT, PrimitiveTypeUsage.LONG);
        assertEquals(PrimitiveTypeUsage.INT.hashCode(), PrimitiveTypeUsage.INT.hashCode());
        assertNotEquals(PrimitiveTypeUsage.INT.hashCode(), PrimitiveTypeUsage.LONG.hashCode());
    }

    @Test
    public void testIsStoredInInt() {
        assertTrue(PrimitiveTypeUsage.INT.isStoredInInt());
        assertFalse(PrimitiveTypeUsage.LONG.isStoredInInt());
    }

    @Test
    public void testDescribe() {
        assertEquals("int", PrimitiveTypeUsage.INT.describe());
        assertEquals("boolean", PrimitiveTypeUsage.BOOLEAN.describe());
    }

    @Test
    public void testSameType() {
        assertTrue(PrimitiveTypeUsage.INT.sameType(PrimitiveTypeUsage.INT));
        assertFalse(PrimitiveTypeUsage.INT.sameType(PrimitiveTypeUsage.LONG));
    }

    @Test
    public void testHasInstanceField() {
        assertFalse(PrimitiveTypeUsage.INT.hasInstanceField("someField", null));
    }

    @Test
    public void testGetInstanceField() {
        assertThrows(IllegalArgumentException.class, () -> PrimitiveTypeUsage.INT.getInstanceField("someField", null));
    }

    @Test
    public void testGetMethod() {
        assertFalse(PrimitiveTypeUsage.INT.getMethod("someMethod", false).isPresent());
    }
}
