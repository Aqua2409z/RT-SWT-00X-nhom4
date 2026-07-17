package ru.stqa.selenium.decorated.alerts;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import ru.stqa.selenium.decorated.Decorated;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UnhandledAlertHandlingWebDriverTest {

    private WebDriver mockDriver;
    private UnhandledAlertHandlingWebDriver decoratedDriver;
    private UnhandledAlertHandler mockHandler;

    @Before
    public void setUp() {
        mockDriver = mock(WebDriver.class);
        decoratedDriver = new UnhandledAlertHandlingWebDriver(mockDriver);
        mockHandler = mock(UnhandledAlertHandler.class);
        decoratedDriver.registerAlertHandler(mockHandler);
    }

    @Test
    public void testRegisterAlertHandler() {
        assertNotNull(decoratedDriver);
        assertEquals(1, decoratedDriver.handlers.size());
    }

    @Test
    public void testHandleUnhandledAlert() throws Throwable {
        UnhandledAlertException exception = new UnhandledAlertException("Test Alert");
        Method method = UnhandledAlertHandlingWebDriver.class.getDeclaredMethod("onErrorGlobal", Decorated.class, Method.class, InvocationTargetException.class, Object[].class);
        method.setAccessible(true);
        
        InvocationTargetException invocationException = new InvocationTargetException(exception);
        Object[] args = new Object[]{};

        try {
            decoratedDriver.onErrorGlobal(null, method, invocationException, args);
        } catch (Throwable t) {
            // Expected to catch the UnhandledAlertException
        }

        verify(mockHandler, times(1)).handleUnhandledAlert(mockDriver, exception);
    }

    @Test
    public void testOnErrorGlobalWithNonAlertException() throws Throwable {
        Exception nonAlertException = new Exception("Non alert exception");
        Method method = UnhandledAlertHandlingWebDriver.class.getDeclaredMethod("onErrorGlobal", Decorated.class, Method.class, InvocationTargetException.class, Object[].class);
        method.setAccessible(true);
        
        InvocationTargetException invocationException = new InvocationTargetException(nonAlertException);
        Object[] args = new Object[]{};

        try {
            decoratedDriver.onErrorGlobal(null, method, invocationException, args);
        } catch (Throwable t) {
            assertEquals(nonAlertException, t);
        }
    }

    @Test
    public void testOnErrorGlobalWithAlertExceptionRetry() throws Throwable {
        UnhandledAlertException exception = new UnhandledAlertException("Test Alert");
        Method method = UnhandledAlertHandlingWebDriver.class.getDeclaredMethod("onErrorGlobal", Decorated.class, Method.class, InvocationTargetException.class, Object[].class);
        method.setAccessible(true);
        
        InvocationTargetException invocationException = new InvocationTargetException(exception);
        Object[] args = new Object[]{};

        // Simulate the first call throwing an UnhandledAlertException
        doThrow(invocationException).when(mockDriver).getCurrentUrl();

        try {
            decoratedDriver.onErrorGlobal(null, method, invocationException, args);
        } catch (Throwable t) {
            // Expected to catch the UnhandledAlertException
        }

        // Verify that the method is retried
        verify(mockDriver, times(2)).getCurrentUrl();
    }
}
