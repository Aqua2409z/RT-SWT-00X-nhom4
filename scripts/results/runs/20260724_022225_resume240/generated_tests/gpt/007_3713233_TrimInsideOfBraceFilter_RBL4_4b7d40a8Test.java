package org.junithelper.core.filter.impl;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test {

    private final TrimInsideOfBraceFilter filter = new TrimInsideOfBraceFilter();

    @Test
    public void testTrimAll_NullInput() {
        assertEquals(null, filter.trimAll(null));
    }

    @Test
    public void testTrimAll_EmptyString() {
        assertEquals("", filter.trimAll(""));
    }

    @Test
    public void testTrimAll_SingleLine() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { int a; }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { int a; }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_MultipleSpaces() {
        String input = "class    Test   {   int a;   }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { int a; }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_InsideString() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { String s = \"   inside   string   \"; }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { String s = \"   inside   string   \"; }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_InsideChar() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { char c = '   inside   char   '; }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { char c = '   inside   char   '; }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_IgnoreBraces() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { if (true) { System.out.println(\"Hello\"); } }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { if (true) { System.out.println(\"Hello\"); } }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_NestedBraces() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { void method() { if (true) { System.out.println(\"Nested\"); } } }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { void method() { if (true) { System.out.println(\"Nested\"); } } }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_EscapedQuotes() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { String s = \"This is a \\\\\"quoted\\\\\" string\"; }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { String s = \"This is a \\\\\"quoted\\\\\" string\"; }";
        assertEquals(expected, filter.trimAll(input));
    }

    @Test
    public void testTrimAll_ComplexInput() {
        String input = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { String s = \"   text with \\\"quotes\\\"   \"; int[] arr = {1, 2, 3}; }";
        String expected = "class TrimInsideOfBraceFilter_RBL4_4b7d40a8Test { String s = \"   text with \\\"quotes\\\"   \"; int[] arr = {1, 2, 3}; }";
        assertEquals(expected, filter.trimAll(input));
    }
}
