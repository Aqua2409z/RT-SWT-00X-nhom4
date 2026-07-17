
package com.amadeus.session;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSessionConfiguration {

    private SessionConfiguration sessionConfig;

    @Before
    public void setUp() {
        sessionConfig = new SessionConfiguration();
    }

    @Test
    public void testDefaultValues() {
        assertEquals(1800, sessionConfig.getMaxInactiveInterval());
        assertTrue(sessionConfig.isDistributable());
        assertTrue(sessionConfig.isSticky());
        assertTrue(sessionConfig.isLoggingMdcActive());
        assertEquals("JSESSIONID", sessionConfig.getLoggingMdcKey());
        assertEquals("default", sessionConfig.getNamespace());
        assertEquals("JSESSIONID", sessionConfig.getSessionIdName());
        assertEquals(SessionConfiguration.DEFAULT_REPLICATION_TRIGGER, sessionConfig.getReplicationTrigger());
    }

    @Test
    public void testSetMaxInactiveInterval() {
        sessionConfig.setMaxInactiveInterval(3600);
        assertEquals(3600, sessionConfig.getMaxInactiveInterval());
    }

    @Test
    public void testSetDistributable() {
        sessionConfig.setDistributable(false);
        assertFalse(sessionConfig.isDistributable());
    }

    @Test
    public void testSetSticky() {
        sessionConfig.setSticky(false);
        assertFalse(sessionConfig.isSticky());
    }

    @Test
    public void testSetLoggingMdcActive() {
        sessionConfig.setLoggingMdcActive(false);
        assertFalse(sessionConfig.isLoggingMdcActive());
    }

    @Test
    public void testSetLoggingMdcKey() {
        sessionConfig.setLoggingMdcKey("NEW_SESSION_ID");
        assertEquals("NEW_SESSION_ID", sessionConfig.getLoggingMdcKey());
    }

    @Test
    public void testSetNamespace() {
        sessionConfig.setNamespace("testNamespace");
        assertEquals("testNamespace", sessionConfig.getNamespace());
    }

    @Test
    public void testSetSessionIdName() {
        sessionConfig.setSessionIdName("NEW_SESSION_NAME");
        assertEquals("NEW_SESSION_NAME", sessionConfig.getSessionIdName());
    }

    @Test
    public void testSetNonCacheable() {
        sessionConfig.setNonCacheable("attr1,attr2");
        assertTrue(sessionConfig.getNonCacheable().contains("attr1"));
        assertTrue(sessionConfig.getNonCacheable().contains("attr2"));
    }

    @Test
    public void testSetReplicationTrigger() {
        sessionConfig.setReplicationTrigger(SessionConfiguration.ReplicationTrigger.SET);
        assertEquals(SessionConfiguration.ReplicationTrigger.SET, sessionConfig.getReplicationTrigger());
    }

    @Test
    public void testSetTrackerInterval() {
        sessionConfig.setTrackerInterval(5000);
        assertEquals(5000, sessionConfig.getTrackerInterval());
    }

    @Test
    public void testSetTrackerLimits() {
        sessionConfig.setTrackerLimits(100);
        assertEquals(100, sessionConfig.getTrackerLimits());
    }

    @Test
    public void testSetEncryptionKey() {
        sessionConfig.setEncryptionKey("testKey");
        assertEquals("testKey", sessionConfig.getEncryptionKey());
        assertTrue(sessionConfig.isUsingEncryption());
    }

    @Test
    public void testSetDelegateWriter() {
        sessionConfig.setDelegateWriter(true);
        assertTrue(sessionConfig.isDelegateWriter());
    }

    @Test
    public void testToString() {
        String str = sessionConfig.toString();
        assertNotNull(str);
        assertTrue(str.contains("SessionConfiguration"));
    }
}
