package software.amazon.awssdk.http.nio.netty.internal;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class HonorCloseOnReleaseChannelPool_RBL4_0c77cc85Test {
    private ChannelPool mockDelegatePool;
    private Channel mockChannel;
    private Promise<Void> mockPromise;
    private HonorCloseOnReleaseChannelPool honorCloseOnReleaseChannelPool;

    @BeforeMethod
    public void setUp() {
        mockDelegatePool = Mockito.mock(ChannelPool.class);
        mockChannel = Mockito.mock(Channel.class);
        mockPromise = Mockito.mock(Promise.class);
        honorCloseOnReleaseChannelPool = new HonorCloseOnReleaseChannelPool(mockDelegatePool);
    }

    @Test
    public void testAcquire() {
        Future<Channel> mockFuture = Mockito.mock(Future.class);
        when(mockDelegatePool.acquire()).thenReturn(mockFuture);

        Future<Channel> result = honorCloseOnReleaseChannelPool.acquire();

        Assert.assertEquals(result, mockFuture);
        verify(mockDelegatePool).acquire();
    }

    @Test
    public void testReleaseChannelWithCloseOnReleaseTrue() {
        when(mockChannel.attr(ChannelAttributeKey.CLOSE_ON_RELEASE)).thenReturn(Mockito.mock(Attribute.class));
        when(mockChannel.attr(ChannelAttributeKey.CLOSE_ON_RELEASE).get()).thenReturn(true);
        when(mockChannel.isOpen()).thenReturn(true);
        when(mockChannel.eventLoop().isShuttingDown()).thenReturn(false);
        
        Future<Void> mockFuture = Mockito.mock(Future.class);
        when(mockDelegatePool.release(mockChannel, mockPromise)).thenReturn(mockFuture);

        honorCloseOnReleaseChannelPool.release(mockChannel, mockPromise);

        verify(mockChannel).close();
        verify(mockDelegatePool).release(mockChannel, mockPromise);
    }

    @Test
    public void testReleaseChannelWithCloseOnReleaseFalse() {
        when(mockChannel.attr(ChannelAttributeKey.CLOSE_ON_RELEASE)).thenReturn(Mockito.mock(Attribute.class));
        when(mockChannel.attr(ChannelAttributeKey.CLOSE_ON_RELEASE).get()).thenReturn(false);
        when(mockChannel.isOpen()).thenReturn(true);
        when(mockChannel.eventLoop().isShuttingDown()).thenReturn(false);
        
        Future<Void> mockFuture = Mockito.mock(Future.class);
        when(mockDelegatePool.release(mockChannel, mockPromise)).thenReturn(mockFuture);

        honorCloseOnReleaseChannelPool.release(mockChannel, mockPromise);

        verify(mockChannel, never()).close();
        verify(mockDelegatePool).release(mockChannel, mockPromise);
    }

    @Test
    public void testClose() {
        honorCloseOnReleaseChannelPool.close();

        verify(mockDelegatePool).close();
    }
}
