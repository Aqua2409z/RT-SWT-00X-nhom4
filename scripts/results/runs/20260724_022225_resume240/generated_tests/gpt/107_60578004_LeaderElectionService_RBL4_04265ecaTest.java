package com.dangdang.ddframe.job.internal.election;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.election.LeaderElectionService;
import com.dangdang.ddframe.job.internal.storage.JobNodeStorage;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class LeaderElectionService_RBL4_04265ecaTest {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private JobNodeStorage jobNodeStorage;
    private LeaderElectionService leaderElectionService;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = Mockito.mock(CoordinatorRegistryCenter.class);
        jobConfiguration = Mockito.mock(JobConfiguration.class);
        jobNodeStorage = Mockito.mock(JobNodeStorage.class);
        leaderElectionService = new LeaderElectionService(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testLeaderForceElection() {
        Mockito.when(jobNodeStorage.isJobNodeExisted(ElectionNode.LEADER_HOST)).thenReturn(false);
        leaderElectionService.leaderForceElection();
        Mockito.verify(jobNodeStorage).executeInLeader(Mockito.eq(ElectionNode.LATCH), Mockito.any(LeaderElectionService.LeaderElectionExecutionCallback.class));
    }

    @Test
    public void testLeaderElection() {
        Mockito.when(jobNodeStorage.isJobNodeExisted(ElectionNode.LEADER_HOST)).thenReturn(false);
        leaderElectionService.leaderElection();
        Mockito.verify(jobNodeStorage).executeInLeader(Mockito.eq(ElectionNode.LATCH), Mockito.any(LeaderElectionService.LeaderElectionExecutionCallback.class));
    }

    @Test
    public void testIsLeaderWhenNoLeader() {
        Mockito.when(jobNodeStorage.getJobNodeData(ElectionNode.LEADER_HOST)).thenReturn(null);
        Mockito.when(jobNodeStorage.isJobNodeExisted(ElectionNode.LEADER_HOST)).thenReturn(false);
        assertFalse(leaderElectionService.isLeader());
    }

    @Test
    public void testIsLeaderWhenIsLeader() {
        String localIp = "127.0.0.1";
        Mockito.when(jobNodeStorage.getJobNodeData(ElectionNode.LEADER_HOST)).thenReturn(localIp);
        Mockito.when(jobNodeStorage.isJobNodeExisted(ElectionNode.LEADER_HOST)).thenReturn(true);
        assertTrue(leaderElectionService.isLeader());
    }

    @Test
    public void testHasLeader() {
        Mockito.when(jobNodeStorage.isJobNodeExisted(ElectionNode.LEADER_HOST)).thenReturn(true);
        assertTrue(leaderElectionService.hasLeader());
    }

    @Test
    public void testRemoveLeader() {
        leaderElectionService.removeLeader();
        Mockito.verify(jobNodeStorage).removeJobNodeIfExisted(ElectionNode.LEADER_HOST);
    }
}
