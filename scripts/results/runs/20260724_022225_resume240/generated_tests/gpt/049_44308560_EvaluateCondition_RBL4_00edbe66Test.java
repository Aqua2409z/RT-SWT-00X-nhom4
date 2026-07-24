
package org.rf.ide.core.execution.server.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluateCondition_RBL4_00edbe66Test {

    private ObjectMapper objectMapper;
    private EvaluateCondition evaluateCondition;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void testToMessageWithValidCondition() throws IOException {
        evaluateCondition = new EvaluateCondition(Arrays.asList("condition1", "arg1", "arg2"));
        String expectedJson = "{\"evaluate_condition\":[\"condition1\",\"arg1\",\"arg2\"]}";
        assertEquals(expectedJson, evaluateCondition.toMessage());
    }

    @Test
    void testToMessageWithEmptyCondition() throws IOException {
        evaluateCondition = new EvaluateCondition(Collections.emptyList());
        String expectedJson = "{\"evaluate_condition\":[]}";
        assertEquals(expectedJson, evaluateCondition.toMessage());
    }

    @Test
    void testToMessageThrowsResponseExceptionOnIOException() {
        // Mocking ObjectMapper to throw IOException
        ObjectMapper mockMapper = new ObjectMapper() {
            @Override
            public String writeValueAsString(Object value) throws IOException {
                throw new IOException("Mocked IOException");
            }
        };

        evaluateCondition = new EvaluateCondition(mockMapper, Arrays.asList("condition1"));
        ResponseException exception = assertThrows(ResponseException.class, evaluateCondition::toMessage);
        assertEquals("Unable to serialize breakpoint condition response arguments to json", exception.getMessage());
    }
}
