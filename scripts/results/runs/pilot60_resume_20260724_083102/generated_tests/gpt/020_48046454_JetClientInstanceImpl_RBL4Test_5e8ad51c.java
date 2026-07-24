package com.hazelcast.jet.impl;

import com.hazelcast.client.impl.client.DistributedObjectInfo;
import com.hazelcast.client.impl.clientside.HazelcastClientInstanceImpl;
import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.impl.JetClientInstanceImpl;
import com.hazelcast.jet.impl.client.protocol.codec.JetGetJobIdsByNameCodec;
import com.hazelcast.jet.impl.client.protocol.codec.JetGetJobSummaryListCodec;
import com.hazelcast.logging.ILogger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;
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
        // Mocking the response from the client
        ClientMessage response = mock(ClientMessage.class);
        when(client.getClientClusterService().getMasterMember().getUuid()).thenReturn(UUID.randomUUID());
        when(jetClientInstance.invokeRequestOnMasterAndDecodeResponse(any(), any())).thenReturn(Collections.singletonList(mock(Job.class)));

        List<Job> jobs = jetClientInstance.getJobs();
        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());
    }

    @Test
    public void testGetJobSummaryList() {
        // Mocking the response from the client
        ClientMessage response = mock(ClientMessage.class);
        when(jetClientInstance.invokeRequestOnMasterAndDecodeResponse(any(), any())).thenReturn(Collections.emptyList());

        List<JobSummary> jobSummaries = jetClientInstance.getJobSummaryList();
        assertNotNull(jobSummaries);
        assertTrue(jobSummaries.isEmpty());
    }

    @Test
    public void testExistsDistributedObject() {
        String serviceName = "testService";
        String objectName = "testObject";
        when(jetClientInstance.invokeRequestOnAnyMemberAndDecodeResponse(any(), any())).thenReturn(true);

        boolean exists = jetClientInstance.existsDistributedObject(serviceName, objectName);
        assertTrue(exists);
    }

    @Test
    public void testGetDistributedObjects() {
        // Mocking the response from the client
        List<DistributedObjectInfo> distributedObjects = Collections.singletonList(mock(DistributedObjectInfo.class));
        when(jetClientInstance.invokeRequestOnAnyMemberAndDecodeResponse(any(), any())).thenReturn(distributedObjects);

        List<DistributedObjectInfo> result = jetClientInstance.getDistributedObjects();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testGetJobIdsByName() {
        String jobName = "testJob";
        List<Long> jobIds = Collections.singletonList(1L);
        when(jetClientInstance.invokeRequestOnMasterAndDecodeResponse(any(), any())).thenReturn(jobIds);

        List<Long> result = jetClientInstance.getJobIdsByName(jobName);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(Long.valueOf(1), result.get(0));
    }

    @Test
    public void testNewJobProxy() {
        long jobId = 1L;
        Object jobDefinition = new Object();
        JobConfig config = new JobConfig();
        Job job = jetClientInstance.newJobProxy(jobId, jobDefinition, config);
        assertNotNull(job);
    }

    @Test
    public void testGetLogger() {
        ILogger logger = mock(ILogger.class);
        when(client.getLoggingService().getLogger(any())).thenReturn(logger);

        ILogger result = jetClientInstance.getLogger();
        assertNotNull(result);
    }
}
