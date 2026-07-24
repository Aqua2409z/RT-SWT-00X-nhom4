
package org.rf.ide.core.execution.agent.event;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rf.ide.core.execution.server.AgentClient;
import org.rf.ide.core.execution.server.response.ProtocolVersion;
import org.rf.ide.core.execution.server.response.ServerResponse.ResponseException;

class VersionsEvent_RBL4_b783240eTest {

    private AgentClient client;
    private Map<String, Object> eventMap;

    @BeforeEach
    void setUp() {
        client = mock(AgentClient.class);
        eventMap = new HashMap<>();
    }

    @Test
    void testFromValidInput() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("cmd_line", "python script.py");
        attributes.put("python", "3.8");
        attributes.put("robot", "4.0");
        attributes.put("protocol", 1);
        attributes.put("pid", 1234);

        eventMap.put("version", List.of(attributes));

        VersionsEvent event = VersionsEvent.from(client, eventMap);

        assertEquals("python script.py", event.getCommandLine());
        assertEquals("3.8", event.getPythonVersion());
        assertEquals("4.0", event.getRobotVersion());
        assertEquals(1, event.getProtocolVersion());
        assertEquals(Optional.of(1234L), event.getPid());
    }

    @Test
    void testFromMissingCommandLine() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("python", "3.8");
        attributes.put("robot", "4.0");
        attributes.put("protocol", 1);
        attributes.put("pid", 1234);

        eventMap.put("version", List.of(attributes));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            VersionsEvent.from(client, eventMap);
        });
        assertEquals("Versions event should have command line, versions of python, robot and protocol", exception.getMessage());
    }

    @Test
    void testFromMissingPythonVersion() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("cmd_line", "python script.py");
        attributes.put("robot", "4.0");
        attributes.put("protocol", 1);
        attributes.put("pid", 1234);

        eventMap.put("version", List.of(attributes));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            VersionsEvent.from(client, eventMap);
        });
        assertEquals("Versions event should have command line, versions of python, robot and protocol", exception.getMessage());
    }

    @Test
    void testFromMissingRobotVersion() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("cmd_line", "python script.py");
        attributes.put("python", "3.8");
        attributes.put("protocol", 1);
        attributes.put("pid", 1234);

        eventMap.put("version", List.of(attributes));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            VersionsEvent.from(client, eventMap);
        });
        assertEquals("Versions event should have command line, versions of python, robot and protocol", exception.getMessage());
    }

    @Test
    void testFromMissingProtocolVersion() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("cmd_line", "python script.py");
        attributes.put("python", "3.8");
        attributes.put("robot", "4.0");
        attributes.put("pid", 1234);

        eventMap.put("version", List.of(attributes));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            VersionsEvent.from(client, eventMap);
        });
        assertEquals("Versions event should have command line, versions of python, robot and protocol", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        Map<String, Object> attributes1 = new HashMap<>();
        attributes1.put("cmd_line", "python script.py");
        attributes1.put("python", "3.8");
        attributes1.put("robot", "4.0");
        attributes1.put("protocol", 1);
        attributes1.put("pid", 1234);

        eventMap.put("version", List.of(attributes1));
        VersionsEvent event1 = VersionsEvent.from(client, eventMap);

        Map<String, Object> attributes2 = new HashMap<>(attributes1);
        VersionsEvent event2 = VersionsEvent.from(client, Map.of("version", List.of(attributes2)));

        assertEquals(event1, event2);
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void testResponderVersionsCorrect() throws ResponseException {
        VersionsEvent event = new VersionsEvent(new VersionsEvent.VersionsEventResponder(client), "cmd", "3.8", "4.0", 1, Optional.of(1234L));
        event.responder().versionsCorrect();
        verify(client).send(any(ProtocolVersion.class));
    }

    @Test
    void testResponderVersionsError() throws ResponseException {
        VersionsEvent event = new VersionsEvent(new VersionsEvent.VersionsEventResponder(client), "cmd", "3.8", "4.0", 1, Optional.of(1234L));
        event.responder().versionsError("error");
        verify(client).send(any(ProtocolVersion.class));
    }
}
