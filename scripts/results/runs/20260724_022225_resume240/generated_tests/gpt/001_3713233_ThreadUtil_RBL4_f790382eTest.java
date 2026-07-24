
package org.junithelper.core.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ThreadUtil_RBL4_f790382eTest {

    @Test
    public void testSleep() {
        long startTime = System.currentTimeMillis();
        ThreadUtil.sleep(100); // Sleep for 100 milliseconds
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Check that the elapsed time is at least 100 milliseconds
        assertTrue("Sleep duration should be at least 100 milliseconds", elapsedTime >= 100);
        
        // Check that the elapsed time is less than 200 milliseconds
        assertTrue("Sleep duration should be less than 200 milliseconds", elapsedTime < 200);
    }

    @Test
    public void testSleepInterrupted() {
        Thread currentThread = Thread.currentThread();
        currentThread.interrupt(); // Interrupt the current thread

        long startTime = System.currentTimeMillis();
        ThreadUtil.sleep(100); // Attempt to sleep for 100 milliseconds
        long elapsedTime = System.currentTimeMillis() - startTime;

        // Check that the sleep method does not block indefinitely
        assertTrue("Sleep should return quickly when interrupted", elapsedTime < 200);
        assertTrue("Thread should be interrupted", currentThread.isInterrupted());
    }
}
