package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.*;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;

public class Headers_RBL4_341e479eTest {
    private Headers headers;

    @Before
    public void setUp() {
        headers = new Headers();
    }

    @Test
    public void testAddHeader() {
        Header header = new Header("Content-Type", "application/json");
        Headers newHeaders = headers.add(header);
        assertEquals(1, newHeaders.size());
        assertTrue(newHeaders.contains(header));
    }

    @Test
    public void testGetFirstHeader() {
        Header header = new Header("Accept", "application/json");
        headers = headers.add(header);
        Optional<Header> firstHeader = headers.getFirstHeader("Accept");
        assertTrue(firstHeader.isPresent());
        assertEquals(header, firstHeader.get());
    }

    @Test
    public void testGetFirstHeaderValue() {
        headers = headers.add("Accept", "application/json");
        Optional<String> firstHeaderValue = headers.getFirstHeaderValue("Accept");
        assertTrue(firstHeaderValue.isPresent());
        assertEquals("application/json", firstHeaderValue.get());
    }

    @Test
    public void testSetHeader() {
        Header header = new Header("Content-Type", "text/html");
        headers = headers.add(header);
        Header newHeader = new Header("Content-Type", "application/json");
        Headers updatedHeaders = headers.set(newHeader);
        assertEquals(1, updatedHeaders.size());
        assertTrue(updatedHeaders.contains(newHeader));
        assertFalse(updatedHeaders.contains(header));
    }

    @Test
    public void testRemoveHeader() {
        headers = headers.add("Accept", "application/json");
        Headers updatedHeaders = headers.remove("Accept");
        assertFalse(updatedHeaders.contains("Accept"));
        assertTrue(updatedHeaders.isEmpty());
    }

    @Test
    public void testGetCacheControl() {
        CacheControl cacheControl = new CacheControl("no-cache");
        headers = headers.withCacheControl(cacheControl);
        Optional<CacheControl> retrievedCacheControl = headers.getCacheControl();
        assertTrue(retrievedCacheControl.isPresent());
        assertEquals(cacheControl, retrievedCacheControl.get());
    }

    @Test
    public void testGetDate() {
        LocalDateTime now = LocalDateTime.now();
        headers = headers.withDate(now);
        Optional<LocalDateTime> retrievedDate = headers.getDate();
        assertTrue(retrievedDate.isPresent());
        assertEquals(now.toLocalDate(), retrievedDate.get().toLocalDate());
    }

    @Test
    public void testGetContentType() {
        headers = headers.withContentType(MIMEType.valueOf("application/json"));
        Optional<MIMEType> contentType = headers.getContentType();
        assertTrue(contentType.isPresent());
        assertEquals(MIMEType.valueOf("application/json"), contentType.get());
    }

    @Test
    public void testGetLocation() {
        URI uri = URI.create("http://example.com");
        headers = headers.withLocation(uri);
        Optional<URI> retrievedLocation = headers.getLocation();
        assertTrue(retrievedLocation.isPresent());
        assertEquals(uri, retrievedLocation.get());
    }

    @Test
    public void testGetContentLength() {
        headers = headers.withContentLength(12345);
        Optional<Long> contentLength = headers.getContentLength();
        assertTrue(contentLength.isPresent());
        assertEquals(Long.valueOf(12345), contentLength.get());
    }

    @Test
    public void testEqualsAndHashCode() {
        Header header = new Header("Accept", "application/json");
        Headers headers1 = headers.add(header);
        Headers headers2 = headers.add(header);
        assertEquals(headers1, headers2);
        assertEquals(headers1.hashCode(), headers2.hashCode());
    }

    @Test
    public void testToString() {
        headers = headers.add("Accept", "application/json");
        String expectedString = "Accept: application/json";
        assertEquals(expectedString, headers.toString().trim());
    }
}
