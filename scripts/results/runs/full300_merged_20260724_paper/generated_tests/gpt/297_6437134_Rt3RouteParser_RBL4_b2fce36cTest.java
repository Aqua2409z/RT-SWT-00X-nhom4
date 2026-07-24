package dk.dma.enav.serialization;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import dk.dma.enav.model.voyage.Route;
import dk.dma.enav.model.voyage.Waypoint;

public class Rt3RouteParser_RBL4_b2fce36cTest {

    private Rt3RouteParser parser;
    private String validXml;
    private String invalidXml;

    @Before
    public void setUp() {
        validXml = "<Route RtName=\"Test Route\"><WayPoints><WayPoint WPName=\"WP1\" Lat=\"600000\" Lon=\"300000\" TurnRadius=\"50\" LegType=\"0\" PortXTE=\"10\" StbXTE=\"10\"/></WayPoints></Route>";
        invalidXml = "<Route><WayPoints></WayPoints></Route>";
    }

    @Test
    public void testParseValidRoute() throws Exception {
        Reader reader = new StringReader(validXml);
        parser = new Rt3RouteParser(reader);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Test Route", route.getName());
        assertEquals(1, route.getWaypoints().size());

        Waypoint waypoint = route.getWaypoints().get(0);
        assertEquals("WP1", waypoint.getName());
        assertEquals(10.0, waypoint.getTurnRad(), 0.01);
        assertEquals(10.0, waypoint.getRouteLeg().getXtdPort(), 0.01);
        assertEquals(10.0, waypoint.getRouteLeg().getXtdStarboard(), 0.01);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidRoute() throws Exception {
        Reader reader = new StringReader(invalidXml);
        parser = new Rt3RouteParser(reader);
        parser.parse();
    }

    @Test
    public void testParseWithFile() throws Exception {
        File tempFile = File.createTempFile("testRoute", ".rt3");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(validXml);
        }

        parser = new Rt3RouteParser(tempFile);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Test Route", route.getName());
        assertEquals(1, route.getWaypoints().size());
    }

    @Test
    public void testParseWithInputStream() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(validXml.getBytes());
        Map<String, String> config = new HashMap<>();
        config.put("name", "Test Route");

        parser = new Rt3RouteParser(inputStream, config);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Test Route", route.getName());
        assertEquals(1, route.getWaypoints().size());
    }

    @Test(expected = IOException.class)
    public void testParseWithMissingLatLon() throws Exception {
        String invalidWaypointXml = "<Route RtName=\"Test Route\"><WayPoints><WayPoint WPName=\"WP1\" Lat=\"\" Lon=\"\"/></WayPoints></Route>";
        Reader reader = new StringReader(invalidWaypointXml);
        parser = new Rt3RouteParser(reader);
        parser.parse();
    }
}
