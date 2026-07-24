package com.dangdang.ddframe.job.internal.storage;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.exception.JobException;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JobNodeStorage_RBL4_c67ff505Test {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private JobNodeStorage jobNodeStorage;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = Mockito.mock(CoordinatorRegistryCenter.class);
        jobConfiguration = Mockito.mock(JobConfiguration.class);
        when(jobConfiguration.getJobName()).thenReturn("testJob");
        jobNodeStorage = new JobNodeStorage(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testIsJobNodeExisted() {
        when(coordinatorRegistryCenter.isExisted("/testJob/node1")).thenReturn(true);
        assertTrue(jobNodeStorage.isJobNodeExisted("node1"));
        
        when(coordinatorRegistryCenter.isExisted("/testJob/node2")).thenReturn(false);
        assertFalse(jobNodeStorage.isJobNodeExisted("node2"));
    }

    @Test
    public void testGetJobNodeData() {
        when(coordinatorRegistryCenter.get("/testJob/node1")).thenReturn("data1");
        assertEquals("data1", jobNodeStorage.getJobNodeData("node1"));
    }

    @Test
    public void testGetJobNodeDataDirectly() {
        when(coordinatorRegistryCenter.getDirectly("/testJob/node1")).thenReturn("data1");
        assertEquals("data1", jobNodeStorage.getJobNodeDataDirectly("node1"));
    }

    @Test
    public void testGetJobNodeChildrenKeys() {
        when(coordinatorRegistryCenter.getChildrenKeys("/testJob/node1")).thenReturn(Arrays.asList("child1", "child2"));
        List<String> childrenKeys = jobNodeStorage.getJobNodeChildrenKeys("node1");
        assertEquals(2, childrenKeys.size());
        assertTrue(childrenKeys.contains("child1"));
        assertTrue(childrenKeys.contains("child2"));
    }

    @Test
    public void testCreateJobNodeIfNeeded() {
        when(coordinatorRegistryCenter.isExisted("/testJob")).thenReturn(true);
        when(coordinatorRegistryCenter.isExisted("/testJob/node1")).thenReturn(false);
        
        jobNodeStorage.createJobNodeIfNeeded("node1");
        verify(coordinatorRegistryCenter).persist("/testJob/node1", "");
    }

    @Test
    public void testRemoveJobNodeIfExisted() {
        when(coordinatorRegistryCenter.isExisted("/testJob/node1")).thenReturn(true);
        
        jobNodeStorage.removeJobNodeIfExisted("node1");
        verify(coordinatorRegistryCenter).remove("/testJob/node1");
    }

    @Test
    public void testFillJobNodeIfNullOrOverwrite() {
        when(coordinatorRegistryCenter.isExisted("/testJob/node1")).thenReturn(false);
        jobNodeStorage.fillJobNodeIfNullOrOverwrite("node1", "value1");
        verify(coordinatorRegistryCenter).persist("/testJob/node1", "value1");
        
        when(coordinatorRegistryCenter.isExisted("/testJob/node1")).thenReturn(true);
        when(jobConfiguration.isOverwrite()).thenReturn(true);
        when(coordinatorRegistryCenter.getDirectly("/testJob/node1")).thenReturn("oldValue");
        jobNodeStorage.fillJobNodeIfNullOrOverwrite("node1", "value1");
        verify(coordinatorRegistryCenter, times(2)).persist("/testJob/node1", "value1");
    }

    @Test
    public void testFillEphemeralJobNode() {
        jobNodeStorage.fillEphemeralJobNode("node1", "value1");
        verify(coordinatorRegistryCenter).persistEphemeral("/testJob/node1", "value1");
    }

    @Test
    public void testUpdateJobNode() {
        jobNodeStorage.updateJobNode("node1", "value1");
        verify(coordinatorRegistryCenter).update("/testJob/node1", "value1");
    }

    @Test
    public void testReplaceJobNode() {
        jobNodeStorage.replaceJobNode("node1", "value1");
        verify(coordinatorRegistryCenter).persist("/testJob/node1", "value1");
    }

    @Test(expected = JobException.class)
    public void testHandleException() {
        jobNodeStorage.executeInTransaction(curatorTransactionFinal -> {
            throw new RuntimeException("Test Exception");
        });
    }

    @Test
    public void testGetRegistryCenterTime() {
        when(coordinatorRegistryCenter.getRegistryCenterTime("/testJob/systemTime/current")).thenReturn(123456789L);
        assertEquals(123456789L, jobNodeStorage.getRegistryCenterTime());
    }
}
