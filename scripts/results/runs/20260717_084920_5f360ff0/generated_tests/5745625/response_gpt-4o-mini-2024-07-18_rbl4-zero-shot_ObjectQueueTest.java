
package com.squareup.tape2;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static org.junit.Assert.*;

public class ObjectQueueTest {

    private ObjectQueue<String> queue;

    private static class TestConverter implements ObjectQueue.Converter<String> {
        @Override
        public String from(byte[] source) {
            return new String(source);
        }

        @Override
        public void toStream(String value, OutputStream sink) throws IOException {
            sink.write(value.getBytes());
        }
    }

    @Before
    public void setUp() {
        queue = ObjectQueue.createInMemory();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testAddAndSize() throws IOException {
        queue.add("test1");
        queue.add("test2");
        assertEquals(2, queue.size());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testPeek() throws IOException {
        queue.add("test1");
        queue.add("test2");
        assertEquals("test1", queue.peek());
    }

    @Test
    public void testPeekWithMax() throws IOException {
        queue.add("test1");
        queue.add("test2");
        List<String> peeked = queue.peek(1);
        assertEquals(1, peeked.size());
        assertEquals("test1", peeked.get(0));
    }

    @Test
    public void testAsList() throws IOException {
        queue.add("test1");
        queue.add("test2");
        List<String> list = queue.asList();
        assertEquals(2, list.size());
        assertEquals("test1", list.get(0));
        assertEquals("test2", list.get(1));
    }

    @Test
    public void testRemove() throws IOException {
        queue.add("test1");
        queue.add("test2");
        queue.remove();
        assertEquals(1, queue.size());
        assertEquals("test2", queue.peek());
    }

    @Test
    public void testClear() throws IOException {
        queue.add("test1");
        queue.add("test2");
        queue.clear();
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IOException.class)
    public void testRemoveMoreThanSize() throws IOException {
        queue.add("test1");
        queue.remove(2); // Should throw IOException
    }
}
