package org.codehaus.httpcache4j.payload;

import org.codehaus.httpcache4j.MIMEType;
import org.codehaus.httpcache4j.payload.FormDataPayload;
import org.codehaus.httpcache4j.uri.QueryParam;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FormDataPayload_RBL4_fff81761Test {

    @Test
    public void testConstructorWithMap() {
        Map<String, List<String>> parameters = new HashMap<>();
        parameters.put("key1", Arrays.asList("value1"));
        parameters.put("key2", Arrays.asList("value2"));

        FormDataPayload payload = new FormDataPayload(parameters);
        assertEquals("key1=value1&key2=value2", payload.getValue());
        assertEquals(MIMEType.valueOf("application/x-www-form-urlencoded"), payload.getMimeType());
    }

    @Test
    public void testConstructorWithList() {
        List<QueryParam> parameters = Arrays.asList(new QueryParam("key1", "value1"), new QueryParam("key2", "value2"));

        FormDataPayload payload = new FormDataPayload(parameters);
        assertEquals("key1=value1&key2=value2", payload.getValue());
    }

    @Test
    public void testConstructorWithString() {
        String formatted = "key1=value1&key2=value2";
        FormDataPayload payload = new FormDataPayload(formatted);
        assertEquals(formatted, payload.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullString() {
        new FormDataPayload(null);
    }

    @Test
    public void testGetInputStream() {
        String formatted = "key1=value1&key2=value2";
        FormDataPayload payload = new FormDataPayload(formatted);
        InputStream inputStream = payload.getInputStream();
        String result = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        assertEquals(formatted, result);
    }

    @Test
    public void testIsAvailable() {
        FormDataPayload payload = new FormDataPayload("key1=value1");
        assertTrue(payload.isAvailable());
    }

    @Test
    public void testLength() {
        String formatted = "key1=value1&key2=value2";
        FormDataPayload payload = new FormDataPayload(formatted);
        assertEquals(formatted.length(), payload.length());
    }

    @Test
    public void testGetValuesDeprecated() {
        String formatted = "key1=value1&key2=value2";
        FormDataPayload payload = new FormDataPayload(formatted);
        assertEquals(formatted, payload.getValues());
    }
}
