
package org.springframework.data.simpledb.attributeutil;

import org.junit.Test;
import org.junit.Assert;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleDBAttributeConverter_RBL4_3b9a0f18Test {

    @Test
    public void testEncodeInteger() {
        String encoded = SimpleDBAttributeConverter.encode(123);
        Assert.assertEquals("123", encoded);
    }

    @Test
    public void testEncodeRealNumber() {
        String encoded = SimpleDBAttributeConverter.encode(123.45);
        Assert.assertEquals("123.45", encoded);
    }

    @Test
    public void testEncodeDate() {
        Date date = new Date();
        String encoded = SimpleDBAttributeConverter.encode(date);
        // Assuming AmazonSimpleDBUtil.encodeDate(date) returns a specific format
        Assert.assertEquals(AmazonSimpleDBUtil.encodeDate(date), encoded);
    }

    @Test
    public void testEncodeString() {
        String encoded = SimpleDBAttributeConverter.encode("test");
        Assert.assertEquals("test", encoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeArrayNull() {
        SimpleDBAttributeConverter.encodeArray(null);
    }

    @Test
    public void testEncodeArray() {
        Object array = new Integer[]{1, 2, 3};
        List<String> encodedList = SimpleDBAttributeConverter.encodeArray(array);
        Assert.assertEquals(3, encodedList.size());
        Assert.assertEquals("1", encodedList.get(0));
        Assert.assertEquals("2", encodedList.get(1));
        Assert.assertEquals("3", encodedList.get(2));
    }

    @Test
    public void testDecodeToFieldOfTypeInteger() throws ParseException {
        Object decoded = SimpleDBAttributeConverter.decodeToFieldOfType("123", Integer.class);
        Assert.assertEquals(123, decoded);
    }

    @Test
    public void testDecodeToFieldOfTypeBigDecimal() throws ParseException {
        Object decoded = SimpleDBAttributeConverter.decodeToFieldOfType("123.45", BigDecimal.class);
        Assert.assertEquals(new BigDecimal("123.45"), decoded);
    }

    @Test
    public void testDecodeToFieldOfTypeDate() throws ParseException {
        String dateString = "2023-01-01"; // Assuming this is the format used
        Object decoded = SimpleDBAttributeConverter.decodeToFieldOfType(dateString, Date.class);
        Assert.assertEquals(AmazonSimpleDBUtil.decodeDate(dateString), decoded);
    }

    @Test
    public void testDecodeToFieldOfTypeBoolean() throws ParseException {
        Object decodedTrue = SimpleDBAttributeConverter.decodeToFieldOfType("true", Boolean.class);
        Assert.assertTrue((Boolean) decodedTrue);
        
        Object decodedFalse = SimpleDBAttributeConverter.decodeToFieldOfType("false", Boolean.class);
        Assert.assertFalse((Boolean) decodedFalse);
    }

    @Test
    public void testDecodeToPrimitiveArray() throws ParseException {
        List<String> values = new ArrayList<>();
        values.add("1");
        values.add("2");
        values.add("3");
        
        Object decodedArray = SimpleDBAttributeConverter.decodeToPrimitiveArray(values, int.class);
        Assert.assertEquals(3, Array.getLength(decodedArray));
        Assert.assertEquals(1, Array.get(decodedArray, 0));
        Assert.assertEquals(2, Array.get(decodedArray, 1));
        Assert.assertEquals(3, Array.get(decodedArray, 2));
    }
}
