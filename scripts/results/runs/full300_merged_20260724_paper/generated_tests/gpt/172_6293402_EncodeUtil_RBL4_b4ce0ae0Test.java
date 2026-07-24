
package com.vaadin.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class EncodeUtil_RBL4_b4ce0ae0Test {

    @Test
    public void testRfc5987Encode_withAsciiCharacters() {
        String input = "Hello World.";
        String expected = "Hello%20World.";
        String actual = EncodeUtil.rfc5987Encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc5987Encode_withSpecialCharacters() {
        String input = "Hello, 世界!";
        String expected = "Hello%2C%20%E4%B8%96%E7%95%8C%21";
        String actual = EncodeUtil.rfc5987Encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc5987Encode_withOnlySpecialCharacters() {
        String input = "!@#$%^&*()";
        String expected = "%21%40%23%24%25%5E%26%2A%28%29";
        String actual = EncodeUtil.rfc5987Encode(input);
        assertEquals(expected, actual);
    }

    @Test
    public void testRfc5987Encode_withEmptyString() {
        String input = "";
        String expected = "";
        String actual = EncodeUtil.rfc5987Encode(input);
        assertEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testRfc5987Encode_withNull() {
        EncodeUtil.rfc5987Encode(null);
    }

    @Test
    public void testRfc5987Encode_withMixedCharacters() {
        String input = "Test 123! @#";
        String expected = "Test%20123%21%20%40%23";
        String actual = EncodeUtil.rfc5987Encode(input);
        assertEquals(expected, actual);
    }
}
