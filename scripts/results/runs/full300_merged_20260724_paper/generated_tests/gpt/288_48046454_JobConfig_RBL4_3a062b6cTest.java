package com.hazelcast.jet.config;

import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.config.ProcessingGuarantee;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class JobConfig_RBL4_3a062b6cTest {

    private JobConfig jobConfig;

    @Before
    public void setUp() {
        jobConfig = new JobConfig();
    }

    @Test
    public void testSetName() {
        jobConfig.setName("TestJob");
        assertEquals("TestJob", jobConfig.getName());
    }

    @Test
    public void testSetSplitBrainProtection() {
        jobConfig.setSplitBrainProtection(true);
        assertTrue(jobConfig.isSplitBrainProtectionEnabled());
    }

    @Test
    public void testSetAutoScaling() {
        jobConfig.setAutoScaling(false);
        assertFalse(jobConfig.isAutoScaling());
    }

    @Test
    public void testSetSuspendOnFailure() {
        jobConfig.setSuspendOnFailure(true);
        assertTrue(jobConfig.isSuspendOnFailure());
    }

    @Test
    public void testSetProcessingGuarantee() {
        jobConfig.setProcessingGuarantee(ProcessingGuarantee.AT_LEAST_ONCE);
        assertEquals(ProcessingGuarantee.AT_LEAST_ONCE, jobConfig.getProcessingGuarantee());
    }

    @Test
    public void testSetSnapshotIntervalMillis() {
        jobConfig.setSnapshotIntervalMillis(5000);
        assertEquals(5000, jobConfig.getSnapshotIntervalMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeSnapshotIntervalMillis() {
        jobConfig.setSnapshotIntervalMillis(-1000);
    }

    @Test
    public void testAddJarWithFile() {
        File file = new File("test.jar");
        jobConfig.addJar(file);
        assertNotNull(jobConfig.getResourceConfigs().get("test.jar"));
    }

    @Test
    public void testAddClasspathResourceWithFile() {
        File file = new File("testResource.txt");
        jobConfig.addClasspathResource(file);
        assertNotNull(jobConfig.getResourceConfigs().get("testResource.txt"));
    }

    @Test
    public void testAttachFile() throws Exception {
        File file = new File("testAttach.txt");
        jobConfig.attachFile(file);
        assertNotNull(jobConfig.getResourceConfigs().get("testAttach.txt"));
    }

    @Test
    public void testAttachDirectory() throws Exception {
        File dir = new File("testDir");
        dir.mkdir();
        jobConfig.attachDirectory(dir);
        assertNotNull(jobConfig.getResourceConfigs().get("testDir"));
    }

    @Test
    public void testRegisterSerializer() {
        jobConfig.registerSerializer(String.class, StringSerializer.class);
        assertEquals(StringSerializer.class.getName(), jobConfig.getSerializerConfigs().get(String.class.getName()));
    }

    @Test
    public void testSetMetricsEnabled() {
        jobConfig.setMetricsEnabled(false);
        assertFalse(jobConfig.isMetricsEnabled());
    }

    @Test
    public void testSetStoreMetricsAfterJobCompletion() {
        jobConfig.setStoreMetricsAfterJobCompletion(true);
        assertTrue(jobConfig.isStoreMetricsAfterJobCompletion());
    }

    @Test
    public void testSetInitialSnapshotName() {
        jobConfig.setInitialSnapshotName("snapshot1");
        assertEquals("snapshot1", jobConfig.getInitialSnapshotName());
    }

    @Test
    public void testEqualsAndHashCode() {
        JobConfig anotherJobConfig = new JobConfig();
        jobConfig.setName("TestJob");
        anotherJobConfig.setName("TestJob");
        assertEquals(jobConfig, anotherJobConfig);
        assertEquals(jobConfig.hashCode(), anotherJobConfig.hashCode());
    }

    @Test
    public void testToString() {
        jobConfig.setName("TestJob");
        String expectedString = "JobConfig {name=TestJob, processingGuarantee=NONE, snapshotIntervalMillis=10000, autoScaling=true, suspendOnFailure=false, splitBrainProtectionEnabled=false, enableMetrics=true, storeMetricsAfterJobCompletion=false, resourceConfigs={}, serializerConfigs={}, classLoaderFactory=null, initialSnapshotName=null}";
        assertEquals(expectedString, jobConfig.toString());
    }
}
