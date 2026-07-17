package io.airlift.airship.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestMavenCoordinates {

    @Test
    public void testFromConfigGAV() {
        MavenCoordinates coordinates = MavenCoordinates.fromConfigGAV("@com.example:artifact:1.0.0");
        assertNotNull(coordinates);
        assertEquals("com.example", coordinates.getGroupId());
        assertEquals("artifact", coordinates.getArtifactId());
        assertEquals("1.0.0", coordinates.getVersion());
        assertEquals("config", coordinates.getPackaging());
    }

    @Test
    public void testFromBinaryGAV() {
        MavenCoordinates coordinates = MavenCoordinates.fromBinaryGAV("com.example:artifact:1.0.0");
        assertNotNull(coordinates);
        assertEquals("com.example", coordinates.getGroupId());
        assertEquals("artifact", coordinates.getArtifactId());
        assertEquals("1.0.0", coordinates.getVersion());
        assertEquals("tar.gz", coordinates.getPackaging());
    }

    @Test
    public void testFromGAV() {
        MavenCoordinates coordinates = MavenCoordinates.fromGAV("com.example:artifact:1.0.0:jar:classifier");
        assertNotNull(coordinates);
        assertEquals("com.example", coordinates.getGroupId());
        assertEquals("artifact", coordinates.getArtifactId());
        assertEquals("1.0.0", coordinates.getVersion());
        assertEquals("jar", coordinates.getPackaging());
        assertEquals("classifier", coordinates.getClassifier());
    }

    @Test
    public void testToConfigGAV() {
        MavenCoordinates coordinates = new MavenCoordinates("com.example", "artifact", "1.0.0", "config", null, null);
        String gav = MavenCoordinates.toConfigGAV(coordinates);
        assertEquals("@com.example:artifact:1.0.0", gav);
    }

    @Test
    public void testToBinaryGAV() {
        MavenCoordinates coordinates = new MavenCoordinates("com.example", "artifact", "1.0.0", "tar.gz", null, null);
        String gav = MavenCoordinates.toBinaryGAV(coordinates);
        assertEquals("com.example:artifact:1.0.0", gav);
    }

    @Test
    public void testToGAV() {
        MavenCoordinates coordinates = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        String gav = coordinates.toGAV(null, true);
        assertEquals("com.example:artifact:jar:classifier:1.0.0(file-1.0.0)", gav);
    }

    @Test
    public void testIsResolved() {
        MavenCoordinates coordinates = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        assertTrue(coordinates.isResolved());

        MavenCoordinates unresolved = new MavenCoordinates(null, "artifact", "1.0.0", "jar", null, null);
        assertFalse(unresolved.isResolved());
    }

    @Test
    public void testEqualsIgnoreVersion() {
        MavenCoordinates coordinates1 = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        MavenCoordinates coordinates2 = new MavenCoordinates("com.example", "artifact", "2.0.0", "jar", "classifier", "file-2.0.0");
        assertTrue(coordinates1.equalsIgnoreVersion(coordinates2));
    }

    @Test
    public void testEquals() {
        MavenCoordinates coordinates1 = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        MavenCoordinates coordinates2 = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        assertTrue(coordinates1.equals(coordinates2));
    }

    @Test
    public void testHashCode() {
        MavenCoordinates coordinates1 = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        MavenCoordinates coordinates2 = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        assertEquals(coordinates1.hashCode(), coordinates2.hashCode());
    }

    @Test
    public void testToString() {
        MavenCoordinates coordinates = new MavenCoordinates("com.example", "artifact", "1.0.0", "jar", "classifier", "file-1.0.0");
        assertEquals("com.example:artifact:jar:classifier:1.0.0(file-1.0.0)", coordinates.toString());
    }
}
