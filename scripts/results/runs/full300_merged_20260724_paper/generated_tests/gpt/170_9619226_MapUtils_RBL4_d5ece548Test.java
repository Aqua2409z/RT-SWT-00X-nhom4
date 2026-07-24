
package org.springframework.data.simpledb.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class MapUtils_RBL4_d5ece548Test {

    @Test
    public void testSplitToChunksOfSize_EmptyMap() {
        Map<String, List<String>> rawMap = new HashMap<>();
        List<Map<String, List<String>>> result = MapUtils.splitToChunksOfSize(rawMap, 2);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSplitToChunksOfSize_SingleEntry() {
        Map<String, List<String>> rawMap = new HashMap<>();
        rawMap.put("key1", Arrays.asList("value1"));
        List<Map<String, List<String>>> result = MapUtils.splitToChunksOfSize(rawMap, 2);
        assertEquals(1, result.size());
        assertTrue(result.get(0).containsKey("key1"));
        assertEquals(Arrays.asList("value1"), result.get(0).get("key1"));
    }

    @Test
    public void testSplitToChunksOfSize_MultipleEntries_LessThanChunkSize() {
        Map<String, List<String>> rawMap = new HashMap<>();
        rawMap.put("key1", Arrays.asList("value1"));
        rawMap.put("key2", Arrays.asList("value2"));
        List<Map<String, List<String>>> result = MapUtils.splitToChunksOfSize(rawMap, 5);
        assertEquals(1, result.size());
        assertTrue(result.get(0).containsKey("key1"));
        assertTrue(result.get(0).containsKey("key2"));
    }

    @Test
    public void testSplitToChunksOfSize_MultipleEntries_EqualToChunkSize() {
        Map<String, List<String>> rawMap = new HashMap<>();
        rawMap.put("key1", Arrays.asList("value1"));
        rawMap.put("key2", Arrays.asList("value2"));
        List<Map<String, List<String>>> result = MapUtils.splitToChunksOfSize(rawMap, 2);
        assertEquals(1, result.size());
        assertTrue(result.get(0).containsKey("key1"));
        assertTrue(result.get(0).containsKey("key2"));
    }

    @Test
    public void testSplitToChunksOfSize_MultipleEntries_GreaterThanChunkSize() {
        Map<String, List<String>> rawMap = new HashMap<>();
        rawMap.put("key1", Arrays.asList("value1"));
        rawMap.put("key2", Arrays.asList("value2"));
        rawMap.put("key3", Arrays.asList("value3"));
        List<Map<String, List<String>>> result = MapUtils.splitToChunksOfSize(rawMap, 2);
        assertEquals(2, result.size());
        assertTrue(result.get(0).containsKey("key1"));
        assertTrue(result.get(0).containsKey("key2"));
        assertTrue(result.get(1).containsKey("key3"));
    }

    @Test
    public void testSplitToChunksOfSize_MultipleEntries_ChunkSizeOne() {
        Map<String, List<String>> rawMap = new HashMap<>();
        rawMap.put("key1", Arrays.asList("value1"));
        rawMap.put("key2", Arrays.asList("value2"));
        List<Map<String, List<String>>> result = MapUtils.splitToChunksOfSize(rawMap, 1);
        assertEquals(2, result.size());
        assertTrue(result.get(0).containsKey("key1"));
        assertTrue(result.get(1).containsKey("key2"));
    }
}
