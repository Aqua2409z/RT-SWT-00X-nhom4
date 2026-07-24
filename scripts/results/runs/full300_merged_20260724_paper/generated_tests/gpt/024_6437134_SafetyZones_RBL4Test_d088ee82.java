package dk.dma.enav.safety;

import dk.dma.enav.model.geometry.Ellipse;
import dk.dma.enav.model.geometry.Position;
import org.junit.Test;
import static org.junit.Assert.*;

public class SafetyZones_RBL4Test_d088ee82 {

    @Test
    public void testVesselExtent() {
        Position position = new Position(10.0, 20.0);
        float hdg = 90.0f;
        float loa = 30.0f;
        float beam = 10.0f;
        float dimStern = 5.0f;
        float dimStarboard = 3.0f;

        Ellipse ellipse = SafetyZones.vesselExtent(position, hdg, loa, beam, dimStern, dimStarboard);
        assertNotNull(ellipse);
        assertEquals(30.0f * 1.0 / 2.0, ellipse.getSemiMajorAxis(), 0.01);
        assertEquals(10.0f * 1.0 / 2.0, ellipse.getSemiMinorAxis(), 0.01);
    }

    @Test
    public void testSafetyZone() {
        Position geodeticReference = new Position(10.0, 20.0);
        Position position = new Position(10.1, 20.1);
        float cog = 90.0f;
        float sog = 5.0f;
        float loa = 30.0f;
        float beam = 10.0f;
        float dimStern = 5.0f;
        float dimStarboard = 3.0f;

        Ellipse ellipse = SafetyZones.safetyZone(geodeticReference, position, cog, sog, loa, beam, dimStern, dimStarboard);
        assertNotNull(ellipse);
        assertTrue(ellipse.getSemiMajorAxis() > 0);
        assertTrue(ellipse.getSemiMinorAxis() > 0);
    }

    @Test
    public void testCreateEllipse() {
        Position geodeticReference = new Position(10.0, 20.0);
        Position position = new Position(10.1, 20.1);
        float direction = 90.0f;
        float loa = 30.0f;
        float beam = 10.0f;
        float dimStern = 5.0f;
        float dimStarboard = 3.0f;
        double l1 = 1.0;
        double b1 = 1.0;
        double xc = 0.5;

        Ellipse ellipse = SafetyZones.createEllipse(geodeticReference, position, direction, loa, beam, dimStern, dimStarboard, l1, b1, xc);
        assertNotNull(ellipse);
        assertEquals(30.0f * l1 / 2.0, ellipse.getSemiMajorAxis(), 0.01);
        assertEquals(10.0f * b1 / 2.0, ellipse.getSemiMinorAxis(), 0.01);
    }
}
