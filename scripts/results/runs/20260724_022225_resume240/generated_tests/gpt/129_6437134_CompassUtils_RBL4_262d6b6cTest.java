package dk.dma.enav.util.compass;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CompassUtils_RBL4_262d6b6cTest {

    @Test
    public void testAbsoluteDirectionalDifference() {
        assertEquals(0.0f, CompassUtils.absoluteDirectionalDifference(0.0f, 0.0f), 0.001);
        assertEquals(90.0f, CompassUtils.absoluteDirectionalDifference(0.0f, 90.0f), 0.001);
        assertEquals(90.0f, CompassUtils.absoluteDirectionalDifference(90.0f, 0.0f), 0.001);
        assertEquals(180.0f, CompassUtils.absoluteDirectionalDifference(0.0f, 180.0f), 0.001);
        assertEquals(180.0f, CompassUtils.absoluteDirectionalDifference(180.0f, 0.0f), 0.001);
        assertEquals(90.0f, CompassUtils.absoluteDirectionalDifference(270.0f, 0.0f), 0.001);
        assertEquals(90.0f, CompassUtils.absoluteDirectionalDifference(0.0f, 270.0f), 0.001);
        assertEquals(180.0f, CompassUtils.absoluteDirectionalDifference(270.0f, 90.0f), 0.001);
        assertEquals(0.0f, CompassUtils.absoluteDirectionalDifference(360.0f, 0.0f), 0.001);
        assertEquals(0.0f, CompassUtils.absoluteDirectionalDifference(-360.0f, 0.0f), 0.001);
    }

    @Test
    public void testDirectionInCompassRange() {
        assertEquals(0.0f, CompassUtils.directionInCompassRange(0.0f), 0.001);
        assertEquals(10.0f, CompassUtils.directionInCompassRange(10.0f), 0.001);
        assertEquals(360.0f, CompassUtils.directionInCompassRange(360.0f), 0.001);
        assertEquals(315.0f, CompassUtils.directionInCompassRange(-45.0f), 0.001);
        assertEquals(10.0f, CompassUtils.directionInCompassRange(370.0f), 0.001);
        assertEquals(180.0f, CompassUtils.directionInCompassRange(540.0f), 0.001);
        assertEquals(270.0f, CompassUtils.directionInCompassRange(-90.0f), 0.001);
    }

    @Test
    public void testCompass2Cartesian() {
        assertEquals(90.0, CompassUtils.compass2cartesian(0.0), 0.001);
        assertEquals(0.0, CompassUtils.compass2cartesian(90.0), 0.001);
        assertEquals(270.0, CompassUtils.compass2cartesian(180.0), 0.001);
        assertEquals(180.0, CompassUtils.compass2cartesian(270.0), 0.001);
        assertEquals(90.0, CompassUtils.compass2cartesian(360.0), 0.001);
        assertEquals(90.0, CompassUtils.compass2cartesian(450.0), 0.001);
    }

    @Test
    public void testCartesian2Compass() {
        assertEquals(0.0, CompassUtils.cartesian2compass(90.0), 0.001);
        assertEquals(90.0, CompassUtils.cartesian2compass(0.0), 0.001);
        assertEquals(180.0, CompassUtils.cartesian2compass(270.0), 0.001);
        assertEquals(270.0, CompassUtils.cartesian2compass(180.0), 0.001);
        assertEquals(0.0, CompassUtils.cartesian2compass(360.0), 0.001);
        assertEquals(0.0, CompassUtils.cartesian2compass(450.0), 0.001);
    }
}
