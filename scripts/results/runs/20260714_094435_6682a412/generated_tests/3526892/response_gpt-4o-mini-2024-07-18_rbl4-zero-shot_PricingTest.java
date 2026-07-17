package com.datascience.gal;

import static org.junit.Assert.*;
import org.junit.Test;

public class PricingTest {

    @Test
    public void testGetNormalizedP_simple() {
        assertEquals(0.5, Pricing.getNormalizedP_simple(0), 0.0001);
        assertEquals(0.7071, Pricing.getNormalizedP_simple(0.5), 0.0001);
        assertEquals(0.8660, Pricing.getNormalizedP_simple(1), 0.0001);
    }

    @Test
    public void testGetNormalizedQexp_simple() {
        assertEquals(0.0, Pricing.getNormalizedQexp_simple(0.5), 0.0001);
        assertEquals(0.25, Pricing.getNormalizedQexp_simple(0.75), 0.0001);
        assertEquals(1.0, Pricing.getNormalizedQexp_simple(1.0), 0.0001);
    }

    @Test
    public void testProbabilityCorrect() {
        assertNull(Pricing.probabilityCorrect(0.6, -1));
        assertEquals(0.5, Pricing.probabilityCorrect(0.5, 0), 0.0001);
        assertNull(Pricing.probabilityCorrect(1.1, 1));
        assertNull(Pricing.probabilityCorrect(-0.1, 1));
        assertEquals(0.875, Pricing.probabilityCorrect(0.8, 3), 0.0001);
        assertEquals(0.5, Pricing.probabilityCorrect(0.5, 5), 0.0001);
    }

    @Test
    public void testPricingFactor() {
        assertNull(Pricing.pricingFactor(0.5, 0.6));
        assertNull(Pricing.pricingFactor(0.6, 0.5));
        assertNull(Pricing.pricingFactor(1.1, 0.9));
        assertNull(Pricing.pricingFactor(0.9, 1.1));
        assertEquals(1.0, Pricing.pricingFactor(0.6, 0.7), 0.0001);
        assertEquals(2.0, Pricing.pricingFactor(0.7, 0.8), 0.0001);
    }
}
