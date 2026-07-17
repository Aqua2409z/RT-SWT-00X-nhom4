package com.here.account.http.apache;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.here.account.http.HttpConstants;
import com.here.account.http.HttpException;
import com.here.account.http.HttpProvider;

public class ApacheHttpClientProviderTest {

    private CloseableHttpClient mockHttpClient;
    private ApacheHttpClientProvider httpClientProvider;

    @Before
    public void setUp() {
        mockHttpClient = mock(CloseableHttpClient.class);
        httpClientProvider = new ApacheHttpClientProvider(mockHttpClient, true);
    }

    @After
    public void tearDown() throws IOException {
        httpClientProvider.close();
    }

    @Test
    public void testGetRequestWithJsonBody() throws Exception {
        String url = "http://example.com";
        String jsonBody = "{\"key\":\"value\"}";
        HttpRequestAuthorizer mockAuthorizer = mock(HttpRequestAuthorizer.class);
        HttpRequest request = httpClientProvider.getRequest(mockAuthorizer, HttpGet.METHOD_NAME, url, jsonBody);

        assertNotNull(request);
        assertTrue(request instanceof ApacheHttpClientProvider.ApacheHttpClientRequest);
    }

    @Test
    public void testGetRequestWithFormParams() throws Exception {
        String url = "http://example.com";
        Map<String, List<String>> formParams = new HashMap<>();
        formParams.put("param1", Collections.singletonList("value1"));
        HttpRequestAuthorizer mockAuthorizer = mock(HttpRequestAuthorizer.class);
        HttpRequest request = httpClientProvider.getRequest(mockAuthorizer, HttpGet.METHOD_NAME, url, formParams);

        assertNotNull(request);
        assertTrue(request instanceof ApacheHttpClientProvider.ApacheHttpClientRequest);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetRequestWithInvalidMethod() throws Exception {
        String url = "http://example.com";
        HttpRequestAuthorizer mockAuthorizer = mock(HttpRequestAuthorizer.class);
        httpClientProvider.getRequest(mockAuthorizer, "INVALID_METHOD", url, null);
    }

    @Test
    public void testExecuteRequest() throws Exception {
        String url = "http://example.com";
        HttpGet httpGet = new HttpGet(new URI(url));
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockHttpClient.execute(httpGet, null)).thenReturn(mockResponse);
        
        HttpRequest request = new ApacheHttpClientProvider.ApacheHttpClientRequest(httpGet);
        HttpResponse response = httpClientProvider.execute(request);

        assertNotNull(response);
        assertEquals(mockResponse, response);
    }

    @Test(expected = HttpException.class)
    public void testExecuteRequestThrowsHttpException() throws Exception {
        String url = "http://example.com";
        HttpGet httpGet = new HttpGet(new URI(url));
        when(mockHttpClient.execute(httpGet, null)).thenThrow(new IOException("Connection error"));

        HttpRequest request = new ApacheHttpClientProvider.ApacheHttpClientRequest(httpGet);
        httpClientProvider.execute(request);
    }

    @Test
    public void testClose() throws IOException {
        httpClientProvider.close();
        verify(mockHttpClient, times(1)).close();
    }
}
