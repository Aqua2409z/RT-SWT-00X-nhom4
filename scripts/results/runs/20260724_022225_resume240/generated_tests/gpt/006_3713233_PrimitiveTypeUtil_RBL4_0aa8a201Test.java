package org.junithelper.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class PrimitiveTypeUtil_RBL4_0aa8a201Test {

    @Test
    public void testIsPrimitive() {
        assertTrue(PrimitiveTypeUtil.isPrimitive("byte"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("short"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("int"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("long"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("char"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("float"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("double"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("boolean"));
        assertTrue(PrimitiveTypeUtil.isPrimitive("void"));
        assertFalse(PrimitiveTypeUtil.isPrimitive("String"));
        assertFalse(PrimitiveTypeUtil.isPrimitive(null));
        assertFalse(PrimitiveTypeUtil.isPrimitive(""));
    }

    @Test
    public void testGetPrimitiveClass() {
        assertEquals(byte.class, PrimitiveTypeUtil.getPrimitiveClass("byte"));
        assertEquals(short.class, PrimitiveTypeUtil.getPrimitiveClass("short"));
        assertEquals(int.class, PrimitiveTypeUtil.getPrimitiveClass("int"));
        assertEquals(long.class, PrimitiveTypeUtil.getPrimitiveClass("long"));
        assertEquals(char.class, PrimitiveTypeUtil.getPrimitiveClass("char"));
        assertEquals(float.class, PrimitiveTypeUtil.getPrimitiveClass("float"));
        assertEquals(double.class, PrimitiveTypeUtil.getPrimitiveClass("double"));
        assertEquals(boolean.class, PrimitiveTypeUtil.getPrimitiveClass("boolean"));
        assertEquals(void.class, PrimitiveTypeUtil.getPrimitiveClass("void"));
        
        try {
            PrimitiveTypeUtil.getPrimitiveClass("String");
            fail("Expected IllegalArgumentException for non-primitive type");
        } catch (IllegalArgumentException e) {
            assertEquals("Not primitive type : String", e.getMessage());
        }
        
        try {
            PrimitiveTypeUtil.getPrimitiveClass(null);
            fail("Expected IllegalArgumentException for null type");
        } catch (IllegalArgumentException e) {
            assertEquals("Not primitive type : null", e.getMessage());
        }
    }

    @Test
    public void testGetTypeDefaultValue() {
        assertEquals("0", PrimitiveTypeUtil.getTypeDefaultValue("byte"));
        assertEquals("0", PrimitiveTypeUtil.getTypeDefaultValue("short"));
        assertEquals("0", PrimitiveTypeUtil.getTypeDefaultValue("int"));
        assertEquals("0L", PrimitiveTypeUtil.getTypeDefaultValue("long"));
        assertEquals("'\\u0000'", PrimitiveTypeUtil.getTypeDefaultValue("char"));
        assertEquals("0.0F", PrimitiveTypeUtil.getTypeDefaultValue("float"));
        assertEquals("0.0", PrimitiveTypeUtil.getTypeDefaultValue("double"));
        assertEquals("false", PrimitiveTypeUtil.getTypeDefaultValue("boolean"));
        assertEquals("void", PrimitiveTypeUtil.getTypeDefaultValue("void"));
        
        try {
            PrimitiveTypeUtil.getTypeDefaultValue("String");
            fail("Expected IllegalArgumentException for non-primitive type");
        } catch (IllegalArgumentException e) {
            assertEquals("Not primitive type : String", e.getMessage());
        }
        
        try {
            PrimitiveTypeUtil.getTypeDefaultValue(null);
            fail("Expected IllegalArgumentException for null type");
        } catch (IllegalArgumentException e) {
            assertEquals("Not primitive type : null", e.getMessage());
        }
    }
}
