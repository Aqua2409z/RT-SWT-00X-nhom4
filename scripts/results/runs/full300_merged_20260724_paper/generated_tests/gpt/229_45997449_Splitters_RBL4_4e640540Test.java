
package com.ebayopensource.webrex.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class Splitters_RBL4_4e640540Test {

    @Test
    public void testSplitByChar() {
        String input = "a,b,c";
        String[] expected = {"a", "b", "c"};
        String[] result = Splitters.split(input, ',');
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSplitByCharWithEmptyString() {
        String input = "";
        String[] expected = {};
        String[] result = Splitters.split(input, ',');
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSplitByCharWithNull() {
        String input = null;
        String[] expected = {};
        String[] result = Splitters.split(input, ',');
        assertArrayEquals(expected, result);
    }

    @Test
    public void testSplitByString() {
        Splitters.StringSplitter splitter = Splitters.by(", ");
        List<String> result = splitter.split("a, b, c");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitByStringWithTrimming() {
        Splitters.StringSplitter splitter = Splitters.by(", ").trim();
        List<String> result = splitter.split(" a,  b, c ");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitByStringWithNoEmptyItems() {
        Splitters.StringSplitter splitter = Splitters.by(", ").noEmptyItem();
        List<String> result = splitter.split("a, , b, c, ");
        List<String> expected = Arrays.asList("a", "b", "c");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitByStringWithEmptyString() {
        Splitters.StringSplitter splitter = Splitters.by(", ");
        List<String> result = splitter.split("");
        List<String> expected = Arrays.asList("");
        assertEquals(expected, result);
    }

    @Test
    public void testSplitByStringWithNull() {
        Splitters.StringSplitter splitter = Splitters.by(", ");
        List<String> result = splitter.split(null);
        assertNull(result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSplitWithUnsupportedOperation() {
        Splitters.StringSplitter splitter = Splitters.by((char) 0);
        splitter.split("test");
    }
}
