package ch.entwine.weblounge.common.impl.scheduler;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Date;

public class PeriodicJobTrigger_RBL4_4c73c73cTest {

    private PeriodicJobTrigger trigger;

    @Before
    public void setUp() {
        trigger = new PeriodicJobTrigger(1000); // 1 second period
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePeriod() {
        new PeriodicJobTrigger(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroPeriod() {
        new PeriodicJobTrigger(0);
    }

    @Test
    public void testGetPeriod() {
        assertEquals(1000, trigger.getPeriod());
    }

    @Test
    public void testSetPeriod() {
        trigger.setPeriod(2000);
        assertEquals(2000, trigger.getPeriod());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPeriodWithNegativeValue() {
        trigger.setPeriod(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPeriodWithZeroValue() {
        trigger.setPeriod(0);
    }

    @Test
    public void testGetNextExecutionAfterBeforeStartTime() {
        Date now = new Date();
        Date nextExecution = trigger.getNextExecutionAfter(new Date(now.getTime() - 10000)); // 10 seconds ago
        assertNotNull(nextExecution);
        assertTrue(nextExecution.getTime() >= trigger.getStartTime());
    }

    @Test
    public void testGetNextExecutionAfterAfterEndTime() {
        trigger.setRepeatCount(1);
        trigger.triggered(new Date());
        Date nextExecution = trigger.getNextExecutionAfter(new Date());
        assertNull(nextExecution);
    }

    @Test
    public void testTriggered() {
        trigger.triggered(new Date());
        assertEquals(1, trigger.getRepeatCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testTriggeredExceedsRepeatCount() {
        trigger.setRepeatCount(1);
        trigger.triggered(new Date());
        trigger.triggered(new Date()); // Should throw exception
    }

    @Test
    public void testReset() {
        trigger.triggered(new Date());
        trigger.reset();
        assertEquals(0, trigger.getRepeatCount());
        assertTrue(trigger.getStartsImmediately());
    }

    @Test
    public void testToString() {
        assertEquals("Periodic job trigger [period=1000 ms]", trigger.toString());
    }

    @Test
    public void testGetStartsImmediately() {
        assertFalse(trigger.getStartsImmediately());
        trigger.setStartImmediately(true);
        assertTrue(trigger.getStartsImmediately());
    }
}
