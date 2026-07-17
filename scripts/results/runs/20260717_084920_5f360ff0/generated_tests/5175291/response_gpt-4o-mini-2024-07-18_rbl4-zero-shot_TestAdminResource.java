
package io.airlift.airship.coordinator;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static javax.ws.rs.core.Response.Status.*;

import com.google.common.base.Predicate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

public class TestAdminResource {

    @Mock
    private Coordinator coordinator;

    @Mock
    private Repository repository;

    @Mock
    private UriInfo uriInfo;

    private AdminResource adminResource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        adminResource = new AdminResource(coordinator, repository);
    }

    @Test
    public void testGetAllCoordinators() {
        List<CoordinatorStatus> mockCoordinators = new ArrayList<>();
        when(coordinator.getCoordinators(any(Predicate.class))).thenReturn(mockCoordinators);
        
        Response response = adminResource.getAllCoordinators(uriInfo);
        
        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(coordinator).getCoordinators(any(Predicate.class));
    }

    @Test
    public void testProvisionCoordinator() throws Exception {
        CoordinatorProvisioningRepresentation provisioning = mock(CoordinatorProvisioningRepresentation.class);
        List<CoordinatorStatus> mockCoordinators = new ArrayList<>();
        when(coordinator.provisionCoordinators(any(), anyInt(), any(), any(), any(), any(), any(), any())).thenReturn(mockCoordinators);
        
        Response response = adminResource.provisionCoordinator(provisioning, uriInfo);
        
        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(coordinator).provisionCoordinators(any(), anyInt(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testGetAllAgents() {
        List<SlotStatus> mockSlotStatus = new ArrayList<>();
        when(coordinator.getAllSlotStatus()).thenReturn(mockSlotStatus);
        List<AgentStatus> mockAgents = new ArrayList<>();
        when(coordinator.getAgents(any(Predicate.class))).thenReturn(mockAgents);
        
        Response response = adminResource.getAllAgents(uriInfo);
        
        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(coordinator).getAllSlotStatus();
        verify(coordinator).getAgents(any(Predicate.class));
    }

    @Test
    public void testProvisionAgent() throws Exception {
        AgentProvisioningRepresentation provisioning = mock(AgentProvisioningRepresentation.class);
        List<AgentStatus> mockAgents = new ArrayList<>();
        when(coordinator.provisionAgents(any(), anyInt(), any(), any(), any(), any(), any(), any())).thenReturn(mockAgents);
        
        Response response = adminResource.provisionAgent(provisioning, uriInfo);
        
        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(coordinator).provisionAgents(any(), anyInt(), any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testTerminateAgent_Success() {
        String agentId = "test-agent-id";
        when(coordinator.terminateAgent(agentId)).thenReturn(new Object());
        
        Response response = adminResource.terminateAgent(agentId, uriInfo);
        
        assertEquals(OK.getStatusCode(), response.getStatus());
        verify(coordinator).terminateAgent(agentId);
    }

    @Test
    public void testTerminateAgent_NotFound() {
        String agentId = "test-agent-id";
        when(coordinator.terminateAgent(agentId)).thenReturn(null);
        
        Response response = adminResource.terminateAgent(agentId, uriInfo);
        
        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
        verify(coordinator).terminateAgent(agentId);
    }
}
