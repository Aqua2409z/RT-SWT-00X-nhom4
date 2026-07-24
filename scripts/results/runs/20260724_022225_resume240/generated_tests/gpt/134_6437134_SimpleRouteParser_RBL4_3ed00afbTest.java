package dk.dma.enav.serialization;

import dk.dma.enav.model.voyage.Route;
import dk.dma.enav.model.voyage.RouteLeg;
import dk.dma.enav.model.voyage.Waypoint;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SimpleRouteParser_RBL4_3ed00afbTest {

    private SimpleRouteParser parser;

    @Before
    public void setUp() {
        // Setup can be done here if needed
    }

    @Test
    public void testParseValidRoute() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\t10.0\t20.0\t30.0\t0\t0.0\t5.0\n";
        parser = new SimpleRouteParser(new StringReader(input));
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Route1", route.getName());
        assertEquals("Departure1", route.getDeparture());
        assertEquals("Destination1", route.getDestination());
        assertEquals(1, route.getWaypoints().size());

        Waypoint waypoint = route.getWaypoints().get(0);
        assertEquals("Waypoint1", waypoint.getName());
        assertEquals(10.0, waypoint.getLatitude(), 0.001);
        assertEquals(20.0, waypoint.getLongitude(), 0.001);
        assertNotNull(waypoint.getRouteLeg());
        assertEquals(30.0, waypoint.getRouteLeg().getSpeed(), 0.001);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidFirstLine() throws IOException {
        String input = "\n" +
                       "Waypoint1\t10.0\t20.0\t30.0\t0\t0.0\t5.0\n";
        parser = new SimpleRouteParser(new StringReader(input));
        parser.parse();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidWaypointLine() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\t10.0\t20.0\t30.0\t0\n"; // Less than 7 fields
        parser = new SimpleRouteParser(new StringReader(input));
        parser.parse();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidLatitude() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\tinvalid\t20.0\t30.0\t0\t0.0\t5.0\n";
        parser = new SimpleRouteParser(new StringReader(input));
        parser.parse();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidLongitude() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\t10.0\tinvalid\t30.0\t0\t0.0\t5.0\n";
        parser = new SimpleRouteParser(new StringReader(input));
        parser.parse();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidSpeed() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\t10.0\t20.0\tinvalid\t0\t0.0\t5.0\n";
        parser = new SimpleRouteParser(new StringReader(input));
        parser.parse();
    }

    @Test(expected = IOException.class)
    public void testParseInvalidTurnRadius() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\t10.0\t20.0\t30.0\t0\t0.0\tinvalid\n";
        parser = new SimpleRouteParser(new StringReader(input));
        parser.parse();
    }

    @Test
    public void testParseWithFile() throws IOException {
        File tempFile = File.createTempFile("testRoute", ".txt");
        String content = "Route1\tDeparture1\tDestination1\n" +
                         "Waypoint1\t10.0\t20.0\t30.0\t0\t0.0\t5.0\n";
        java.nio.file.Files.write(tempFile.toPath(), content.getBytes());

        parser = new SimpleRouteParser(tempFile);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Route1", route.getName());
        assertEquals("Departure1", route.getDeparture());
        assertEquals("Destination1", route.getDestination());
        assertEquals(1, route.getWaypoints().size());

        tempFile.deleteOnExit();
    }

    @Test
    public void testParseWithInputStream() throws IOException {
        String input = "Route1\tDeparture1\tDestination1\n" +
                       "Waypoint1\t10.0\t20.0\t30.0\t0\t0.0\t5.0\n";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Map<String, String> config = new HashMap<>();
        parser = new SimpleRouteParser(inputStream, config);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Route1", route.getName());
        assertEquals("Departure1", route.getDeparture());
        assertEquals("Destination1", route.getDestination());
        assertEquals(1, route.getWaypoints().size());
    }
}
