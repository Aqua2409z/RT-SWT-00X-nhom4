package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.*;
import org.codehaus.httpcache4j.payload.Payload;
import org.codehaus.httpcache4j.uri.URIBuilder;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.*;

public class HTTPRequest_RBL4_a04d55a6Test {
    private URI uri;
    private HTTPMethod method;
    private Headers headers;
    private Optional<Challenge> challenge;
    private Optional<Payload> payload;
    private HTTPRequest httpRequest;

    @Before
    public void setUp() {
        uri = URI.create("http://example.com");
        method = HTTPMethod.GET;
        headers = new Headers();
        challenge = Optional.empty();
        payload = Optional.empty();
        httpRequest = new HTTPRequest(uri, method, headers, challenge, payload);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullURI() {
        new HTTPRequest(null, method, headers, challenge, payload);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullChallenge() {
        new HTTPRequest(uri, method, headers, null, payload);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullPayload() {
        new HTTPRequest(uri, method, headers, challenge, null);
    }

    @Test
    public void testCopy() {
        HTTPRequest copiedRequest = httpRequest.copy();
        assertEquals(httpRequest, copiedRequest);
    }

    @Test
    public void testGetRequestURI() {
        assertEquals(uri, httpRequest.getRequestURI());
    }

    @Test
    public void testGetNormalizedURI() {
        assertEquals(URIBuilder.fromURI(uri).toNormalizedURI(), httpRequest.getNormalizedURI());
    }

    @Test
    public void testGetHeaders() {
        assertEquals(headers, httpRequest.getHeaders());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, httpRequest.getMethod());
    }

    @Test
    public void testAddHeader() {
        Header header = new Header("Content-Type", "application/json");
        HTTPRequest updatedRequest = httpRequest.addHeader(header);
        assertNotEquals(httpRequest, updatedRequest);
        assertEquals(updatedRequest.getHeaders().get("Content-Type"), "application/json");
    }

    @Test
    public void testSetHeader() {
        HTTPRequest updatedRequest = httpRequest.setHeader("Content-Type", "application/json");
        assertEquals(updatedRequest.getHeaders().get("Content-Type"), "application/json");
    }

    @Test
    public void testWithMethod() {
        HTTPMethod newMethod = HTTPMethod.POST;
        HTTPRequest updatedRequest = httpRequest.withMethod(newMethod);
        assertNotEquals(httpRequest, updatedRequest);
        assertEquals(newMethod, updatedRequest.getMethod());
    }

    @Test
    public void testWithChallenge() {
        Challenge newChallenge = new Challenge("Basic", "realm");
        HTTPRequest updatedRequest = httpRequest.withChallenge(newChallenge);
        assertNotEquals(httpRequest, updatedRequest);
        assertTrue(updatedRequest.getChallenge().isPresent());
    }

    @Test
    public void testWithPayload() {
        Payload newPayload = new Payload("application/json", "{\"key\":\"value\"}");
        HTTPRequest updatedRequest = httpRequest.withPayload(newPayload);
        assertNotEquals(httpRequest, updatedRequest);
        assertTrue(updatedRequest.getPayload().isPresent());
    }

    @Test
    public void testHasPayload() {
        assertFalse(httpRequest.hasPayload());
        Payload newPayload = new Payload("application/json", "{\"key\":\"value\"}");
        HTTPRequest updatedRequest = httpRequest.withPayload(newPayload);
        assertTrue(updatedRequest.hasPayload());
    }

    @Test
    public void testIsSecure() {
        HTTPRequest secureRequest = new HTTPRequest(URI.create("https://example.com"));
        assertTrue(secureRequest.isSecure());
        assertFalse(httpRequest.isSecure());
    }

    @Test
    public void testEqualsAndHashCode() {
        HTTPRequest anotherRequest = new HTTPRequest(uri, method, headers, challenge, payload);
        assertEquals(httpRequest, anotherRequest);
        assertEquals(httpRequest.hashCode(), anotherRequest.hashCode());
    }
}
