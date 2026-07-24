package com.hazelcast.jet.impl.util;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SkewReductionPolicy_RBL4Test_80a96ca0 {

    private SkewReductionPolicy policy;

    @Before
    public void setUp() {
        policy = new SkewReductionPolicy(3);
    }

    @Test
    public void testConstructorWithDefaultValues() {
        assertEquals(Long.MAX_VALUE, policy.maxSkew);
        assertEquals(Long.MAX_VALUE, policy.priorityDrainingThreshold);
        assertFalse(policy.forceAdvanceWm);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMaxSkew() {
        new SkewReductionPolicy(3, -1, Long.MAX_VALUE, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePriorityDrainingThreshold() {
        new SkewReductionPolicy(3, Long.MAX_VALUE, -1, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithPriorityDrainingThresholdGreaterThanMaxSkew() {
        new SkewReductionPolicy(3, 10, 20, false);
    }

    @Test
    public void testToQueueIndex() {
        assertEquals(0, policy.toQueueIndex(0));
        assertEquals(1, policy.toQueueIndex(1));
        assertEquals(2, policy.toQueueIndex(2));
    }

    @Test
    public void testObserveWm() {
        assertFalse(policy.observeWm(0, 5));
        assertTrue(policy.observeWm(1, 10));
        assertTrue(policy.observeWm(2, 15));
        assertArrayEquals(new long[]{5, 10, 15}, policy.queueWms);
    }

    @Test(expected = JetException.class)
    public void testObserveWmThrowsExceptionOnNonMonotonicWatermark() {
        policy.observeWm(0, 5);
        policy.observeWm(0, 3); // should throw exception
    }

    @Test
    public void testShouldStopDraining() {
        policy.observeWm(0, 5);
        policy.observeWm(1, 10);
        policy.observeWm(2, 15);
        
        assertFalse(policy.shouldStopDraining(0, true));
        assertFalse(policy.shouldStopDraining(1, true));
        assertFalse(policy.shouldStopDraining(2, true));
        
        policy.priorityDrainingThreshold = 5;
        assertTrue(policy.shouldStopDraining(0, true));
    }

    @Test
    public void testBottomObservedWm() {
        policy.observeWm(0, 5);
        policy.observeWm(1, 10);
        assertEquals(5, policy.bottomObservedWm());
    }

    @Test
    public void testAdjustDrainingOrder() {
        policy.observeWm(0, 5);
        policy.observeWm(1, 10);
        policy.observeWm(2, 15);
        
        assertTrue(policy.observeWm(0, 20)); // should reorder
        assertArrayEquals(new int[]{1, 2, 0}, policy.drainOrderToQIdx);
    }
}
