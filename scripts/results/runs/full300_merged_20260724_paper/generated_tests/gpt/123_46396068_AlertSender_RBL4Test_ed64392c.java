
package com.kakao.hbase.common.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

public class AlertSender_RBL4Test_ed64392c {
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private PrintStream originalOut;

    @Before
    public void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void testSendIncrementsCounter() {
        int initialCount = AlertSender.getSendCount();
        AlertSender.send("echo", "Test alert message");
        int newCount = AlertSender.getSendCount();
        assertEquals(initialCount + 1, newCount);
    }

    @Test
    public void testSendOutputOnError() {
        AlertSender.send("nonexistent_command", "Test alert message");
        String output = outputStreamCaptor.toString().trim();
        assert(output.contains("Exit Code:"));
    }

    @Test
    public void testGetSendCount() {
        AlertSender.send("echo", "Test alert message");
        AlertSender.send("echo", "Another test alert message");
        assertEquals(2, AlertSender.getSendCount());
    }
}
