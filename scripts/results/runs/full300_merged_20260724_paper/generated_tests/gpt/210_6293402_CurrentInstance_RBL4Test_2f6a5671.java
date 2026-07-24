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
        CurrentInstance.set(UI.class, testUI);
        UI retrievedUI = CurrentInstance.get(UI.class);
        assertNotNull(retrievedUI);
        assertEquals(testUI, retrievedUI);
    }

    @Test
    public void testSetAndGetVaadinSession() {
        CurrentInstance.set(VaadinSession.class, testSession);
        VaadinSession retrievedSession = CurrentInstance.get(VaadinSession.class);
        assertNotNull(retrievedSession);
        assertEquals(testSession, retrievedSession);
    }

    @Test
    public void testSetAndGetVaadinService() {
        CurrentInstance.set(VaadinService.class, testService);
        VaadinService retrievedService = CurrentInstance.get(VaadinService.class);
        assertNotNull(retrievedService);
        assertEquals(testService, retrievedService);
    }

    @Test
    public void testClearAll() {
        CurrentInstance.set(UI.class, testUI);
        CurrentInstance.clearAll();
        assertNull(CurrentInstance.get(UI.class));
    }

    @Test
    public void testDefineFallbackResolver() {
        CurrentInstance.defineFallbackResolver(String.class, () -> "Fallback String");
        String result = CurrentInstance.get(String.class);
        assertEquals("Fallback String", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineFallbackResolverNull() {
        CurrentInstance.defineFallbackResolver(String.class, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefineFallbackResolverAlreadyDefined() {
        CurrentInstance.defineFallbackResolver(String.class, () -> "First Fallback");
        CurrentInstance.defineFallbackResolver(String.class, () -> "Second Fallback");
    }

    @Test
    public void testRestoreInstances() {
        CurrentInstance.set(UI.class, testUI);
        Map<Class<?>, CurrentInstance> oldInstances = CurrentInstance.getInstances();
        CurrentInstance.clearAll();
        CurrentInstance.restoreInstances(oldInstances);
        assertNotNull(CurrentInstance.get(UI.class));
        assertEquals(testUI, CurrentInstance.get(UI.class));
    }

    @Test
    public void testGetInstances() {
        CurrentInstance.set(UI.class, testUI);
        Map<Class<?>, CurrentInstance> instances = CurrentInstance.getInstances();
        assertTrue(instances.containsKey(UI.class));
        assertEquals(testUI, instances.get(UI.class).instance.get());
    }
}
