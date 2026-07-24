
package turin.context;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.*;

public class Context_RBL4Test_34266f79 {

    private Context<String> context;

    @Before
    public void setUp() {
        context = new Context<String>() {};
    }

    @Test
    public void testGetEmptyContext() {
        Optional<String> value = context.get();
        assertFalse(value.isPresent());
    }

    @Test
    public void testEnterContext() {
        context.enterContext("TestValue");
        Optional<String> value = context.get();
        assertTrue(value.isPresent());
        assertEquals("TestValue", value.get());
    }

    @Test
    public void testExitContext() {
        context.enterContext("TestValue1");
        context.enterContext("TestValue2");
        context.exitContext();
        Optional<String> value = context.get();
        assertTrue(value.isPresent());
        assertEquals("TestValue1", value.get());
    }

    @Test
    public void testExitContextEmpty() {
        context.enterContext("TestValue");
        context.exitContext();
        context.exitContext(); // Exiting again should not throw an exception
        Optional<String> value = context.get();
        assertFalse(value.isPresent());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(context.isEmpty());
        context.enterContext("TestValue");
        assertFalse(context.isEmpty());
        context.exitContext();
        assertTrue(context.isEmpty());
    }

    @Test
    public void testMultipleContexts() {
        context.enterContext("Value1");
        context.enterContext("Value2");
        context.enterContext("Value3");
        
        assertEquals("Value3", context.get().get());
        
        context.exitContext();
        assertEquals("Value2", context.get().get());
        
        context.exitContext();
        assertEquals("Value1", context.get().get());
        
        context.exitContext();
        assertFalse(context.get().isPresent());
    }
}
