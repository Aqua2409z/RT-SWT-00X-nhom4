package com.vaadin.util;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.UI;

import java.util.Map;

public class CurrentInstance_RBL4Test_2f6a5671 {

    private UI testUI;
    private VaadinSession testSession;
    private VaadinService testService;

    @Before
    public void setUp() {
        testUI = new UI() {
            @Override
            protected void init(VaadinRequest request) {
            }
        };
        testSession = new VaadinSession(testService);
        testService = VaadinService.getCurrent();
    }

    @After
    public void tearDown() {
        CurrentInstance.clearAll();
    }

    @Test
    public void testSetAndGetUI() {
        CurrentInstance.setCurrent(testUI);
        UI retrievedUI = CurrentInstance.get(UI.class);
        assertNotNull(retrievedUI);
        assertEquals(testUI, retrievedUI);
    }

    @Test
    public void testSetAndGetVaadinSession() {
        CurrentInstance.setCurrent(testSession);
        VaadinSession retrievedSession = CurrentInstance.get(VaadinSession.class);
        assertNotNull(retrievedSession);
        assertEquals(testSession, retrievedSession);
    }

    @Test
    public void testSetAndGetVaadinService() {
        CurrentInstance.setCurrent(testSession);
        VaadinService retrievedService = CurrentInstance.get(VaadinService.class);
        assertNotNull(retrievedService);
        assertEquals(testService, retrievedService);
    }

    @Test
    public void testClearAll() {
        CurrentInstance.setCurrent(testUI);
        CurrentInstance.clearAll();
        assertNull(CurrentInstance.get(UI.class));
    }

    @Test
    public void testFallbackResolver() {
        CurrentInstance.defineFallbackResolver(String.class, () -> "Fallback String");
        String fallback = CurrentInstance.get(String.class);
        assertEquals("Fallback String", fallback);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineFallbackResolver_Null() {
        CurrentInstance.defineFallbackResolver(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineFallbackResolver_AlreadyDefined() {
        CurrentInstance.defineFallbackResolver(String.class, () -> "First Resolver");
        CurrentInstance.defineFallbackResolver(String.class, () -> "Second Resolver");
    }

    @Test
    public void testRestoreInstances() {
        Map<Class<?>, CurrentInstance> oldInstances = CurrentInstance.setCurrent(testUI);
        CurrentInstance.restoreInstances(oldInstances);
        UI retrievedUI = CurrentInstance.get(UI.class);
        assertEquals(testUI, retrievedUI);
    }

    @Test
    public void testGetInstances() {
        CurrentInstance.setCurrent(testUI);
        Map<Class<?>, CurrentInstance> instances = CurrentInstance.getInstances();
        assertTrue(instances.containsKey(UI.class));
        assertNotNull(instances.get(UI.class));
    }
}
