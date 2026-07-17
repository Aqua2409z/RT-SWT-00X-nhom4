
package ca.uhn.fhir.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class UrlUtilTest {

    @Test
    public void testConstructAbsoluteUrl() {
        assertEquals("http://example.com/endpoint", UrlUtil.constructAbsoluteUrl("http://example.com", "endpoint"));
        assertEquals("http://example.com/endpoint", UrlUtil.constructAbsoluteUrl("http://example.com/", "endpoint"));
        assertEquals("http://example.com/endpoint", UrlUtil.constructAbsoluteUrl("http://example.com/", "/endpoint"));
        assertEquals("http://example.com/endpoint", UrlUtil.constructAbsoluteUrl("http://example.com", "/endpoint"));
        assertEquals("http://example.com", UrlUtil.constructAbsoluteUrl(null, "http://example.com"));
        assertNull(UrlUtil.constructAbsoluteUrl(null, null));
        assertEquals("invalid_endpoint", UrlUtil.constructAbsoluteUrl("http://example.com", "invalid_endpoint"));
    }

    @Test
    public void testIsAbsolute() {
        assertTrue(UrlUtil.isAbsolute("http://example.com"));
        assertTrue(UrlUtil.isAbsolute("https://example.com"));
        assertFalse(UrlUtil.isAbsolute("ftp://example.com"));
        assertFalse(UrlUtil.isAbsolute("example.com"));
        assertFalse(UrlUtil.isAbsolute(null));
    }

    @Test
    public void testConstructRelativeUrl() {
        assertEquals("child", UrlUtil.constructRelativeUrl("http://example.com/parent", "http://example.com/parent/child"));
        assertEquals("child", UrlUtil.constructRelativeUrl("http://example.com/parent/", "http://example.com/parent/child"));
        assertEquals("child", UrlUtil.constructRelativeUrl("http://example.com/parent/", "/child"));
        assertEquals("http://example.com/parent/child", UrlUtil.constructRelativeUrl("http://example.com/parent", "http://example.com/parent/child"));
        assertEquals("http://example.com/parent/child", UrlUtil.constructRelativeUrl(null, "http://example.com/parent/child"));
        assertNull(UrlUtil.constructRelativeUrl(null, null));
    }

    @Test
    public void testIsValid() {
        assertTrue(UrlUtil.isValid("http://example.com"));
        assertTrue(UrlUtil.isValid("https://example.com"));
        assertFalse(UrlUtil.isValid("ftp://example.com"));
        assertFalse(UrlUtil.isValid("example.com"));
        assertFalse(UrlUtil.isValid("http:/example.com"));
        assertFalse(UrlUtil.isValid(null));
        assertFalse(UrlUtil.isValid("http"));
        assertFalse(UrlUtil.isValid("htp://example.com"));
    }

    @Test
    public void testUnescape() {
        assertEquals("hello world", UrlUtil.unescape("hello%20world"));
        assertEquals("http://example.com", UrlUtil.unescape("http%3A%2F%2Fexample.com"));
        assertNull(UrlUtil.unescape(null));
        assertEquals("no%20encoding", UrlUtil.unescape("no%20encoding"));
    }

    @Test(expected = Error.class)
    public void testUnescapeUnsupportedEncoding() {
        // This should not happen as UTF-8 is always supported
        UrlUtil.unescape("%E0%A4%A");
    }
}
