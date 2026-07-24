package dk.dma.enav.model.geometry;

import dk.dma.enav.model.geometry.PositionTime;
import dk.dma.enav.model.geometry.Position;
import org.junit.Test;
import static org.junit.Assert.*;

public class PositionTime_RBL4Test_e9d79346 {

    @Test
    public void testConstructorAndGetters() {
        PositionTime positionTime = new PositionTime(10.0, 20.0, 1000L);
        assertEquals(10.0, positionTime.getLatitude(), 0.001);
        assertEquals(20.0, positionTime.getLongitude(), 0.001);
        assertEquals(1000L, positionTime.getTime());
    }

    @Test
    public void testEquals() {
        PositionTime positionTime1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime positionTime2 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime positionTime3 = new PositionTime(15.0, 25.0, 2000L);
        
        assertTrue(positionTime1.equals(positionTime2));
        assertFalse(positionTime1.equals(positionTime3));
    }

    @Test
    public void testHashCode() {
        PositionTime positionTime1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime positionTime2 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime positionTime3 = new PositionTime(15.0, 25.0, 2000L);
        
        assertEquals(positionTime1.hashCode(), positionTime2.hashCode());
        assertNotEquals(positionTime1.hashCode(), positionTime3.hashCode());
    }

    @Test
    public void testCreate() {
        PositionTime positionTime = PositionTime.create(10.0, 20.0, 1000L);
        assertEquals(10.0, positionTime.getLatitude(), 0.001);
        assertEquals(20.0, positionTime.getLongitude(), 0.001);
        assertEquals(1000L, positionTime.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateExtrapolatedTimeEarlier() {
        PositionTime pt1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime.createExtrapolated(pt1, 90.0f, 10.0f, 500L);
    }

    @Test
    public void testCreateExtrapolated() {
        PositionTime pt1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime result = PositionTime.createExtrapolated(pt1, 90.0f, 10.0f, 2000L);
        
        assertNotNull(result);
        assertEquals(10.0, result.getLatitude(), 0.001);
        assertEquals(20.0 + 5.144, result.getLongitude(), 0.001); // 10 knots for 1 second
        assertEquals(2000L, result.getTime());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInterpolatedTimeEarlier() {
        PositionTime pt1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime pt2 = new PositionTime(15.0, 25.0, 2000L);
        PositionTime.createInterpolated(pt1, pt2, 500L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateInterpolatedTimeLater() {
        PositionTime pt1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime pt2 = new PositionTime(15.0, 25.0, 2000L);
        PositionTime.createInterpolated(pt1, pt2, 2500L);
    }

    @Test
    public void testCreateInterpolated() {
        PositionTime pt1 = new PositionTime(10.0, 20.0, 1000L);
        PositionTime pt2 = new PositionTime(15.0, 25.0, 2000L);
        PositionTime result = PositionTime.createInterpolated(pt1, pt2, 1500L);
        
        assertNotNull(result);
        assertEquals(12.5, result.getLatitude(), 0.001);
        assertEquals(22.5, result.getLongitude(), 0.001);
        assertEquals(1500L, result.getTime());
    }
}
