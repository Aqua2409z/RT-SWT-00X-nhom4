
package de.voidnode.trading4j.functionality.smoothers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.voidnode.trading4j.domain.Ratio;

public class SimpleMovingAverageRatio_RBL4_3960d3dfTest {

    private SimpleMovingAverageRatio simpleMovingAverage;
    private static final int AGGREGATION_COUNT = 3;

    @Before
    public void setUp() {
        simpleMovingAverage = new SimpleMovingAverageRatio(AGGREGATION_COUNT);
    }

    @Test
    public void testSmoothWithLessDataPoints() {
        Ratio ratio1 = new Ratio(1.0);
        Ratio ratio2 = new Ratio(2.0);

        assertFalse(simpleMovingAverage.smooth(ratio1).isPresent());
        assertFalse(simpleMovingAverage.smooth(ratio2).isPresent());
    }

    @Test
    public void testSmoothWithExactDataPoints() {
        Ratio ratio1 = new Ratio(1.0);
        Ratio ratio2 = new Ratio(2.0);
        Ratio ratio3 = new Ratio(3.0);

        simpleMovingAverage.smooth(ratio1);
        simpleMovingAverage.smooth(ratio2);
        Optional<Ratio> result = simpleMovingAverage.smooth(ratio3);

        assertTrue(result.isPresent());
        assertEquals(2.0, result.get().asBasic(), 0.001);
    }

    @Test
    public void testSmoothWithMoreDataPoints() {
        Ratio ratio1 = new Ratio(1.0);
        Ratio ratio2 = new Ratio(2.0);
        Ratio ratio3 = new Ratio(3.0);
        Ratio ratio4 = new Ratio(4.0);

        simpleMovingAverage.smooth(ratio1);
        simpleMovingAverage.smooth(ratio2);
        simpleMovingAverage.smooth(ratio3);
        Optional<Ratio> result = simpleMovingAverage.smooth(ratio4);

        assertTrue(result.isPresent());
        assertEquals(3.0, result.get().asBasic(), 0.001);
    }

    @Test
    public void testSmoothWithNegativeValues() {
        Ratio ratio1 = new Ratio(-1.0);
        Ratio ratio2 = new Ratio(-2.0);
        Ratio ratio3 = new Ratio(-3.0);
        Ratio ratio4 = new Ratio(-4.0);

        simpleMovingAverage.smooth(ratio1);
        simpleMovingAverage.smooth(ratio2);
        simpleMovingAverage.smooth(ratio3);
        Optional<Ratio> result = simpleMovingAverage.smooth(ratio4);

        assertTrue(result.isPresent());
        assertEquals(-3.0, result.get().asBasic(), 0.001);
    }

    @Test
    public void testSmoothWithMixedValues() {
        Ratio ratio1 = new Ratio(1.0);
        Ratio ratio2 = new Ratio(2.0);
        Ratio ratio3 = new Ratio(-3.0);
        Ratio ratio4 = new Ratio(4.0);

        simpleMovingAverage.smooth(ratio1);
        simpleMovingAverage.smooth(ratio2);
        simpleMovingAverage.smooth(ratio3);
        Optional<Ratio> result = simpleMovingAverage.smooth(ratio4);

        assertTrue(result.isPresent());
        assertEquals(1.0, result.get().asBasic(), 0.001);
    }
}
