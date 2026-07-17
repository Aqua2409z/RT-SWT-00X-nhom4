package com.google.cloud.runtimes.tomcat.session;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Session;
import org.apache.catalina.Store;
import org.apache.catalina.session.StandardSession;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class DatastoreManagerTest {

    private DatastoreManager datastoreManager;
    private Store mockStore;

    @Before
    public void setUp() {
        datastoreManager = new DatastoreManager();
        mockStore = mock(Store.class);
        datastoreManager.setStore(mockStore);
    }

    @Test(expected = LifecycleException.class)
    public void testStartInternal_NoStoreConfigured() throws LifecycleException {
        datastoreManager.setStore(null);
        datastoreManager.startInternal();
    }

    @Test
    public void testStartInternal_StoreIsStarted() throws LifecycleException {
        datastoreManager.startInternal();
        verify(mockStore).start();
    }

    @Test
    public void testFindSession_SessionExists() throws IOException, ClassNotFoundException {
        String sessionId = "session1";
        Session mockSession = mock(Session.class);
        when(mockStore.load(sessionId)).thenReturn(mockSession);

        Session result = datastoreManager.findSession(sessionId);
        assertNotNull(result);
        assertEquals(mockSession, result);
    }

    @Test
    public void testFindSession_SessionDoesNotExist() throws IOException, ClassNotFoundException {
        String sessionId = "session2";
        when(mockStore.load(sessionId)).thenReturn(null);

        Session result = datastoreManager.findSession(sessionId);
        assertNull(result);
    }

    @Test
    public void testRemove_SessionRemovedFromStore() throws IOException {
        String sessionId = "session3";
        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn(sessionId);

        datastoreManager.remove(mockSession);
        verify(mockStore).remove(sessionId);
    }

    @Test
    public void testGetActiveSessionsFull() throws IOException {
        when(mockStore.getSize()).thenReturn(5);

        int result = datastoreManager.getActiveSessionsFull();
        assertEquals(5, result);
    }

    @Test
    public void testGetSessionIdsFull() throws IOException {
        String[] keys = {"session1", "session2"};
        when(mockStore.keys()).thenReturn(keys);

        Set<String> result = datastoreManager.getSessionIdsFull();
        Set<String> expected = new HashSet<>(Set.of(keys));
        assertEquals(expected, result);
    }

    @Test
    public void testStopInternal_StoreIsStopped() throws LifecycleException {
        datastoreManager.stopInternal();
        verify(mockStore).stop();
    }

    @Test
    public void testProcessExpires() {
        datastoreManager.processExpires();
        verify(mockStore).processExpires();
    }

    @Test
    public void testGetNewSession() {
        StandardSession session = datastoreManager.getNewSession();
        assertNotNull(session);
        assertTrue(session instanceof DatastoreSession);
    }
}
