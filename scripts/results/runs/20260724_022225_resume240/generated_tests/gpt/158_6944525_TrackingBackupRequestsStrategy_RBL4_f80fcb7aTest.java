package com.linkedin.d2.backuprequests;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.Optional;

public class TrackingBackupRequestsStrategy_RBL4_f80fcb7aTest {

    private BackupRequestsStrategy mockDelegate;
    private TrackingBackupRequestsStrategy trackingStrategy;

    @BeforeMethod
    public void setUp() {
        mockDelegate = new MockBackupRequestsStrategy();
        trackingStrategy = new TrackingBackupRequestsStrategy(mockDelegate);
    }

    @Test
    public void testGetTimeUntilBackupRequestNano() {
        Optional<Long> delay = trackingStrategy.getTimeUntilBackupRequestNano();
        Assert.assertTrue(delay.isPresent());
        Assert.assertEquals(delay.get().longValue(), 100L);
    }

    @Test
    public void testRecordCompletion() {
        trackingStrategy.recordCompletion(200L);
        Assert.assertTrue(true); // Assuming no exceptions means success
    }

    @Test
    public void testIsBackupRequestAllowed() {
        boolean allowed = trackingStrategy.isBackupRequestAllowed();
        Assert.assertTrue(allowed);
        Assert.assertEquals(trackingStrategy.getStats().getAllowed(), 1);
    }

    @Test
    public void testBackupRequestSuccess() {
        trackingStrategy.backupRequestSuccess();
        Assert.assertEquals(trackingStrategy.getStats().getSuccessful(), 1);
    }

    @Test
    public void testGetStats() {
        BackupRequestsStrategyStats stats = trackingStrategy.getStats();
        Assert.assertEquals(stats.getAllowed(), 0);
        Assert.assertEquals(stats.getSuccessful(), 0);
    }

    @Test
    public void testGetDiffStats() {
        trackingStrategy.isBackupRequestAllowed();
        trackingStrategy.backupRequestSuccess();
        BackupRequestsStrategyStats diffStats = trackingStrategy.getDiffStats();
        Assert.assertEquals(diffStats.getAllowed(), 1);
        Assert.assertEquals(diffStats.getSuccessful(), 1);
    }

    @Test
    public void testToString() {
        String str = trackingStrategy.toString();
        Assert.assertNotNull(str);
        Assert.assertTrue(str.contains("TrackingBackupRequestsStrategy"));
    }

    private class TrackingBackupRequestsStrategy_RBL4_f80fcb7aTest implements BackupRequestsStrategy {
        @Override
        public Optional<Long> getTimeUntilBackupRequestNano() {
            return Optional.of(100L);
        }

        @Override
        public void recordCompletion(long responseTime) {
            // Mock implementation
        }

        @Override
        public boolean isBackupRequestAllowed() {
            return true;
        }
    }
}
