
package com.softavail.commsrouter.api.dto.model.skill;

import com.softavail.commsrouter.api.exception.BadValueException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NumberInterval_RBL4Test_e3efc426 {

    private NumberIntervalBoundary lowInclusive;
    private NumberIntervalBoundary highInclusive;
    private NumberIntervalBoundary lowExclusive;
    private NumberIntervalBoundary highExclusive;
    private NumberInterval numberInterval;

    @Before
    public void setUp() {
        lowInclusive = new NumberIntervalBoundary(1.0, true);
        highInclusive = new NumberIntervalBoundary(5.0, true);
        lowExclusive = new NumberIntervalBoundary(1.0, false);
        highExclusive = new NumberIntervalBoundary(5.0, false);
        numberInterval = new NumberInterval(lowInclusive, highInclusive);
    }

    @Test
    public void testEquals() {
        NumberInterval sameInterval = new NumberInterval(lowInclusive, highInclusive);
        assertTrue(numberInterval.equals(sameInterval));
        
        NumberInterval differentInterval = new NumberInterval(lowExclusive, highInclusive);
        assertFalse(numberInterval.equals(differentInterval));
    }

    @Test
    public void testHashCode() {
        NumberInterval sameInterval = new NumberInterval(lowInclusive, highInclusive);
        assertEquals(numberInterval.hashCode(), sameInterval.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("[1.0, 5.0]", numberInterval.toString());
    }

    @Test(expected = BadValueException.class)
    public void testValidateLowNull() throws BadValueException {
        numberInterval.setLow(null);
        numberInterval.validate();
    }

    @Test(expected = BadValueException.class)
    public void testValidateHighNull() throws BadValueException {
        numberInterval.setHigh(null);
        numberInterval.validate();
    }

    @Test(expected = BadValueException.class)
    public void testValidateLowGreaterThanHigh() throws BadValueException {
        numberInterval.setHigh(new NumberIntervalBoundary(0.0, true));
        numberInterval.validate();
    }

    @Test(expected = BadValueException.class)
    public void testValidateLowEqualHighNotInclusive() throws BadValueException {
        numberInterval.setHigh(new NumberIntervalBoundary(1.0, false));
        numberInterval.validate();
    }

    @Test
    public void testOverlaps() {
        NumberInterval overlappingInterval = new NumberInterval(new NumberIntervalBoundary(4.0, true), new NumberIntervalBoundary(6.0, true));
        assertTrue(numberInterval.overlaps(overlappingInterval));
        
        NumberInterval nonOverlappingInterval = new NumberInterval(new NumberIntervalBoundary(6.0, true), new NumberIntervalBoundary(7.0, true));
        assertFalse(numberInterval.overlaps(nonOverlappingInterval));
    }

    @Test
    public void testContains() {
        assertTrue(numberInterval.contains(1.0));
        assertTrue(numberInterval.contains(3.0));
        assertTrue(numberInterval.contains(5.0));
        assertFalse(numberInterval.contains(0.5));
        assertFalse(numberInterval.contains(5.1));
    }

    @Test
    public void testDoesNotOverlap() {
        NumberInterval nonOverlappingInterval = new NumberInterval(new NumberIntervalBoundary(6.0, true), new NumberIntervalBoundary(7.0, true));
        assertTrue(numberInterval.doesNotOverlap(nonOverlappingInterval));
        
        NumberInterval overlappingInterval = new NumberInterval(new NumberIntervalBoundary(4.0, true), new NumberIntervalBoundary(6.0, true));
        assertFalse(numberInterval.doesNotOverlap(overlappingInterval));
    }
}
