package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.HTTPMethod;
import org.junit.Test;
import static org.junit.Assert.*;

public class HTTPMethod_RBL4_383f035cTest {

    @Test
    public void testValueOf() {
        assertEquals(HTTPMethod.GET, HTTPMethod.valueOf("GET"));
        assertEquals(HTTPMethod.POST, HTTPMethod.valueOf("POST"));
        assertEquals(HTTPMethod.PUT, HTTPMethod.valueOf("PUT"));
        assertEquals(HTTPMethod.DELETE, HTTPMethod.valueOf("DELETE"));
        assertEquals(HTTPMethod.OPTIONS, HTTPMethod.valueOf("OPTIONS"));
        assertEquals(HTTPMethod.PATCH, HTTPMethod.valueOf("PATCH"));
        assertEquals(HTTPMethod.TRACE, HTTPMethod.valueOf("TRACE"));
        assertEquals(HTTPMethod.HEAD, HTTPMethod.valueOf("HEAD"));
        assertEquals(HTTPMethod.CONNECT, HTTPMethod.valueOf("CONNECT"));
        assertEquals(HTTPMethod.PURGE, HTTPMethod.valueOf("PURGE"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNull() {
        HTTPMethod.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmpty() {
        HTTPMethod.valueOf("");
    }

    @Test
    public void testEquals() {
        assertTrue(HTTPMethod.GET.equals(HTTPMethod.valueOf("get")));
        assertFalse(HTTPMethod.GET.equals(HTTPMethod.POST));
    }

    @Test
    public void testHashCode() {
        assertEquals(HTTPMethod.GET.hashCode(), HTTPMethod.valueOf("GET").hashCode());
        assertNotEquals(HTTPMethod.GET.hashCode(), HTTPMethod.POST.hashCode());
    }

    @Test
    public void testCanHavePayload() {
        assertTrue(HTTPMethod.POST.canHavePayload());
        assertTrue(HTTPMethod.PUT.canHavePayload());
        assertTrue(HTTPMethod.PATCH.canHavePayload());
        assertFalse(HTTPMethod.GET.canHavePayload());
        assertFalse(HTTPMethod.DELETE.canHavePayload());
    }

    @Test
    public void testIsSafe() {
        assertTrue(HTTPMethod.GET.isSafe());
        assertTrue(HTTPMethod.HEAD.isSafe());
        assertFalse(HTTPMethod.POST.isSafe());
        assertFalse(HTTPMethod.PUT.isSafe());
        assertFalse(HTTPMethod.DELETE.isSafe());
    }

    @Test
    public void testIsCacheable() {
        assertTrue(HTTPMethod.GET.isCacheable());
        assertTrue(HTTPMethod.HEAD.isCacheable());
        assertFalse(HTTPMethod.POST.isCacheable());
        assertFalse(HTTPMethod.PUT.isCacheable());
        assertFalse(HTTPMethod.DELETE.isCacheable());
    }

    @Test
    public void testIsIdempotent() {
        assertTrue(HTTPMethod.GET.isIdempotent());
        assertTrue(HTTPMethod.PUT.isIdempotent());
        assertTrue(HTTPMethod.DELETE.isIdempotent());
        assertFalse(HTTPMethod.POST.isIdempotent());
        assertFalse(HTTPMethod.PATCH.isIdempotent());
    }

    @Test
    public void testToString() {
        assertEquals("GET", HTTPMethod.GET.toString());
        assertEquals("POST", HTTPMethod.POST.toString());
        assertEquals("PUT", HTTPMethod.PUT.toString());
        assertEquals("DELETE", HTTPMethod.DELETE.toString());
    }

    @Test
    public void testCompareTo() {
        assertTrue(HTTPMethod.GET.compareTo(HTTPMethod.POST) < 0);
        assertTrue(HTTPMethod.POST.compareTo(HTTPMethod.GET) > 0);
        assertTrue(HTTPMethod.GET.compareTo(HTTPMethod.GET) == 0);
    }

    @Test
    public void testValues() {
        HTTPMethod[] methods = HTTPMethod.values();
        assertNotNull(methods);
        assertTrue(methods.length > 0);
        assertTrue(Arrays.asList(methods).contains(HTTPMethod.GET));
        assertTrue(Arrays.asList(methods).contains(HTTPMethod.POST));
        assertTrue(Arrays.asList(methods).contains(HTTPMethod.PUT));
        assertTrue(Arrays.asList(methods).contains(HTTPMethod.DELETE));
    }
}
