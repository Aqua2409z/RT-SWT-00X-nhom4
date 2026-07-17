
package org.confucius.commons.lang.io.scanner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class SimpleFileScannerTest {

    private SimpleFileScanner fileScanner;
    private File tempDir;

    @Before
    public void setUp() throws IOException {
        fileScanner = SimpleFileScanner.INSTANCE;
        tempDir = Files.createTempDirectory("testDir").toFile();
    }

    @Test
    public void testScanWithEmptyDirectory() {
        Set<File> result = fileScanner.scan(tempDir, false);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testScanWithSingleFile() throws IOException {
        File tempFile = new File(tempDir, "testFile.txt");
        tempFile.createNewFile();

        Set<File> result = fileScanner.scan(tempDir, false);
        assertEquals(1, result.size());
        assertTrue(result.contains(tempFile));
    }

    @Test
    public void testScanWithSubdirectory() throws IOException {
        File subDir = new File(tempDir, "subDir");
        subDir.mkdir();
        File tempFile = new File(subDir, "testFile.txt");
        tempFile.createNewFile();

        Set<File> result = fileScanner.scan(tempDir, false);
        assertEquals(1, result.size());
        assertTrue(result.contains(tempDir));
    }

    @Test
    public void testScanWithRecursive() throws IOException {
        File subDir = new File(tempDir, "subDir");
        subDir.mkdir();
        File tempFile = new File(subDir, "testFile.txt");
        tempFile.createNewFile();

        Set<File> result = fileScanner.scan(tempDir, true);
        assertEquals(2, result.size());
        assertTrue(result.contains(tempDir));
        assertTrue(result.contains(tempFile));
    }

    @Test
    public void testScanWithCustomFilter() throws IOException {
        IOFileFilter mockFilter = mock(IOFileFilter.class);
        when(mockFilter.accept(any(File.class))).thenReturn(true);

        File tempFile = new File(tempDir, "testFile.txt");
        tempFile.createNewFile();

        Set<File> result = fileScanner.scan(tempDir, false, mockFilter);
        assertEquals(1, result.size());
        assertTrue(result.contains(tempFile));
    }

    @Test
    public void testScanWithFilterRejectingFiles() throws IOException {
        IOFileFilter mockFilter = mock(IOFileFilter.class);
        when(mockFilter.accept(any(File.class))).thenReturn(false);

        File tempFile = new File(tempDir, "testFile.txt");
        tempFile.createNewFile();

        Set<File> result = fileScanner.scan(tempDir, false, mockFilter);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testScanWithNullDirectory() {
        try {
            fileScanner.scan(null, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @After
    public void tearDown() throws IOException {
        for (File file : tempDir.listFiles()) {
            file.delete();
        }
        tempDir.delete();
    }
}
