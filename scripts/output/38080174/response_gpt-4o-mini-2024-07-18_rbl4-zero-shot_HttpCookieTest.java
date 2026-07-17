package io.datakernel.http;

import io.datakernel.bytebuf.ByteBuf;
import io.datakernel.common.parse.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class HttpCookieTest {
    private HttpCookie cookie;

    @Before
    public void setUp() {
        cookie = HttpCookie.of("testCookie", "testValue");
    }

    @Test
    public void testGetName() {
        assertEquals("testCookie", cookie.getName());
    }

    @Test
    public void testGetValue() {
        assertEquals("testValue", cookie.getValue());
    }

    @Test
    public void testSetValue() {
        cookie.setValue("newValue");
        assertEquals("newValue", cookie.getValue());
    }

    @Test
    public void testWithValue() {
        HttpCookie newCookie = cookie.withValue("anotherValue");
        assertEquals("anotherValue", newCookie.getValue());
    }

    @Test
    public void testWithExpirationDate() {
        Instant expiration = Instant.now().plusSeconds(3600);
        cookie.withExpirationDate(expiration);
        assertEquals(expiration.getEpochSecond(), cookie.getExpirationDate().getEpochSecond());
    }

    @Test
    public void testWithMaxAge() {
        cookie.withMaxAge(3600);
        assertEquals(3600, cookie.getMaxAge());
    }

    @Test
    public void testWithMaxAgeDuration() {
        cookie.withMaxAge(Duration.ofHours(1));
        assertEquals(3600, cookie.getMaxAge());
    }

    @Test
    public void testWithDomain() {
        cookie.withDomain("example.com");
        assertEquals("example.com", cookie.getDomain());
    }

    @Test
    public void testWithPath() {
        cookie.withPath("/path");
        assertEquals("/path", cookie.getPath());
    }

    @Test
    public void testWithSecure() {
        cookie.withSecure(true);
        assertTrue(cookie.isSecure());
    }

    @Test
    public void testWithHttpOnly() {
        cookie.withHttpOnly(true);
        assertTrue(cookie.isHttpOnly());
    }

    @Test
    public void testWithExtension() {
        cookie.withExtension("extensionValue");
        assertEquals("extensionValue", cookie.getExtension());
    }

    @Test
    public void testEquals() {
        HttpCookie anotherCookie = HttpCookie.of("testCookie", "testValue");
        assertTrue(cookie.equals(anotherCookie));
    }

    @Test
    public void testNotEquals() {
        HttpCookie differentCookie = HttpCookie.of("differentCookie", "testValue");
        assertFalse(cookie.equals(differentCookie));
    }

    @Test
    public void testParseFull() throws ParseException {
        byte[] bytes = "Set-Cookie: testCookie=testValue; Max-Age=3600; Domain=example.com; Path=/path; Secure; HttpOnly".getBytes();
        List<HttpCookie> cookies = new ArrayList<>();
        HttpCookie.parseFull(bytes, 0, bytes.length, cookies);
        assertEquals(1, cookies.size());
        HttpCookie parsedCookie = cookies.get(0);
        assertEquals("testCookie", parsedCookie.getName());
        assertEquals("testValue", parsedCookie.getValue());
        assertEquals(3600, parsedCookie.getMaxAge());
        assertEquals("example.com", parsedCookie.getDomain());
        assertEquals("/path", parsedCookie.getPath());
        assertTrue(parsedCookie.isSecure());
        assertTrue(parsedCookie.isHttpOnly());
    }

    @Test
    public void testRenderFull() {
        cookie.withMaxAge(3600).withDomain("example.com").withPath("/path").withSecure(true).withHttpOnly(true);
        ByteBuf buf = ByteBuf.allocate(256);
        cookie.renderFull(buf);
        String rendered = new String(buf.array(), 0, buf.tail());
        assertTrue(rendered.contains("Set-Cookie: testCookie=testValue"));
        assertTrue(rendered.contains("Max-Age=3600"));
        assertTrue(rendered.contains("Domain=example.com"));
        assertTrue(rendered.contains("Path=/path"));
        assertTrue(rendered.contains("Secure"));
        assertTrue(rendered.contains("HttpOnly"));
    }
}
