package com.dangdang.ddframe.job.internal.statistics;

import com.dangdang.ddframe.job.api.JobConfiguration;
import com.dangdang.ddframe.job.internal.server.ServerService;
import com.dangdang.ddframe.reg.base.CoordinatorRegistryCenter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ProcessCountJob_RBL4_fc02f3bfTest {

    private CoordinatorRegistryCenter coordinatorRegistryCenter;
    private JobConfiguration jobConfiguration;
    private ServerService serverService;
    private ProcessCountJob processCountJob;

    @Before
    public void setUp() {
        coordinatorRegistryCenter = Mockito.mock(CoordinatorRegistryCenter.class);
        jobConfiguration = Mockito.mock(JobConfiguration.class);
        serverService = Mockito.mock(ServerService.class);
        when(jobConfiguration.getJobName()).thenReturn("testJob");
        
        processCountJob = new ProcessCountJob(coordinatorRegistryCenter, jobConfiguration);
    }

    @Test
    public void testRun() {
        // Mocking static methods
        ProcessCountStatistics mockStatistics = Mockito.mock(ProcessCountStatistics.class);
        when(mockStatistics.getProcessSuccessCount("testJob")).thenReturn(10);
        when(mockStatistics.getProcessFailureCount("testJob")).thenReturn(2);
        
        // Run the job
        processCountJob.run();
        
        // Verify interactions
        verify(serverService).persistProcessSuccessCount(10);
        verify(serverService).persistProcessFailureCount(2);
        verify(mockStatistics).reset("testJob");
    }
}
