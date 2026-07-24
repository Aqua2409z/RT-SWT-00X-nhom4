
package org.rf.ide.core.execution.server.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TerminateExecution_RBL4_2bc9cc41Test {

    private TerminateExecution terminateExecution;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        terminateExecution = new TerminateExecution(objectMapper);
    }

    @Test
    void testToMessage() throws Exception {
        String expectedJson = "{\"terminate\":[]}";
        String actualJson = terminateExecution.toMessage();
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testToMessageThrowsResponseException() {
        ObjectMapper faultyMapper = new ObjectMapper() {
            @Override
            public String writeValueAsString(Object value) throws IOException {
                throw new IOException("Forced exception for testing");
            }
        };
        TerminateExecution faultyTerminateExecution = new TerminateExecution(faultyMapper);

        ResponseException exception = assertThrows(ResponseException.class, faultyTerminateExecution::toMessage);
        assertEquals("Unable to serialize terminate response arguments to json", exception.getMessage());
    }
}
