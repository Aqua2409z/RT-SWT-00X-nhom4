package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.*;
import org.codehaus.httpcache4j.payload.InputStreamPayload;
import org.codehaus.httpcache4j.payload.Payload;
import org.codehaus.httpcache4j.util.ThrowableFunction;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;

public class HTTPResponse_RBL4_589cc16fTest {

    private Headers headers;
    private StatusLine statusLine;
    private Payload payload;

    @Before
    public void setUp() {
        headers = new Headers(Collections.emptyList());
        statusLine = new StatusLine(Status.OK);
        payload = new InputStreamPayload(new ByteArrayInputStream("Test payload".getBytes()), "text/plain", 12);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullStatusLine() {
        new HTTPResponse(Optional.empty(), null, headers);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullPayload() {
        new HTTPResponse(null, statusLine, headers);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullHeaders() {
        new HTTPResponse(Optional.empty(), statusLine, null);
    }

    @Test
    public void testHasPayload() {
        HTTPResponse responseWithPayload = new HTTPResponse(Optional.of(payload), statusLine, headers);
        assertTrue(responseWithPayload.hasPayload());

        HTTPResponse responseWithoutPayload = new HTTPResponse(Optional.empty(), statusLine, headers);
        assertFalse(responseWithoutPayload.hasPayload());
    }

    @Test
    public void testGetStatus() {
        HTTPResponse response = new HTTPResponse(Optional.empty(), statusLine, headers);
        assertEquals(Status.OK, response.getStatus());
    }

    @Test
    public void testGetHeaders() {
        HTTPResponse response = new HTTPResponse(Optional.empty(), statusLine, headers);
        assertEquals(headers, response.getHeaders());
    }

    @Test
    public void testIsCached() {
        Header cacheHeader = CacheHeaderBuilder.getBuilder().createHITXCacheHeader();
        headers = new Headers(Collections.singletonList(cacheHeader));
        HTTPResponse response = new HTTPResponse(Optional.empty(), statusLine, headers);
        assertTrue(response.isCached());
    }

    @Test
    public void testTransformWithPayload() {
        HTTPResponse response = new HTTPResponse(Optional.of(payload), statusLine, headers);
        Optional<String> result = response.transform(p -> {
            try (InputStream is = p.getInputStream()) {
                byte[] data = new byte[12];
                is.read(data);
                return new String(data);
            } catch (IOException e) {
                return null;
            }
        });
        assertTrue(result.isPresent());
        assertEquals("Test payload", result.get());
    }

    @Test
    public void testTransformWithoutPayload() {
        HTTPResponse response = new HTTPResponse(Optional.empty(), statusLine, headers);
        Optional<String> result = response.transform(p -> "Should not be called");
        assertFalse(result.isPresent());
    }

    @Test
    public void testConsume() throws IOException {
        HTTPResponse response = new HTTPResponse(Optional.of(payload), statusLine, headers);
        response.consume();
        assertTrue(response.hasPayload()); // Ensure payload is still present after consume
    }

    @Test
    public void testEqualsAndHashCode() {
        HTTPResponse response1 = new HTTPResponse(Optional.of(payload), statusLine, headers);
        HTTPResponse response2 = new HTTPResponse(Optional.of(payload), statusLine, headers);
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());

        HTTPResponse response3 = new HTTPResponse(Optional.empty(), statusLine, headers);
        assertNotEquals(response1, response3);
    }
}
