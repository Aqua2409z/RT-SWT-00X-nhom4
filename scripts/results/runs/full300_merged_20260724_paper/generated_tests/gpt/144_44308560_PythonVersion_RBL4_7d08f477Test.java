
package org.rf.ide.core.environment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PythonVersion_RBL4_7d08f477Test {

    @Test
    void testFromValidVersion() {
        PythonVersion version = PythonVersion.from("(Python 3.8.5) on linux");
        assertEquals(3, version.major);
        assertEquals(8, version.minor);
        assertEquals(5, version.micro);
    }

    @Test
    void testFromInvalidVersion() {
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            PythonVersion.from("Invalid version string");
        });
        assertEquals("Unable to recognize Python version number", exception.getMessage());
    }

    @Test
    void testIsDeprecatedForPython2() {
        PythonVersion version = new PythonVersion(2, 6, 0);
        assertTrue(version.isDeprecated());
    }

    @Test
    void testIsDeprecatedForPython3() {
        PythonVersion version = new PythonVersion(3, 3, 0);
        assertTrue(version.isDeprecated());
    }

    @Test
    void testIsNotDeprecatedForPython2() {
        PythonVersion version = new PythonVersion(2, 7, 0);
        assertFalse(version.isDeprecated());
    }

    @Test
    void testIsNotDeprecatedForPython3() {
        PythonVersion version = new PythonVersion(3, 4, 0);
        assertFalse(version.isDeprecated());
    }

    @Test
    void testAsString() {
        PythonVersion version = new PythonVersion(3, 8, 5);
        assertEquals("3.8.5", version.asString());
    }

    @Test
    void testEqualsSameVersion() {
        PythonVersion version1 = new PythonVersion(3, 8, 5);
        PythonVersion version2 = new PythonVersion(3, 8, 5);
        assertEquals(version1, version2);
    }

    @Test
    void testEqualsDifferentVersion() {
        PythonVersion version1 = new PythonVersion(3, 8, 5);
        PythonVersion version2 = new PythonVersion(3, 7, 9);
        assertNotEquals(version1, version2);
    }

    @Test
    void testHashCode() {
        PythonVersion version1 = new PythonVersion(3, 8, 5);
        PythonVersion version2 = new PythonVersion(3, 8, 5);
        assertEquals(version1.hashCode(), version2.hashCode());
    }

    @Test
    void testToString() {
        PythonVersion version = new PythonVersion(3, 8, 5);
        assertEquals("org.rf.ide.core.environment.PythonVersion[major=3, minor=8, micro=5]", version.toString());
    }
}
