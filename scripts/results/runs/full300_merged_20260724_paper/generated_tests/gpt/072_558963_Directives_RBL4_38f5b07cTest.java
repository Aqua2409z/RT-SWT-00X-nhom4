package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.Directive;
import org.codehaus.httpcache4j.Directives;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class Directives_RBL4_38f5b07cTest {
    private Directives directives;

    @Before
    public void setUp() {
        Directive directive1 = new Directive("key1", "value1");
        Directive directive2 = new Directive("key2", "value2");
        directives = new Directives(Arrays.asList(directive1, directive2));
    }

    @Test
    public void testHasDirective() {
        assertTrue(directives.hasDirective("key1"));
        assertFalse(directives.hasDirective("key3"));
    }

    @Test
    public void testGet() {
        assertEquals("value1", directives.get("key1"));
        assertEquals("", directives.get("key3"));
    }

    @Test
    public void testGetAsDirective() {
        assertNotNull(directives.getAsDirective("key1"));
        assertNull(directives.getAsDirective("key3"));
    }

    @Test
    public void testSize() {
        assertEquals(2, directives.size());
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (Directive directive : directives) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testToString() {
        assertEquals("Directive{name='key1', value='value1'}, Directive{name='key2', value='value2'}", directives.toString());
    }

    @Test
    public void testAdd() {
        Directive directive3 = new Directive("key3", "value3");
        Directives newDirectives = directives.add(directive3);
        assertEquals(3, newDirectives.size());
        assertEquals("value3", newDirectives.get("key3"));
    }
}
