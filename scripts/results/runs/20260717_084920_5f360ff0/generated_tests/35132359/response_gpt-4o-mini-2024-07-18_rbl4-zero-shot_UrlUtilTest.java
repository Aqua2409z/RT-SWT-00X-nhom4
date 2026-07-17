
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
        assertEquals("relativeEndpoint", UrlUtil.constructAbsoluteUrl("http://example.com", "relativeEndpoint"));
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
        assertEquals("http://example.com/parent/child", UrlUtil.constructRelativeUrl("http://example.com/parent/", "http://example.com/parent/child"));
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
        assertFalse(UrlUtil.isValid("http://"));
        assertFalse(UrlUtil.isValid("http://a"));
    }

    @Test
    public void testUnescape() {
        assertEquals("hello world", UrlUtil.unescape("hello%20world"));
        assertEquals("hello world", UrlUtil.unescape("hello world"));
        assertNull(UrlUtil.unescape(null));
        assertEquals("hello%20world", UrlUtil.unescape("hello%20world"));
    }

    @Test(expected = Error.class)
    public void testUnescapeUnsupportedEncoding() {
        // This test is to ensure that the Error is thrown if UTF-8 is not supported
        // However, since UTF-8 is always supported in Java, this is more of a theoretical test.
        UrlUtil.unescape("%E0%A4%A");
    }
}
