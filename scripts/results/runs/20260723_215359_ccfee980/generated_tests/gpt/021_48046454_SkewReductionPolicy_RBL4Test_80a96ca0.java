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
    public void testConstructorWithValidParameters() {
        SkewReductionPolicy customPolicy = new SkewReductionPolicy(3, 10, 5, false);
        assertNotNull(customPolicy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeMaxSkew() {
        new SkewReductionPolicy(3, -1, 5, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePriorityDrainingThreshold() {
        new SkewReductionPolicy(3, 10, -1, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidThresholds() {
        new SkewReductionPolicy(3, 5, 10, false);
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
    }

    @Test(expected = JetException.class)
    public void testObserveWmWithNonMonotonicWatermark() {
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
        
        policy = new SkewReductionPolicy(3, 10, 5, false);
        policy.observeWm(0, 5);
        policy.observeWm(1, 20);
        assertTrue(policy.shouldStopDraining(1, true)); // skew exceeds priority threshold
    }

    @Test
    public void testBottomObservedWm() {
        policy.observeWm(0, 5);
        policy.observeWm(1, 10);
        policy.observeWm(2, 15);
        assertEquals(5, policy.bottomObservedWm());
    }

    @Test
    public void testAdjustDrainingOrder() {
        policy.observeWm(0, 5);
        policy.observeWm(1, 10);
        policy.observeWm(2, 15);
        assertTrue(policy.observeWm(0, 20)); // should reorder
        assertEquals(0, policy.toQueueIndex(2)); // 20 should be at the end
    }
}
