
package de.voidnode.trading4j.api;

import org.junit.Test;
import static org.junit.Assert.*;

public class Failed_RBL4Test_3e31fae6 {

    @Test
    public void testConstructorAndGetReason() {
        String reason = "Some failure reason";
        Failed failed = new Failed(reason);
        assertEquals(reason, failed.getReason());
    }

    @Test
    public void testToString() {
        String reason = "Another failure reason";
        Failed failed = new Failed(reason);
        assertEquals(reason, failed.toString());
    }

    @Test
    public void testEqualsSameObject() {
        String reason = "Same reason";
        Failed failed = new Failed(reason);
        assertTrue(failed.equals(failed));
    }

    @Test
    public void testEqualsDifferentClass() {
        String reason = "Different class Failed_RBL4Test_3e31fae6";
        Failed failed = new Failed(reason);
        assertFalse(failed.equals(new Object()));
    }

    @Test
    public void testEqualsNull() {
        String reason = "Null comparison reason";
        Failed failed = new Failed(reason);
        assertFalse(failed.equals(null));
    }

    @Test
    public void testEqualsDifferentReason() {
        Failed failed1 = new Failed("Reason 1");
        Failed failed2 = new Failed("Reason 2");
        assertFalse(failed1.equals(failed2));
    }

    @Test
    public void testEqualsSameReason() {
        Failed failed1 = new Failed("Same reason");
        Failed failed2 = new Failed("Same reason");
        assertTrue(failed1.equals(failed2));
    }

    @Test
    public void testHashCode() {
        Failed failed1 = new Failed("Hash reason");
        Failed failed2 = new Failed("Hash reason");
        assertEquals(failed1.hashCode(), failed2.hashCode());
    }

    @Test
    public void testHashCodeDifferentReason() {
        Failed failed1 = new Failed("Reason 1");
        Failed failed2 = new Failed("Reason 2");
        assertNotEquals(failed1.hashCode(), failed2.hashCode());
    }

    @Test
    public void testHashCodeNullReason() {
        Failed failed1 = new Failed(null);
        Failed failed2 = new Failed(null);
        assertEquals(failed1.hashCode(), failed2.hashCode());
    }
}
