
package ds.stack;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayStack_RBL4_b8276d51Test {
    private ArrayStack<Integer> stack;

    @Before
    public void setUp() {
        stack = new ArrayStack<>(5);
    }

    @Test
    public void testPushAndPop() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        
        assertEquals(Integer.valueOf(3), stack.pop());
        assertEquals(Integer.valueOf(2), stack.pop());
        assertEquals(Integer.valueOf(1), stack.pop());
    }

    @Test(expected = IllegalStateException.class)
    public void testPopFromEmptyStack() {
        stack.pop();
    }

    @Test
    public void testIsEmpty() {
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testPushOverflow() {
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        stack.push(5);
        stack.push(6); // This should throw an exception
    }
}
