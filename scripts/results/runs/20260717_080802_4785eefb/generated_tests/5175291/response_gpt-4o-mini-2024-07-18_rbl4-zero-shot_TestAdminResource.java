
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
import java.util.Arrays;
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
        CoordinatorStatus coordinatorStatus1 = mock(CoordinatorStatus.class);
        CoordinatorStatus coordinatorStatus2 = mock(CoordinatorStatus.class);
        when(coordinator.getCoordinators(any(Predicate.class))).thenReturn(Arrays.asList(coordinatorStatus1, coordinatorStatus2));
        
        Response response = adminResource.getAllCoordinators(uriInfo);
        
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testProvisionCoordinator() throws Exception {
        CoordinatorProvisioningRepresentation provisioning = mock(CoordinatorProvisioningRepresentation.class);
        when(provisioning.getCoordinatorConfig()).thenReturn("config");
        when(provisioning.getCoordinatorCount()).thenReturn(1);
        when(coordinator.provisionCoordinators(any(), anyInt(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(mock(CoordinatorStatus.class)));

        Response response = adminResource.provisionCoordinator(provisioning, uriInfo);
        
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testGetAllAgents() {
        AgentStatus agentStatus1 = mock(AgentStatus.class);
        AgentStatus agentStatus2 = mock(AgentStatus.class);
        when(coordinator.getAllSlotStatus()).thenReturn(Arrays.asList(mock(SlotStatus.class)));
        when(coordinator.getAgents(any(Predicate.class))).thenReturn(Arrays.asList(agentStatus1, agentStatus2));
        
        Response response = adminResource.getAllAgents(uriInfo);
        
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
        assertTrue(response.getHeaders().containsKey("Airship-Agents-Version"));
    }

    @Test
    public void testProvisionAgent() throws Exception {
        AgentProvisioningRepresentation provisioning = mock(AgentProvisioningRepresentation.class);
        when(provisioning.getAgentConfig()).thenReturn("config");
        when(provisioning.getAgentCount()).thenReturn(1);
        when(coordinator.provisionAgents(any(), anyInt(), any(), any(), any(), any(), any(), any()))
                .thenReturn(Arrays.asList(mock(AgentStatus.class)));

        Response response = adminResource.provisionAgent(provisioning, uriInfo);
        
        assertEquals(200, response.getStatus());
        assertNotNull(response.getEntity());
    }

    @Test
    public void testTerminateAgent() {
        String agentId = "test-agent-id";
        when(coordinator.terminateAgent(agentId)).thenReturn(mock(AgentStatus.class));

        Response response = adminResource.terminateAgent(agentId, uriInfo);
        
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testTerminateAgentNotFound() {
        String agentId = "non-existent-agent-id";
        when(coordinator.terminateAgent(agentId)).thenReturn(null);

        Response response = adminResource.terminateAgent(agentId, uriInfo);
        
        assertEquals(NOT_FOUND.getStatusCode(), response.getStatus());
    }
}
