
package org.rf.ide.core.execution.agent.event;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.rf.ide.core.execution.agent.Status;

class SuiteEndedEvent_RBL4_7aed3afcTest {

    @Test
    void testFromValidEventMap() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("end_suite", List.of("SuiteName", Map.of(
                "elapsedtime", 100,
                "message", "No errors",
                "status", "SUCCESS"
        )));

        SuiteEndedEvent event = SuiteEndedEvent.from(eventMap);

        assertEquals("SuiteName", event.getName());
        assertEquals(100, event.getElapsedTime());
        assertEquals(Status.SUCCESS, event.getStatus());
        assertEquals("No errors", event.getErrorMessage());
    }

    @Test
    void testFromInvalidEventMapMissingElapsedTime() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("end_suite", List.of("SuiteName", Map.of(
                "message", "No errors",
                "status", "SUCCESS"
        )));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SuiteEndedEvent.from(eventMap);
        });
        assertEquals("Suite ended event should have status, elapsed time and message attributes", exception.getMessage());
    }

    @Test
    void testFromInvalidEventMapMissingMessage() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("end_suite", List.of("SuiteName", Map.of(
                "elapsedtime", 100,
                "status", "SUCCESS"
        )));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SuiteEndedEvent.from(eventMap);
        });
        assertEquals("Suite ended event should have status, elapsed time and message attributes", exception.getMessage());
    }

    @Test
    void testFromInvalidEventMapMissingStatus() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("end_suite", List.of("SuiteName", Map.of(
                "elapsedtime", 100,
                "message", "No errors"
        )));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            SuiteEndedEvent.from(eventMap);
        });
        assertEquals("Suite ended event should have status, elapsed time and message attributes", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        SuiteEndedEvent event1 = new SuiteEndedEvent("SuiteName", 100, Status.SUCCESS, "No errors");
        SuiteEndedEvent event2 = new SuiteEndedEvent("SuiteName", 100, Status.SUCCESS, "No errors");
        SuiteEndedEvent event3 = new SuiteEndedEvent("AnotherSuite", 200, Status.FAILURE, "Some errors");

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }
}
