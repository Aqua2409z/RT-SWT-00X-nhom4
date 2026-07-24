package org.apache.calcite.avatica.metrics.dropwizard;

import org.apache.calcite.avatica.metrics.MetricsSystemConfiguration;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystem;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystemConfiguration;
import org.apache.calcite.avatica.metrics.dropwizard.DropwizardMetricsSystemFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class DropwizardMetricsSystemFactory_RBL4Test_33421196 {

    @Test
    public void testCreateWithValidConfiguration() {
        DropwizardMetricsSystemConfiguration config = new DropwizardMetricsSystemConfiguration();
        DropwizardMetricsSystemFactory factory = new DropwizardMetricsSystemFactory();
        
        DropwizardMetricsSystem metricsSystem = factory.create(config);
        
        assertNotNull(metricsSystem);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithInvalidConfiguration() {
        MetricsSystemConfiguration<?> invalidConfig = new MetricsSystemConfiguration<Object>() {};
        DropwizardMetricsSystemFactory factory = new DropwizardMetricsSystemFactory();
        
        factory.create(invalidConfig);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateWithNullConfiguration() {
        DropwizardMetricsSystemFactory factory = new DropwizardMetricsSystemFactory();
        
        factory.create(null);
    }
}
