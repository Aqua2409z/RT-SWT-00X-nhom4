package software.amazon.awssdk.http.nio.netty.internal.http2;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.pool.ChannelPool;
import io.netty.handler.codec.http2.Http2SettingsFrame;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

public class Http2SettingsFrameHandler_RBL4_27231889Test {

    private Channel channel;
    private ChannelHandlerContext ctx;
    private Http2SettingsFrameHandler handler;
    private AtomicReference<ChannelPool> channelPoolRef;
    private ChannelPool channelPool;

    @BeforeMethod
    public void setUp() {
        channel = mock(Channel.class);
        ctx = mock(ChannelHandlerContext.class);
        channelPoolRef = new AtomicReference<>();
        channelPool = mock(ChannelPool.class);
        channelPoolRef.set(channelPool);
        handler = new Http2SettingsFrameHandler(channel, 10, channelPoolRef);
    }

    @Test
    public void testChannelRead0_withValidSettings() {
        Http2SettingsFrame settingsFrame = mock(Http2SettingsFrame.class);
        when(settingsFrame.settings()).thenReturn(new Http2SettingsFrame.Settings(5));

        handler.channelRead0(ctx, settingsFrame);

        verify(channel).attr(MAX_CONCURRENT_STREAMS).set(5L);
        verify(channel).attr(PROTOCOL_FUTURE).get().complete(Protocol.HTTP2);
    }

    @Test
    public void testChannelRead0_withNullMaxConcurrentStreams() {
        Http2SettingsFrame settingsFrame = mock(Http2SettingsFrame.class);
        when(settingsFrame.settings()).thenReturn(new Http2SettingsFrame.Settings(null));

        handler.channelRead0(ctx, settingsFrame);

        verify(channel).attr(MAX_CONCURRENT_STREAMS).set(10L);
        verify(channel).attr(PROTOCOL_FUTURE).get().complete(Protocol.HTTP2);
    }

    @Test
    public void testChannelUnregistered_whenProtocolFutureNotDone() {
        CompletableFuture<Protocol> protocolFuture = mock(CompletableFuture.class);
        when(channel.attr(PROTOCOL_FUTURE).get()).thenReturn(protocolFuture);
        when(protocolFuture.isDone()).thenReturn(false);

        handler.channelUnregistered(ctx);

        verify(protocolFuture).completeExceptionally(any(IOException.class));
        verify(ctx).fireExceptionCaught(any(IOException.class));
        verify(channelPool).release(channel);
    }

    @Test
    public void testExceptionCaught() {
        Throwable cause = new RuntimeException("Test Exception");

        handler.exceptionCaught(ctx, cause);

        verify(channel).attr(PROTOCOL_FUTURE).get().completeExceptionally(cause);
        verify(ctx).fireExceptionCaught(cause);
        verify(channelPool).release(channel);
    }

    @Test
    public void testChannelError() {
        Throwable cause = new RuntimeException("Test Error");

        handler.channelError(cause, channel, ctx);

        verify(channel).attr(PROTOCOL_FUTURE).get().completeExceptionally(cause);
        verify(ctx).fireExceptionCaught(cause);
        verify(channelPool).release(channel);
    }

    @Test
    public void testChannelError_whenChannelIsActive() {
        when(channel.isActive()).thenReturn(true);

        handler.channelError(new RuntimeException("Test Error"), channel, ctx);

        verify(channel).close();
        verify(channelPool).release(channel);
    }

    @Test
    public void testChannelError_whenChannelIsNotActive() {
        when(channel.isActive()).thenReturn(false);

        handler.channelError(new RuntimeException("Test Error"), channel, ctx);

        verify(channel, never()).close();
        verify(channelPool).release(channel);
    }
}
