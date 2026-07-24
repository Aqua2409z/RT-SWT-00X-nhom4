package com.dangdang.ddframe.job.api;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.dangdang.ddframe.job.exception.JobException;

import java.util.Arrays;

public class JobExecutionMultipleShardingContext_RBL4Test_c6723af1 {
    
    private JobExecutionMultipleShardingContext context;

    @Before
    public void setUp() {
        context = new JobExecutionMultipleShardingContext();
        context.setShardingItems(Arrays.asList(1, 2, 3));
        context.getShardingItemParameters().put(1, "param1");
        context.getShardingItemParameters().put(2, "param2");
        context.getShardingItemParameters().put(3, "param3");
        context.getOffsets().put(1, "offset1");
        context.getOffsets().put(2, "offset2");
        context.getOffsets().put(3, "offset3");
    }

    @Test
    public void testCreateJobExecutionSingleShardingContext() {
        JobExecutionSingleShardingContext singleContext = context.createJobExecutionSingleShardingContext(1);
        assertNotNull(singleContext);
        assertEquals(1, singleContext.getShardingItem());
        assertEquals("param1", singleContext.getShardingItemParameter());
        assertEquals("offset1", singleContext.getOffset());
    }

    @Test(expected = JobException.class)
    public void testCreateJobExecutionSingleShardingContextWithInvalidItem() {
        context.createJobExecutionSingleShardingContext(4);
    }

    @Test
    public void testToString() {
        String expectedString = String.format(
            "jobName: null, shardingTotalCount: null, shardingItems: %s, shardingItemParameters: %s, jobParameter: null",
            context.getShardingItems(), context.getShardingItemParameters()
        );
        assertEquals(expectedString, context.toString());
    }

    @Test
    public void testSetShardingItems() {
        context.setShardingItems(Arrays.asList(4, 5, 6));
        assertEquals(Arrays.asList(4, 5, 6), context.getShardingItems());
    }

    @Test
    public void testSetOffsets() {
        context.getOffsets().put(4, "offset4");
        assertEquals("offset4", context.getOffsets().get(4));
    }
}
