package net.krotscheck.kangaroo.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class RequestUtilTest {

    private ContainerRequestContext requestContext;
    private MultivaluedMap<String, String> headers;

    @Before
    public void setUp() {
        requestContext = mock(ContainerRequestContext.class);
        headers = mock(MultivaluedMap.class);
        when(requestContext.getHeaders()).thenReturn(headers);
    }

    @Test
    public void testGetCORSRequestedMethod() {
        when(headers.getFirst(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD)).thenReturn("POST");
        String method = RequestUtil.getCORSRequestedMethod(requestContext);
        assertEquals("POST", method);
    }

    @Test
    public void testGetCORSRequestedHeaders() {
        when(headers.getFirst(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)).thenReturn("header1, header2, header3");
        List<String> expectedHeaders = Arrays.asList("header1", "header2", "header3");
        List<String> actualHeaders = RequestUtil.getCORSRequestedHeaders(requestContext);
        assertEquals(expectedHeaders, actualHeaders);
    }

    @Test
    public void testGetCORSRequestedHeadersEmpty() {
        when(headers.getFirst(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS)).thenReturn(null);
        List<String> actualHeaders = RequestUtil.getCORSRequestedHeaders(requestContext);
        assertTrue(actualHeaders.isEmpty());
    }

    @Test
    public void testGetOrigin() throws Exception {
        when(headers.getFirst(HttpHeaders.ORIGIN)).thenReturn("http://example.com");
        URI origin = RequestUtil.getOrigin(requestContext);
        assertNotNull(origin);
        assertEquals("http", origin.getScheme());
        assertEquals("example.com", origin.getHost());
    }

    @Test
    public void testGetReferer() throws Exception {
        when(headers.getFirst(HttpHeaders.REFERER)).thenReturn("http://example.com/page");
        URI referer = RequestUtil.getReferer(requestContext);
        assertNotNull(referer);
        assertEquals("http", referer.getScheme());
        assertEquals("example.com", referer.getHost());
    }

    @Test
    public void testGetHost() throws Exception {
        when(headers.getFirst(HttpHeaders.HOST)).thenReturn("example.com:8080");
        when(requestContext.getUriInfo().getRequestUri().getScheme()).thenReturn("http");
        URI host = RequestUtil.getHost(requestContext);
        assertNotNull(host);
        assertEquals("http", host.getScheme());
        assertEquals("example.com", host.getHost());
        assertEquals(8080, host.getPort());
    }

    @Test(expected = InvalidHostException.class)
    public void testGetHostInvalid() throws Exception {
        when(headers.getFirst(HttpHeaders.HOST)).thenReturn("invalid_host");
        when(requestContext.getUriInfo().getRequestUri().getScheme()).thenReturn("http");
        RequestUtil.getHost(requestContext);
    }

    @Test
    public void testGetForwardedHost() throws Exception {
        when(headers.getFirst(HttpHeaders.X_FORWARDED_HOST)).thenReturn("forwarded.com");
        when(headers.getFirst(HttpHeaders.X_FORWARDED_PORT)).thenReturn("8080");
        when(headers.getFirst(HttpHeaders.X_FORWARDED_PROTO)).thenReturn("https");
        URI forwardedHost = RequestUtil.getForwardedHost(requestContext);
        assertNotNull(forwardedHost);
        assertEquals("https", forwardedHost.getScheme());
        assertEquals("forwarded.com", forwardedHost.getHost());
        assertEquals(8080, forwardedHost.getPort());
    }

    @Test
    public void testIsCrossOriginRequest() throws Exception {
        when(headers.getFirst(HttpHeaders.ORIGIN)).thenReturn("http://origin.com");
        when(headers.getFirst(HttpHeaders.REFERER)).thenReturn("http://referer.com");
        when(headers.getFirst(HttpHeaders.HOST)).thenReturn("example.com");
        when(requestContext.getUriInfo().getRequestUri().getScheme()).thenReturn("http");
        
        assertTrue(RequestUtil.isCrossOriginRequest(requestContext));
    }

    @Test
    public void testIsCrossOriginRequestSameOrigin() throws Exception {
        when(headers.getFirst(HttpHeaders.ORIGIN)).thenReturn("http://example.com");
        when(headers.getFirst(HttpHeaders.REFERER)).thenReturn("http://example.com");
        when(headers.getFirst(HttpHeaders.HOST)).thenReturn("example.com");
        when(requestContext.getUriInfo().getRequestUri().getScheme()).thenReturn("http");
        
        assertFalse(RequestUtil.isCrossOriginRequest(requestContext));
    }
}
