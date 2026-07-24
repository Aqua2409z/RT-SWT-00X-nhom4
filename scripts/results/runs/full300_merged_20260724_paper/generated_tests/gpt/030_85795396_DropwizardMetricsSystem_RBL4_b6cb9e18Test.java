package org.apache.calcite.avatica.metrics.dropwizard;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.calcite.avatica.metrics.Counter;
import org.apache.calcite.avatica.metrics.Gauge;
import org.apache.calcite.avatica.metrics.Histogram;
import org.apache.calcite.avatica.metrics.Meter;
import org.apache.calcite.avatica.metrics.Timer;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystem;
import com.codahale.metrics.MetricRegistry;
import org.junit.Before;
import org.junit.Test;

public class DropwizardMetricsSystem_RBL4_b6cb9e18Test {

    private MetricRegistry metricRegistry;
    private DropwizardMetricsSystem metricsSystem;

    @Before
    public void setUp() {
        metricRegistry = mock(MetricRegistry.class);
        metricsSystem = new DropwizardMetricsSystem(metricRegistry);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullRegistry() {
        new DropwizardMetricsSystem(null);
    }

    @Test
    public void testGetTimer() {
        String timerName = "testTimer";
        Timer timer = mock(Timer.class);
        when(metricRegistry.timer(timerName)).thenReturn(timer);

        Timer result = metricsSystem.getTimer(timerName);
        assertNotNull(result);
        assertEquals(timer, result);
    }

    @Test
    public void testGetHistogram() {
        String histogramName = "testHistogram";
        Histogram histogram = mock(Histogram.class);
        when(metricRegistry.histogram(histogramName)).thenReturn(histogram);

        Histogram result = metricsSystem.getHistogram(histogramName);
        assertNotNull(result);
        assertEquals(histogram, result);
    }

    @Test
    public void testGetMeter() {
        String meterName = "testMeter";
        Meter meter = mock(Meter.class);
        when(metricRegistry.meter(meterName)).thenReturn(meter);

        Meter result = metricsSystem.getMeter(meterName);
        assertNotNull(result);
        assertEquals(meter, result);
    }

    @Test
    public void testGetCounter() {
        String counterName = "testCounter";
        Counter counter = mock(Counter.class);
        when(metricRegistry.counter(counterName)).thenReturn(counter);

        Counter result = metricsSystem.getCounter(counterName);
        assertNotNull(result);
        assertEquals(counter, result);
    }

    @Test
    public void testRegister() {
        String gaugeName = "testGauge";
        Gauge<Integer> gauge = mock(Gauge.class);
        DropwizardGauge<Integer> dropwizardGauge = mock(DropwizardGauge.class);
        when(metricRegistry.register(eq(gaugeName), any())).thenReturn(dropwizardGauge);

        metricsSystem.register(gaugeName, gauge);
        verify(metricRegistry).register(eq(gaugeName), any(DropwizardGauge.class));
    }
}
