
package com.zuoxiaolong.niubi.job.core.helper;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class LoggerHelper_RBL4_5d49c4feTest {

    private Logger mockLogger;

    @Before
    public void setUp() {
        mockLogger = Mockito.mock(Logger.class);
        Logger.getLogger(LoggerHelper.class);
        when(Logger.getLogger(any(Class.class))).thenReturn(mockLogger);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInfoWithNullMessage() {
        LoggerHelper.info((Class<?>) null, null);
    }

    @Test
    public void testInfoWithClassAndMessage() {
        when(mockLogger.isInfoEnabled()).thenReturn(true);
        LoggerHelper.info(LoggerHelper.class, "Info message");
        verify(mockLogger).info("Info message");
    }

    @Test
    public void testInfoWithMessage() {
        when(mockLogger.isInfoEnabled()).thenReturn(true);
        LoggerHelper.info("Info message");
        verify(mockLogger).info("Info message");
    }

    @Test
    public void testInfoWithMessageAndThrowable() {
        when(mockLogger.isInfoEnabled()).thenReturn(true);
        Throwable throwable = new Throwable("Test throwable");
        LoggerHelper.info("Info message", throwable);
        verify(mockLogger).info("Info message", throwable);
    }

    @Test
    public void testDebugWithClassAndMessage() {
        when(mockLogger.isDebugEnabled()).thenReturn(true);
        LoggerHelper.debug(LoggerHelper.class, "Debug message");
        verify(mockLogger).debug("Debug message");
    }

    @Test
    public void testDebugWithMessage() {
        when(mockLogger.isDebugEnabled()).thenReturn(true);
        LoggerHelper.debug("Debug message");
        verify(mockLogger).debug("Debug message");
    }

    @Test
    public void testDebugWithMessageAndThrowable() {
        when(mockLogger.isDebugEnabled()).thenReturn(true);
        Throwable throwable = new Throwable("Test throwable");
        LoggerHelper.debug("Debug message", throwable);
        verify(mockLogger).debug("Debug message", throwable);
    }

    @Test
    public void testWarnWithClassAndMessage() {
        LoggerHelper.warn(LoggerHelper.class, "Warn message");
        verify(mockLogger).warn("Warn message");
    }

    @Test
    public void testWarnWithMessage() {
        LoggerHelper.warn("Warn message");
        verify(mockLogger).warn("Warn message");
    }

    @Test
    public void testWarnWithMessageAndThrowable() {
        Throwable throwable = new Throwable("Test throwable");
        LoggerHelper.warn("Warn message", throwable);
        verify(mockLogger).warn("Warn message", throwable);
    }

    @Test
    public void testErrorWithClassAndMessage() {
        Throwable throwable = new Throwable("Test throwable");
        LoggerHelper.error(LoggerHelper.class, "Error message", throwable);
        verify(mockLogger).error("Error message", throwable);
    }

    @Test
    public void testErrorWithMessage() {
        Throwable throwable = new Throwable("Test throwable");
        LoggerHelper.error("Error message", throwable);
        verify(mockLogger).error("Error message", throwable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testErrorWithNullMessage() {
        LoggerHelper.error((Class<?>) null, null);
    }
}
