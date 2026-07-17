
package com.amadeus.session;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class TestRepositoryBackedSession {

    private SessionData sessionData;
    private SessionManager sessionManager;
    private SessionFactory sessionFactory;
    private RepositoryBackedSession session;

    @Before
    public void setUp() {
        sessionData = mock(SessionData.class);
        sessionManager = mock(SessionManager.class);
        sessionFactory = mock(SessionFactory.class);
        when(sessionData.getId()).thenReturn("test-session-id");
        when(sessionData.getCreationTime()).thenReturn(System.currentTimeMillis());
        when(sessionData.getLastAccessedTime()).thenReturn(System.currentTimeMillis());
        when(sessionData.getMaxInactiveInterval()).thenReturn(30);
        when(sessionData.isNew()).thenReturn(false);
        when(sessionManager.getConfiguration()).thenReturn(mock(SessionConfiguration.class));
        when(sessionManager.getRepository()).thenReturn(mock(SessionRepository.class));
        session = new RepositoryBackedSession(sessionData, sessionManager, sessionFactory);
    }

    @Test
    public void testGetId() {
        assertEquals("test-session-id", session.getId());
    }

    @Test
    public void testGetCreationTime() {
        long creationTime = session.getCreationTime();
        assertTrue(creationTime > 0);
    }

    @Test
    public void testGetLastAccessedTime() {
        long lastAccessedTime = session.getLastAccessedTime();
        assertTrue(lastAccessedTime > 0);
    }

    @Test
    public void testGetMaxInactiveInterval() {
        assertEquals(30, session.getMaxInactiveInterval());
    }

    @Test
    public void testIsNew() {
        assertFalse(session.isNew());
    }

    @Test
    public void testSetAttribute() {
        session.setAttribute("key1", "value1");
        assertEquals("value1", session.getAttribute("key1"));
    }

    @Test
    public void testRemoveAttribute() {
        session.setAttribute("key1", "value1");
        session.removeAttribute("key1");
        assertNull(session.getAttribute("key1"));
    }

    @Test
    public void testGetAttributeNames() {
        session.setAttribute("key1", "value1");
        session.setAttribute("key2", "value2");
        Enumeration<String> attributeNames = session.getAttributeNames();
        Set<String> namesSet = new HashSet<>();
        while (attributeNames.hasMoreElements()) {
            namesSet.add(attributeNames.nextElement());
        }
        assertTrue(namesSet.contains("key1"));
        assertTrue(namesSet.contains("key2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidate() {
        session.invalidate();
        session.getId(); // This should throw an exception
    }

    @Test
    public void testIsValid() {
        assertTrue(session.isValid());
        session.invalidate();
        assertFalse(session.isValid());
    }

    @Test
    public void testCommit() {
        session.setAttribute("key1", "value1");
        session.commit();
        assertTrue(session.isCommitted());
    }

    @Test
    public void testIsDirty() {
        assertFalse(session.isDirty());
        session.setAttribute("key1", "value1");
        assertTrue(session.isDirty());
    }

    @Test
    public void testGetConcurrentUses() {
        assertEquals(0, session.getConcurrentUses());
        session.checkUsedAndLock();
        assertEquals(1, session.getConcurrentUses());
    }

    @Test
    public void testGetCommitter() {
        assertNotNull(session.getCommitter());
    }

    @Test
    public void testIsExpired() {
        when(sessionData.getLastAccessedTime()).thenReturn(System.currentTimeMillis() - 100000);
        assertTrue(session.isExpired());
    }
}
