package com.dangdang.ddframe.job.plugin.sharding.strategy;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AverageAllocationJobShardingStrategy_RBL4_bd05f034Test {
    
    private AverageAllocationJobShardingStrategy strategy;

    @Before
    public void setUp() {
        strategy = new AverageAllocationJobShardingStrategy();
    }

    @Test
    public void testShardingWithEmptyServerList() {
        List<String> servers = Arrays.asList();
        JobShardingStrategyOption option = new JobShardingStrategyOption(0);
        Map<String, List<Integer>> result = strategy.sharding(servers, option);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testShardingWithOneServer() {
        List<String> servers = Arrays.asList("server1");
        JobShardingStrategyOption option = new JobShardingStrategyOption(5);
        Map<String, List<Integer>> result = strategy.sharding(servers, option);
        assertEquals(1, result.size());
        assertEquals(Arrays.asList(0, 1, 2, 3, 4), result.get("server1"));
    }

    @Test
    public void testShardingWithMultipleServersEvenDistribution() {
        List<String> servers = Arrays.asList("server1", "server2", "server3");
        JobShardingStrategyOption option = new JobShardingStrategyOption(6);
        Map<String, List<Integer>> result = strategy.sharding(servers, option);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList(0, 1), result.get("server1"));
        assertEquals(Arrays.asList(2, 3), result.get("server2"));
        assertEquals(Arrays.asList(4, 5), result.get("server3"));
    }

    @Test
    public void testShardingWithMultipleServersOddDistribution() {
        List<String> servers = Arrays.asList("server1", "server2", "server3");
        JobShardingStrategyOption option = new JobShardingStrategyOption(7);
        Map<String, List<Integer>> result = strategy.sharding(servers, option);
        assertEquals(3, result.size());
        assertEquals(Arrays.asList(0, 1, 6), result.get("server1"));
        assertEquals(Arrays.asList(2, 3), result.get("server2"));
        assertEquals(Arrays.asList(4, 5), result.get("server3"));
    }

    @Test
    public void testShardingWithMoreShardsThanServers() {
        List<String> servers = Arrays.asList("server1", "server2");
        JobShardingStrategyOption option = new JobShardingStrategyOption(10);
        Map<String, List<Integer>> result = strategy.sharding(servers, option);
        assertEquals(2, result.size());
        assertEquals(Arrays.asList(0, 1, 2, 4, 6, 8), result.get("server1"));
        assertEquals(Arrays.asList(3, 5, 7, 9), result.get("server2"));
    }
}
