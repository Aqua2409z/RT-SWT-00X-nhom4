package software.amazon.awssdk.http.nio.netty.internal.http2;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http2.Http2GoAwayFrame;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import software.amazon.awssdk.http.nio.netty.internal.ChannelAttributeKey;
import software.amazon.awssdk.http.nio.netty.internal.http2.GoAwayException;
import software.amazon.awssdk.http.nio.netty.internal.http2.Http2MultiplexedChannelPool;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class Http2GoAwayEventListener_RBL4Test_729d1168 {

    private Channel parentChannel;
    private Http2MultiplexedChannelPool channelPool;
    private Http2GoAwayEventListener eventListener;

    @BeforeMethod
    public void setUp() {
        parentChannel = Mockito.mock(Channel.class);
        channelPool = Mockito.mock(Http2MultiplexedChannelPool.class);
        when(parentChannel.attr(ChannelAttributeKey.HTTP2_MULTIPLEXED_CHANNEL_POOL)).thenReturn(ChannelAttributeKey.HTTP2_MULTIPLEXED_CHANNEL_POOL);
        eventListener = new Http2GoAwayEventListener(parentChannel);
    }

    @Test
    public void testOnGoAwayReceived_withChannelPool() {
        when(parentChannel.attr(ChannelAttributeKey.HTTP2_MULTIPLEXED_CHANNEL_POOL).get()).thenReturn(channelPool);
        int lastStreamId = 1;
        long errorCode = 0;
        ByteBuf debugData = Unpooled.copiedBuffer("Debug data", StandardCharsets.UTF_8);

        eventListener.onGoAwayReceived(lastStreamId, errorCode, debugData);

        verify(channelPool).handleGoAway(parentChannel, lastStreamId, any(GoAwayException.class));
        debugData.release();
    }

    @Test
    public void testOnGoAwayReceived_withoutChannelPool() {
        when(parentChannel.attr(ChannelAttributeKey.HTTP2_MULTIPLEXED_CHANNEL_POOL).get()).thenReturn(null);
        int lastStreamId = 1;
        long errorCode = 0;
        ByteBuf debugData = Unpooled.copiedBuffer("Debug data", StandardCharsets.UTF_8);

        eventListener.onGoAwayReceived(lastStreamId, errorCode, debugData);

        verify(channelPool, never()).handleGoAway(any(), anyInt(), any());
        verify(parentChannel.pipeline()).fireExceptionCaught(any(GoAwayException.class));
        debugData.release();
    }
}
