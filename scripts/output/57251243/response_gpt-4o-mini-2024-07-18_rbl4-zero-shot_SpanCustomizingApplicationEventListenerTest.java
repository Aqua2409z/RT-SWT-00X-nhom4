
package com.nike.wingtips.jersey2;

import com.nike.wingtips.tags.KnownZipkinTags;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.glassfish.jersey.server.internal.routing.RoutingContext;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.uri.UriTemplate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javax.ws.rs.ext.Provider;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SpanCustomizingApplicationEventListenerTest {

    private SpanCustomizingApplicationEventListener listener;
    private ContainerRequest mockRequest;
    private ExtendedUriInfo mockUriInfo;

    @Before
    public void setUp() {
        listener = new SpanCustomizingApplicationEventListener();
        mockRequest = mock(ContainerRequest.class);
        mockUriInfo = mock(ExtendedUriInfo.class);
    }

    @Test
    public void testOnEvent_RequestMatched() {
        RequestEvent mockEvent = mock(RequestEvent.class);
        when(mockEvent.getType()).thenReturn(RequestEvent.Type.REQUEST_MATCHED);
        when(mockEvent.getContainerRequest()).thenReturn(mockRequest);
        
        UriTemplate mockTemplate = mock(UriTemplate.class);
        when(mockTemplate.getTemplate()).thenReturn("/test");
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.singletonList(mockTemplate));
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        
        listener.onEvent(mockEvent);
        
        verify(mockRequest).setProperty(KnownZipkinTags.HTTP_ROUTE, "/test");
    }

    @Test
    public void testOnEvent_RequestNotMatched() {
        RequestEvent mockEvent = mock(RequestEvent.class);
        when(mockEvent.getType()).thenReturn(RequestEvent.Type.FINISHED);
        
        listener.onEvent(mockEvent);
        
        verify(mockRequest, never()).setProperty(anyString(), anyString());
    }

    @Test
    public void testRoute_WithMatchedTemplates() {
        UriTemplate mockTemplate1 = mock(UriTemplate.class);
        when(mockTemplate1.getTemplate()).thenReturn("/resource");
        UriTemplate mockTemplate2 = mock(UriTemplate.class);
        when(mockTemplate2.getTemplate()).thenReturn("/method");
        
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Arrays.asList(mockTemplate1, mockTemplate2));
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        
        String result = listener.route(mockRequest);
        
        assertEquals("/resource/method", result);
    }

    @Test
    public void testRoute_EmptyBasePath() {
        when(mockUriInfo.getBaseUri()).thenReturn(new URI("http://localhost:8080/"));
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.emptyList());
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        
        String result = listener.route(mockRequest);
        
        assertEquals("", result);
    }

    @Test
    public void testRoute_SingleTemplate() {
        UriTemplate mockTemplate = mock(UriTemplate.class);
        when(mockTemplate.getTemplate()).thenReturn("/single");
        
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.singletonList(mockTemplate));
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        
        String result = listener.route(mockRequest);
        
        assertEquals("/single", result);
    }
}
