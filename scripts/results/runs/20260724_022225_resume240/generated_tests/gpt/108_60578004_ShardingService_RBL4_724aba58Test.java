package com.dangdang.ddframe.job.internal.sharding;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.sharding.ShardingService;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShardingService_RBL4_724aba58Test {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private JobNodeStorage jobNodeStorage;
    private ShardingService shardingService;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = Mockito.mock(CoordinatorRegistryCenter.class);
        jobConfiguration = Mockito.mock(JobConfiguration.class);
        jobNodeStorage = Mockito.mock(JobNodeStorage.class);
        when(jobConfiguration.getJobName()).thenReturn("testJob");
        shardingService = new ShardingService(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testSetReshardingFlag() {
        shardingService.setReshardingFlag();
        verify(jobNodeStorage).createJobNodeIfNeeded(ShardingNode.NECESSARY);
    }

    @Test
    public void testIsNeedShardingWhenNodeExists() {
        when(jobNodeStorage.isJobNodeExisted(ShardingNode.NECESSARY)).thenReturn(true);
        assertTrue(shardingService.isNeedSharding());
    }

    @Test
    public void testIsNeedShardingWhenNodeDoesNotExist() {
        when(jobNodeStorage.isJobNodeExisted(ShardingNode.NECESSARY)).thenReturn(false);
        assertFalse(shardingService.isNeedSharding());
    }

    @Test
    public void testShardingIfNecessaryWhenNotNeeded() {
        when(shardingService.isNeedSharding()).thenReturn(false);
        shardingService.shardingIfNecessary();
        verify(jobNodeStorage, never()).fillEphemeralJobNode(anyString(), anyString());
    }

    @Test
    public void testGetLocalHostShardingItemsWhenNodeExists() {
        String ip = "127.0.0.1";
        when(shardingService.localHostService.getIp()).thenReturn(ip);
        when(jobNodeStorage.isJobNodeExisted(ShardingNode.getShardingNode(ip))).thenReturn(true);
        when(jobNodeStorage.getJobNodeDataDirectly(ShardingNode.getShardingNode(ip))).thenReturn("0,1");

        List<Integer> shardingItems = shardingService.getLocalHostShardingItems();
        assertEquals(2, shardingItems.size());
        assertTrue(shardingItems.contains(0));
        assertTrue(shardingItems.contains(1));
    }

    @Test
    public void testGetLocalHostShardingItemsWhenNodeDoesNotExist() {
        String ip = "127.0.0.1";
        when(shardingService.localHostService.getIp()).thenReturn(ip);
        when(jobNodeStorage.isJobNodeExisted(ShardingNode.getShardingNode(ip))).thenReturn(false);

        List<Integer> shardingItems = shardingService.getLocalHostShardingItems();
        assertTrue(shardingItems.isEmpty());
    }
}
