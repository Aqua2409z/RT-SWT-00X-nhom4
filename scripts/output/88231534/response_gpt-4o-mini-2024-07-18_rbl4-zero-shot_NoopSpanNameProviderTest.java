package io.opentracing.contrib.mongo.common.providers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import com.mongodb.event.CommandStartedEvent;
import org.junit.Before;
import org.junit.Test;

public class NoopSpanNameProviderTest {

    private NoopSpanNameProvider provider;

    @Before
    public void setUp() {
        provider = new NoopSpanNameProvider();
    }

    @Test
    public void testGenerateNameWithValidEvent() {
        CommandStartedEvent event = new CommandStartedEvent(1, "testCommand", null, null, null, null, null, null);
        String result = provider.generateName(event);
        assertEquals("testCommand", result);
    }

    @Test
    public void testGenerateNameWithNullEvent() {
        String result = provider.generateName(null);
        assertEquals(NoopSpanNameProvider.NO_OPERATION, result);
    }

    @Test
    public void testGenerateNameWithNullCommandName() {
        CommandStartedEvent event = new CommandStartedEvent(1, null, null, null, null, null, null, null);
        String result = provider.generateName(event);
        assertEquals(NoopSpanNameProvider.NO_OPERATION, result);
    }
}
