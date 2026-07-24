package dk.dma.enav.model.geometry;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BoundingBox_RBL4_1f61159dTest {
    private BoundingBox boundingBox;
    private Position positionInside;
    private Position positionOutside;

    @Before
    public void setUp() {
        boundingBox = BoundingBox.create(10, 20, 30, 40, CoordinateSystem.CARTESIAN);
        positionInside = Position.create(15, 35);
        positionOutside = Position.create(25, 45);
    }

    @Test
    public void testContains() {
        assertTrue(boundingBox.contains(positionInside));
        assertFalse(boundingBox.contains(positionOutside));
    }

    @Test
    public void testEquals() {
        BoundingBox sameBox = BoundingBox.create(10, 20, 30, 40, CoordinateSystem.CARTESIAN);
        BoundingBox differentBox = BoundingBox.create(5, 15, 25, 35, CoordinateSystem.CARTESIAN);
        
        assertTrue(boundingBox.equals(sameBox));
        assertFalse(boundingBox.equals(differentBox));
    }

    @Test
    public void testGetRandom() {
        Position randomPosition = boundingBox.getRandom();
        assertTrue(boundingBox.contains(randomPosition));
    }

    @Test
    public void testGetArea() {
        float area = boundingBox.getArea();
        assertTrue(area > 0);
    }

    @Test
    public void testGetCenterPoint() {
        Position center = boundingBox.getCenterPoint();
        assertEquals(15.0, center.getLatitude(), 0.01);
        assertEquals(35.0, center.getLongitude(), 0.01);
    }

    @Test
    public void testGetLatitudeSize() {
        assertEquals(10.0, boundingBox.getLatitudeSize(), 0.01);
    }

    @Test
    public void testGetLongitudeSize() {
        assertEquals(10.0, boundingBox.getLongitudeSize(), 0.01);
    }

    @Test
    public void testIncludeBoundingBox() {
        BoundingBox otherBox = BoundingBox.create(5, 25, 20, 50, CoordinateSystem.CARTESIAN);
        BoundingBox includedBox = boundingBox.include(otherBox);
        
        assertEquals(5.0, includedBox.getMinLat(), 0.01);
        assertEquals(25.0, includedBox.getMaxLat(), 0.01);
        assertEquals(20.0, includedBox.getMinLon(), 0.01);
        assertEquals(50.0, includedBox.getMaxLon(), 0.01);
    }

    @Test
    public void testIncludePosition() {
        BoundingBox includedBox = boundingBox.include(Position.create(5, 35));
        
        assertEquals(5.0, includedBox.getMinLat(), 0.01);
        assertEquals(20.0, includedBox.getMaxLat(), 0.01);
        assertEquals(30.0, includedBox.getMinLon(), 0.01);
        assertEquals(40.0, includedBox.getMaxLon(), 0.01);
    }

    @Test
    public void testIntersects() {
        BoundingBox otherBox = BoundingBox.create(15, 25, 35, 45, CoordinateSystem.CARTESIAN);
        assertTrue(boundingBox.intersects(otherBox));
        
        BoundingBox nonIntersectingBox = BoundingBox.create(25, 35, 45, 55, CoordinateSystem.CARTESIAN);
        assertFalse(boundingBox.intersects(nonIntersectingBox));
    }

    @Test
    public void testToString() {
        String expected = boundingBox.getUpperLeft() + " -> " + boundingBox.getLowerRight();
        assertEquals(expected, boundingBox.toString());
    }
}
