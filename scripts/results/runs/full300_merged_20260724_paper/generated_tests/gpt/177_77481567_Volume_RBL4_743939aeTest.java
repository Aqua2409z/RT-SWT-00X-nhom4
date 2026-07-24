
package de.voidnode.trading4j.domain;

import org.junit.Test;
import static org.junit.Assert.*;

public class Volume_RBL4_743939aeTest {

    @Test
    public void testVolumeConstructionAndAsAbsolute() {
        VolumeUnit unit = VolumeUnit.BASE; // Assuming BASE is a valid unit
        Volume volume = new Volume(1000, unit);
        assertEquals(1000, volume.asAbsolute());
    }

    @Test
    public void testVolumeAsLot() {
        VolumeUnit unit = VolumeUnit.BASE; // Assuming BASE is a valid unit
        Volume volume = new Volume(1000000, unit);
        assertEquals(10.0, volume.asLot(), 0.00001);
    }

    @Test
    public void testVolumeEquality() {
        VolumeUnit unit = VolumeUnit.BASE; // Assuming BASE is a valid unit
        Volume volume1 = new Volume(1000, unit);
        Volume volume2 = new Volume(1000, unit);
        Volume volume3 = new Volume(2000, unit);
        
        assertTrue(volume1.equals(volume2));
        assertFalse(volume1.equals(volume3));
        assertFalse(volume1.equals(null));
        assertFalse(volume1.equals(new Object()));
    }

    @Test
    public void testVolumeHashCode() {
        VolumeUnit unit = VolumeUnit.BASE; // Assuming BASE is a valid unit
        Volume volume1 = new Volume(1000, unit);
        Volume volume2 = new Volume(1000, unit);
        Volume volume3 = new Volume(2000, unit);
        
        assertEquals(volume1.hashCode(), volume2.hashCode());
        assertNotEquals(volume1.hashCode(), volume3.hashCode());
    }

    @Test
    public void testVolumeToString() {
        VolumeUnit unit = VolumeUnit.BASE; // Assuming BASE is a valid unit
        Volume volume = new Volume(1000000, unit);
        assertEquals("10.0 LOT", volume.toString());
    }
}
