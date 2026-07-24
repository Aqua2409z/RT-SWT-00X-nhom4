package com.hazelcast.jet.impl;

import com.hazelcast.client.impl.client.DistributedObjectInfo;
import com.hazelcast.client.impl.clientside.HazelcastClientInstanceImpl;
import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.impl.JetClientInstanceImpl;
import com.hazelcast.jet.impl.client.protocol.codec.JetGetJobIdsCodec;
import com.hazelcast.jet.impl.client.protocol.codec.JetGetJobSummaryListCodec;
import com.hazelcast.jet.impl.client.protocol.codec.JetExistsDistributedObjectCodec;
import com.hazelcast.logging.ILogger;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class JetClientInstanceImpl_RBL4Test_5e8ad51c {

    private HazelcastClientInstanceImpl client;
    private JetClientInstanceImpl jetClientInstance;

    @Before
    public void setUp() {
        client = mock(HazelcastClientInstanceImpl.class);
        jetClientInstance = new JetClientInstanceImpl(client);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetConfig() {
        jetClientInstance.getConfig();
    }

    @Test
    public void testGetJobs() {
        List<Long> jobIds = Collections.singletonList(1L);
        when(client.getClientClusterService().getMasterMember().getUuid()).thenReturn(mock(UUID.class));
        when(jetClientInstance.invokeRequestOnMasterAndDecodeResponse(any(), any())).thenReturn(jobIds);

        List<Job> jobs = jetClientInstance.getJobs();
        assertEquals(1, jobs.size());
    }

    @Test
    public void testGetJobSummaryList() {
        List<JobSummary> jobSummaries = Collections.singletonList(mock(JobSummary.class));
        when(jetClientInstance.invokeRequestOnMasterAndDecodeResponse(any(), any())).thenReturn(jobSummaries);

        List<JobSummary> result = jetClientInstance.getJobSummaryList();
        assertEquals(1, result.size());
    }

    @Test
    public void testExistsDistributedObject() {
        when(jetClientInstance.invokeRequestOnAnyMemberAndDecodeResponse(any(), any())).thenReturn(true);

        boolean exists = jetClientInstance.existsDistributedObject("serviceName", "objectName");
        assertTrue(exists);
    }

    @Test
    public void testGetDistributedObjects() {
        List<DistributedObjectInfo> distributedObjects = Collections.singletonList(mock(DistributedObjectInfo.class));
        when(jetClientInstance.invokeRequestOnAnyMemberAndDecodeResponse(any(), any())).thenReturn(distributedObjects);

        List<DistributedObjectInfo> result = jetClientInstance.getDistributedObjects();
        assertEquals(1, result.size());
    }

    @Test
    public void testGetJobIdsByName() {
        List<Long> jobIds = Collections.singletonList(1L);
        when(jetClientInstance.invokeRequestOnMasterAndDecodeResponse(any(), any())).thenReturn(jobIds);

        List<Long> result = jetClientInstance.getJobIdsByName("jobName");
        assertEquals(1, result.size());
    }

    @Test
    public void testNewJobProxy() {
        JobConfig config = new JobConfig();
        Job job = jetClientInstance.newJobProxy(1L, new Object(), config);
        assertNotNull(job);
    }

    @Test
    public void testGetLogger() {
        ILogger logger = mock(ILogger.class);
        when(client.getLoggingService().getLogger(JetClientInstanceImpl.class)).thenReturn(logger);

        ILogger result = jetClientInstance.getLogger();
        assertNotNull(result);
    }
}
