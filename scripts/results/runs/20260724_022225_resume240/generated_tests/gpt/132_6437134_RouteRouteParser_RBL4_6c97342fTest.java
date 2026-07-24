package dk.dma.enav.serialization;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import dk.dma.enav.model.voyage.Route;
import dk.dma.enav.model.voyage.Waypoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

public class RouteRouteParser_RBL4_6c97342fTest {

    private RouteRouteParser parser;
    private String validXml;
    private String invalidXml;

    @Before
    public void setUp() {
        validXml = "<Route><Summaries><Name>Test Route</Name><DepartureTime>2023-10-01T12:00:00.000+00:00</DepartureTime></Summaries><ControlPoints><ControlPoints><RouteName>Test Route</RouteName><SequenceNumber>1</SequenceNumber><Name>Waypoint 1</Name><Latitude>55.0</Latitude><Longitude>10.0</Longitude><TurnRadius>100</TurnRadius><DepartingControlLineType>RhumbLine</DepartingControlLineType><DepartingTrackSpeed>10</DepartingTrackSpeed></ControlPoints></ControlPoints></Route>";
        invalidXml = "<Route><Summaries></Summaries></Route>";
    }

    @After
    public void tearDown() {
        parser = null;
    }

    @Test
    public void testParseValidXml() throws Exception {
        parser = new RouteRouteParser(new StringReader(validXml));
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Test Route", route.getName());
        assertEquals(1, route.getWaypoints().size());

        Waypoint waypoint = route.getWaypoints().get(0);
        assertEquals("Waypoint 1", waypoint.getName());
        assertEquals(55.0, waypoint.getLatitude(), 0.01);
        assertEquals(10.0, waypoint.getLongitude(), 0.01);
        assertEquals(100 / (1.852 * 1000), waypoint.getTurnRad(), 0.01);
    }

    @Test(expected = IOException.class)
    public void testParseInvalidXml() throws Exception {
        parser = new RouteRouteParser(new StringReader(invalidXml));
        parser.parse();
    }

    @Test
    public void testParseWithFile() throws Exception {
        File tempFile = File.createTempFile("testRoute", ".xml");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(validXml);
        }

        parser = new RouteRouteParser(tempFile);
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

        parser = new RouteRouteParser(inputStream, config);
        Route route = parser.parse();

        assertNotNull(route);
        assertEquals("Test Route", route.getName());
        assertEquals(1, route.getWaypoints().size());
    }
}
