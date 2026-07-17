
package org.confucius.commons.lang.io.scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.lang3.StringUtils;
import org.confucius.commons.lang.filter.JarEntryFilter;
import org.confucius.commons.lang.util.jar.JarUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SimpleJarEntryScannerTest {

    private SimpleJarEntryScanner scanner;

    @Before
    public void setUp() {
        scanner = SimpleJarEntryScanner.INSTANCE;
    }

    @Test(expected = NullPointerException.class)
    public void testScanWithNullURL() throws Exception {
        scanner.scan(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScanWithInvalidURL() throws Exception {
        URL invalidUrl = new URL("http://invalid-url");
        scanner.scan(invalidUrl, true);
    }

    @Test
    public void testScanWithValidURL() throws Exception {
        URL jarUrl = getClass().getResource("/test.jar"); // Assuming test.jar is in the resources
        Set<JarEntry> entries = scanner.scan(jarUrl, true);
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
    }

    @Test
    public void testScanWithFilter() throws Exception {
        URL jarUrl = getClass().getResource("/test.jar");
        JarEntryFilter filter = new JarEntryFilter() {
            @Override
            public boolean accept(JarEntry jarEntry) {
                return jarEntry.getName().endsWith(".class");
            }
        };
        Set<JarEntry> entries = scanner.scan(jarUrl, true, filter);
        assertNotNull(entries);
        for (JarEntry entry : entries) {
            assertTrue(entry.getName().endsWith(".class"));
        }
    }

    @Test(expected = NullPointerException.class)
    public void testScanWithNullJarFile() throws Exception {
        scanner.scan((JarFile) null, true);
    }

    @Test
    public void testScanJarFile() throws Exception {
        URL jarUrl = getClass().getResource("/test.jar");
        JarFile jarFile = JarUtils.toJarFile(jarUrl);
        Set<JarEntry> entries = scanner.scan(jarFile, true);
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
    }

    @Test
    public void testScanJarFileWithRecursive() throws Exception {
        URL jarUrl = getClass().getResource("/test.jar");
        JarFile jarFile = JarUtils.toJarFile(jarUrl);
        Set<JarEntry> entries = scanner.scan(jarFile, true);
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
    }

    @Test
    public void testScanJarFileWithNonRecursive() throws Exception {
        URL jarUrl = getClass().getResource("/test.jar");
        JarFile jarFile = JarUtils.toJarFile(jarUrl);
        Set<JarEntry> entries = scanner.scan(jarFile, false);
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
    }

    @Test
    public void testScanJarFileWithEmptyRelativePath() throws Exception {
        URL jarUrl = getClass().getResource("/test.jar");
        JarFile jarFile = JarUtils.toJarFile(jarUrl);
        Set<JarEntry> entries = scanner.scan(jarFile, StringUtils.EMPTY, true, null);
        assertNotNull(entries);
        assertFalse(entries.isEmpty());
    }
}
