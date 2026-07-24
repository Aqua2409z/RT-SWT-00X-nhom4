package org.apache.calcite.avatica.metrics.dropwizard;

import com.codahale.metrics.Histogram;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardHistogram;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DropwizardHistogram_RBL4_0b144c66Test {

    private Histogram mockHistogram;
    private DropwizardHistogram dropwizardHistogram;

    @Before
    public void setUp() {
        mockHistogram = new Histogram(new com.codahale.metrics.ExponentiallyDecayingReservoir());
        dropwizardHistogram = new DropwizardHistogram(mockHistogram);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullHistogram() {
        new DropwizardHistogram(null);
    }

    @Test
    public void testUpdateWithInt() {
        dropwizardHistogram.update(5);
        assertEquals(1, mockHistogram.getCount());
        assertEquals(5, mockHistogram.getSnapshot().getMean(), 0.001);
    }

    @Test
    public void testUpdateWithLong() {
        dropwizardHistogram.update(10L);
        assertEquals(1, mockHistogram.getCount());
        assertEquals(10, mockHistogram.getSnapshot().getMean(), 0.001);
    }

    @Test
    public void testMultipleUpdates() {
        dropwizardHistogram.update(5);
        dropwizardHistogram.update(10);
        dropwizardHistogram.update(15);
        assertEquals(3, mockHistogram.getCount());
        assertEquals(10, mockHistogram.getSnapshot().getMean(), 0.001);
    }
}
