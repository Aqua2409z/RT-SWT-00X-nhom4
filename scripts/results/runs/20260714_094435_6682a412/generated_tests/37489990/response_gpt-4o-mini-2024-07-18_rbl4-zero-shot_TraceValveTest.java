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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.ServletException;
import java.io.IOException;

public class TraceValveTest {

    private TraceValve traceValve;

    @Mock
    private Request request;

    @Mock
    private Response response;

    @Mock
    private TraceService traceService;

    @Mock
    private Tracer tracer;

    @Mock
    private TraceGrpcApiService traceGrpcApiService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        traceValve = new TraceValve();
        traceValve.setTraceService(traceService);
        when(traceService.getTracer()).thenReturn(tracer);
        when(traceService.getSpanContextFactory()).thenReturn(mock(SpanContextFactory.class));
        when(traceService.getSpanContextHandler()).thenReturn(mock(SpanContextHandle.class));
    }

    @Test
    public void testInitTraceServiceWithValidDelay() throws LifecycleException {
        traceValve.setTraceScheduledDelay(5);
        traceValve.initTraceService();
        assertNotNull(traceValve);
    }

    @Test(expected = LifecycleException.class)
    public void testInitTraceServiceWithInvalidDelay() throws LifecycleException {
        traceValve.setTraceScheduledDelay(-1);
        traceValve.initTraceService();
    }

    @Test
    public void testInvokeWithTraceHeader() throws IOException, ServletException {
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
    public void testInvokeWithoutTraceHeader() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/test");
        when(response.getStatus()).thenReturn(200);

        traceValve.invoke(request, response);

        verify(tracer).startSpan("/test");
        verify(tracer).endSpan(any());
    }

    @Test
    public void testCreateLabels() {
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/test");
        when(request.getProtocol()).thenReturn("HTTP/1.1");
        when(request.getHeader(HttpHeaders.USER_AGENT)).thenReturn("Mozilla/5.0");
        when(request.getHeader(HttpHeaders.CONTENT_LENGTH)).thenReturn("100");
        when(response.getHeader(HttpHeaders.CONTENT_LENGTH)).thenReturn("200");
        when(response.getStatus()).thenReturn(200);

        Labels labels = traceValve.createLabels(request, response);

        assertNotNull(labels);
        assertEquals("GET", labels.get(HttpLabels.HTTP_METHOD.getValue()).get(0));
        assertEquals("/test", labels.get(HttpLabels.HTTP_URL.getValue()).get(0));
        assertEquals("HTTP/1.1", labels.get(HttpLabels.HTTP_CLIENT_PROTOCOL.getValue()).get(0));
        assertEquals("Mozilla/5.0", labels.get(HttpLabels.HTTP_USER_AGENT.getValue()).get(0));
        assertEquals("100", labels.get(HttpLabels.HTTP_REQUEST_SIZE.getValue()).get(0));
        assertEquals("200", labels.get(HttpLabels.HTTP_RESPONSE_SIZE.getValue()).get(0));
        assertEquals("200", labels.get(HttpLabels.HTTP_STATUS_CODE.getValue()).get(0));
    }
}
