
package com.anjlab.sat3;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestSimpleTier {
    private SimpleTier tier;

    @Before
    public void setUp() {
        tier = SimpleTier.createCompleteTier(1, 2, 3);
    }

    @Test
    public void testCreateCompleteTier() {
        assertNotNull(tier);
        assertEquals(8, tier.size());
    }

    @Test
    public void testClone() {
        SimpleTier clonedTier = (SimpleTier) tier.clone();
        assertNotSame(tier, clonedTier);
        assertEquals(tier, clonedTier);
    }

    @Test
    public void testAdd() {
        ITripletValue triplet = SimpleTripletValueFactory.getTripletValue(1);
        tier.add(triplet);
        assertTrue(tier.contains(triplet));
        assertEquals(9, tier.size());
    }

    @Test
    public void testRemove() {
        ITripletValue triplet = SimpleTripletValueFactory.getTripletValue(1);
        tier.add(triplet);
        tier.remove(triplet);
        assertFalse(tier.contains(triplet));
        assertEquals(8, tier.size());
    }

    @Test
    public void testIntersect() {
        ITripletValue triplet = SimpleTripletValueFactory.getTripletValue(1);
        tier.add(triplet);
        tier.intersect(triplet);
        assertEquals(1, tier.size());
    }

    @Test
    public void testUnion() {
        SimpleTier anotherTier = SimpleTier.createCompleteTier(4, 5, 6);
        tier.union(anotherTier);
        assertEquals(8, tier.size());
    }

    @Test
    public void testSwapAB() {
        tier.swapAB();
        // Add assertions to verify the state after swap
    }

    @Test
    public void testSwapAC() {
        tier.swapAC();
        // Add assertions to verify the state after swap
    }

    @Test
    public void testSwapBC() {
        tier.swapBC();
        // Add assertions to verify the state after swap
    }

    @Test
    public void testToString() {
        String result = tier.toString();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testAdjoinRight() {
        SimpleTier anotherTier = SimpleTier.createCompleteTier(4, 5, 6);
        tier.adjoinRight(anotherTier);
        // Add assertions to verify the state after adjoin
    }

    @Test
    public void testAdjoinLeft() {
        SimpleTier anotherTier = SimpleTier.createCompleteTier(4, 5, 6);
        tier.adjoinLeft(anotherTier);
        // Add assertions to verify the state after adjoin
    }

    @Test
    public void testConcretize() {
        tier.concretize(1, Value.AllPlain);
        assertEquals(Value.AllPlain, tier.valueOfA());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConcretizeInvalid() {
        tier.concretize(99, Value.AllPlain);
    }

    @Test
    public void testInverse() {
        tier.inverse();
        assertNotEquals(8, tier.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tier.isEmpty());
        tier.remove(SimpleTripletValueFactory.getTripletValue(1));
        assertTrue(tier.isEmpty());
    }

    @Test
    public void testEquals() {
        SimpleTier anotherTier = SimpleTier.createCompleteTier(1, 2, 3);
        assertTrue(tier.equals(anotherTier));
    }
}
