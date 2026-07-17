package io.airlift.airship.shared;

import org.junit.Test;
import static org.junit.Assert.*;

public class TestMavenCoordinates {

    @Test
    public void testFromConfigGAV() {
        MavenCoordinates coordinates = MavenCoordinates.fromConfigGAV("@groupId:artifactId:version");
        assertNotNull(coordinates);
        assertEquals("groupId", coordinates.getGroupId());
        assertEquals("artifactId", coordinates.getArtifactId());
        assertEquals("version", coordinates.getVersion());
        assertEquals("config", coordinates.getPackaging());
        
        assertNull(MavenCoordinates.fromConfigGAV("groupId:artifactId:version"));
    }

    @Test
    public void testFromBinaryGAV() {
        MavenCoordinates coordinates = MavenCoordinates.fromBinaryGAV("groupId:artifactId:version");
        assertNotNull(coordinates);
        assertEquals("groupId", coordinates.getGroupId());
        assertEquals("artifactId", coordinates.getArtifactId());
        assertEquals("version", coordinates.getVersion());
        assertEquals("tar.gz", coordinates.getPackaging());
    }

    @Test
    public void testFromGAV() {
        MavenCoordinates coordinates = MavenCoordinates.fromGAV("groupId:artifactId:version:packaging:classifier");
        assertNotNull(coordinates);
        assertEquals("groupId", coordinates.getGroupId());
        assertEquals("artifactId", coordinates.getArtifactId());
        assertEquals("version", coordinates.getVersion());
        assertEquals("packaging", coordinates.getPackaging());
        assertEquals("classifier", coordinates.getClassifier());

        assertNull(MavenCoordinates.fromGAV("invalid:coordinates"));
    }

    @Test
    public void testToConfigGAV() {
        MavenCoordinates coordinates = new MavenCoordinates("groupId", "artifactId", "version", "config", null, null);
        String configGAV = MavenCoordinates.toConfigGAV(coordinates);
        assertEquals("@groupId:artifactId:version:config", configGAV);
    }

    @Test
    public void testToBinaryGAV() {
        MavenCoordinates coordinates = new MavenCoordinates("groupId", "artifactId", "version", "tar.gz", null, null);
        String binaryGAV = MavenCoordinates.toBinaryGAV(coordinates);
        assertEquals("groupId:artifactId:version:tar.gz", binaryGAV);
    }

    @Test
    public void testToGAV() {
        MavenCoordinates coordinates = new MavenCoordinates("groupId", "artifactId", "version", "packaging", "classifier", "fileVersion");
        String gav = coordinates.toGAV(null, true);
        assertEquals("groupId:artifactId:packaging:classifier:version(fileVersion)", gav);
        
        gav = coordinates.toGAV("packaging", false);
        assertEquals("groupId:artifactId:classifier:fileVersion", gav);
    }

    @Test
    public void testEqualsIgnoreVersion() {
        MavenCoordinates coordinates1 = new MavenCoordinates("groupId", "artifactId", "version1", "packaging", "classifier", "fileVersion");
        MavenCoordinates coordinates2 = new MavenCoordinates("groupId", "artifactId", "version2", "packaging", "classifier", "fileVersion");
        assertTrue(coordinates1.equalsIgnoreVersion(coordinates2));
        
        coordinates2 = new MavenCoordinates("groupId", "artifactId", "version2", "differentPackaging", "classifier", "fileVersion");
        assertFalse(coordinates1.equalsIgnoreVersion(coordinates2));
    }

    @Test
    public void testIsResolved() {
        MavenCoordinates coordinates = new MavenCoordinates("groupId", "artifactId", "version", "packaging", "classifier", "fileVersion");
        assertTrue(coordinates.isResolved());

        coordinates = new MavenCoordinates(null, "artifactId", "version", "packaging", "classifier", null);
        assertFalse(coordinates.isResolved());
    }

    @Test
    public void testEqualsAndHashCode() {
        MavenCoordinates coordinates1 = new MavenCoordinates("groupId", "artifactId", "version", "packaging", "classifier", "fileVersion");
        MavenCoordinates coordinates2 = new MavenCoordinates("groupId", "artifactId", "version", "packaging", "classifier", "fileVersion");
        assertEquals(coordinates1, coordinates2);
        assertEquals(coordinates1.hashCode(), coordinates2.hashCode());

        coordinates2 = new MavenCoordinates("groupId", "artifactId", "differentVersion", "packaging", "classifier", "fileVersion");
        assertNotEquals(coordinates1, coordinates2);
    }

    @Test
    public void testToString() {
        MavenCoordinates coordinates = new MavenCoordinates("groupId", "artifactId", "version", "packaging", "classifier", "fileVersion");
        assertEquals("groupId:artifactId:packaging:classifier:version(fileVersion)", coordinates.toString());
    }
}
