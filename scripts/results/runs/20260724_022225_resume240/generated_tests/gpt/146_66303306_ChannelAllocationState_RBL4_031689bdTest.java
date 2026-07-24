
package com.bazaarvoice.emodb.event.db.astyanax;

import com.google.common.collect.PeekingIterator;
import org.apache.commons.lang3.tuple.Pair;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

public class ChannelAllocationState_RBL4_031689bdTest {
    private ChannelAllocationState channelAllocationState;
    private SlabRef slabRef;
    private PeekingIterator<Integer> eventSizes;

    @BeforeMethod
    public void setUp() {
        channelAllocationState = new ChannelAllocationState();
        slabRef = Mockito.mock(SlabRef.class);
        eventSizes = Mockito.mock(PeekingIterator.class);
    }

    @Test
    public void testAttachAndAllocate() {
        Mockito.when(eventSizes.hasNext()).thenReturn(true);
        Mockito.when(eventSizes.next()).thenReturn(10);
        Mockito.when(eventSizes.peek()).thenReturn(10);

        SlabAllocation allocation = channelAllocationState.attachAndAllocate(slabRef, eventSizes);
        Assert.assertNotNull(allocation);
    }

    @Test
    public void testAttach() {
        channelAllocationState.attach(slabRef);
        Assert.assertTrue(channelAllocationState.isAttached());
    }

    @Test
    public void testDetach() {
        channelAllocationState.attach(slabRef);
        SlabRef detachedSlab = channelAllocationState.detach();
        Assert.assertNotNull(detachedSlab);
        Assert.assertFalse(channelAllocationState.isAttached());
    }

    @Test
    public void testRotateIfNecessary() {
        channelAllocationState.attach(slabRef);
        channelAllocationState.rotateIfNecessary();
        Assert.assertTrue(channelAllocationState.isAttached());
        
        // Simulate slab expiration
        try {
            Thread.sleep(Constants.SLAB_ROTATE_TTL.toMillis() + 1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        channelAllocationState.rotateIfNecessary();
        Assert.assertFalse(channelAllocationState.isAttached());
    }

    @Test
    public void testAllocate() {
        Mockito.when(eventSizes.hasNext()).thenReturn(true);
        Mockito.when(eventSizes.next()).thenReturn(10);
        Mockito.when(eventSizes.peek()).thenReturn(10);
        
        channelAllocationState.attach(slabRef);
        SlabAllocation allocation = channelAllocationState.allocate(eventSizes);
        Assert.assertNotNull(allocation);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAllocateThrowsExceptionWhenNoEvents() {
        Mockito.when(eventSizes.hasNext()).thenReturn(false);
        channelAllocationState.allocate(eventSizes);
    }

    @Test
    public void testDetachReturnsNullWhenNotAttached() {
        SlabRef detachedSlab = channelAllocationState.detach();
        Assert.assertNull(detachedSlab);
    }
}
