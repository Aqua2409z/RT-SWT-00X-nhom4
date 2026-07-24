package software.amazon.awssdk.http.nio.netty.internal;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import software.amazon.awssdk.http.nio.netty.SdkEventLoopGroup;
import software.amazon.awssdk.http.nio.netty.internal.SharedSdkEventLoopGroup;

import java.util.concurrent.TimeUnit;

public class SharedSdkEventLoopGroup_RBL4_9e42aa37Test {

    private SdkEventLoopGroup eventLoopGroup;

    @BeforeClass
    public void setUp() {
        eventLoopGroup = SharedSdkEventLoopGroup.get();
    }

    @Test
    public void testGetSharedEventLoopGroup() {
        Assert.assertNotNull(eventLoopGroup, "EventLoopGroup should not be null");
    }

    @Test
    public void testReferenceCountIncrements() {
        int initialCount = SharedSdkEventLoopGroup.referenceCount();
        SharedSdkEventLoopGroup.get();
        Assert.assertEquals(SharedSdkEventLoopGroup.referenceCount(), initialCount + 1, "Reference count should increment");
    }

    @Test
    public void testDecrementReferenceCountAndShutdown() throws Exception {
        SharedSdkEventLoopGroup.get(); // Increment reference count
        int currentCount = SharedSdkEventLoopGroup.referenceCount();
        Assert.assertEquals(currentCount, 2, "Reference count should be 2 after getting it twice");

        // Decrement reference count and check if it returns a future
        eventLoopGroup.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
        Assert.assertEquals(SharedSdkEventLoopGroup.referenceCount(), 1, "Reference count should be 1 after one shutdown");

        // Shutdown again to bring it to 0 and check if it shuts down gracefully
        eventLoopGroup.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
        Assert.assertEquals(SharedSdkEventLoopGroup.referenceCount(), 0, "Reference count should be 0 after final shutdown");
    }

    @AfterClass
    public void tearDown() {
        // Ensure that the event loop group is properly shut down
        eventLoopGroup.shutdownGracefully(0, 0, TimeUnit.MILLISECONDS);
    }
}
