
package com.yahoo.parsec.clients;

import com.ning.http.client.Request;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.NewCookie;
import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParsecAsyncHttpRequest_RBL4_4328525aTest {

    @Test
    public void testBuilder() throws Exception {
        NewCookie cookie = new NewCookie("name", "value");
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .setMethod("POST")
                .setUrl("http://example.com")
                .setBody("test body")
                .setContentLength(9)
                .setMaxRetries(5)
                .addCookie(cookie)
                .addHeader("Content-Type", "application/json")
                .addQueryParam("queryKey", "queryValue")
                .addFormParam("formKey", "formValue")
                .build();

        Assert.assertEquals(request.getMethod(), "POST");
        Assert.assertEquals(request.getUrl(), "http://example.com");
        Assert.assertEquals(request.getBody(), "test body");
        Assert.assertEquals(request.getContentLength(), 9);
        Assert.assertEquals(request.getMaxRetries(), 5);
        Assert.assertEquals(request.getCookies().iterator().next().getName(), "name");
        Assert.assertEquals(request.getHeaders().get("Content-Type").get(0), "application/json");
        Assert.assertEquals(request.getQueryParams().get("queryKey").get(0), "queryValue");
        Assert.assertEquals(request.getFormParams().get("formKey").get(0), "formValue");
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        ParsecAsyncHttpRequest request1 = new ParsecAsyncHttpRequest.Builder()
                .setMethod("GET")
                .setUrl("http://example.com")
                .build();

        ParsecAsyncHttpRequest request2 = new ParsecAsyncHttpRequest.Builder()
                .setMethod("GET")
                .setUrl("http://example.com")
                .build();

        Assert.assertEquals(request1, request2);
        Assert.assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    public void testGetters() throws Exception {
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .setMethod("GET")
                .setUrl("http://example.com")
                .setMaxRetries(3)
                .setFollowRedirects(true)
                .setRequestTimeout(1000)
                .setBodyEncoding("UTF-8")
                .build();

        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getUrl(), "http://example.com");
        Assert.assertEquals(request.getMaxRetries(), 3);
        Assert.assertTrue(request.isFollowRedirect());
        Assert.assertEquals(request.getBodyEncoding(), "UTF-8");
    }

    @Test
    public void testRetryStatusCodes() throws Exception {
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .addRetryStatusCode(500)
                .addRetryStatusCode(503)
                .build();

        List<Integer> retryStatusCodes = request.getRetryStatusCodes();
        Assert.assertTrue(retryStatusCodes.contains(500));
        Assert.assertTrue(retryStatusCodes.contains(503));
        Assert.assertEquals(retryStatusCodes.size(), 2);
    }

    @Test
    public void testRetryExceptions() throws Exception {
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .addRetryException(NullPointerException.class)
                .addRetryException(IllegalArgumentException.class)
                .build();

        List<Class<? extends Throwable>> retryExceptions = request.getRetryExceptions();
        Assert.assertTrue(retryExceptions.contains(NullPointerException.class));
        Assert.assertTrue(retryExceptions.contains(IllegalArgumentException.class));
        Assert.assertEquals(retryExceptions.size(), 2);
    }

    @Test
    public void testGetUri() throws Exception {
        ParsecAsyncHttpRequest request = new ParsecAsyncHttpRequest.Builder()
                .setUrl("http://example.com")
                .build();

        URI uri = request.getUri();
        Assert.assertEquals(uri.toString(), "http://example.com/");
    }
}
