
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
    private RepositoryBackedSession session;

    @Before
    public void setUp() {
        cookieSessionTracking = new CookieSessionTracking();
        request = mock(RequestWithSession.class);
        httpRequest = mock(HttpServletRequest.class);
        httpResponse = mock(HttpServletResponse.class);
        session = mock(RepositoryBackedSession.class);
    }

    @Test
    public void testRetrieveIdWithValidCookie() {
        Cookie cookie = new Cookie("sessionId", "12345");
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{cookie});
        when(request.getHttpServletRequest()).thenReturn(httpRequest);
        
        cookieSessionTracking.configure(new SessionConfiguration());
        IdAndSource result = cookieSessionTracking.retrieveId(request);
        
        assertNotNull(result);
        assertEquals("12345", result.getId());
        assertTrue(result.isSource());
    }

    @Test
    public void testRetrieveIdWithInvalidCookie() {
        when(httpRequest.getCookies()).thenReturn(new Cookie[]{new Cookie("sessionId", null)});
        when(request.getHttpServletRequest()).thenReturn(httpRequest);
        
        IdAndSource result = cookieSessionTracking.retrieveId(request);
        
        assertNull(result);
    }

    @Test
    public void testPropagateSessionWithValidSession() {
        when(request.getRepositoryBackedSession(false)).thenReturn(session);
        when(session.isValid()).thenReturn(true);
        when(session.getId()).thenReturn("12345");
        when(httpRequest.isSecure()).thenReturn(false);
        
        cookieSessionTracking.propagateSession(request, httpResponse);
        
        verify(httpResponse).addCookie(argThat(cookie -> 
            "12345".equals(cookie.getValue()) && 
            cookie.getMaxAge() != 0));
    }

    @Test
    public void testPropagateSessionWithInvalidSession() {
        when(request.getRepositoryBackedSession(false)).thenReturn(session);
        when(session.isValid()).thenReturn(false);
        
        cookieSessionTracking.propagateSession(request, httpResponse);
        
        verify(httpResponse).addCookie(argThat(cookie -> 
            cookie.getMaxAge() == 0));
    }

    @Test
    public void testConfigureWithHttpOnly() {
        SessionConfiguration config = mock(SessionConfiguration.class);
        when(config.getAttribute(CookieSessionTracking.COOKIE_HTTP_ONLY_PARAMETER, "true")).thenReturn("true");
        
        cookieSessionTracking.configure(config);
        
        assertTrue(cookieSessionTracking.isCookieTracking());
    }

    @Test
    public void testConfigureWithSecure() {
        SessionConfiguration config = mock(SessionConfiguration.class);
        when(config.getAttribute(CookieSessionTracking.SECURE_COOKIE_PARAMETER, "false")).thenReturn("true");
        
        cookieSessionTracking.configure(config);
        
        assertTrue(cookieSessionTracking.secure);
    }

    @Test
    public void testCookiePath() {
        cookieSessionTracking.contextPath = "/app";
        assertEquals("/app", cookieSessionTracking.cookiePath());
        
        cookieSessionTracking.contextPath = null;
        assertEquals(CookieSessionTracking.DEFAULT_CONTEXT_PATH, cookieSessionTracking.cookiePath());
    }
}
