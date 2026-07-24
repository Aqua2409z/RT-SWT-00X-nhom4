
package de.voidnode.trading4j.domain.monetary;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccuratePrice_RBL4_ed9b3a40Test {

    @Test
    public void testConstructorAndAsRawValue() {
        AccuratePrice price = new AccuratePrice(10.5);
        assertEquals(10.5, price.asRawValue(), 0.001);
    }

    @Test
    public void testEqualsSameObject() {
        AccuratePrice price = new AccuratePrice(10.5);
        assertTrue(price.equals(price));
    }

    @Test
    public void testEqualsDifferentObjectSameValue() {
        AccuratePrice price1 = new AccuratePrice(10.5);
        AccuratePrice price2 = new AccuratePrice(10.5);
        assertTrue(price1.equals(price2));
    }

    @Test
    public void testEqualsDifferentValue() {
        AccuratePrice price1 = new AccuratePrice(10.5);
        AccuratePrice price2 = new AccuratePrice(20.5);
        assertFalse(price1.equals(price2));
    }

    @Test
    public void testEqualsNull() {
        AccuratePrice price = new AccuratePrice(10.5);
        assertFalse(price.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        AccuratePrice price = new AccuratePrice(10.5);
        assertFalse(price.equals("Not an AccuratePrice"));
    }

    @Test
    public void testHashCodeSameValue() {
        AccuratePrice price1 = new AccuratePrice(10.5);
        AccuratePrice price2 = new AccuratePrice(10.5);
        assertEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    public void testHashCodeDifferentValue() {
        AccuratePrice price1 = new AccuratePrice(10.5);
        AccuratePrice price2 = new AccuratePrice(20.5);
        assertNotEquals(price1.hashCode(), price2.hashCode());
    }

    @Test
    public void testToString() {
        AccuratePrice price = new AccuratePrice(10.5);
        assertEquals("10.5", price.toString());
    }
}
