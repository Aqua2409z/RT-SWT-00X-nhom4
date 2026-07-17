
package com.nike.wingtips.jersey2;

import com.nike.wingtips.tags.KnownZipkinTags;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.ExtendedUriInfo;
import org.glassfish.jersey.server.internal.routing.RoutingContext;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
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
    private RequestEvent mockRequestEvent;

    @Before
    public void setUp() {
        listener = new SpanCustomizingApplicationEventListener();
        mockRequest = mock(ContainerRequest.class);
        mockUriInfo = mock(ExtendedUriInfo.class);
        mockRequestEvent = mock(RequestEvent.class);
    }

    @Test
    public void testOnRequestStart() {
        when(mockRequestEvent.getType()).thenReturn(RequestEvent.Type.START);
        RequestEventListener result = listener.onRequest(mockRequestEvent);
        assertEquals(listener, result);
    }

    @Test
    public void testOnRequestMatched() {
        when(mockRequestEvent.getType()).thenReturn(RequestEvent.Type.REQUEST_MATCHED);
        when(mockRequestEvent.getContainerRequest()).thenReturn(mockRequest);
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.singletonList(new UriTemplate("/test")));
        when(mockUriInfo.getBaseUri()).thenReturn(mock(UriTemplate.class));

        listener.onEvent(mockRequestEvent);

        verify(mockRequest).setProperty(KnownZipkinTags.HTTP_ROUTE, "/test");
    }

    @Test
    public void testRouteWithNoTemplates() {
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.emptyList());
        when(mockUriInfo.getBaseUri()).thenReturn(mock(UriTemplate.class));

        String result = listener.route(mockRequest);
        assertEquals("", result);
    }

    @Test
    public void testRouteWithBasePath() {
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.singletonList(new UriTemplate("/test")));
        when(mockUriInfo.getBaseUri()).thenReturn(mock(UriTemplate.class));
        when(mockUriInfo.getBaseUri().getPath()).thenReturn("/base");

        String result = listener.route(mockRequest);
        assertEquals("/base/test", result);
    }

    @Test
    public void testRouteWithMultipleTemplates() {
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Arrays.asList(new UriTemplate("/test1"), new UriTemplate("/test2")));
        when(mockUriInfo.getBaseUri()).thenReturn(mock(UriTemplate.class));
        when(mockUriInfo.getBaseUri().getPath()).thenReturn("/base");

        String result = listener.route(mockRequest);
        assertEquals("/base/test1/test2", result);
    }

    @Test
    public void testRouteWithEmptyBasePath() {
        when(mockRequest.getUriInfo()).thenReturn(mockUriInfo);
        when(mockUriInfo.getMatchedTemplates()).thenReturn(Collections.singletonList(new UriTemplate("/test")));
        when(mockUriInfo.getBaseUri()).thenReturn(mock(UriTemplate.class));
        when(mockUriInfo.getBaseUri().getPath()).thenReturn("/");

        String result = listener.route(mockRequest);
        assertEquals("/test", result);
    }
}
