package software.amazon.awssdk.http.nio.netty.internal.utils;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class ChannelUtils_RBL4_6d41ba87Test {

    private ChannelPipeline pipeline;
    private Channel channel;
    private AttributeKey<String> attributeKey;

    @BeforeMethod
    public void setUp() {
        pipeline = Mockito.mock(ChannelPipeline.class);
        channel = Mockito.mock(Channel.class);
        attributeKey = AttributeKey.valueOf("testKey");
    }

    @Test
    public void testRemoveIfExistsWithClassHandlers() {
        ChannelHandler handler1 = Mockito.mock(ChannelHandler.class);
        ChannelHandler handler2 = Mockito.mock(ChannelHandler.class);

        when(pipeline.get(handler1.getClass())).thenReturn(handler1);
        when(pipeline.get(handler2.getClass())).thenReturn(null);

        ChannelUtils.removeIfExists(pipeline, handler1.getClass(), handler2.getClass());

        verify(pipeline).remove(handler1.getClass());
        verify(pipeline, never()).remove(handler2.getClass());
    }

    @Test
    public void testRemoveIfExistsWithStringHandlers() {
        String handlerName1 = "handler1";
        String handlerName2 = "handler2";

        when(pipeline.get(handlerName1)).thenReturn(handlerName1);
        when(pipeline.get(handlerName2)).thenReturn(null);

        ChannelUtils.removeIfExists(pipeline, handlerName1, handlerName2);

        verify(pipeline).remove(handlerName1);
        verify(pipeline, never()).remove(handlerName2);
    }

    @Test
    public void testGetAttributeExists() {
        Attribute<String> attribute = Mockito.mock(Attribute.class);
        when(channel.attr(attributeKey)).thenReturn(attribute);
        when(attribute.get()).thenReturn("value");

        Optional<String> result = ChannelUtils.getAttribute(channel, attributeKey);

        assert result.isPresent();
        assert "value".equals(result.get());
    }

    @Test
    public void testGetAttributeNotExists() {
        when(channel.attr(attributeKey)).thenReturn(null);

        Optional<String> result = ChannelUtils.getAttribute(channel, attributeKey);

        assert !result.isPresent();
    }
}
