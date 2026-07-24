package com.hazelcast.jet.impl.processor;

import com.hazelcast.jet.core.EventTimePolicy;
import com.hazelcast.jet.impl.processor.InsertWatermarksP;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InsertWatermarksP_RBL4_c320ce76Test {

    private InsertWatermarksP<String> processor;
    private EventTimePolicy<String> eventTimePolicy;

    @Before
    public void setUp() {
        eventTimePolicy = mock(EventTimePolicy.class);
        processor = new InsertWatermarksP<>(eventTimePolicy);
    }

    @Test
    public void testTryProcessWithNullItem() {
        assertTrue(processor.tryProcess());
    }

    @Test
    public void testTryProcessWithItem() {
        String item = "testItem";
        when(eventTimePolicy.getEventTime(item)).thenReturn(1L);
        assertTrue(processor.tryProcess(0, item));
    }

    @Test
    public void testSaveToSnapshot() {
        when(eventTimePolicy.getWatermark(0)).thenReturn(1L);
        assertTrue(processor.saveToSnapshot());
    }

    @Test
    public void testRestoreFromSnapshot() {
        processor.restoreFromSnapshot(broadcastKey(InsertWatermarksP.Keys.LAST_EMITTED_WM), 1L);
    }

    @Test
    public void testFinishSnapshotRestore() {
        processor.restoreFromSnapshot(broadcastKey(InsertWatermarksP.Keys.LAST_EMITTED_WM), 1L);
        assertTrue(processor.finishSnapshotRestore());
    }
}
