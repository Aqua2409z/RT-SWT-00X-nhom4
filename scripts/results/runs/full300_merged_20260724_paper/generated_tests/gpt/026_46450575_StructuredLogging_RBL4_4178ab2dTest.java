
package com.spotify.flo.contrib.styx;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class StructuredLogging_RBL4_4178ab2dTest {

    private Logger rootLogger;
    private LoggerContext loggerContext;
    private ConsoleAppender<ILoggingEvent> appender;

    @Before
    public void setUp() {
        rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        loggerContext = mock(LoggerContext.class);
        appender = mock(ConsoleAppender.class);
        
        when(rootLogger.getLoggerContext()).thenReturn(loggerContext);
        when(loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)).thenReturn(rootLogger);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testConstructor() {
        new StructuredLogging();
    }

    @Test
    public void testConfigureStructuredLoggingDefault() {
        StructuredLogging.configureStructuredLogging();

        verify(loggerContext).reset();
        verify(appender).setTarget("System.err");
        verify(appender).setName("stderr");
        verify(appender).setContext(loggerContext);
        verify(appender).start();
        verify(rootLogger).addAppender(appender);
        verify(rootLogger).setLevel(anyInt());
        verifyStatic(SLF4JBridgeHandler.class);
        SLF4JBridgeHandler.install();
    }

    @Test
    public void testConfigureStructuredLoggingWithLevel() {
        Level level = Level.DEBUG;
        StructuredLogging.configureStructuredLogging(level);

        verify(loggerContext).reset();
        verify(appender).setTarget("System.err");
        verify(appender).setName("stderr");
        verify(appender).setContext(loggerContext);
        verify(appender).start();
        verify(rootLogger).addAppender(appender);
        verify(rootLogger).setLevel(level.toInt());
        verifyStatic(SLF4JBridgeHandler.class);
        SLF4JBridgeHandler.install();
    }

    @Test
    public void testDefaultLoggingLevel() {
        System.setProperty("FLO_LOGGING_LEVEL", "ERROR");
        Level level = StructuredLogging.defaultLoggingLevel();
        assertEquals(Level.ERROR, level);
        
        System.clearProperty("FLO_LOGGING_LEVEL");
        level = StructuredLogging.defaultLoggingLevel();
        assertEquals(Level.INFO, level);
    }
}
