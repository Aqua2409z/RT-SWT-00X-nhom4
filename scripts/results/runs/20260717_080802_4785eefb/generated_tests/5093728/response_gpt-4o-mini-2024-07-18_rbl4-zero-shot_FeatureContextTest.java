package com.googlecode.fitchy;

import com.googlecode.fitchy.Feature;
import com.googlecode.fitchy.FeatureContext;
import com.googlecode.fitchy.FeatureStatus;
import com.googlecode.fitchy.exception.FeatureAlreadyExistsException;
import com.googlecode.fitchy.util.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FeatureContextTest {

    private FeatureContext featureContext;
    private Configuration configuration;

    @Before
    public void setUp() {
        configuration = Configuration.getDefault();
        featureContext = new FeatureContext(configuration);
    }

    @Test
    public void testAddFeatureWithValidKey() {
        Feature feature = featureContext.addFeature("feature1");
        assertNotNull(feature);
        assertEquals("feature1", feature.getName());
        assertEquals(configuration.enabledStatus, feature.getStatus());
        assertEquals(1, featureContext.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFeatureWithNullKey() {
        featureContext.addFeature((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFeatureWithEmptyKey() {
        featureContext.addFeature("");
    }

    @Test(expected = FeatureAlreadyExistsException.class)
    public void testAddFeatureWithExistingKey() {
        featureContext.addFeature("feature1");
        featureContext.addFeature("feature1");
    }

    @Test
    public void testAddFeatureWithFeatureObject() {
        Feature feature = new Feature("feature2", configuration.enabledStatus);
        Feature addedFeature = featureContext.addFeature(feature);
        assertNotNull(addedFeature);
        assertEquals("feature2", addedFeature.getName());
        assertEquals(1, featureContext.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddFeatureWithNullFeature() {
        featureContext.addFeature((Feature) null);
    }

    @Test(expected = FeatureAlreadyExistsException.class)
    public void testAddFeatureWithExistingFeature() {
        Feature feature = new Feature("feature3", configuration.enabledStatus);
        featureContext.addFeature(feature);
        featureContext.addFeature(feature);
    }

    @Test
    public void testHasFeature() {
        featureContext.addFeature("feature4");
        assertTrue(featureContext.hasFeature("feature4"));
        assertFalse(featureContext.hasFeature("nonExistentFeature"));
    }

    @Test
    public void testFeatureHasStatus() {
        featureContext.addFeature("feature5");
        assertTrue(featureContext.featureHasStatus("feature5", configuration.enabledStatus));
        assertFalse(featureContext.featureHasStatus("feature5", FeatureStatus.DISABLED));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFeatureHasStatusWithNullKey() {
        featureContext.featureHasStatus(null, configuration.enabledStatus);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFeatureHasStatusWithEmptyKey() {
        featureContext.featureHasStatus("", configuration.enabledStatus);
    }

    @Test
    public void testClear() {
        featureContext.addFeature("feature6");
        assertEquals(1, featureContext.size());
        featureContext.clear();
        assertEquals(0, featureContext.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, featureContext.size());
        featureContext.addFeature("feature7");
        assertEquals(1, featureContext.size());
    }

    @Test
    public void testGetConfig() {
        assertEquals(configuration, featureContext.getConfig());
    }
}
