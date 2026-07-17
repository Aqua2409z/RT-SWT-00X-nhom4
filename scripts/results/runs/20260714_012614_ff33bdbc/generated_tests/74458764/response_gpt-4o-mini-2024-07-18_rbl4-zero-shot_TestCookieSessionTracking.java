
package com.amadeus.session.servlet;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import com.amadeus.session.RepositoryBackedSession;
import com.amadeus.session.RequestWithSession;
import com.amadeus.session.SessionConfiguration;

public class TestCookieSessionTracking {
    private CookieSessionTracking cookieSessionTracking;
    private RequestWithSession request;
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private SessionConfiguration sessionConfiguration;

    @Before
    public void setUp() {
        cookieSessionTracking = new CookieSessionTracking();
        request = mock(RequestWithSession.class);
        httpRequest = mock(HttpServletRequest.class);
        httpResponse = mock(HttpServletResponse.class);
        sessionConfiguration = mock(SessionConfiguration.class);
        
        when(request.getHttpServletRequest()).thenReturn(httpRequest);
    }

    @Test
    public void testRetrieveIdWithValidCookie() {
        Cookie cookie = new Cookie("sessionId", "12345");
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        
        IdAndSource result = cookieSessionTracking.retrieveId(request);
        
        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertTrue(result.isSource());
    }

    @Test
    public void testRetrieveIdWithInvalidCookie() {
        Cookie cookie = new Cookie("sessionId", null);
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        
        IdAndSource result = cookieSessionTracking.retrieveId(request);
        
        assertNull(result);
    }

    @Test
    public void testPropagateSessionWithValidSession() {
        RepositoryBackedSession session = mock(RepositoryBackedSession.class);
        when(session.getId()).thenReturn("12345");
        when(session.isValid()).thenReturn(true);
        when(request.getRepositoryBackedSession(false)).thenReturn(session);
        
        cookieSessionTracking.propagateSession(request, httpResponse);
        
        Cookie[] cookies = httpResponse.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.length);
        assertEquals("12345", cookies[0].getValue());
        assertEquals("/", cookies[0].getPath());
    }

    @Test
    public void testPropagateSessionWithInvalidSession() {
        when(request.getRepositoryBackedSession(false)).thenReturn(null);
        
        cookieSessionTracking.propagateSession(request, httpResponse);
        
        Cookie[] cookies = httpResponse.getCookies();
        assertNotNull(cookies);
        assertEquals(1, cookies.length);
        assertEquals("", cookies[0].getValue());
        assertEquals(0, cookies[0].getMaxAge());
    }

    @Test
    public void testConfigure() {
        when(sessionConfiguration.getAttribute(CookieSessionTracking.COOKIE_HTTP_ONLY_PARAMETER, "true")).thenReturn("false");
        when(sessionConfiguration.getAttribute(CookieSessionTracking.SECURE_COOKIE_PARAMETER, "false")).thenReturn("true");
        when(sessionConfiguration.getAttribute(CookieSessionTracking.COOKIE_CONTEXT_PATH_PARAMETER, null)).thenReturn("/app");
        
        cookieSessionTracking.configure(sessionConfiguration);
        
        assertFalse(cookieSessionTracking.isCookieTracking());
    }

    @Test
    public void testIsCookieTracking() {
        assertTrue(cookieSessionTracking.isCookieTracking());
    }
}
