package dk.dma.ais.tracker.eventEmittingTracker;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisMessage5;
import dk.dma.ais.message.AisStaticCommon;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.eventEmittingTracker.Track;
import dk.dma.ais.tracker.eventEmittingTracker.TrackingReport;
import dk.dma.ais.tracker.eventEmittingTracker.AisTrackingReport;
import dk.dma.ais.tracker.eventEmittingTracker.InterpolatedTrackingReport;
import dk.dma.enav.model.geometry.Position;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TrackTest {

    private Track track;
    private AisPacket aisPacketStatic;
    private AisPacket aisPacketPosition;
    private AisMessage5 aisMessage5;
    private AisStaticCommon aisStaticCommon;
    private AisMessage aisMessage;

    @Before
    public void setUp() {
        track = new Track(123456789);
        aisPacketStatic = mock(AisPacket.class);
        aisPacketPosition = mock(AisPacket.class);
        aisMessage5 = mock(AisMessage5.class);
        aisStaticCommon = mock(AisStaticCommon.class);
        aisMessage = mock(AisMessage.class);
    }

    @Test
    public void testConstructor() {
        assertEquals(123456789, track.getMmsi());
    }

    @Test
    public void testUpdateStaticReport() {
        when(aisPacketStatic.tryGetAisMessage()).thenReturn(aisStaticCommon);
        when(aisStaticCommon.getCallsign()).thenReturn("TestCallsign");
        when(aisStaticCommon.getShipType()).thenReturn(1);
        when(aisStaticCommon.getName()).thenReturn("TestShip");
        when(aisStaticCommon.getDimBow()).thenReturn(10);
        when(aisStaticCommon.getDimStern()).thenReturn(10);
        when(aisStaticCommon.getDimPort()).thenReturn(5);
        when(aisStaticCommon.getDimStarboard()).thenReturn(5);
        when(aisMessage5.getImo()).thenReturn(1234567L);
        when(aisPacketStatic.tryGetAisMessage()).thenReturn(aisMessage5);

        track.update(aisPacketStatic);

        assertEquals("TestCallsign", track.getCallsign());
        assertEquals(Integer.valueOf(1), track.getShipType());
        assertEquals("TestShip", track.getShipName());
        assertEquals(Integer.valueOf(20), track.getVesselLength());
        assertEquals(Integer.valueOf(10), track.getShipDimensionBow());
        assertEquals(Integer.valueOf(10), track.getShipDimensionStern());
        assertEquals(Integer.valueOf(5), track.getShipDimensionPort());
        assertEquals(Integer.valueOf(5), track.getShipDimensionStarboard());
        assertEquals(Integer.valueOf(1234567), track.getIMO());
    }

    @Test
    public void testUpdatePositionReport() {
        when(aisPacketPosition.tryGetAisMessage()).thenReturn(aisMessage);
        when(aisMessage.getUserId()).thenReturn(123456789);
        when(aisMessage.getCog()).thenReturn(1000L);
        when(aisMessage.getSog()).thenReturn(2000L);
        when(aisMessage.getTrueHeading()).thenReturn(180L);
        Position position = mock(Position.class);
        when(aisMessage.getValidPosition()).thenReturn(position);
        when(aisPacketPosition.getBestTimestamp()).thenReturn(1000L);

        track.update(aisPacketPosition);

        assertNotNull(track.getNewestTrackingReport());
        assertEquals(1000L, track.getTimeOfLastPositionReport());
    }

    @Test
    public void testGetTrackingReports() {
        when(aisPacketPosition.tryGetAisMessage()).thenReturn(aisMessage);
        when(aisMessage.getUserId()).thenReturn(123456789);
        when(aisMessage.getCog()).thenReturn(1000L);
        when(aisMessage.getSog()).thenReturn(2000L);
        when(aisMessage.getTrueHeading()).thenReturn(180L);
        Position position = mock(Position.class);
        when(aisMessage.getValidPosition()).thenReturn(position);
        when(aisPacketPosition.getBestTimestamp()).thenReturn(1000L);

        track.update(aisPacketPosition);
        assertEquals(1, track.getTrackingReports().size());
    }

    @Test
    public void testSetProperty() {
        track.setProperty("testKey", "testValue");
        assertEquals("testValue", track.getProperty("testKey"));
    }

    @Test
    public void testRemoveProperty() {
        track.setProperty("testKey", "testValue");
        track.removeProperty("testKey");
        assertNull(track.getProperty("testKey"));
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        track.setProperty("testKey", "testValue");
        Track clonedTrack = track.clone();
        assertEquals(track.getProperty("testKey"), clonedTrack.getProperty("testKey"));
        assertNotSame(track, clonedTrack);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateInvalidPacket() {
        when(aisPacketPosition.tryGetAisMessage()).thenReturn(null);
        track.update(aisPacketPosition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateMismatchedMMSI() {
        when(aisPacketPosition.tryGetAisMessage()).thenReturn(aisMessage);
        when(aisMessage.getUserId()).thenReturn(987654321);
        track.update(aisPacketPosition);
    }

    @Test
    public void testPredict() {
        when(aisPacketPosition.tryGetAisMessage()).thenReturn(aisMessage);
        when(aisMessage.getUserId()).thenReturn(123456789);
        when(aisMessage.getCog()).thenReturn(1000L);
        when(aisMessage.getSog()).thenReturn(2000L);
        when(aisMessage.getTrueHeading()).thenReturn(180L);
        Position position = mock(Position.class);
        when(aisMessage.getValidPosition()).thenReturn(position);
        when(aisPacketPosition.getBestTimestamp()).thenReturn(1000L);

        track.update(aisPacketPosition);
        track.predict(2000L);

        assertNotNull(track.getNewestTrackingReport());
    }
}
