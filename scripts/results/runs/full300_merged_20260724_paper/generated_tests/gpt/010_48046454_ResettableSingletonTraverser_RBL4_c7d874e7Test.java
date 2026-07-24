package com.hazelcast.jet.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ResettableSingletonTraverser_RBL4_c7d874e7Test {
    private ResettableSingletonTraverser<String> traverser;

    @Before
    public void setUp() {
        traverser = new ResettableSingletonTraverser<>();
    }

    @Test
    public void testNextReturnsItem() {
        traverser.accept("item1");
        assertEquals("item1", traverser.next());
    }

    @Test
    public void testNextResetsItem() {
        traverser.accept("item2");
        traverser.next();
        assertNull(traverser.next());
    }

    @Test
    public void testAcceptThrowsExceptionIfItemNotConsumed() {
        traverser.accept("item3");
        try {
            traverser.accept("item4");
            fail("Expected an AssertionError to be thrown");
        } catch (AssertionError e) {
            assertEquals("accept() called, but previous item still present. Previous: item3, new: item4", e.getMessage());
        }
    }

    @Test
    public void testAcceptAllowsSettingNewItemAfterNext() {
        traverser.accept("item5");
        traverser.next();
        traverser.accept("item6");
        assertEquals("item6", traverser.next());
    }

    @Test
    public void testNextReturnsNullWhenNoItemSet() {
        assertNull(traverser.next());
    }
}
