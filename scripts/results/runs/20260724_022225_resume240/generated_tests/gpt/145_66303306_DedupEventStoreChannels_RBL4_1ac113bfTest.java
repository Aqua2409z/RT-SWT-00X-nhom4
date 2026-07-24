
package com.bazaarvoice.emodb.event.api;

import org.testng.Assert;
import org.testng.annotations.Test;

public class DedupEventStoreChannels_RBL4_1ac113bfTest {

    @Test
    public void testIsolatedChannels() {
        DedupEventStoreChannels channels = DedupEventStoreChannels.isolated("writePrefix_", "readPrefix_");
        
        String writeChannel = channels.writeChannel("testQueue");
        String readChannel = channels.readChannel("testQueue");
        
        Assert.assertEquals(writeChannel, "writePrefix_testQueue");
        Assert.assertEquals(readChannel, "readPrefix_testQueue");
    }

    @Test
    public void testSharedWriteChannel() {
        DedupEventStoreChannels channels = DedupEventStoreChannels.sharedWriteChannel("readPrefix_");
        
        String writeChannel = channels.writeChannel("testQueue");
        String readChannel = channels.readChannel("testQueue");
        
        Assert.assertEquals(writeChannel, "testQueue");
        Assert.assertEquals(readChannel, "readPrefix_testQueue");
    }

    @Test
    public void testQueueFromWriteChannel() {
        DedupEventStoreChannels channels = DedupEventStoreChannels.isolated("writePrefix_", "readPrefix_");
        
        String queueName = channels.queueFromWriteChannel("writePrefix_testQueue");
        
        Assert.assertEquals(queueName, "testQueue");
    }

    @Test
    public void testQueueFromReadChannel() {
        DedupEventStoreChannels channels = DedupEventStoreChannels.isolated("writePrefix_", "readPrefix_");
        
        String queueName = channels.queueFromReadChannel("readPrefix_testQueue");
        
        Assert.assertEquals(queueName, "testQueue");
    }

    @Test
    public void testQueueFromWriteChannelWithInvalidPrefix() {
        DedupEventStoreChannels channels = DedupEventStoreChannels.isolated("writePrefix_", "readPrefix_");
        
        String queueName = channels.queueFromWriteChannel("readPrefix_testQueue");
        
        Assert.assertNull(queueName);
    }

    @Test
    public void testQueueFromReadChannelWithInvalidPrefix() {
        DedupEventStoreChannels channels = DedupEventStoreChannels.isolated("writePrefix_", "readPrefix_");
        
        String queueName = channels.queueFromReadChannel("writePrefix_testQueue");
        
        Assert.assertNull(queueName);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testIsolatedChannelsWithNullWritePrefix() {
        DedupEventStoreChannels.isolated(null, "readPrefix_");
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testIsolatedChannelsWithNullReadPrefix() {
        DedupEventStoreChannels.isolated("writePrefix_", null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIsolatedChannelsWithSamePrefixes() {
        DedupEventStoreChannels.isolated("samePrefix", "samePrefix");
    }
}
