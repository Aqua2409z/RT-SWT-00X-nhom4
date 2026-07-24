package com.vaadin.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.portlet.ClientDataRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;

import org.junit.Before;
import org.junit.Test;

public class VaadinPortletRequest_RBL4_5e6ec39eTest {

    private VaadinPortletRequest vaadinPortletRequest;
    private PortletRequest mockRequest;
    private VaadinPortletService mockService;

    @Before
    public void setUp() {
        mockRequest = mock(PortletRequest.class);
        mockService = mock(VaadinPortletService.class);
        vaadinPortletRequest = new VaadinPortletRequest(mockRequest, mockService);
    }

    @Test
    public void testGetPortletRequest() {
        assertEquals(mockRequest, vaadinPortletRequest.getPortletRequest());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetContentLength_NotClientDataRequest() {
        when(mockRequest instanceof ClientDataRequest).thenReturn(false);
        vaadinPortletRequest.getContentLength();
    }

    @Test
    public void testGetContentLength_ClientDataRequest() {
        ClientDataRequest mockClientDataRequest = mock(ClientDataRequest.class);
        when(mockRequest instanceof ClientDataRequest).thenReturn(true);
        when(mockRequest).thenReturn(mockClientDataRequest);
        when(mockClientDataRequest.getContentLength()).thenReturn(100);
        
        assertEquals(100, vaadinPortletRequest.getContentLength());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetInputStream_NotClientDataRequest() throws IOException {
        when(mockRequest instanceof ClientDataRequest).thenReturn(false);
        vaadinPortletRequest.getInputStream();
    }

    @Test
    public void testGetInputStream_ClientDataRequest() throws IOException {
        ClientDataRequest mockClientDataRequest = mock(ClientDataRequest.class);
        InputStream mockInputStream = mock(InputStream.class);
        when(mockRequest instanceof ClientDataRequest).thenReturn(true);
        when(mockRequest).thenReturn(mockClientDataRequest);
        when(mockClientDataRequest.getPortletInputStream()).thenReturn(mockInputStream);
        
        assertEquals(mockInputStream, vaadinPortletRequest.getInputStream());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetReader_NotClientDataRequest() throws IOException {
        when(mockRequest instanceof ClientDataRequest).thenReturn(false);
        vaadinPortletRequest.getReader();
    }

    @Test
    public void testGetReader_ClientDataRequest() throws IOException {
        ClientDataRequest mockClientDataRequest = mock(ClientDataRequest.class);
        BufferedReader mockReader = mock(BufferedReader.class);
        when(mockRequest instanceof ClientDataRequest).thenReturn(true);
        when(mockRequest).thenReturn(mockClientDataRequest);
        when(mockClientDataRequest.getReader()).thenReturn(mockReader);
        
        assertEquals(mockReader, vaadinPortletRequest.getReader());
    }

    @Test
    public void testGetPathInfo_ResourceRequest() {
        ResourceRequest mockResourceRequest = mock(ResourceRequest.class);
        when(mockRequest instanceof ResourceRequest).thenReturn(true);
        when(mockRequest).thenReturn(mockResourceRequest);
        when(mockResourceRequest.getResourceID()).thenReturn("resourceID");
        when(mockResourceRequest.getParameter(ApplicationConstants.V_RESOURCE_PATH)).thenReturn("pathInfo");
        
        assertEquals("pathInfo", vaadinPortletRequest.getPathInfo());
    }

    @Test
    public void testGetWrappedSession() {
        PortletSession mockSession = mock(PortletSession.class);
        when(mockRequest.getPortletSession(true)).thenReturn(mockSession);
        
        assertNotNull(vaadinPortletRequest.getWrappedSession());
    }

    @Test
    public void testGetPortalProperty() {
        when(mockRequest.getPortalContext().getProperty("propertyName")).thenReturn("propertyValue");
        
        assertEquals("propertyValue", vaadinPortletRequest.getPortalProperty("propertyName"));
    }

    @Test
    public void testGetPortletPreference() {
        PortletPreferences mockPreferences = mock(PortletPreferences.class);
        when(mockRequest.getPreferences()).thenReturn(mockPreferences);
        when(mockPreferences.getValue("preferenceName", null)).thenReturn("preferenceValue");
        
        assertEquals("preferenceValue", vaadinPortletRequest.getPortletPreference("preferenceName"));
    }

    @Test
    public void testGetService() {
        assertEquals(mockService, vaadinPortletRequest.getService());
    }

    @Test
    public void testGetCurrentPortletRequest() {
        assertNull(VaadinPortletRequest.getCurrentPortletRequest());
    }

    @Test
    public void testGetCurrent() {
        assertNull(VaadinPortletRequest.getCurrent());
    }
}
