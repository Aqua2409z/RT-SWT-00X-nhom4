package dk.dma.enav.util;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class CoordinateConverter_RBL4_8d23156cTest {
    private CoordinateConverter converter;

    @Before
    public void setUp() {
        // Initialize the CoordinateConverter with a central point
        converter = new CoordinateConverter(10.0, 20.0); // Example central point
    }

    @Test
    public void testLon2x() {
        double x = converter.lon2x(10.0, 20.0);
        assertEquals(0.0, x, 1e-6); // Central point should return x = 0
        x = converter.lon2x(11.0, 20.0);
        assertEquals(111319.9, x, 1e-1); // Approximate value for 1 degree east
    }

    @Test
    public void testLat2y() {
        double y = converter.lat2y(10.0, 20.0);
        assertEquals(0.0, y, 1e-6); // Central point should return y = 0
        y = converter.lat2y(10.0, 21.0);
        assertEquals(111319.9, y, 1e-1); // Approximate value for 1 degree north
    }

    @Test
    public void testX2Lon() {
        double lon = converter.x2Lon(111319.9, 0.0);
        assertEquals(11.0, lon, 1e-1); // Should return longitude for 1 degree east
    }

    @Test
    public void testY2Lat() {
        double lat = converter.y2Lat(0.0, 111319.9);
        assertEquals(21.0, lat, 1e-1); // Should return latitude for 1 degree north
    }

    @Test
    public void testLon2xAndLat2yConsistency() {
        double lon = 11.0;
        double lat = 21.0;
        double x = converter.lon2x(lon, lat);
        double y = converter.lat2y(lon, lat);
        
        double calculatedLon = converter.x2Lon(x, y);
        double calculatedLat = converter.y2Lat(x, y);
        
        assertEquals(lon, calculatedLon, 1e-1);
        assertEquals(lat, calculatedLat, 1e-1);
    }
}
