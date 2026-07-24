
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
    public void testGetWhenEmpty() {
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
        context.enterContext("TestValue");
        context.exitContext();
        Optional<String> value = context.get();
        assertFalse(value.isPresent());
    }

    @Test
    public void testMultipleContexts() {
        context.enterContext("FirstValue");
        context.enterContext("SecondValue");
        Optional<String> value = context.get();
        assertTrue(value.isPresent());
        assertEquals("SecondValue", value.get());

        context.exitContext();
        value = context.get();
        assertTrue(value.isPresent());
        assertEquals("FirstValue", value.get());

        context.exitContext();
        value = context.get();
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
}
