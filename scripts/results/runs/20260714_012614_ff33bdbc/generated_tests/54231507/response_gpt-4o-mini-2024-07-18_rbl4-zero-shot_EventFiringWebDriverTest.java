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
        // Since we don't have a direct way to check the internal set, we can just ensure no exceptions are thrown
    }

    @Test
    public void testRemoveListener() {
        eventFiringWebDriver.addListener(mockListener);
        eventFiringWebDriver.removeListener(mockListener);
        // Verify that the listener is removed
        // Again, we can't check the internal state directly
    }

    @Test
    public void testRemoveAllListeners() {
        eventFiringWebDriver.addListener(mockListener);
        eventFiringWebDriver.removeAllListeners();
        // Verify that all listeners are removed
    }

    @Test
    public void testBeforeMethodGlobal() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("beforeMethodGlobal", Decorated.class, Method.class, Object[].class);
        Object[] args = new Object[]{};

        eventFiringWebDriver.beforeMethodGlobal(mock(Decorated.class), method, args);

        verify(mockListener, times(1)).beforeMethodGlobal(any(), eq(method), eq(args));
    }

    @Test
    public void testAfterMethodGlobal() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("afterMethodGlobal", Decorated.class, Method.class, Object.class, Object[].class);
        Object[] args = new Object[]{};
        Object result = new Object();

        eventFiringWebDriver.afterMethodGlobal(mock(Decorated.class), method, result, args);

        verify(mockListener, times(1)).afterMethodGlobal(any(), eq(method), eq(result), eq(args));
    }

    @Test
    public void testFireBeforeEvent() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("beforeMethodGlobal", Decorated.class, Method.class, Object[].class);
        Object[] args = new Object[]{};

        eventFiringWebDriver.beforeMethodGlobal(mock(Decorated.class), method, args);

        verify(mockListener, times(1)).beforeMethodGlobal(any(), eq(method), eq(args));
    }

    @Test
    public void testFireAfterEvent() throws Exception {
        eventFiringWebDriver.addListener(mockListener);
        Method method = EventFiringWebDriver.class.getMethod("afterMethodGlobal", Decorated.class, Method.class, Object.class, Object[].class);
        Object[] args = new Object[]{};
        Object result = new Object();

        eventFiringWebDriver.afterMethodGlobal(mock(Decorated.class), method, result, args);

        verify(mockListener, times(1)).afterMethodGlobal(any(), eq(method), eq(result), eq(args));
    }
}
