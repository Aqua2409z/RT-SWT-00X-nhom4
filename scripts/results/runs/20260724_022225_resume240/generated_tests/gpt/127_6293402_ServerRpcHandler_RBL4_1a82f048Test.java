package com.vaadin.server.communication;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.communication.ServerRpcHandler;
import com.vaadin.shared.communication.MethodInvocation;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServerRpcHandler_RBL4_1a82f048Test {

    private ServerRpcHandler serverRpcHandler;
    private VaadinRequest mockRequest;
    private UI mockUI;

    @Before
    public void setUp() {
        serverRpcHandler = new ServerRpcHandler();
        mockRequest = mock(VaadinRequest.class);
        mockUI = mock(UI.class);
    }

    @Test
    public void testHandleRpcWithEmptyMessage() throws Exception {
        StringReader reader = new StringReader("");
        serverRpcHandler.handleRpc(mockUI, reader, mockRequest);
        // No exception should be thrown
    }

    @Test(expected = InvalidUIDLSecurityKeyException.class)
    public void testHandleRpcWithInvalidCsrfToken() throws Exception {
        String jsonString = "{\"" + ApplicationConstants.CSRF_TOKEN + "\":\"invalid\"}";
        when(mockRequest.getService().getDeploymentConfiguration()).thenReturn(mock(DeploymentConfiguration.class));
        when(mockRequest.getService().getDeploymentConfiguration().isSyncIdCheckEnabled()).thenReturn(true);
        when(mockRequest.getService().getSession()).thenReturn(mock(Session.class));
        when(mockRequest.getService().getSession().getCommunicationManager()).thenReturn(mock(CommunicationManager.class));
        
        StringReader reader = new StringReader(jsonString);
        serverRpcHandler.handleRpc(mockUI, reader, mockRequest);
    }

    @Test
    public void testHandleRpcWithValidMessage() throws Exception {
        String jsonString = "{\"" + ApplicationConstants.CSRF_TOKEN + "\":\"valid\",\"" + ApplicationConstants.RPC_INVOCATIONS + "\":[[\"connectorId\",\"interfaceName\",\"methodName\",[]]]}";
        StringReader reader = new StringReader(jsonString);
        serverRpcHandler.handleRpc(mockUI, reader, mockRequest);
        // No exception should be thrown
    }

    @Test
    public void testGetCsrfToken() {
        String jsonString = "{\"" + ApplicationConstants.CSRF_TOKEN + "\":\"tokenValue\"}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        assertEquals("tokenValue", rpcRequest.getCsrfToken());
    }

    @Test
    public void testGetRpcInvocationsData() {
        String jsonString = "{\"" + ApplicationConstants.RPC_INVOCATIONS + "\":[[\"connectorId\",\"interfaceName\",\"methodName\",[]]]}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        JsonArray invocationsData = rpcRequest.getRpcInvocationsData();
        assertEquals(1, invocationsData.length());
    }

    @Test
    public void testGetSyncId() {
        String jsonString = "{\"" + ApplicationConstants.SERVER_SYNC_ID + "\":123}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        assertEquals(123, rpcRequest.getSyncId());
    }

    @Test
    public void testIsResynchronize() {
        String jsonString = "{\"" + ApplicationConstants.RESYNCHRONIZE_ID + "\":true}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        assertTrue(rpcRequest.isResynchronize());
    }

    @Test
    public void testGetClientToServerId() {
        String jsonString = "{\"" + ApplicationConstants.CLIENT_TO_SERVER_ID + "\":456}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        assertEquals(456, rpcRequest.getClientToServerId());
    }

    @Test
    public void testGetRawJson() {
        String jsonString = "{\"key\":\"value\"}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        JsonObject rawJson = rpcRequest.getRawJson();
        assertEquals("value", rawJson.getString("key"));
    }

    @Test
    public void testGetWidgetsetVersion() {
        String jsonString = "{\"" + ApplicationConstants.WIDGETSET_VERSION_ID + "\":\"1.0\"}";
        RpcRequest rpcRequest = new ServerRpcHandler.RpcRequest(jsonString, mockRequest);
        assertEquals("1.0", rpcRequest.getWidgetsetVersion());
    }
}
