package ru.stqa.selenium.decorated.events;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;
import ru.stqa.selenium.decorated.Decorated;
import ru.stqa.selenium.decorated.events.EventFiringWebDriver;
import ru.stqa.selenium.decorated.events.WebDriverListener;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class EventFiringWebDriverTest {

    private WebDriver mockDriver;
    private EventFiringWebDriver eventFiringWebDriver;
    private WebDriverListener mockListener;

    @Before
    public void setUp() {
        mockDriver = mock(WebDriver.class);
        eventFiringWebDriver = new EventFiringWebDriver(mockDriver);
        mockListener = mock(WebDriverListener.class);
    }

    @Test
    public void testAddListener() {
        eventFiringWebDriver.addListener(mockListener);
        // Verify that the listener is added
        assertTrue(eventFiringWebDriver.listeners.contains(mockListener));
    }

    @Test
    public void testRemoveListener() {
        eventFiringWebDriver.addListener(mockListener);
        eventFiringWebDriver.removeListener(mockListener);
        // Verify that the listener is removed
        assertFalse(eventFiringWebDriver.listeners.contains(mockListener));
    }

    @Test
    public void testRemoveAllListeners() {
        eventFiringWebDriver.addListener(mockListener);
        eventFiringWebDriver.removeAllListeners();
        // Verify that all listeners are removed
        assertTrue(eventFiringWebDriver.listeners.isEmpty());
    }

    @Test
    public void testBeforeMethodGlobal() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("beforeMethodGlobal", Decorated.class, Method.class, Object[].class);
        Object[] args = new Object[]{};

        eventFiringWebDriver.beforeMethodGlobal(mock(Decorated.class), method, args);

        // Verify that the listener's before method is called
        verify(mockListener, times(1)).beforeMethodGlobal(any(), eq(method), eq(args));
    }

    @Test
    public void testAfterMethodGlobal() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("afterMethodGlobal", Decorated.class, Method.class, Object.class, Object[].class);
        Object[] args = new Object[]{};
        Object result = new Object();

        eventFiringWebDriver.afterMethodGlobal(mock(Decorated.class), method, result, args);

        // Verify that the listener's after method is called
        verify(mockListener, times(1)).afterMethodGlobal(any(), eq(method), eq(result), eq(args));
    }

    @Test
    public void testFireBeforeEvent() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("beforeMethodGlobal", Decorated.class, Method.class, Object[].class);
        Object[] args = new Object[]{};

        eventFiringWebDriver.beforeMethodGlobal(mock(Decorated.class), method, args);

        // Verify that the listener's before method is called
        verify(mockListener, times(1)).beforeMethodGlobal(any(), eq(method), eq(args));
    }

    @Test
    public void testFireAfterEvent() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("afterMethodGlobal", Decorated.class, Method.class, Object.class, Object[].class);
        Object[] args = new Object[]{};
        Object result = new Object();

        eventFiringWebDriver.afterMethodGlobal(mock(Decorated.class), method, result, args);

        // Verify that the listener's after method is called
        verify(mockListener, times(1)).afterMethodGlobal(any(), eq(method), eq(result), eq(args));
    }
}
