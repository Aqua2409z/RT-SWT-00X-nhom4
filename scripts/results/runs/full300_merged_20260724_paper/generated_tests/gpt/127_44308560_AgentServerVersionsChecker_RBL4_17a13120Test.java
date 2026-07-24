
package org.rf.ide.core.execution.server;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rf.ide.core.execution.agent.event.VersionsEvent;
import org.rf.ide.core.execution.server.response.ServerResponse.ResponseException;

class AgentServerVersionsChecker_RBL4_17a13120Test {

    private AgentServerVersionsChecker checker;

    @Mock
    private VersionsEvent event;

    @Mock
    private VersionsEvent.Responder responder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        checker = new AgentServerVersionsChecker();
        when(event.responder()).thenReturn(responder);
    }

    @Test
    void testHandleVersions_withCorrectVersions() {
        when(event.getPythonVersion()).thenReturn("3.8");
        when(event.getRobotVersion()).thenReturn("4.0");
        when(event.getProtocolVersion()).thenReturn(AgentConnectionServer.RED_AGENT_PROTOCOL_VERSION);

        checker.handleVersions(event);

        verify(responder).versionsCorrect();
        verify(responder, never()).versionsError(anyString());
    }

    @Test
    void testHandleVersions_withProtocolMismatch() {
        when(event.getPythonVersion()).thenReturn("3.8");
        when(event.getRobotVersion()).thenReturn("4.0");
        when(event.getProtocolVersion()).thenReturn(999); // Invalid protocol version

        Exception exception = assertThrows(RobotAgentEventsListenerException.class, () -> {
            checker.handleVersions(event);
        });

        verify(responder).versionsError("RED & Agent protocol mismatch.\n" +
                "\tRED version: " + AgentConnectionServer.RED_AGENT_PROTOCOL_VERSION + "\n" +
                "\tAgent version: 999");
        assertEquals("RED & Agent protocol mismatch.\n" +
                "\tRED version: " + AgentConnectionServer.RED_AGENT_PROTOCOL_VERSION + "\n" +
                "\tAgent version: 999", exception.getMessage());
    }

    @Test
    void testHandleVersions_withResponseException() {
        when(event.getPythonVersion()).thenReturn("3.8");
        when(event.getRobotVersion()).thenReturn("4.0");
        when(event.getProtocolVersion()).thenReturn(AgentConnectionServer.RED_AGENT_PROTOCOL_VERSION);
        doThrow(new ResponseException("Test exception")).when(responder).versionsCorrect();

        Exception exception = assertThrows(RobotAgentEventsListenerException.class, () -> {
            checker.handleVersions(event);
        });

        assertEquals("Unable to send response to client", exception.getMessage());
        assertNotNull(exception.getCause());
        assertEquals("Test exception", exception.getCause().getMessage());
    }
}
