package io.datakernel.common;

import io.datakernel.common.Utils;
import io.datakernel.common.parse.ParseException;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void testOf() {
        assertEquals("Hello", Utils.of(() -> "Hello"));
    }

    @Test
    public void testFirstNonNull() {
        assertEquals("A", Utils.firstNonNull(null, "A"));
        assertEquals("B", Utils.firstNonNull("B", "A"));
        assertEquals("C", Utils.firstNonNull(null, null, "C"));
    }

    @Test
    public void testFirstNonNullVarargs() {
        assertEquals("A", Utils.firstNonNull(null, null, "A", "B"));
        assertEquals("B", Utils.firstNonNull(null, "B", "A"));
        assertNull(Utils.firstNonNull(null, null, null));
    }

    @Test
    public void testNullToEmptyString() {
        assertEquals("", Utils.nullToEmpty(null));
        assertEquals("Hello", Utils.nullToEmpty("Hello"));
    }

    @Test
    public void testNullToEmptySet() {
        Set<String> nullSet = null;
        Set<String> emptySet = Utils.nullToEmpty(nullSet);
        assertTrue(emptySet.isEmpty());
    }

    @Test
    public void testNullToEmptyList() {
        List<String> nullList = null;
        List<String> emptyList = Utils.nullToEmpty(nullList);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    public void testNullToEmptyMap() {
        Map<String, String> nullMap = null;
        Map<String, String> emptyMap = Utils.nullToEmpty(nullMap);
        assertTrue(emptyMap.isEmpty());
    }

    @Test
    public void testNullToDefault() {
        assertEquals("Default", Utils.nullToDefault(null, "Default"));
        assertEquals("Value", Utils.nullToDefault("Value", "Default"));
    }

    @Test
    public void testNullToSupplier() {
        assertEquals("Default", Utils.nullToSupplier(null, () -> "Default"));
        assertEquals("Value", Utils.nullToSupplier("Value", () -> "Default"));
    }

    @Test(expected = ParseException.class)
    public void testParseInetSocketAddressInvalidPort() throws ParseException {
        Utils.parseInetSocketAddress("localhost:70000");
    }

    @Test(expected = ParseException.class)
    public void testParseInetSocketAddressInvalidAddress() throws ParseException {
        Utils.parseInetSocketAddress("invalidAddress:8080");
    }

    @Test
    public void testParseInetSocketAddressValid() throws ParseException {
        InetSocketAddress address = Utils.parseInetSocketAddress("localhost:8080");
        assertEquals("localhost", address.getHostName());
        assertEquals(8080, address.getPort());
    }

    @Test
    public void testDeepHashCode() {
        assertEquals(0, Utils.deepHashCode(null));
        assertEquals(1, Utils.deepHashCode(1));
        assertEquals(Arrays.deepHashCode(new int[]{1, 2, 3}), Utils.deepHashCode(new int[]{1, 2, 3}));
    }

    @Test
    public void testArraysEquals() {
        byte[] array1 = {1, 2, 3};
        byte[] array2 = {1, 2, 3};
        assertTrue(Utils.arraysEquals(array1, 0, 3, array2, 0, 3));
        assertFalse(Utils.arraysEquals(array1, 0, 3, array2, 1, 2));
    }

    @Test
    public void testLoadResource() throws IOException {
        String resourceName = "test.txt"; // Ensure this resource exists in your classpath
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        byte[] result = Utils.loadResource(inputStream);
        assertArrayEquals("Test content".getBytes(), result);
    }

    @Test
    public void testNullifyWithRunnable() {
        String[] value = { "Hello" };
        Utils.nullify(value[0], () -> value[0] = null);
        assertNull(value[0]);
    }

    @Test
    public void testNullifyWithConsumer() {
        String[] value = { "Hello" };
        Utils.nullify(value[0], v -> value[0] = null);
        assertNull(value[0]);
    }

    @Test
    public void testNullifyWithBiConsumer() {
        String[] value = { "Hello" };
        Utils.nullify(value[0], (v, arg) -> value[0] = null, null);
        assertNull(value[0]);
    }
}
