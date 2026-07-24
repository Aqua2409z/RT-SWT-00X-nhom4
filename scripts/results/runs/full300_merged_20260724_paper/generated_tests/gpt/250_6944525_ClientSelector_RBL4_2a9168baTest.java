package com.linkedin.d2.balancer.strategies.relative;

import com.linkedin.d2.balancer.KeyMapper;
import com.linkedin.d2.balancer.clients.TrackerClient;
import com.linkedin.d2.balancer.util.hashing.HashFunction;
import com.linkedin.d2.balancer.util.hashing.Ring;
import com.linkedin.r2.message.Request;
import com.linkedin.r2.message.RequestContext;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class ClientSelector_RBL4_2a9168baTest {

    private ClientSelector clientSelector;
    private HashFunction<Request> mockHashFunction;
    private Ring<URI> mockRing;
    private Map<URI, TrackerClient> mockTrackerClients;
    private Request mockRequest;
    private RequestContext mockRequestContext;

    @BeforeMethod
    public void setUp() {
        mockHashFunction = Mockito.mock(HashFunction.class);
        mockRing = Mockito.mock(Ring.class);
        mockTrackerClients = new HashMap<>();
        mockRequest = Mockito.mock(Request.class);
        mockRequestContext = Mockito.mock(RequestContext.class);
        clientSelector = new ClientSelector(mockHashFunction);
    }

    @Test
    public void testGetTrackerClientWithTargetHost() {
        URI targetHostUri = URI.create("http://target-host");
        TrackerClient mockTrackerClient = Mockito.mock(TrackerClient.class);
        Mockito.when(mockTrackerClient.getUri()).thenReturn(targetHostUri);
        mockTrackerClients.put(targetHostUri, mockTrackerClient);
        Mockito.when(KeyMapper.TargetHostHints.getRequestContextTargetHost(mockRequestContext)).thenReturn(targetHostUri);

        TrackerClient result = clientSelector.getTrackerClient(mockRequest, mockRequestContext, mockRing, mockTrackerClients);

        Assert.assertEquals(result, mockTrackerClient);
    }

    @Test
    public void testGetTrackerClientFromRing() {
        URI uri = URI.create("http://host1");
        TrackerClient mockTrackerClient = Mockito.mock(TrackerClient.class);
        Mockito.when(mockTrackerClient.getUri()).thenReturn(uri);
        mockTrackerClients.put(uri, mockTrackerClient);
        Mockito.when(mockHashFunction.hash(mockRequest)).thenReturn(0);
        Mockito.when(mockRing.get(0)).thenReturn(uri);

        TrackerClient result = clientSelector.getTrackerClient(mockRequest, mockRequestContext, mockRing, mockTrackerClients);

        Assert.assertEquals(result, mockTrackerClient);
    }

    @Test
    public void testGetTrackerClientWithExcludedHosts() {
        URI uri = URI.create("http://host1");
        TrackerClient mockTrackerClient = Mockito.mock(TrackerClient.class);
        Mockito.when(mockTrackerClient.getUri()).thenReturn(uri);
        mockTrackerClients.put(uri, mockTrackerClient);
        Mockito.when(mockHashFunction.hash(mockRequest)).thenReturn(0);
        Mockito.when(mockRing.get(0)).thenReturn(uri);
        LoadBalancerStrategy.ExcludedHostHints.addRequestContextExcludedHost(mockRequestContext, uri);

        TrackerClient result = clientSelector.getTrackerClient(mockRequest, mockRequestContext, mockRing, mockTrackerClients);

        Assert.assertNull(result);
    }

    @Test
    public void testGetTrackerClientWhenRingIsOutdated() {
        URI uri1 = URI.create("http://host1");
        URI uri2 = URI.create("http://host2");
        TrackerClient mockTrackerClient1 = Mockito.mock(TrackerClient.class);
        TrackerClient mockTrackerClient2 = Mockito.mock(TrackerClient.class);
        Mockito.when(mockTrackerClient1.getUri()).thenReturn(uri1);
        Mockito.when(mockTrackerClient2.getUri()).thenReturn(uri2);
        mockTrackerClients.put(uri1, mockTrackerClient1);
        mockTrackerClients.put(uri2, mockTrackerClient2);
        Mockito.when(mockHashFunction.hash(mockRequest)).thenReturn(0);
        Mockito.when(mockRing.get(0)).thenReturn(uri1);
        LoadBalancerStrategy.ExcludedHostHints.addRequestContextExcludedHost(mockRequestContext, uri1);

        TrackerClient result = clientSelector.getTrackerClient(mockRequest, mockRequestContext, mockRing, mockTrackerClients);

        Assert.assertEquals(result, mockTrackerClient2);
    }

    @Test
    public void testGetTrackerClientWhenNoClientsAvailable() {
        Mockito.when(mockHashFunction.hash(mockRequest)).thenReturn(0);
        Mockito.when(mockRing.get(0)).thenReturn(URI.create("http://nonexistent-host"));

        TrackerClient result = clientSelector.getTrackerClient(mockRequest, mockRequestContext, mockRing, mockTrackerClients);

        Assert.assertNull(result);
    }
}
