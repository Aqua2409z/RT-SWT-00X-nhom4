package dk.dma.ais.tracker.targetTracker;

import dk.dma.ais.message.AisMessage;
import dk.dma.ais.message.AisTargetType;
import dk.dma.ais.packet.AisPacket;
import dk.dma.ais.packet.AisPacketSource;
import dk.dma.ais.tracker.targetTracker.TargetInfo;
import dk.dma.ais.tracker.targetTracker.TargetTracker;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.*;

public class TargetTrackerTest {

    private TargetTracker targetTracker;

    @Before
    public void setUp() {
        targetTracker = new TargetTracker();
    }

    @Test
    public void testCount() {
        assertEquals(0, targetTracker.count(source -> true));
    }

    @Test
    public void testCountNumberOfReports() {
        assertEquals(0, targetTracker.countNumberOfReports());
    }

    @Test
    public void testGetNonExistentTarget() {
        assertNull(targetTracker.get(123456789));
    }

    @Test
    public void testGetPacketSourcesForMMSI() {
        Set<AisPacketSource> sources = targetTracker.getPacketSourcesForMMSI(123456789);
        assertTrue(sources.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, targetTracker.size());
    }

    @Test
    public void testUpdateAndGet() {
        AisPacket packet = createMockAisPacket(123456789, AisTargetType.AIS_TARGET_TYPE_A);
        targetTracker.update(packet);
        
        TargetInfo targetInfo = targetTracker.get(123456789);
        assertNotNull(targetInfo);
        assertEquals(123456789, targetInfo.getMmsi());
    }

    @Test
    public void testRemoveAll() {
        AisPacket packet = createMockAisPacket(123456789, AisTargetType.AIS_TARGET_TYPE_A);
        targetTracker.update(packet);
        
        targetTracker.removeAll(targetInfo -> targetInfo.getMmsi() == 123456789);
        assertNull(targetTracker.get(123456789));
    }

    @Test
    public void testStream() {
        AisPacket packet = createMockAisPacket(123456789, AisTargetType.AIS_TARGET_TYPE_A);
        targetTracker.update(packet);
        
        long count = targetTracker.stream().count();
        assertEquals(1, count);
    }

    private AisPacket createMockAisPacket(int mmsi, AisTargetType targetType) {
        AisPacket packet = new AisPacket() {
            @Override
            public AisMessage tryGetAisMessage() {
                return new AisMessage() {
                    @Override
                    public int getUserId() {
                        return mmsi;
                    }

                    @Override
                    public AisTargetType getTargetType() {
                        return targetType;
                    }
                };
            }

            @Override
            public Date getTimestamp() {
                return new Date();
            }
        };
        return packet;
    }
}
