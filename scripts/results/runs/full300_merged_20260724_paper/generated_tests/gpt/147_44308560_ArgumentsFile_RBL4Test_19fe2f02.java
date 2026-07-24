
package org.rf.ide.core.execution;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArgumentsFile_RBL4Test_19fe2f02 {

    private ArgumentsFile argumentsFile;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        argumentsFile = new ArgumentsFile();
        tempDir = Files.createTempDirectory("argsFileTest");
        System.setProperty("java.io.tmpdir", tempDir.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        for (File file : tempDir.toFile().listFiles()) {
            file.delete();
        }
        tempDir.toFile().delete();
    }

    @Test
    void testAddLineWithoutValue() {
        argumentsFile.addLine("arg1");
        String content = argumentsFile.generateContent();
        assertEquals("arg1", content.trim());
    }

    @Test
    void testAddLineWithValue() {
        argumentsFile.addLine("arg1", "value1");
        String content = argumentsFile.generateContent();
        assertEquals("arg1 value1", content.trim());
    }

    @Test
    void testAddCommentLine() {
        argumentsFile.addCommentLine("This is a comment");
        String content = argumentsFile.generateContent();
        assertEquals("# This is a comment", content.trim());
    }

    @Test
    void testGenerateContentWithMultipleLines() {
        argumentsFile.addLine("arg1", "value1");
        argumentsFile.addLine("arg2", "value2");
        argumentsFile.addCommentLine("This is a comment");
        String content = argumentsFile.generateContent();
        assertTrue(content.contains("arg1 value1"));
        assertTrue(content.contains("arg2 value2"));
        assertTrue(content.contains("# This is a comment"));
    }

    @Test
    void testWriteToTemporaryOrUseAlreadyExisting() throws IOException {
        argumentsFile.addLine("arg1", "value1");
        File file = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertTrue(file.exists());
        assertEquals("args_00000000000000000000000000000000.arg", file.getName());

        // Write again to check if it uses the existing file
        File existingFile = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertEquals(file, existingFile);
    }

    @Test
    void testWriteToTemporaryCreatesNewFile() throws IOException {
        argumentsFile.addLine("arg1", "value1");
        File file1 = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertTrue(file1.exists());

        // Change content to create a new file
        argumentsFile.addLine("arg2", "value2");
        File file2 = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertTrue(file2.exists());
        assertNotEquals(file1, file2);
    }

    @Test
    void testGenerateContentEmpty() {
        String content = argumentsFile.generateContent();
        assertEquals("", content);
    }
}
