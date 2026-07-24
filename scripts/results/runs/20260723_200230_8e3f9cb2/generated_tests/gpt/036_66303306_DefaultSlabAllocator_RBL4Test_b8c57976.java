
package com.bazaarvoice.emodb.event.db.astyanax;

import com.bazaarvoice.emodb.common.dropwizard.lifecycle.LifeCycleRegistry;
import com.bazaarvoice.emodb.common.uuid.TimeUUIDs;
import com.bazaarvoice.emodb.event.core.MetricsGroupName;
import com.codahale.metrics.MetricRegistry;
import com.google.common.collect.PeekingIterator;
import com.google.common.collect.Iterators;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;

public class DefaultSlabAllocator_RBL4Test_b8c57976 {
    private LifeCycleRegistry lifeCycleRegistry;
    private ManifestPersister manifestPersister;
    private MetricRegistry metricRegistry;
    private DefaultSlabAllocator slabAllocator;

    @BeforeMethod
    public void setUp() {
        lifeCycleRegistry = Mockito.mock(LifeCycleRegistry.class);
        manifestPersister = Mockito.mock(ManifestPersister.class);
        metricRegistry = new MetricRegistry();
        slabAllocator = new DefaultSlabAllocator(lifeCycleRegistry, manifestPersister, "test.metrics", metricRegistry);
    }

    @Test
    public void testAllocateWithValidParameters() {
        PeekingIterator<Integer> eventSizes = Iterators.peekingIterator(Arrays.asList(100, 200, 300).iterator());
        SlabAllocation allocation = slabAllocator.allocate("testChannel", 2, eventSizes);
        
        Assert.assertNotNull(allocation);
        Assert.assertTrue(allocation.getSlabRef() instanceof SlabRef);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAllocateWithNullChannelName() {
        PeekingIterator<Integer> eventSizes = Iterators.peekingIterator(Arrays.asList(100, 200).iterator());
        slabAllocator.allocate(null, 2, eventSizes);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testAllocateWithZeroDesiredCount() {
        PeekingIterator<Integer> eventSizes = Iterators.peekingIterator(Arrays.asList(100, 200).iterator());
        slabAllocator.allocate("testChannel", 0, eventSizes);
    }

    @Test
    public void testDefaultAllocationCount() {
        PeekingIterator<Integer> eventSizes = Iterators.peekingIterator(Arrays.asList(100, 200, 300).iterator());
        Pair<Integer, Integer> result = DefaultSlabAllocator.defaultAllocationCount(0, 0, eventSizes);
        
        Assert.assertEquals(result.getLeft().intValue(), 3);
        Assert.assertEquals(result.getRight().intValue(), 600);
    }

    @Test
    public void testDefaultAllocationCountWithExceedingSize() {
        PeekingIterator<Integer> eventSizes = Iterators.peekingIterator(Arrays.asList(1000, 2000).iterator());
        Pair<Integer, Integer> result = DefaultSlabAllocator.defaultAllocationCount(0, 0, eventSizes);
        
        Assert.assertEquals(result.getLeft().intValue(), 0);
        Assert.assertEquals(result.getRight().intValue(), 0);
    }

    @Test
    public void testCloseChannelState() {
        ChannelAllocationState channelState = Mockito.mock(ChannelAllocationState.class);
        SlabRef slabRef = Mockito.mock(SlabRef.class);
        Mockito.when(channelState.detach()).thenReturn(slabRef);
        
        slabAllocator.closeChannelState(channelState);
        
        Mockito.verify(slabRef).release();
        Assert.assertEquals(metricRegistry.meter("test.metrics.DefaultSlabAllocator.inactive_slabs").getCount(), 1);
    }

    @Test
    public void testCreateSlab() {
        SlabRef slabRef = slabAllocator.createSlab("testChannel");
        Assert.assertNotNull(slabRef);
        Assert.assertEquals(slabRef.getChannel(), "testChannel");
    }

    @Test
    public void testGenerateSlabId() {
        ByteBuffer slabId = slabAllocator.generateSlabId();
        Assert.assertNotNull(slabId);
        Assert.assertTrue(slabId.hasRemaining());
    }
}
