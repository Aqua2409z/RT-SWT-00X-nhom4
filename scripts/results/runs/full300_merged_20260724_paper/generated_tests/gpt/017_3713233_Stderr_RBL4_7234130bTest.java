
package org.junithelper.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class Stderr_RBL4_7234130bTest {

    @Test(expected = IllegalArgumentException.class)
    public void testPrintfWithEmptyFormat() {
        Stderr.printf("", "value");
    }

    @Test
    public void testPrintfWithValidFormat() {
        // Redirecting System.err to capture output for testing
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        Stderr.printf("Hello %s", "World");
        assertEquals("Hello World", outContent.toString().trim());
        
        // Reset System.err
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }

    @Test
    public void testP() {
        // Redirecting System.err to capture output for testing
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        Stderr.p("Test message");
        assertEquals("Test message", outContent.toString().trim());
        
        // Reset System.err
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.out)));
    }
}
