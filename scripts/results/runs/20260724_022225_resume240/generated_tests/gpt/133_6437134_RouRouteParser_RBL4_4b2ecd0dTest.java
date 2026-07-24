package dk.dma.enav.serialization;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import dk.dma.enav.model.voyage.Route;
import dk.dma.enav.model.voyage.Waypoint;
import dk.dma.enav.model.voyage.RouteLeg;
import dk.dma.enav.model.voyage.RouteLeg.Heading;

public class RouRouteParser_RBL4_4b2ecd0dTest {

    private RouRouteParser parser;
    private StringReader validInput;
    private StringReader invalidInput;

    @Before
    public void setUp() {
        String validData = "ROUTE HEADER INFORMATION\n" +
                "Route name: Test Route\n" +
                "SOG default: 10.0\n" +
                "WAYPOINT\n" +
                "Name: WP1\n" +
                "Latitude: 55.0\n" +
                "Longitude: 10.0\n" +
                "Turn radius: 5.0\n" +
                "SOG: 12.0\n" +
                "Leg type: 1\n" +
                "Circles: 0 0 1.0 0 1.0\n" +
                "\n" +
                "WAYPOINT\n" +
                "Name: WP2\n" +
                "Latitude: 56.0\n" +
                "Longitude: 11.0\n" +
                "Turn radius: 6.0\n" +
                "SOG: 14.0\n" +
                "Leg type: 2\n" +
                "Circles: 0 0 2.0 0 2.0\n";

        validInput = new StringReader(validData);

        String invalidData = "ROUTE HEADER INFORMATION\n" +
                "Route name: Test Route\n" +
                "SOG default: 10.0\n" +
                "WAYPOINT\n" +
                "Name: WP1\n" +
                "Latitude: 55.0\n" +
                "Longitude: \n"; // Missing longitude

        invalidInput = new StringReader(invalidData);
    }

    @Test
    public void testParseValidInput() throws IOException {
        parser = new RouRouteParser(validInput);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Test Route", route.getName());
        assertEquals(2, route.getWaypoints().size());

        Waypoint wp1 = route.getWaypoints().get(0);
        assertEquals("WP1", wp1.getName());
        assertEquals(Double.valueOf(55.0), wp1.getLatitude());
        assertEquals(Double.valueOf(10.0), wp1.getLongitude());
        assertEquals(Double.valueOf(5.0), wp1.getTurnRad());
        assertEquals(Double.valueOf(12.0), wp1.getRouteLeg().getSpeed());
        assertEquals(Heading.RL, wp1.getRouteLeg().getHeading());

        Waypoint wp2 = route.getWaypoints().get(1);
        assertEquals("WP2", wp2.getName());
        assertEquals(Double.valueOf(56.0), wp2.getLatitude());
        assertEquals(Double.valueOf(11.0), wp2.getLongitude());
        assertEquals(Double.valueOf(6.0), wp2.getTurnRad());
        assertEquals(Double.valueOf(14.0), wp2.getRouteLeg().getSpeed());
        assertEquals(Heading.GC, wp2.getRouteLeg().getHeading());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidInput() throws IOException {
        parser = new RouRouteParser(invalidInput);
        parser.parse();
    }

    @Test
    public void testParseEmptyInput() throws IOException {
        StringReader emptyInput = new StringReader("");
        parser = new RouRouteParser(emptyInput);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("NO NAME", route.getName());
        assertTrue(route.getWaypoints().isEmpty());
    }
}
