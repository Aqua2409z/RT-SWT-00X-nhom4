
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
        String content = argumentsFile.generateContent();
        assertEquals("arg1 value1\narg2 value2", content.trim());
    }

    @Test
    void testWriteToTemporaryOrUseAlreadyExisting() throws IOException {
        argumentsFile.addLine("arg1", "value1");
        File file = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertTrue(file.exists());

        // Write again to check if it returns the same file
        File sameFile = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertEquals(file.getName(), sameFile.getName());
    }

    @Test
    void testWriteToTemporaryCreatesFile() throws IOException {
        argumentsFile.addLine("arg1", "value1");
        File file = argumentsFile.writeToTemporaryOrUseAlreadyExisting();
        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    void testGenerateContentWithEmptyArguments() {
        String content = argumentsFile.generateContent();
        assertEquals("", content);
    }
}
