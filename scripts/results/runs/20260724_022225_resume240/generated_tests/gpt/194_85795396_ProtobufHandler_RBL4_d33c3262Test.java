package org.apache.calcite.avatica.remote;

import org.apache.calcite.avatica.metrics.MetricsSystem;
import org.apache.calcite.avatica.metrics.Timer;
import org.apache.calcite.avatica.remote.ProtobufHandler;
import org.apache.calcite.avatica.remote.Service;
import org.apache.calcite.avatica.remote.Service.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ProtobufHandler_RBL4_d33c3262Test {

    private ProtobufHandler protobufHandler;
    private Service mockService;
    private ProtobufTranslation mockTranslation;
    private MetricsSystem mockMetrics;
    private Timer mockTimer;

    @Before
    public void setUp() {
        mockService = Mockito.mock(Service.class);
        mockTranslation = Mockito.mock(ProtobufTranslation.class);
        mockMetrics = Mockito.mock(MetricsSystem.class);
        mockTimer = Mockito.mock(Timer.class);
        
        when(mockMetrics.getTimer(anyString())).thenReturn(mockTimer);
        
        protobufHandler = new ProtobufHandler(mockService, mockTranslation, mockMetrics);
    }

    @Test
    public void testApply() throws IOException {
        byte[] requestBytes = new byte[]{1, 2, 3};
        Service.Request mockRequest = Mockito.mock(Service.Request.class);
        Service.Response mockResponse = Mockito.mock(Service.Response.class);
        
        when(mockTranslation.parseRequest(requestBytes)).thenReturn(mockRequest);
        when(mockService.apply(mockRequest)).thenReturn(mockResponse);
        
        HandlerResponse<byte[]> response = protobufHandler.apply(requestBytes);
        
        assertNotNull(response);
        verify(mockTranslation).parseRequest(requestBytes);
        verify(mockService).apply(mockRequest);
    }

    @Test
    public void testDecode() throws IOException {
        byte[] serializedRequest = new byte[]{4, 5, 6};
        Service.Request mockRequest = Mockito.mock(Service.Request.class);
        
        when(mockTranslation.parseRequest(serializedRequest)).thenReturn(mockRequest);
        
        Service.Request request = protobufHandler.decode(serializedRequest);
        
        assertNotNull(request);
        assertArrayEquals(mockRequest, request);
        verify(mockTranslation).parseRequest(serializedRequest);
    }

    @Test
    public void testEncode() throws IOException {
        Service.Response mockResponse = Mockito.mock(Service.Response.class);
        byte[] serializedResponse = new byte[]{7, 8, 9};
        
        when(mockTranslation.serializeResponse(mockResponse)).thenReturn(serializedResponse);
        
        byte[] responseBytes = protobufHandler.encode(mockResponse);
        
        assertArrayEquals(serializedResponse, responseBytes);
        verify(mockTranslation).serializeResponse(mockResponse);
    }
}
