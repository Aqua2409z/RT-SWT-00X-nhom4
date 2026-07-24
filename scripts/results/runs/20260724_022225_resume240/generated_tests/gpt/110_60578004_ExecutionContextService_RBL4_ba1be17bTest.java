package com.dangdang.ddframe.job.internal.execution;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.internal.config.ConfigurationService;
import com.dangdang.ddframe.job.internal.offset.OffsetService;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExecutionContextService_RBL4_ba1be17bTest {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private JobNodeStorage jobNodeStorage;
    private ConfigurationService configService;
    private OffsetService offsetService;
    private ExecutionContextService executionContextService;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = mock(CoordinatorRegistryCenter.class);
        jobConfiguration = mock(JobConfiguration.class);
        jobNodeStorage = mock(JobNodeStorage.class);
        configService = mock(ConfigurationService.class);
        offsetService = mock(OffsetService.class);
        
        when(jobConfiguration.getJobName()).thenReturn("testJob");
        when(configService.getShardingTotalCount()).thenReturn(5);
        when(configService.isMonitorExecution()).thenReturn(true);
        when(configService.getJobParameter()).thenReturn("param");
        when(configService.getFetchDataCount()).thenReturn(10);
        when(configService.getShardingItemParameters()).thenReturn(Collections.singletonMap(0, "param0"));
        
        executionContextService = new ExecutionContextService(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testGetJobExecutionShardingContextWithEmptyShardingItems() {
        JobExecutionMultipleShardingContext context = executionContextService.getJobExecutionShardingContext(Collections.emptyList());
        assertEquals("testJob", context.getJobName());
        assertEquals(5, context.getShardingTotalCount());
        assertTrue(context.getShardingItems().isEmpty());
        assertEquals("param", context.getJobParameter());
        assertTrue(context.getShardingItemParameters().isEmpty());
    }

    @Test
    public void testGetJobExecutionShardingContextWithShardingItems() {
        List<Integer> shardingItems = Arrays.asList(0, 1, 2);
        when(configService.getShardingItemParameters()).thenReturn(Collections.singletonMap(0, "param0"));
        when(offsetService.getOffsets(shardingItems)).thenReturn(Collections.singletonMap(0, 0L));

        JobExecutionMultipleShardingContext context = executionContextService.getJobExecutionShardingContext(shardingItems);
        
        assertEquals("testJob", context.getJobName());
        assertEquals(5, context.getShardingTotalCount());
        assertEquals(shardingItems, context.getShardingItems());
        assertEquals("param", context.getJobParameter());
        assertTrue(context.getShardingItemParameters().containsKey(0));
        assertEquals("param0", context.getShardingItemParameters().get(0));
        assertEquals((Long) 0L, context.getOffsets().get(0));
    }

    @Test
    public void testRemoveRunningItems() {
        List<Integer> shardingItems = Arrays.asList(0, 1, 2);
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        
        executionContextService.getClass().getDeclaredMethod("removeRunningItems", List.class).setAccessible(true);
        executionContextService.getClass().getDeclaredMethod("removeRunningItems", List.class).invoke(executionContextService, shardingItems);
        
        assertTrue(shardingItems.isEmpty());
    }

    @Test
    public void testIsRunningItem() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        assertTrue(executionContextService.getClass().getDeclaredMethod("isRunningItem", int.class).invoke(executionContextService, 0));
        
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(false);
        assertFalse(executionContextService.getClass().getDeclaredMethod("isRunningItem", int.class).invoke(executionContextService, 1));
    }
}
