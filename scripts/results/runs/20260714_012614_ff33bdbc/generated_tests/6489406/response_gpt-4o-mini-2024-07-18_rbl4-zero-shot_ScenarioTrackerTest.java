package dk.dma.ais.tracker.scenarioTracker;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisPositionMessage;
import dk.dma.ais.message.NavigationalStatus;
import dk.dma.ais.packet.AisPacket;
import dk.dma.enav.model.geometry.BoundingBox;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.PositionTime;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class ScenarioTrackerTest {

    private ScenarioTracker scenarioTracker;

    @Before
    public void setUp() {
        scenarioTracker = new ScenarioTracker();
    }

    @Test
    public void testScenarioBeginAndEnd() {
        AisPacket packet1 = createAisPacket(123456789, new Date(1000), true);
        AisPacket packet2 = createAisPacket(123456789, new Date(2000), true);
        AisPacket packet3 = createAisPacket(987654321, new Date(1500), true);

        scenarioTracker.update(packet1);
        scenarioTracker.update(packet2);
        scenarioTracker.update(packet3);

        assertEquals(packet1.getBestTimestamp(), scenarioTracker.scenarioBegin().getTime());
        assertEquals(packet2.getBestTimestamp(), scenarioTracker.scenarioEnd().getTime());
    }

    @Test
    public void testBoundingBox() {
        AisPacket packet = createAisPacket(123456789, new Date(), true);
        scenarioTracker.update(packet);
        BoundingBox boundingBox = scenarioTracker.boundingBox();
        assertNotNull(boundingBox);
    }

    @Test
    public void testGetTargets() {
        AisPacket packet = createAisPacket(123456789, new Date(), true);
        scenarioTracker.update(packet);
        assertEquals(1, scenarioTracker.size());
        assertEquals(1, scenarioTracker.getTargets().size());
    }

    @Test
    public void testGetTargetsHavingPositionUpdates() {
        AisPacket packet = createAisPacket(123456789, new Date(), true);
        scenarioTracker.update(packet);
        assertEquals(1, scenarioTracker.getTargetsHavingPositionUpdates().size());
    }

    @Test
    public void testTagTarget() {
        AisPacket packet = createAisPacket(123456789, new Date(), true);
        scenarioTracker.update(packet);
        scenarioTracker.tagTarget(123456789, "TestTag");
        assertTrue(scenarioTracker.get(123456789).isTagged("TestTag"));
    }

    private AisPacket createAisPacket(int mmsi, Date timestamp, boolean validPosition) {
        AisMessage message;
        if (validPosition) {
            message = new AisPositionMessage(mmsi, new Position(1.0, 1.0), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        } else {
            message = new AisMessage5(mmsi, "Test Ship", 0, "Test Destination", 0, 0, 0, 0);
        }
        return new AisPacket(message, timestamp);
    }
}
