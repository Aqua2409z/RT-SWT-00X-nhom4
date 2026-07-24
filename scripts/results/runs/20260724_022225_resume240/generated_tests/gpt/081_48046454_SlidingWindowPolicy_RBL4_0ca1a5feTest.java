package com.hazelcast.jet.core;

import org.junit.Test;
import static org.junit.Assert.*;

public class SlidingWindowPolicy_RBL4_0ca1a5feTest {

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_frameSizeNegative() {
        new SlidingWindowPolicy(-1, 0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_frameOffsetNegative() {
        new SlidingWindowPolicy(1, -1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_frameOffsetGreaterThanFrameSize() {
        new SlidingWindowPolicy(1, 2, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_framesPerWindowNegative() {
        new SlidingWindowPolicy(1, 0, -1);
    }

    @Test
    public void testConstructor_validParameters() {
        SlidingWindowPolicy policy = new SlidingWindowPolicy(10, 5, 2);
        assertEquals(10, policy.frameSize());
        assertEquals(5, policy.frameOffset());
        assertEquals(20, policy.windowSize());
    }

    @Test
    public void testIsTumbling() {
        SlidingWindowPolicy tumblingPolicy = new SlidingWindowPolicy(10, 0, 1);
        assertTrue(tumblingPolicy.isTumbling());

        SlidingWindowPolicy slidingPolicy = new SlidingWindowPolicy(10, 0, 2);
        assertFalse(slidingPolicy.isTumbling());
    }

    @Test
    public void testFloorFrameTs() {
        SlidingWindowPolicy policy = new SlidingWindowPolicy(10, 5, 2);
        assertEquals(5, policy.floorFrameTs(7));
        assertEquals(0, policy.floorFrameTs(2));
        assertEquals(Long.MIN_VALUE, policy.floorFrameTs(Long.MIN_VALUE));
    }

    @Test
    public void testHigherFrameTs() {
        SlidingWindowPolicy policy = new SlidingWindowPolicy(10, 5, 2);
        assertEquals(15, policy.higherFrameTs(7));
        assertEquals(10, policy.higherFrameTs(0));
        assertEquals(Long.MAX_VALUE, policy.higherFrameTs(Long.MAX_VALUE));
    }

    @Test
    public void testWithOffset() {
        SlidingWindowPolicy policy = new SlidingWindowPolicy(10, 5, 2);
        SlidingWindowPolicy newPolicy = policy.withOffset(2);
        assertEquals(10, newPolicy.frameSize());
        assertEquals(2, newPolicy.frameOffset());
        assertEquals(20, newPolicy.windowSize());
    }

    @Test
    public void testToTumblingByFrame() {
        SlidingWindowPolicy policy = new SlidingWindowPolicy(10, 5, 2);
        SlidingWindowPolicy tumblingPolicy = policy.toTumblingByFrame();
        assertEquals(10, tumblingPolicy.frameSize());
        assertEquals(0, tumblingPolicy.frameOffset());
        assertEquals(10, tumblingPolicy.windowSize());
        assertTrue(tumblingPolicy.isTumbling());
    }

    @Test
    public void testSlidingWinPolicy() {
        SlidingWindowPolicy policy = SlidingWindowPolicy.slidingWinPolicy(20, 5);
        assertEquals(5, policy.frameSize());
        assertEquals(0, policy.frameOffset());
        assertEquals(20, policy.windowSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSlidingWinPolicy_windowSizeNotMultipleOfSlideBy() {
        SlidingWindowPolicy.slidingWinPolicy(20, 6);
    }

    @Test
    public void testTumblingWinPolicy() {
        SlidingWindowPolicy policy = SlidingWindowPolicy.tumblingWinPolicy(20);
        assertEquals(20, policy.frameSize());
        assertEquals(0, policy.frameOffset());
        assertEquals(20, policy.windowSize());
        assertTrue(policy.isTumbling());
    }
}
