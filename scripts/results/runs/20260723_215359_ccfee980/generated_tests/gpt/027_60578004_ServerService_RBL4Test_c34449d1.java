package com.dangdang.ddframe.job.internal.server;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ServerService_RBL4Test_c34449d1 {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private JobNodeStorage jobNodeStorage;
    private ServerService serverService;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = mock(CoordinatorRegistryCenter.class);
        jobConfiguration = mock(JobConfiguration.class);
        jobNodeStorage = mock(JobNodeStorage.class);
        when(coordinatorRegistryCenter.getJobNodeStorage(anyString())).thenReturn(jobNodeStorage);
        serverService = new ServerService(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testClearPreviousServerStatus() {
        serverService.clearPreviousServerStatus();
        verify(jobNodeStorage).removeJobNodeIfExisted(anyString());
    }

    @Test
    public void testPersistServerOnline() {
        when(jobConfiguration.isOverwrite()).thenReturn(true);
        when(jobConfiguration.isDisabled()).thenReturn(false);
        serverService.persistServerOnline();
        verify(jobNodeStorage).fillJobNodeIfNullOrOverwrite(anyString(), anyString());
        verify(jobNodeStorage).fillEphemeralJobNode(anyString(), any());
        verify(jobNodeStorage).removeJobNodeIfExisted(anyString());
    }

    @Test
    public void testClearJobTriggerStatus() {
        serverService.clearJobTriggerStatus();
        verify(jobNodeStorage).removeJobNodeIfExisted(anyString());
    }

    @Test
    public void testClearJobPausedStatus() {
        serverService.clearJobPausedStatus();
        verify(jobNodeStorage).removeJobNodeIfExisted(anyString());
    }

    @Test
    public void testIsJobPausedManually() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        assertTrue(serverService.isJobPausedManually());
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(false);
        assertFalse(serverService.isJobPausedManually());
    }

    @Test
    public void testProcessServerShutdown() {
        serverService.processServerShutdown();
        verify(jobNodeStorage).removeJobNodeIfExisted(anyString());
    }

    @Test
    public void testUpdateServerStatus() {
        serverService.updateServerStatus(ServerStatus.READY);
        verify(jobNodeStorage).updateJobNode(anyString(), any());
    }

    @Test
    public void testRemoveServerStatus() {
        serverService.removeServerStatus();
        verify(jobNodeStorage).removeJobNodeIfExisted(anyString());
    }

    @Test
    public void testGetAllServers() {
        when(jobNodeStorage.getJobNodeChildrenKeys(anyString())).thenReturn(Arrays.asList("server1", "server2"));
        List<String> servers = serverService.getAllServers();
        assertEquals(2, servers.size());
        assertEquals("server1", servers.get(0));
        assertEquals("server2", servers.get(1));
    }

    @Test
    public void testGetAvailableServers() {
        when(jobNodeStorage.getJobNodeChildrenKeys(anyString())).thenReturn(Arrays.asList("server1", "server2"));
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(false);
        List<String> availableServers = serverService.getAvailableServers();
        assertEquals(1, availableServers.size());
        assertEquals("server1", availableServers.get(0));
    }

    @Test
    public void testIsAvailableServer() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        assertTrue(serverService.isAvailableServer("serverIp"));
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(false);
        assertFalse(serverService.isAvailableServer("serverIp"));
    }

    @Test
    public void testIsLocalServerDisabled() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        assertTrue(serverService.isLocalServerDisabled());
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(false);
        assertFalse(serverService.isLocalServerDisabled());
    }

    @Test
    public void testIsLocalhostServerReady() {
        when(jobNodeStorage.isJobNodeExisted(anyString())).thenReturn(true);
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn(ServerStatus.READY.name());
        assertTrue(serverService.isLocalhostServerReady());
        when(jobNodeStorage.getJobNodeData(anyString())).thenReturn("NOT_READY");
        assertFalse(serverService.isLocalhostServerReady());
    }

    @Test
    public void testPersistProcessSuccessCount() {
        serverService.persistProcessSuccessCount(10);
        verify(jobNodeStorage).replaceJobNode(anyString(), eq(10));
    }

    @Test
    public void testPersistProcessFailureCount() {
        serverService.persistProcessFailureCount(5);
        verify(jobNodeStorage).replaceJobNode(anyString(), eq(5));
    }
}
