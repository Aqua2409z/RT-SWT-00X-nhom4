
package com.yahoo.parsec.clients;

import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Request;
import com.ning.http.client.Response;
import com.ning.http.client.filter.RequestFilter;
import com.ning.http.client.filter.ResponseFilter;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ParsecAsyncHttpClient_RBL4Test_b68a888f {

    private ParsecAsyncHttpClient client;

    @BeforeClass
    public void setUp() {
        AsyncHttpClientConfig.Builder configBuilder = new AsyncHttpClientConfig.Builder();
        client = new ParsecAsyncHttpClient.Builder(configBuilder)
                .setMaxConnections(100)
                .setMaxConnectionsPerHost(10)
                .setRequestTimeout(5000)
                .setReadTimeout(5000)
                .setConnectTimeout(5000)
                .build();
    }

    @AfterClass
    public void tearDown() {
        client.close();
    }

    @Test
    public void testCriticalExecute() throws ExecutionException {
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .setMethod("GET")
                .setUrl("http://example.com")
                .build();

        CompletableFuture<Response> responseFuture = client.criticalExecute(request);
        Response response = responseFuture.get();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testExecute() throws ExecutionException {
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .setMethod("GET")
                .setUrl("http://example.com")
                .build();

        CompletableFuture<Response> responseFuture = client.execute(request);
        Response response = responseFuture.get();

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test
    public void testGetCacheStats() {
        ParsecAsyncHttpClient.CacheStats cacheStats = client.getCacheStats();
        Assert.assertNotNull(cacheStats);
    }

    @Test
    public void testGetMaxConnections() {
        Assert.assertEquals(client.getMaxConnections(), 100);
    }

    @Test
    public void testGetMaxConnectionsPerHost() {
        Assert.assertEquals(client.getMaxConnectionsPerHost(), 10);
    }

    @Test
    public void testAddRequestFilter() {
        RequestFilter filter = new RequestFilter() {
            @Override
            public void filterRequest(Request request) {
                // Custom filter logic
            }
        };
        client.getNingClientConfig().addRequestFilter(filter);
        List<RequestFilter> filters = client.getRequestFilters();
        Assert.assertTrue(filters.contains(filter));
    }

    @Test
    public void testAddResponseFilter() {
        ResponseFilter filter = new ResponseFilter() {
            @Override
            public void filterResponse(Response response) {
                // Custom filter logic
            }
        };
        client.getNingClientConfig().addResponseFilter(filter);
        List<ResponseFilter> filters = client.getResponseFilters();
        Assert.assertTrue(filters.contains(filter));
    }

    @Test
    public void testIsClosed() {
        Assert.assertFalse(client.isClosed());
        client.close();
        Assert.assertTrue(client.isClosed());
    }

    @Test
    public void testSetRetryInterval() {
        client = new ParsecAsyncHttpClient.Builder(new AsyncHttpClientConfig.Builder())
                .setRetryIntervalInMilliSeconds(1000)
                .build();
        Assert.assertTrue(client.retryIntervalMillisOpt.isPresent());
        Assert.assertEquals(client.retryIntervalMillisOpt.get().longValue(), 1000);
    }
}
