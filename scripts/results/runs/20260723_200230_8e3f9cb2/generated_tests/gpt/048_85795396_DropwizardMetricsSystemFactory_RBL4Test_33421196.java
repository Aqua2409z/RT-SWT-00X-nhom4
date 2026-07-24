package org.apache.calcite.avatica.metrics.dropwizard;

import org.apache.calcite.avatica.metrics.MetricsSystemConfiguration;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystem;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystemConfiguration;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystemFactory;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DropwizardMetricsSystemFactory_RBL4Test_33421196 {

    @Test
    public void testCreateWithValidConfiguration() {
        DropwizardMetricsSystemConfiguration config = new DropwizardMetricsSystemConfiguration();
        DropwizardMetricsSystemFactory factory = new DropwizardMetricsSystemFactory();
        
        DropwizardMetricsSystem metricsSystem = factory.create(config);
        
        assertNotNull("Metrics system should not be null", metricsSystem);
    }

    @Test
    public void testCreateWithInvalidConfiguration() {
        MetricsSystemConfiguration<?> invalidConfig = new MetricsSystemConfiguration<Object>() {
            // This is a mock configuration that does not extend DropwizardMetricsSystemConfiguration
        };
        DropwizardMetricsSystemFactory factory = new DropwizardMetricsSystemFactory();
        
        try {
            factory.create(invalidConfig);
            fail("Expected IllegalStateException for invalid configuration");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }

    @Test
    public void testCreateWithNullConfiguration() {
        DropwizardMetricsSystemFactory factory = new DropwizardMetricsSystemFactory();
        
        try {
            factory.create(null);
            fail("Expected IllegalStateException for null configuration");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}
