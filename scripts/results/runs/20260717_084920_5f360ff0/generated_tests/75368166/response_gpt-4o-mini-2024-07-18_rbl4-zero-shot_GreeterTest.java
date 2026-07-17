
package demo.org.powermock.examples.simple;

import org.junit.Test;
import static org.junit.Assert.*;

public class GreeterTest {

    @Test
    public void testGetMessage() {
        String expectedMessage = SimpleConfig.getGreeting() + " " + SimpleConfig.getTarget();
        String actualMessage = Greeter.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testRunLogsMessage() {
        Logger logger = new Logger();
        Greeter greeter = new Greeter();
        
        // Assuming Logger has a method to capture logs
        logger.clearLogs(); // Clear previous logs
        greeter.run(3, "Hello World");
        
        String[] logs = logger.getLogs();
        assertEquals(3, logs.length);
        assertEquals("Hello World", logs[0]);
        assertEquals("Hello World", logs[1]);
        assertEquals("Hello World", logs[2]);
    }
}
