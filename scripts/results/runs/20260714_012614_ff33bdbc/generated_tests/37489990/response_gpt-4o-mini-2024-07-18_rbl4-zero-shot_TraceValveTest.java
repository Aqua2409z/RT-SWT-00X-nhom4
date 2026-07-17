package com.google.cloud.runtimes.tomcat.trace;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.cloud.trace.Trace;
import com.google.cloud.trace.Tracer;
import com.google.cloud.trace.core.Labels;
import com.google.cloud.trace.service.TraceGrpcApiService;
import com.google.cloud.trace.service.TraceService;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import java.io.IOException;

public class TraceValveTest {

    private TraceValve traceValve;
    private TraceService traceService;
    private Request request;
    private Response response;
    private Tracer tracer;

    @Before
    public void setUp() throws Exception {
        traceValve = new TraceValve();
        traceService = mock(TraceService.class);
        request = mock(Request.class);
        response = mock(Response.class);
        tracer = mock(Tracer.class);

        traceValve.setTraceService(traceService);
        when(traceService.getTracer()).thenReturn(tracer);
    }

    @Test
    public void testInitTraceService_Success() throws Exception {
        traceValve.setTraceScheduledDelay(5);
        traceValve.initTraceService();
        verify(traceService).getTracer();
    }

    @Test(expected = LifecycleException.class)
    public void testInitTraceService_Failure_NegativeDelay() throws Exception {
        traceValve.setTraceScheduledDelay(-1);
        traceValve.initTraceService();
    }

    @Test(expected = LifecycleException.class)
    public void testInitTraceService_Failure_ZeroDelay() throws Exception {
        traceValve.setTraceScheduledDelay(0);
        traceValve.initTraceService();
    }

    @Test
    public void testInvoke_WithTraceHeader() throws IOException, ServletException {
        String traceHeader = "traceId/spanId";
        when(request.getHeader(TraceValve.X_CLOUD_TRACE_HEADER)).thenReturn(traceHeader);
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getStatus()).thenReturn(200);

        traceValve.invoke(request, response);

        ArgumentCaptor<Labels> labelsCaptor = ArgumentCaptor.forClass(Labels.class);
        verify(tracer).startSpan("/test");
        verify(tracer).annotateSpan(any(), labelsCaptor.capture());
        verify(tracer).endSpan(any());
        assertNotNull(labelsCaptor.getValue());
    }

    @Test
    public void testInvoke_WithoutTraceHeader() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getStatus()).thenReturn(200);

        traceValve.invoke(request, response);

        verify(tracer).startSpan("/test");
        verify(tracer).endSpan(any());
    }

    @Test
    public void testCreateLabels() {
        Labels labels = traceValve.createLabels(request, response);
        assertNotNull(labels);
    }

    @Test
    public void testAnnotateIfNotEmpty() {
        Labels.Builder labelsBuilder = Labels.builder();
        traceValve.annotateIfNotEmpty(labelsBuilder, "key", "value");
        Labels labels = labelsBuilder.build();
        assertTrue(labels.contains("key", "value"));
    }

    @Test
    public void testAnnotateIfNotEmpty_NullValue() {
        Labels.Builder labelsBuilder = Labels.builder();
        traceValve.annotateIfNotEmpty(labelsBuilder, "key", null);
        Labels labels = labelsBuilder.build();
        assertFalse(labels.contains("key", "value"));
    }

    @Test
    public void testAnnotateIfNotEmpty_EmptyValue() {
        Labels.Builder labelsBuilder = Labels.builder();
        traceValve.annotateIfNotEmpty(labelsBuilder, "key", "");
        Labels labels = labelsBuilder.build();
        assertFalse(labels.contains("key", "value"));
    }
}
