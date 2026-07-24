package ch.entwine.weblounge.common.impl.url;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.entwine.weblounge.common.url.Path;

public class UrlImpl_RBL4_8ce169b0Test {

    private UrlImpl url;

    @Before
    public void setUp() {
        url = new UrlImpl("/test/path");
    }

    @Test
    public void testGetPath() {
        assertEquals("/test/path", url.getPath());
    }

    @Test
    public void testSetPath() {
        url.setPath("new/path");
        assertEquals("/new/path", url.getPath());
    }

    @Test
    public void testGetPathSeparator() {
        assertEquals('/', url.getPathSeparator());
    }

    @Test
    public void testStartsWith() {
        assertTrue(url.startsWith("/test"));
        assertFalse(url.startsWith("/wrong"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartsWithNull() {
        url.startsWith(null);
    }

    @Test
    public void testEndsWith() {
        assertTrue(url.endsWith("path"));
        assertFalse(url.endsWith("wrong"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndsWithNull() {
        url.endsWith(null);
    }

    @Test
    public void testIsPrefixOf() {
        Path otherUrl = new UrlImpl("/test/path/extra");
        assertTrue(url.isPrefixOf(otherUrl));
        
        otherUrl = new UrlImpl("/wrong/path");
        assertFalse(url.isPrefixOf(otherUrl));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsPrefixOfNull() {
        url.isPrefixOf(null);
    }

    @Test
    public void testIsExtensionOf() {
        Path otherUrl = new UrlImpl("/test");
        assertTrue(url.isExtensionOf(otherUrl));
        
        otherUrl = new UrlImpl("/wrong");
        assertFalse(url.isExtensionOf(otherUrl));
    }

    @Test
    public void testConcat() {
        String result = UrlImpl.concat("/test", "path", '/');
        assertEquals("/test/path", result);
        
        result = UrlImpl.concat("/test/", "/path", '/');
        assertEquals("/test/path", result);
        
        result = UrlImpl.concat("/test", "/path", '/');
        assertEquals("/test/path", result);
        
        result = UrlImpl.concat("/test/", "path", '/');
        assertEquals("/test/path", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConcatNullPrefix() {
        UrlImpl.concat(null, "path", '/');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConcatNullSuffix() {
        UrlImpl.concat("/test", null, '/');
    }

    @Test
    public void testTrim() {
        assertEquals("/test/path/", url.trim("  /test/path  "));
        assertEquals("/test/path/", url.trim("/test/path/"));
        assertEquals("/test/path/", url.trim("/test/path"));
        assertEquals("/test/path/", url.trim("  /test/path/  "));
        assertEquals("/test/path/", url.trim("/test//path/"));
    }

    @Test
    public void testEquals() {
        UrlImpl otherUrl = new UrlImpl("/test/path");
        assertTrue(url.equals(otherUrl));
        
        otherUrl = new UrlImpl("/test/other");
        assertFalse(url.equals(otherUrl));
    }

    @Test
    public void testHashCode() {
        assertEquals(url.hashCode(), new UrlImpl("/test/path").hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("/test/path", url.toString());
    }
}
