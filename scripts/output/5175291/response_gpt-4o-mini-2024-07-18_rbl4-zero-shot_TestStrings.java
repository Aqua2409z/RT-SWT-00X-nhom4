
package io.airlift.airship.shared;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class TestStrings {

    @Test
    public void testCommonPrefixSegments() {
        assertEquals(2, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/b/e")));
        assertEquals(1, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/x/e")));
        assertEquals(0, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/x/b/d", "/y/x/e")));
        assertEquals(0, Strings.commonPrefixSegments('/', Collections.emptyList()));
    }

    @Test
    public void testCommonPrefixSegmentsWithMinSize() {
        assertEquals(1, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/b/e"), 1));
        assertEquals(0, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/x/e"), 2));
        assertThrows(IllegalArgumentException.class, () -> Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/x/b/d", "/y/x/e"), 1));
    }

    @Test
    public void testTrimLeadingSegments() {
        assertEquals("/c/d", Strings.trimLeadingSegments("/a/b/c/d", '/', 2));
        assertEquals("/b/c/d", Strings.trimLeadingSegments("/a/b/c/d", '/', 1));
        assertEquals("/a/b/c/d", Strings.trimLeadingSegments("/a/b/c/d", '/', 0));
        assertThrows(IllegalArgumentException.class, () -> Strings.trimLeadingSegments("a/b/c/d", '/', 1));
        assertThrows(IllegalArgumentException.class, () -> Strings.trimLeadingSegments("/a/b/c/d", '/', 5));
    }

    @Test
    public void testShortestUniquePrefix() {
        assertEquals(1, Strings.shortestUniquePrefix(Arrays.asList("apple", "apricot", "banana")));
        assertEquals(2, Strings.shortestUniquePrefix(Arrays.asList("apple", "apricot", "banana", "ap")));
        assertThrows(IllegalArgumentException.class, () -> Strings.shortestUniquePrefix(Arrays.asList("apple", "apple", "banana")));
        assertEquals(1, Strings.shortestUniquePrefix(Collections.singletonList("apple")));
    }

    @Test
    public void testSafeTruncate() {
        assertEquals("abc", Strings.safeTruncate("abcdef", 3));
        assertEquals("abcdef", Strings.safeTruncate("abcdef", 10));
        assertEquals(null, Strings.safeTruncate(null, 3));
        assertEquals("", Strings.safeTruncate("", 3));
    }
}
