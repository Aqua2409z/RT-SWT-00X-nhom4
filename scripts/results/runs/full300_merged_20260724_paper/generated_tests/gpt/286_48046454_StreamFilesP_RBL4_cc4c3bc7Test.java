package com.hazelcast.jet.impl.connector;

import com.hazelcast.function.BiFunctionEx;
import com.hazelcast.jet.JetException;
import com.hazelcast.jet.core.ProcessorMetaSupplier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class StreamFilesP_RBL4_cc4c3bc7Test {

    private StreamFilesP<String> processor;
    private Path testDirectory;
    private final String testDirPath = "testDir";
    private final String testFileName = "testFile.txt";

    @Before
    public void setUp() throws Exception {
        testDirectory = Paths.get(testDirPath);
        Files.createDirectories(testDirectory);
        processor = new StreamFilesP<>(testDirPath, StandardCharsets.UTF_8, "*.txt", false, (fileName, line) -> line);
        processor.init(new TestContext());
    }

    @After
    public void tearDown() throws Exception {
        processor.close();
        Files.walk(testDirectory)
                .map(Path::toFile)
                .forEach(File::delete);
        Files.deleteIfExists(testDirectory);
    }

    @Test
    public void testFileCreation() throws IOException {
        createTestFile("Hello World\nThis is a test line.\n");
        assertTrue(processor.complete());
    }

    @Test
    public void testFileModification() throws IOException {
        createTestFile("Initial line.\n");
        processor.complete();
        modifyTestFile("Modified line.\n");
        assertTrue(processor.complete());
    }

    @Test
    public void testFileDeletion() throws IOException {
        createTestFile("Line to be deleted.\n");
        processor.complete();
        deleteTestFile();
        assertTrue(processor.complete());
    }

    @Test(expected = JetException.class)
    public void testInvalidFilePath() throws IOException {
        processor = new StreamFilesP<>("invalidPath", StandardCharsets.UTF_8, "*.txt", false, (fileName, line) -> line);
        processor.init(new TestContext());
        assertFalse(processor.complete());
    }

    private void createTestFile(String content) throws IOException {
        File testFile = new File(testDirectory.toFile(), testFileName);
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(content);
        }
    }

    private void modifyTestFile(String content) throws IOException {
        File testFile = new File(testDirectory.toFile(), testFileName);
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write(content);
        }
    }

    private void deleteTestFile() {
        File testFile = new File(testDirectory.toFile(), testFileName);
        testFile.delete();
    }

    private static class StreamFilesP_RBL4_cc4c3bc7Test implements StreamFilesP.Context {
        @Override
        public int globalProcessorIndex() {
            return 0;
        }

        @Override
        public int localProcessorIndex() {
            return 0;
        }

        @Override
        public int totalParallelism() {
            return 1;
        }

        @Override
        public int localParallelism() {
            return 1;
        }
    }
}
