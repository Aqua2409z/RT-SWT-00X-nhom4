
package com.spotify.flo.contrib.styx;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TerminationLogging_RBL4_bd50ba45Test {

    private TerminationLogging terminationLogging;
    private Map<String, String> originalEnv;

    @Before
    public void setUp() {
        terminationLogging = new TerminationLogging();
        originalEnv = new HashMap<>(System.getenv());
    }

    @After
    public void tearDown() {
        // Restore original environment variables if needed
    }

    @Test
    public void testAcceptWritesToLogFile() throws IOException {
        // Arrange
        String logFilePath = "termination_log.json";
        System.setProperty("STYX_TERMINATION_LOG", logFilePath);
        System.setProperty("STYX_COMPONENT_ID", "test_component");
        System.setProperty("STYX_WORKFLOW_ID", "test_workflow");
        System.setProperty("STYX_PARAMETER", "test_parameter");
        System.setProperty("STYX_EXECUTION_ID", "test_execution");

        // Act
        terminationLogging.accept(0);

        // Assert
        Path path = Paths.get(logFilePath);
        assertTrue(Files.exists(path));
        String content = new String(Files.readAllBytes(path));
        String expectedContent = "{\"component_id\": \"test_component\","
                + "\"workflow_id\": \"test_workflow\","
                + "\"parameter\": \"test_parameter\","
                + "\"execution_id\": \"test_execution\","
                + "\"event\": \"exited\","
                + "\"exit_code\": 0}";
        assertTrue(content.equals(expectedContent));

        // Clean up
        Files.delete(path);
    }

    @Test
    public void testAcceptDoesNotWriteWhenLogPathNotSet() {
        // Arrange
        System.clearProperty("STYX_TERMINATION_LOG");

        // Act
        terminationLogging.accept(0);

        // Assert
        // No exception should be thrown and nothing should be written
    }

    @Test(expected = RuntimeException.class)
    public void testAcceptThrowsExceptionOnIOException() throws IOException {
        // Arrange
        String logFilePath = "invalid_path/termination_log.json";
        System.setProperty("STYX_TERMINATION_LOG", logFilePath);

        // Act
        terminationLogging.accept(1);
    }
}
