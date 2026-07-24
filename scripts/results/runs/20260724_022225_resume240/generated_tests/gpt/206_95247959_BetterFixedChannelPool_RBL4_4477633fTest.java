package software.amazon.awssdk.http.nio.netty.internal.utils;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import software.amazon.awssdk.http.nio.netty.internal.utils.BetterFixedChannelPool;
import software.amazon.awssdk.http.nio.netty.internal.SdkChannelPool;
import software.amazon.awssdk.metrics.MetricCollector;

import java.util.concurrent.CompletableFuture;

public class BetterFixedChannelPool_RBL4_4477633fTest {

    private BetterFixedChannelPool pool;
    private SdkChannelPool mockDelegatePool;
    private EventExecutor mockExecutor;
    private Channel mockChannel;

    @BeforeMethod
    public void setUp() {
        mockDelegatePool = mock(SdkChannelPool.class);
        mockExecutor = mock(EventExecutor.class);
        mockChannel = mock(Channel.class);
        
        pool = BetterFixedChannelPool.builder()
                .channelPool(mockDelegatePool)
                .executor(mockExecutor)
                .maxConnections(2)
                .maxPendingAcquires(2)
                .acquireTimeoutAction(BetterFixedChannelPool.AcquireTimeoutAction.FAIL)
                .acquireTimeoutMillis(1000)
                .build();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderWithInvalidMaxConnections() {
        BetterFixedChannelPool.builder()
                .channelPool(mockDelegatePool)
                .executor(mockExecutor)
                .maxConnections(0)
                .maxPendingAcquires(2)
                .build();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testBuilderWithInvalidMaxPendingAcquires() {
        BetterFixedChannelPool.builder()
                .channelPool(mockDelegatePool)
                .executor(mockExecutor)
                .maxConnections(2)
                .maxPendingAcquires(0)
                .build();
    }

    @Test
    public void testAcquireSuccess() throws Exception {
        Promise<Channel> promise = mock(Promise.class);
        when(mockExecutor.inEventLoop()).thenReturn(true);
        when(mockDelegatePool.acquire(any())).thenReturn(promise);

        Future<Channel> future = pool.acquire();
        assertNotNull(future);
        verify(mockDelegatePool).acquire(any());
    }

    @Test
    public void testAcquireFailureWhenPoolClosed() throws Exception {
        pool.close();
        Future<Channel> future = pool.acquire();
        assertTrue(future.cause() instanceof IllegalStateException);
    }

    @Test
    public void testReleaseSuccess() throws Exception {
        Promise<Void> promise = mock(Promise.class);
        when(mockExecutor.inEventLoop()).thenReturn(true);
        when(mockDelegatePool.release(mockChannel, any())).thenReturn(promise);

        Future<Void> future = pool.release(mockChannel);
        assertNotNull(future);
        verify(mockDelegatePool).release(mockChannel, any());
    }

    @Test
    public void testCollectChannelPoolMetrics() throws Exception {
        MetricCollector mockMetrics = mock(MetricCollector.class);
        CompletableFuture<Void> delegateFuture = CompletableFuture.completedFuture(null);
        when(mockDelegatePool.collectChannelPoolMetrics(mockMetrics)).thenReturn(delegateFuture);

        CompletableFuture<Void> future = pool.collectChannelPoolMetrics(mockMetrics);
        assertNotNull(future);
        future.join(); // Wait for completion
        verify(mockMetrics, times(1)).reportMetric(any(), anyInt());
    }

    @Test
    public void testClose() {
        pool.close();
        verify(mockDelegatePool).close();
    }
}
