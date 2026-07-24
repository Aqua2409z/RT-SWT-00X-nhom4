package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.Header;
import org.codehaus.httpcache4j.Directives;
import org.codehaus.httpcache4j.util.AuthDirectivesParser;
import org.codehaus.httpcache4j.util.DirectivesParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class Header_RBL4Test_81a76271 {

    @Test(expected = IllegalArgumentException.class)
    public void testHeaderConstructorWithEmptyName() {
        new Header("", "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHeaderConstructorWithNullName() {
        new Header(null, "value");
    }

    @Test
    public void testHeaderConstructorWithValidNameAndValue() {
        Header header = new Header("Content-Type", "application/json");
        assertEquals("Content-Type: application/json", header.toString());
    }

    @Test
    public void testHeaderConstructorWithDirectives() {
        Directives directives = new Directives("max-age=3600");
        Header header = new Header("Cache-Control", directives);
        assertEquals("Cache-Control: max-age=3600", header.toString());
        assertEquals(directives, header.getDirectives());
    }

    @Test
    public void testGetDirectivesWithAuthenticationHeader() {
        Header header = new Header("Authorization", "Bearer token");
        Directives directives = header.getDirectives();
        assertNotNull(directives);
        assertTrue(directives instanceof AuthDirectivesParser);
    }

    @Test
    public void testGetDirectivesWithNonAuthenticationHeader() {
        Header header = new Header("Cache-Control", "no-cache");
        Directives directives = header.getDirectives();
        assertNotNull(directives);
        assertTrue(directives instanceof DirectivesParser);
    }

    @Test
    public void testValueOfWithValidHeaderString() {
        Header header = Header.valueOf("Content-Type: application/json");
        assertEquals("Content-Type", header.getName());
        assertEquals("application/json", header.getValue());
    }

    @Test
    public void testValueOfWithHeaderWithoutValue() {
        Header header = Header.valueOf("Content-Type:");
        assertEquals("Content-Type", header.getName());
        assertEquals("", header.getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfWithInvalidHeaderString() {
        Header.valueOf("InvalidHeaderString");
    }
}
