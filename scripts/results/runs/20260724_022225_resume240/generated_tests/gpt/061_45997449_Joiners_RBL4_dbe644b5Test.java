
package com.ebayopensource.webrex.util;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.List;

public class Joiners_RBL4_dbe644b5Test {

    @Test
    public void testJoinWithCharDelimiter() {
        Joiners.StringJoiner joiner = Joiners.by(',');
        String result = joiner.join("apple", "banana", "cherry");
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinWithStringDelimiter() {
        Joiners.StringJoiner joiner = Joiners.by(" - ");
        String result = joiner.join("apple", "banana", "cherry");
        assertEquals("apple - banana - cherry", result);
    }

    @Test
    public void testJoinWithNullArray() {
        Joiners.StringJoiner joiner = Joiners.by(',');
        String result = joiner.join((String[]) null);
        assertNull(result);
    }

    @Test
    public void testJoinWithEmptyArray() {
        Joiners.StringJoiner joiner = Joiners.by(',');
        String result = joiner.join(new String[0]);
        assertEquals("", result);
    }

    @Test
    public void testJoinWithList() {
        Joiners.StringJoiner joiner = Joiners.by(';');
        List<String> list = Arrays.asList("apple", "banana", "cherry");
        String result = joiner.join(list);
        assertEquals("apple;banana;cherry", result);
    }

    @Test
    public void testJoinWithNullList() {
        Joiners.StringJoiner joiner = Joiners.by(',');
        String result = joiner.join((List<String>) null);
        assertNull(result);
    }

    @Test
    public void testJoinWithNoEmptyItem() {
        Joiners.StringJoiner joiner = Joiners.by(',').noEmptyItem();
        String result = joiner.join("apple", "", "banana", null, "cherry");
        assertEquals("apple,banana,cherry", result);
    }

    @Test
    public void testJoinWithPrefixDelimiter() {
        Joiners.StringJoiner joiner = Joiners.by(',').prefixDelimiter();
        String result = joiner.join("apple", "banana", "cherry");
        assertEquals(",apple,banana,cherry", result);
    }

    @Test
    public void testJoinWithCustomBuilder() {
        Joiners.StringJoiner joiner = Joiners.by(',');
        Joiners.IBuilder<Integer> builder = new Joiners.IBuilder<Integer>() {
            @Override
            public String asString(Integer item) {
                return "Number: " + item;
            }
        };
        List<Integer> list = Arrays.asList(1, 2, 3);
        String result = joiner.join(list, builder);
        assertEquals("Number: 1,Number: 2,Number: 3", result);
    }
}
