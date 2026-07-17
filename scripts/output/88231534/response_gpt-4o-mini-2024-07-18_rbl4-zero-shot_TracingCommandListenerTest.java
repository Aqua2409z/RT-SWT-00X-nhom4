package io.opentracing.contrib.mongo.common;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.contrib.mongo.common.ExcludedCommand;
import io.opentracing.contrib.mongo.common.SpanDecorator;
import io.opentracing.contrib.mongo.common.TracingCommandListener;
import io.opentracing.tag.Tags;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class TracingCommandListenerTest {

    private Tracer tracer;
    private Span span;
    private TracingCommandListener listener;

    @Before
    public void setUp() {
        tracer = mock(Tracer.class);
        span = mock(Span.class);
        when(tracer.buildSpan(anyString())).thenReturn(mock(Tracer.SpanBuilder.class));
        when(tracer.buildSpan(anyString()).start()).thenReturn(span);
        listener = new TracingCommandListener(tracer, null, Collections.emptyList());
    }

    @Test
    public void testCommandStarted() {
        CommandStartedEvent event = mock(CommandStartedEvent.class);
        when(event.getRequestId()).thenReturn(1);
        when(event.getCommand()).thenReturn(new BsonDocument("test", new BsonValue() {}));

        listener.commandStarted(event);

        assertNotNull(listener.cache.get(1));
        verify(tracer).buildSpan(anyString());
        verify(span).finish();
    }

    @Test
    public void testCommandSucceeded() {
        CommandStartedEvent startEvent = mock(CommandStartedEvent.class);
        when(startEvent.getRequestId()).thenReturn(1);
        when(startEvent.getCommand()).thenReturn(new BsonDocument("test", new BsonValue() {}));
        listener.commandStarted(startEvent);

        CommandSucceededEvent succeededEvent = mock(CommandSucceededEvent.class);
        when(succeededEvent.getRequestId()).thenReturn(1);

        listener.commandSucceeded(succeededEvent);

        assertNull(listener.cache.get(1));
        verify(span).finish();
    }

    @Test
    public void testCommandFailed() {
        CommandStartedEvent startEvent = mock(CommandStartedEvent.class);
        when(startEvent.getRequestId()).thenReturn(1);
        when(startEvent.getCommand()).thenReturn(new BsonDocument("test", new BsonValue() {}));
        listener.commandStarted(startEvent);

        CommandFailedEvent failedEvent = mock(CommandFailedEvent.class);
        when(failedEvent.getRequestId()).thenReturn(1);

        listener.commandFailed(failedEvent);

        assertNull(listener.cache.get(1));
        verify(span).finish();
    }

    @Test
    public void testExcludedCommands() {
        ExcludedCommand excludedCommand = mock(ExcludedCommand.class);
        when(excludedCommand.entrySet()).thenReturn(Collections.emptySet());
        listener = new TracingCommandListener(tracer, null, Collections.singletonList(excludedCommand));

        CommandStartedEvent event = mock(CommandStartedEvent.class);
        when(event.getRequestId()).thenReturn(1);
        when(event.getCommand()).thenReturn(new BsonDocument("test", new BsonValue() {}));

        listener.commandStarted(event);

        assertNull(listener.cache.get(1));
    }
}
