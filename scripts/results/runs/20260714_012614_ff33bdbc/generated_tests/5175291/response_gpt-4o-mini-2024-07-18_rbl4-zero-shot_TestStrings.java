
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
        assertEquals(1, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/x/y")));
        assertEquals(0, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/x/y/z")));
        assertEquals(0, Strings.commonPrefixSegments('/', Collections.emptyList()));
    }

    @Test
    public void testCommonPrefixSegmentsWithMinSize() {
        assertEquals(1, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/x/y"), 1));
        assertEquals(0, Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/x/y/z"), 1));
        assertThrows(IllegalArgumentException.class, () -> Strings.commonPrefixSegments('/', Arrays.asList("/a/b/c", "/a/b/d", "/a/x/y"), 2));
    }

    @Test
    public void testTrimLeadingSegments() {
        assertEquals("/c/d", Strings.trimLeadingSegments("/a/b/c/d", '/', 2));
        assertEquals("/b/c/d", Strings.trimLeadingSegments("/a/b/c/d", '/', 1));
        assertEquals("/a/b/c/d", Strings.trimLeadingSegments("/a/b/c/d", '/', 5));
        assertThrows(IllegalArgumentException.class, () -> Strings.trimLeadingSegments("a/b/c/d", '/', 1));
        assertThrows(IllegalArgumentException.class, () -> Strings.trimLeadingSegments("/a/b/c/d", '/', 0));
    }

    @Test
    public void testShortestUniquePrefix() {
        assertEquals(1, Strings.shortestUniquePrefix(Arrays.asList("apple", "banana", "apricot")));
        assertEquals(2, Strings.shortestUniquePrefix(Arrays.asList("apple", "apricot", "banana")));
        assertThrows(IllegalArgumentException.class, () -> Strings.shortestUniquePrefix(Arrays.asList("apple", "banana", "apple")));
        assertEquals(1, Strings.shortestUniquePrefix(Arrays.asList("apple", "banana"), 1));
    }

    @Test
    public void testShortestUniquePrefixWithMinSize() {
        assertEquals(2, Strings.shortestUniquePrefix(Arrays.asList("apple", "banana", "apricot"), 2));
        assertEquals(1, Strings.shortestUniquePrefix(Arrays.asList("apple", "banana"), 1));
        assertThrows(IllegalArgumentException.class, () -> Strings.shortestUniquePrefix(Arrays.asList("apple", "banana", "apple"), 1));
    }

    @Test
    public void testSafeTruncate() {
        assertEquals("test", Strings.safeTruncate("test", 4));
        assertEquals("te", Strings.safeTruncate("test", 2));
        assertEquals(null, Strings.safeTruncate(null, 4));
        assertEquals("test", Strings.safeTruncate("test", 10));
    }
}
