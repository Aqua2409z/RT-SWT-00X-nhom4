
package ds.heap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayHeap_RBL4_cc0b81f1Test {
    private ArrayHeap<Integer> heap;

    @Before
    public void setUp() {
        heap = new ArrayHeap<>(10);
    }

    @Test
    public void testEnqueueAndPeekMinimum() {
        heap.enqueue(5);
        heap.enqueue(3);
        heap.enqueue(8);
        assertEquals(Integer.valueOf(3), heap.peekMinimum());
    }

    @Test
    public void testDequeueMinimum() {
        heap.enqueue(5);
        heap.enqueue(3);
        heap.enqueue(8);
        assertEquals(Integer.valueOf(3), heap.dequeueMinimum());
        assertEquals(Integer.valueOf(5), heap.peekMinimum());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueFromEmptyHeap() {
        heap.dequeueMinimum();
    }

    @Test
    public void testSize() {
        assertEquals(0, heap.size());
        heap.enqueue(5);
        assertEquals(1, heap.size());
        heap.enqueue(3);
        assertEquals(2, heap.size());
        heap.dequeueMinimum();
        assertEquals(1, heap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnqueueToFullHeap() {
        ArrayHeap<Integer> fullHeap = new ArrayHeap<>(2);
        fullHeap.enqueue(1);
        fullHeap.enqueue(2);
        fullHeap.enqueue(3); // This should throw an exception
    }

    @Test
    public void testMultipleEnqueueAndDequeue() {
        heap.enqueue(10);
        heap.enqueue(20);
        heap.enqueue(5);
        heap.enqueue(15);
        heap.enqueue(30);
        
        assertEquals(Integer.valueOf(5), heap.dequeueMinimum());
        assertEquals(Integer.valueOf(10), heap.dequeueMinimum());
        assertEquals(Integer.valueOf(15), heap.dequeueMinimum());
        assertEquals(Integer.valueOf(20), heap.dequeueMinimum());
        assertEquals(Integer.valueOf(30), heap.dequeueMinimum());
    }

    @Test
    public void testPeekMinimumOnSingleElement() {
        heap.enqueue(42);
        assertEquals(Integer.valueOf(42), heap.peekMinimum());
        heap.dequeueMinimum();
        assertEquals(0, heap.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testPeekMinimumOnEmptyHeap() {
        heap.peekMinimum();
    }
}
