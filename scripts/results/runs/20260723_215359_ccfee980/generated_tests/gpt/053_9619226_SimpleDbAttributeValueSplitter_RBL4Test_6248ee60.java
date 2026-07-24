
package org.springframework.data.simpledb.attributeutil;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;

public class SimpleDbAttributeValueSplitter_RBL4Test_6248ee60 {

    private SimpleDbAttributeValueSplitter splitter;

    @Before
    public void setUp() {
        splitter = new SimpleDbAttributeValueSplitter();
    }

    @Test
    public void testSplitAttributeValuesWithExceedingLengths() {
        Map<String, String> rawAttributes = new HashMap<>();
        String longValue = "a".repeat(1025); // 1025 characters
        rawAttributes.put("key1", longValue);
        rawAttributes.put("key2", "shortValue");

        Map<String, List<String>> result = SimpleDbAttributeValueSplitter.splitAttributeValuesWithExceedingLengths(rawAttributes);

        Assert.assertEquals(2, result.size());
        Assert.assertTrue(result.get("key1").size() > 1);
        Assert.assertEquals("shortValue", result.get("key2").get(0));
    }

    @Test
    public void testCombineAttributeValuesWithExceedingLengths() {
        Map<String, List<String>> multiValueAttributes = new HashMap<>();
        multiValueAttributes.put("key1", Arrays.asList("0@value1", "1@value2"));
        multiValueAttributes.put("key2", Collections.singletonList("0@shortValue"));

        Map<String, String> result = SimpleDbAttributeValueSplitter.combineAttributeValuesWithExceedingLengths(multiValueAttributes);

        Assert.assertEquals("value1value2", result.get("key1"));
        Assert.assertEquals("shortValue", result.get("key2"));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void testCombineAttributeValuesWithInvalidPattern() {
        Map<String, List<String>> multiValueAttributes = new HashMap<>();
        multiValueAttributes.put("key1", Collections.singletonList("invalidPattern"));

        SimpleDbAttributeValueSplitter.combineAttributeValuesWithExceedingLengths(multiValueAttributes);
    }

    @Test
    public void testSplitExceedingValue() {
        String longValue = "a".repeat(2048); // 2048 characters
        List<String> result = SimpleDbAttributeValueSplitter.splitExceedingValue(longValue);

        Assert.assertEquals(3, result.size());
        Assert.assertTrue(result.get(0).startsWith("0@"));
        Assert.assertTrue(result.get(1).startsWith("1@"));
        Assert.assertTrue(result.get(2).startsWith("2@"));
    }

    @Test
    public void testSplitAttributeValuesWithNoExceedingLengths() {
        Map<String, String> rawAttributes = new HashMap<>();
        rawAttributes.put("key1", "shortValue");

        Map<String, List<String>> result = SimpleDbAttributeValueSplitter.splitAttributeValuesWithExceedingLengths(rawAttributes);

        Assert.assertEquals(1, result.size());
        Assert.assertEquals("shortValue", result.get("key1").get(0));
    }
}
