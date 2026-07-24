package com.dangdang.ddframe.job.internal.config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationNode_RBL4_5ed65ca9Test {
    
    private ConfigurationNode configurationNode;

    @Before
    public void setUp() {
        configurationNode = new ConfigurationNode("testJob");
    }

    @Test
    public void testIsShardingTotalCountPath() {
        assertTrue(configurationNode.isShardingTotalCountPath("config/shardingTotalCount"));
        assertFalse(configurationNode.isShardingTotalCountPath("config/otherPath"));
    }

    @Test
    public void testIsShardingStrategyClassPath() {
        assertTrue(configurationNode.isShardingStrategyClassPath("config/jobShardingStrategyClass"));
        assertFalse(configurationNode.isShardingStrategyClassPath("config/otherPath"));
    }

    @Test
    public void testIsMonitorExecutionPath() {
        assertTrue(configurationNode.isMonitorExecutionPath("config/monitorExecution"));
        assertFalse(configurationNode.isMonitorExecutionPath("config/otherPath"));
    }

    @Test
    public void testIsFailoverPath() {
        assertTrue(configurationNode.isFailoverPath("config/failover"));
        assertFalse(configurationNode.isFailoverPath("config/otherPath"));
    }

    @Test
    public void testIsCronPath() {
        assertTrue(configurationNode.isCronPath("config/cron"));
        assertFalse(configurationNode.isCronPath("config/otherPath"));
    }

    @Test
    public void testIsSkipTimeStartPath() {
        assertTrue(configurationNode.isSkipTimeStartPath("config/skipStartTime"));
        assertFalse(configurationNode.isSkipTimeStartPath("config/otherPath"));
    }

    @Test
    public void testIsSkipTimeEndPath() {
        assertTrue(configurationNode.isSkipTimeEndPath("config/skipEndTime"));
        assertFalse(configurationNode.isSkipTimeEndPath("config/otherPath"));
    }
}
