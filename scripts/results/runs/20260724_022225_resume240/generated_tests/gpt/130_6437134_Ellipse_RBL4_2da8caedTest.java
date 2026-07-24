package dk.dma.enav.model.geometry;

import dk.dma.enav.model.geometry.Ellipse;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class Ellipse_RBL4_2da8caedTest {

    private Position referencePosition;
    private CoordinateSystem coordinateSystem;
    private Ellipse ellipse1;
    private Ellipse ellipse2;

    @Before
    public void setUp() {
        referencePosition = Position.create(10.0, 20.0);
        coordinateSystem = new CoordinateSystem(); // Assuming a default constructor exists
        ellipse1 = new Ellipse(referencePosition, 100, 50, 30, coordinateSystem);
        ellipse2 = new Ellipse(referencePosition, 150, 75, 60, coordinateSystem);
    }

    @Test
    public void testGetGeodeticReference() {
        assertEquals(referencePosition, ellipse1.getGeodeticReference());
    }

    @Test
    public void testGetX() {
        assertEquals(0.0, ellipse1.getX(), 0.001);
    }

    @Test
    public void testGetY() {
        assertEquals(0.0, ellipse1.getY(), 0.001);
    }

    @Test
    public void testGetAlpha() {
        assertEquals(100, ellipse1.getAlpha(), 0.001);
    }

    @Test
    public void testGetBeta() {
        assertEquals(50, ellipse1.getBeta(), 0.001);
    }

    @Test
    public void testGetThetaDeg() {
        assertEquals(30, ellipse1.getThetaDeg(), 0.001);
    }

    @Test
    public void testIntersects() {
        assertTrue(ellipse1.intersects(ellipse2));
    }

    @Test
    public void testContains() {
        Position insidePosition = Position.create(10.0, 20.0); // Assuming this is inside the ellipse
        assertTrue(ellipse1.contains(insidePosition));
        
        Position outsidePosition = Position.create(200.0, 300.0); // Assuming this is outside the ellipse
        assertFalse(ellipse1.contains(outsidePosition));
    }

    @Test
    public void testSamplePerimeter() {
        List<Position> perimeter = ellipse1.samplePerimeter(10);
        assertEquals(10, perimeter.size());
    }

    @Test
    public void testGetMajorAxisGeodeticHeading() {
        double heading = ellipse1.getMajorAxisGeodeticHeading();
        assertEquals(30, heading, 0.001);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIntersectsWithDifferentElement() {
        ellipse1.intersects(new Element() {}); // Assuming Element is an interface
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testContainsWithDifferentElement() {
        ellipse1.contains(new Element() {}); // Assuming Element is an interface
    }
}
