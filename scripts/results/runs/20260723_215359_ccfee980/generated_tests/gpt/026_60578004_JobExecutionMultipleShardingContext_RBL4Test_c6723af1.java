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
        
        singleContext = context.createJobExecutionSingleShardingContext(2);
        assertEquals(2, singleContext.getShardingItem());
        assertEquals("param2", singleContext.getShardingItemParameter());
        assertEquals("offset2", singleContext.getOffset());
        
        singleContext = context.createJobExecutionSingleShardingContext(3);
        assertEquals(3, singleContext.getShardingItem());
        assertEquals("param3", singleContext.getShardingItemParameter());
        assertEquals("offset3", singleContext.getOffset());
    }

    @Test(expected = JobException.class)
    public void testCreateJobExecutionSingleShardingContextWithInvalidItem() {
        context.createJobExecutionSingleShardingContext(4);
    }

    @Test
    public void testToString() {
        String expectedString = String.format(
            "jobName: null, shardingTotalCount: 0, shardingItems: %s, shardingItemParameters: %s, jobParameter: null",
            context.getShardingItems(), context.getShardingItemParameters()
        );
        assertEquals(expectedString, context.toString());
    }
}
