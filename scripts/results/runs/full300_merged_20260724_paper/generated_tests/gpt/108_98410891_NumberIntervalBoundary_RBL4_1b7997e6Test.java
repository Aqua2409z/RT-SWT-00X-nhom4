
package com.softavail.commsrouter.api.dto.model.skill;

import com.softavail.commsrouter.api.exception.BadValueException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberIntervalBoundary_RBL4_1b7997e6Test {

    private NumberIntervalBoundary boundary;

    @Before
    public void setUp() {
        boundary = new NumberIntervalBoundary();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(boundary.getBoundary());
        assertNull(boundary.getInclusive());
    }

    @Test
    public void testConstructorWithBoundary() {
        boundary = new NumberIntervalBoundary(5.0);
        assertEquals(Double.valueOf(5.0), boundary.getBoundary());
        assertNull(boundary.getInclusive());
    }

    @Test
    public void testConstructorWithBoundaryAndInclusive() {
        boundary = new NumberIntervalBoundary(5.0, true);
        assertEquals(Double.valueOf(5.0), boundary.getBoundary());
        assertTrue(boundary.isInclusive());
    }

    @Test
    public void testEquals() {
        NumberIntervalBoundary boundary1 = new NumberIntervalBoundary(5.0, true);
        NumberIntervalBoundary boundary2 = new NumberIntervalBoundary(5.0, true);
        NumberIntervalBoundary boundary3 = new NumberIntervalBoundary(5.0, false);
        NumberIntervalBoundary boundary4 = new NumberIntervalBoundary(10.0, true);

        assertTrue(boundary1.equals(boundary2));
        assertFalse(boundary1.equals(boundary3));
        assertFalse(boundary1.equals(boundary4));
        assertFalse(boundary1.equals(null));
        assertFalse(boundary1.equals("String"));
    }

    @Test
    public void testHashCode() {
        NumberIntervalBoundary boundary1 = new NumberIntervalBoundary(5.0, true);
        NumberIntervalBoundary boundary2 = new NumberIntervalBoundary(5.0, true);
        NumberIntervalBoundary boundary3 = new NumberIntervalBoundary(5.0, false);

        assertEquals(boundary1.hashCode(), boundary2.hashCode());
        assertNotEquals(boundary1.hashCode(), boundary3.hashCode());
    }

    @Test
    public void testCompareBoundaryTo() {
        NumberIntervalBoundary boundary1 = new NumberIntervalBoundary(5.0);
        NumberIntervalBoundary boundary2 = new NumberIntervalBoundary(10.0);

        assertTrue(boundary1.compareBoundaryTo(boundary2) < 0);
        assertTrue(boundary2.compareBoundaryTo(boundary1) > 0);
        assertTrue(boundary1.compareBoundaryTo(boundary1) == 0);
    }

    @Test(expected = BadValueException.class)
    public void testValidateThrowsExceptionWhenBoundaryIsNull() throws BadValueException {
        boundary.validate();
    }

    @Test
    public void testValidateDoesNotThrowExceptionWhenBoundaryIsSet() throws BadValueException {
        boundary.setBoundary(5.0);
        boundary.validate(); // Should not throw
    }

    @Test
    public void testGetAndSetBoundary() {
        boundary.setBoundary(10.0);
        assertEquals(Double.valueOf(10.0), boundary.getBoundary());
    }

    @Test
    public void testGetAndSetInclusive() {
        boundary.setInclusive(true);
        assertTrue(boundary.isInclusive());
        boundary.setInclusive(false);
        assertFalse(boundary.isInclusive());
    }

    @Test
    public void testIsInclusiveWhenNull() {
        assertFalse(boundary.isInclusive());
    }

    @Test
    public void testStaticPositiveInfinity() {
        assertEquals(Double.POSITIVE_INFINITY, NumberIntervalBoundary.POSITIVE_INFINITY.getBoundary());
    }

    @Test
    public void testStaticNegativeInfinity() {
        assertEquals(Double.NEGATIVE_INFINITY, NumberIntervalBoundary.NEGATIVE_INFINITY.getBoundary());
    }
}
