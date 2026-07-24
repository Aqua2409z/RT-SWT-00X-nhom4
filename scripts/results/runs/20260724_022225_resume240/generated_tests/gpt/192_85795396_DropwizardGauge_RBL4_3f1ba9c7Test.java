package org.apache.calcite.avatica.metrics.dropwizard;

import org.apache.calcite.avatica.metrics.dropwizard.DropwizardGauge;
import org.apache.calcite.avatica.metrics.Gauge;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DropwizardGauge_RBL4_3f1ba9c7Test {

    private static class DropwizardGauge_RBL4_3f1ba9c7Test implements Gauge<Integer> {
        private final Integer value;

        public TestGauge(Integer value) {
            this.value = value;
        }

        @Override
        public Integer getValue() {
            return value;
        }
    }

    @Test
    public void testGetValue() {
        TestGauge testGauge = new TestGauge(42);
        DropwizardGauge<Integer> dropwizardGauge = new DropwizardGauge<>(testGauge);
        
        assertEquals(Integer.valueOf(42), dropwizardGauge.getValue());
    }

    @Test
    public void testGetValueWithNull() {
        TestGauge testGauge = new TestGauge(null);
        DropwizardGauge<Integer> dropwizardGauge = new DropwizardGauge<>(testGauge);
        
        assertEquals(null, dropwizardGauge.getValue());
    }

    @Test
    public void testGetValueWithNegative() {
        TestGauge testGauge = new TestGauge(-10);
        DropwizardGauge<Integer> dropwizardGauge = new DropwizardGauge<>(testGauge);
        
        assertEquals(Integer.valueOf(-10), dropwizardGauge.getValue());
    }

    @Test
    public void testGetValueWithZero() {
        TestGauge testGauge = new TestGauge(0);
        DropwizardGauge<Integer> dropwizardGauge = new DropwizardGauge<>(testGauge);
        
        assertEquals(Integer.valueOf(0), dropwizardGauge.getValue());
    }
}
