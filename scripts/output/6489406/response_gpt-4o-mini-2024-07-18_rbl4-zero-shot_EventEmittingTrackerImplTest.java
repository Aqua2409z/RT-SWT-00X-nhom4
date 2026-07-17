package dk.dma.ais.tracker.eventEmittingTracker;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.message.IVesselPositionMessage;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.tracker.Target;
import dk.dma.ais.tracker.eventEmittingTracker.EventEmittingTrackerImpl;
import dk.dma.ais.tracker.eventEmittingTracker.events.CellChangedEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.PositionChangedEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.TimeEvent;
import dk.dma.ais.tracker.eventEmittingTracker.events.TrackStaleEvent;
import dk.dma.enav.model.geometry.Position;
import dk.dma.enav.model.geometry.grid.Cell;
import dk.dma.enav.model.geometry.grid.Grid;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Collection;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class EventEmittingTrackerImplTest {

    private EventEmittingTrackerImpl tracker;
    private Grid grid;
    private AisPacket packet;
    private IVesselPositionMessage positionMessage;

    @Before
    public void setUp() {
        grid = mock(Grid.class);
        tracker = new EventEmittingTrackerImpl(grid);
        packet = mock(AisPacket.class);
        positionMessage = mock(IVesselPositionMessage.class);
    }

    @Test
    public void testUpdateWithValidPacket() {
        when(packet.getBestTimestamp()).thenReturn(System.currentTimeMillis());
        when(packet.tryGetAisMessage()).thenReturn(positionMessage);
        when(positionMessage.getUserId()).thenReturn(123456789);
        when(positionMessage.getTargetType()).thenReturn(AisTargetType.A);
        when(positionMessage.getPos()).thenReturn(mock(Position.class));

        tracker.update(packet);

        Target target = tracker.get(123456789);
        assertNotNull(target);
    }

    @Test
    public void testUpdateWithBlacklistedMMSI() {
        tracker = new EventEmittingTrackerImpl(grid, 123456789);
        when(packet.getBestTimestamp()).thenReturn(System.currentTimeMillis());
        when(packet.tryGetAisMessage()).thenReturn(positionMessage);
        when(positionMessage.getUserId()).thenReturn(123456789);
        when(positionMessage.getTargetType()).thenReturn(AisTargetType.A);

        tracker.update(packet);

        Target target = tracker.get(123456789);
        assertNull(target);
    }

    @Test
    public void testFirePositionChangedEvent() {
        when(packet.getBestTimestamp()).thenReturn(System.currentTimeMillis());
        when(packet.tryGetAisMessage()).thenReturn(positionMessage);
        when(positionMessage.getUserId()).thenReturn(123456789);
        when(positionMessage.getTargetType()).thenReturn(AisTargetType.A);
        when(positionMessage.getPos()).thenReturn(mock(Position.class));

        ArgumentCaptor<PositionChangedEvent> positionChangedEventCaptor = ArgumentCaptor.forClass(PositionChangedEvent.class);
        tracker.registerSubscriber(new Object() {
            @Subscribe
            public void handlePositionChanged(PositionChangedEvent event) {
                // Handle event
            }
        });

        tracker.update(packet);

        verify(tracker.getEventBus()).post(positionChangedEventCaptor.capture());
        assertNotNull(positionChangedEventCaptor.getValue());
    }

    @Test
    public void testGetTracks() {
        when(packet.getBestTimestamp()).thenReturn(System.currentTimeMillis());
        when(packet.tryGetAisMessage()).thenReturn(positionMessage);
        when(positionMessage.getUserId()).thenReturn(123456789);
        when(positionMessage.getTargetType()).thenReturn(AisTargetType.A);
        when(positionMessage.getPos()).thenReturn(mock(Position.class));

        tracker.update(packet);
        Collection<Target> tracks = tracker.getTracks();
        assertEquals(1, tracks.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, tracker.size());
        when(packet.getBestTimestamp()).thenReturn(System.currentTimeMillis());
        when(packet.tryGetAisMessage()).thenReturn(positionMessage);
        when(positionMessage.getUserId()).thenReturn(123456789);
        when(positionMessage.getTargetType()).thenReturn(AisTargetType.A);
        when(positionMessage.getPos()).thenReturn(mock(Position.class));

        tracker.update(packet);
        assertEquals(1, tracker.size());
    }

    @Test
    public void testTrackStaleEvent() {
        when(packet.getBestTimestamp()).thenReturn(System.currentTimeMillis());
        when(packet.tryGetAisMessage()).thenReturn(positionMessage);
        when(positionMessage.getUserId()).thenReturn(123456789);
        when(positionMessage.getTargetType()).thenReturn(AisTargetType.A);
        when(positionMessage.getPos()).thenReturn(mock(Position.class));

        tracker.update(packet);
        // Simulate a delay to make the track stale
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tracker.update(packet);
        // Verify that a TrackStaleEvent is posted
        // This would require a similar event capturing mechanism as above
    }
}
