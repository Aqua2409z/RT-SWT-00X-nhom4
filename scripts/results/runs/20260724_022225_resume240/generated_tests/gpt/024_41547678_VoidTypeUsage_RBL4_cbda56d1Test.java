
package me.tomassetti.turin.typesystem;

import me.tomassetti.jvm.JvmType;
import me.tomassetti.turin.symbols.Symbol;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;

public class VoidTypeUsage_RBL4_cbda56d1Test {

    @Test
    public void testIsVoid() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        assertTrue(voidTypeUsage.isVoid());
    }

    @Test
    public void testToString() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        assertEquals("VoidTypeUsage{}", voidTypeUsage.toString());
    }

    @Test
    public void testEquals() {
        VoidTypeUsage voidTypeUsage1 = new VoidTypeUsage();
        VoidTypeUsage voidTypeUsage2 = new VoidTypeUsage();
        assertTrue(voidTypeUsage1.equals(voidTypeUsage2));
        assertFalse(voidTypeUsage1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        assertEquals(127, voidTypeUsage.hashCode());
    }

    @Test
    public void testReplaceTypeVariables() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        assertSame(voidTypeUsage, voidTypeUsage.replaceTypeVariables(new HashMap<>()));
    }

    @Test
    public void testSameType() {
        VoidTypeUsage voidTypeUsage1 = new VoidTypeUsage();
        VoidTypeUsage voidTypeUsage2 = new VoidTypeUsage();
        assertTrue(voidTypeUsage1.sameType(voidTypeUsage2));
    }

    @Test
    public void testJvmType() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        JvmType jvmType = voidTypeUsage.jvmType();
        assertEquals("V", jvmType.getName());
    }

    @Test
    public void testCanBeAssignedTo() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        assertFalse(voidTypeUsage.canBeAssignedTo(new VoidTypeUsage()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetInstanceField() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        voidTypeUsage.getInstanceField("fieldName", new Symbol("instance"));
    }

    @Test
    public void testDescribe() {
        VoidTypeUsage voidTypeUsage = new VoidTypeUsage();
        assertEquals("void", voidTypeUsage.describe());
    }
}
