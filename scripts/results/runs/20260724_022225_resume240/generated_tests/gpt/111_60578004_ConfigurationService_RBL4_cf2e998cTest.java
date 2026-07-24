package com.dangdang.ddframe.job.internal.config;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.exception.JobConflictException;
import com.dangdang.ddframe.job.exception.ShardingItemParametersException;
import com.dangdang.ddframe.job.exception.TimeDiffIntolerableException;
import com.dangdang.ddframe.job.internal.config.ConfigurationService;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationService_RBL4_cf2e998cTest {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private JobNodeStorage jobNodeStorage;
    private ConfigurationService configurationService;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = mock(CoordinatorRegistryCenter.class);
        jobConfiguration = mock(JobConfiguration.class);
        jobNodeStorage = mock(JobNodeStorage.class);
        configurationService = new ConfigurationService(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testPersistJobConfiguration_NoConflict() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(false);
        when(jobNodeStorage.getJobConfiguration()).thenReturn(jobConfiguration);
        when(jobConfiguration.getJobClass()).thenReturn(ConfigurationService.class);
        
        configurationService.persistJobConfiguration();
        
        verify(jobNodeStorage, times(1)).fillJobNodeIfNullOrOverwrite(anyString(), anyString());
    }

    @Test(expected = JobConflictException.class)
    public void testPersistJobConfiguration_Conflict() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("com.dangdang.ddframe.job.internal.config.ConfigurationService");
        when(jobNodeStorage.getJobConfiguration()).thenReturn(jobConfiguration);
        when(jobConfiguration.getJobClass()).thenReturn(String.class);
        
        configurationService.persistJobConfiguration();
    }

    @Test
    public void testGetShardingTotalCount() {
        when(jobNodeStorage.getJobNodeDataDirectly(anyString())).thenReturn("5");
        
        int result = configurationService.getShardingTotalCount();
        
        assertEquals(5, result);
    }

    @Test
    public void testGetShardingItemParameters() {
        when(jobNodeStorage.getJobNodeDataDirectly(anyString())).thenReturn("0=param0,1=param1");
        
        Map<Integer, String> result = configurationService.getShardingItemParameters();
        
        Map<Integer, String> expected = new HashMap<>();
        expected.put(0, "param0");
        expected.put(1, "param1");
        
        assertEquals(expected, result);
    }

    @Test(expected = ShardingItemParametersException.class)
    public void testGetShardingItemParameters_InvalidFormat() {
        when(jobNodeStorage.getJobNodeDataDirectly(anyString())).thenReturn("0param0,1=param1");
        
        configurationService.getShardingItemParameters();
    }

    @Test
    public void testGetJobParameter() {
        when(jobNodeStorage.getJobNodeDataDirectly(anyString())).thenReturn("jobParam");
        
        String result = configurationService.getJobParameter();
        
        assertEquals("jobParam", result);
    }

    @Test
    public void testGetCron() {
        when(jobNodeStorage.getJobNodeDataDirectly(anyString())).thenReturn("0/5 * * * * ?");
        
        String result = configurationService.getCron();
        
        assertEquals("0/5 * * * * ?", result);
    }

    @Test
    public void testIsMonitorExecution() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("true");
        
        boolean result = configurationService.isMonitorExecution();
        
        assertTrue(result);
    }

    @Test
    public void testGetProcessCountIntervalSeconds() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("10");
        
        int result = configurationService.getProcessCountIntervalSeconds();
        
        assertEquals(10, result);
    }

    @Test
    public void testGetConcurrentDataProcessThreadCount() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("3");
        
        int result = configurationService.getConcurrentDataProcessThreadCount();
        
        assertEquals(3, result);
    }

    @Test
    public void testGetFetchDataCount() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("100");
        
        int result = configurationService.getFetchDataCount();
        
        assertEquals(100, result);
    }

    @Test(expected = TimeDiffIntolerableException.class)
    public void testCheckMaxTimeDiffSecondsTolerable() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("5");
        when(jobNodeStorage.getRegistryCenterTime()).thenReturn(System.currentTimeMillis() + 10000);
        
        configurationService.checkMaxTimeDiffSecondsTolerable();
    }

    @Test
    public void testIsFailover() {
        when(configurationService.isMonitorExecution()).thenReturn(true);
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("true");
        
        boolean result = configurationService.isFailover();
        
        assertTrue(result);
    }

    @Test
    public void testIsMisfire() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("false");
        
        boolean result = configurationService.isMisfire();
        
        assertFalse(result);
    }

    @Test
    public void testGetJobShardingStrategyClass() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("com.dangdang.ddframe.job.strategy.ShardingStrategy");
        
        String result = configurationService.getJobShardingStrategyClass();
        
        assertEquals("com.dangdang.ddframe.job.strategy.ShardingStrategy", result);
    }

    @Test
    public void testGetMonitorPort() {
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("8080");
        
        int result = configurationService.getMonitorPort();
        
        assertEquals(8080, result);
    }

    @Test
    public void testGetJobName() {
        when(jobNodeStorage.getJobConfiguration()).thenReturn(jobConfiguration);
        when(jobConfiguration.getJobName()).thenReturn("testJob");
        
        String result = configurationService.getJobName();
        
        assertEquals("testJob", result);
    }

    @Test
    public void testInSkipTime() {
        Date now = new Date();
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn(DateUtil.formatDate(now));
        
        boolean result = configurationService.inSkipTime(now);
        
        assertTrue(result);
    }
}
