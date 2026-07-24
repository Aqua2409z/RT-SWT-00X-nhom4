
package com.linkedin.d2.balancer.util.hashing;

import com.linkedin.d2.balancer.Facilities;
import com.linkedin.d2.balancer.ServiceUnavailableException;
import com.linkedin.d2.balancer.util.LoadBalancerUtil;
import com.linkedin.d2.balancer.util.URIKeyPair;
import com.linkedin.d2.balancer.util.URIMappingResult;
import com.linkedin.d2.balancer.util.partitions.PartitionAccessor;
import com.linkedin.d2.balancer.util.partitions.PartitionInfoProvider;
import com.linkedin.d2.balancer.util.partitions.DefaultPartitionAccessor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RingBasedUriMapper_RBL4_077b6da2Test {

    private HashRingProvider mockHashRingProvider;
    private PartitionInfoProvider mockPartitionInfoProvider;
    private RingBasedUriMapper uriMapper;

    @BeforeMethod
    public void setUp() {
        mockHashRingProvider = Mockito.mock(HashRingProvider.class);
        mockPartitionInfoProvider = Mockito.mock(PartitionInfoProvider.class);
        uriMapper = new RingBasedUriMapper(mockHashRingProvider, mockPartitionInfoProvider);
    }

    @Test
    public void testMapUrisWithEmptyList() throws ServiceUnavailableException {
        List<URIKeyPair<String>> requestUriKeyPairs = Collections.emptyList();
        URIMappingResult<String> result = uriMapper.mapUris(requestUriKeyPairs);
        Assert.assertTrue(result.getHostToKeySet().isEmpty());
        Assert.assertTrue(result.getUnmapped().isEmpty());
        Assert.assertTrue(result.getHostToPartitionId().isEmpty());
    }

    @Test
    public void testMapUrisWithSingleRequest() throws ServiceUnavailableException {
        URI uri = URI.create("http://example.com");
        URIKeyPair<String> uriKeyPair = new URIKeyPair<>(uri, "key1");
        List<URIKeyPair<String>> requestUriKeyPairs = Collections.singletonList(uriKeyPair);

        PartitionAccessor mockAccessor = Mockito.mock(PartitionAccessor.class);
        Mockito.when(mockAccessor.getMaxPartitionId()).thenReturn(0);
        Mockito.when(mockPartitionInfoProvider.getPartitionAccessor(Mockito.anyString())).thenReturn(mockAccessor);
        
        URIMappingResult<String> result = uriMapper.mapUris(requestUriKeyPairs);
        Assert.assertFalse(result.getHostToKeySet().isEmpty());
    }

    @Test(expectedExceptions = ServiceUnavailableException.class)
    public void testNeedScatterGatherThrowsException() throws ServiceUnavailableException {
        Mockito.when(mockPartitionInfoProvider.getPartitionAccessor(Mockito.anyString()))
                .thenThrow(new ServiceUnavailableException("Service unavailable"));
        uriMapper.needScatterGather("testService");
    }

    @Test
    public void testNeedScatterGatherWithPartitioningEnabled() throws ServiceUnavailableException {
        PartitionAccessor mockAccessor = Mockito.mock(PartitionAccessor.class);
        Mockito.when(mockAccessor.getMaxPartitionId()).thenReturn(1);
        Mockito.when(mockPartitionInfoProvider.getPartitionAccessor("testService")).thenReturn(mockAccessor);
        
        boolean result = uriMapper.needScatterGather("testService");
        Assert.assertTrue(result);
    }

    @Test
    public void testNeedScatterGatherWithStickyEnabled() throws ServiceUnavailableException {
        PartitionAccessor mockAccessor = Mockito.mock(PartitionAccessor.class);
        Mockito.when(mockAccessor.getMaxPartitionId()).thenReturn(0);
        Mockito.when(mockPartitionInfoProvider.getPartitionAccessor("testService")).thenReturn(mockAccessor);
        
        HashFunction<Request> mockHashFunction = Mockito.mock(HashFunction.class);
        Mockito.when(mockHashRingProvider.getRequestHashFunction("testService")).thenReturn(new URIRegexHash());
        
        boolean result = uriMapper.needScatterGather("testService");
        Assert.assertTrue(result);
    }

    @Test
    public void testDistributeToPartitionsUnpartitioned() {
        URIKeyPair<String> uriKeyPair = new URIKeyPair<>(URI.create("http://example.com"), "key1");
        List<URIKeyPair<String>> requestUriKeyPairs = Collections.singletonList(uriKeyPair);
        Map<Integer, List<URIKeyPair<String>>> result = uriMapper.distributeToPartitionsUnpartitioned(requestUriKeyPairs);
        Assert.assertEquals(result.size(), 1);
        Assert.assertTrue(result.containsKey(DefaultPartitionAccessor.DEFAULT_PARTITION_ID));
    }

    @Test
    public void testConvertURIKeyPairListToKeySet() {
        URIKeyPair<String> uriKeyPair1 = new URIKeyPair<>(URI.create("http://example.com"), "key1");
        URIKeyPair<String> uriKeyPair2 = new URIKeyPair<>(URI.create("http://example.com"), "key2");
        List<URIKeyPair<String>> list = Arrays.asList(uriKeyPair1, uriKeyPair2);
        Set<String> keys = RingBasedUriMapper.convertURIKeyPairListToKeySet(list);
        Assert.assertEquals(keys.size(), 2);
        Assert.assertTrue(keys.contains("key1"));
        Assert.assertTrue(keys.contains("key2"));
    }
}
