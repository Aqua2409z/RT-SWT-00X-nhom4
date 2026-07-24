
package me.tomassetti.turin.typesystem;

import me.tomassetti.jvm.JvmType;
import me.tomassetti.turin.parser.ast.virtual.ArrayLength;
import me.tomassetti.turin.symbols.Symbol;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.*;

public class ArrayTypeUsage_RBL4_cf9a0032Test {

    private TypeUsage componentType;
    private ArrayTypeUsage arrayTypeUsage;

    @Before
    public void setUp() {
        componentType = new MockTypeUsage(); // Assuming MockTypeUsage is a mock implementation of TypeUsage
        arrayTypeUsage = new ArrayTypeUsage(componentType);
    }

    @Test
    public void testGetComponentType() {
        assertEquals(componentType, arrayTypeUsage.getComponentType());
    }

    @Test
    public void testEquals() {
        ArrayTypeUsage anotherArrayTypeUsage = new ArrayTypeUsage(componentType);
        assertTrue(arrayTypeUsage.equals(anotherArrayTypeUsage));
        assertFalse(arrayTypeUsage.equals(new ArrayTypeUsage(new MockTypeUsage())));
    }

    @Test
    public void testHashCode() {
        assertEquals(arrayTypeUsage.hashCode(), new ArrayTypeUsage(componentType).hashCode());
    }

    @Test
    public void testIsArray() {
        assertTrue(arrayTypeUsage.isArray());
    }

    @Test
    public void testIsReference() {
        assertTrue(arrayTypeUsage.isReference());
    }

    @Test
    public void testAsArrayTypeUsage() {
        assertEquals(arrayTypeUsage, arrayTypeUsage.asArrayTypeUsage());
    }

    @Test
    public void testJvmType() {
        JvmType expectedJvmType = new JvmType("[" + componentType.jvmType().getSignature());
        assertEquals(expectedJvmType, arrayTypeUsage.jvmType());
    }

    @Test
    public void testHasInstanceField() {
        assertTrue(arrayTypeUsage.hasInstanceField("length", new Symbol("testSymbol")));
        assertFalse(arrayTypeUsage.hasInstanceField("nonExistentField", new Symbol("testSymbol")));
    }

    @Test
    public void testGetInstanceField() {
        Symbol instance = new Symbol("testSymbol");
        assertTrue(arrayTypeUsage.getInstanceField("length", instance) instanceof ArrayLength);
        try {
            arrayTypeUsage.getInstanceField("nonExistentField", instance);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("An array has no field named nonExistentField", e.getMessage());
        }
    }

    @Test
    public void testGetMethod() {
        assertFalse(arrayTypeUsage.getMethod("someMethod", false).isPresent());
    }

    @Test
    public void testCanBeAssignedTo() {
        TypeUsage compatibleType = new ArrayTypeUsage(componentType);
        TypeUsage incompatibleType = new MockTypeUsage(); // Assuming this is not an array type
        assertTrue(arrayTypeUsage.canBeAssignedTo(compatibleType));
        assertFalse(arrayTypeUsage.canBeAssignedTo(incompatibleType));
    }

    @Test
    public void testToString() {
        assertTrue(arrayTypeUsage.toString().contains("ArrayTypeUsage"));
    }

    @Test
    public void testReplaceTypeVariables() {
        assertEquals(arrayTypeUsage, arrayTypeUsage.replaceTypeVariables(Collections.emptyMap()));
    }

    @Test
    public void testSameType() {
        ArrayTypeUsage sameTypeArray = new ArrayTypeUsage(componentType);
        ArrayTypeUsage differentTypeArray = new ArrayTypeUsage(new MockTypeUsage());
        assertTrue(arrayTypeUsage.sameType(sameTypeArray));
        assertFalse(arrayTypeUsage.sameType(differentTypeArray));
    }

    @Test
    public void testDescribe() {
        assertTrue(arrayTypeUsage.describe().contains("array of"));
    }
}
