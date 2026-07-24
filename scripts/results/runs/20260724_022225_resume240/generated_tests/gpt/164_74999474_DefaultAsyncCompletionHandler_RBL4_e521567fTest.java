
package com.yahoo.parsec.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.Response;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.xml.ws.http.HTTPException;
import java.util.HashSet;
import java.util.Set;

public class DefaultAsyncCompletionHandler_RBL4_e521567fTest {
    private DefaultAsyncCompletionHandler<String> handler;
    private ObjectMapper objectMapper;
    private Response response;

    @BeforeMethod
    public void setUp() {
        objectMapper = new ObjectMapper();
        handler = new DefaultAsyncCompletionHandler<>(String.class, objectMapper);
        response = Mockito.mock(Response.class);
    }

    @Test
    public void testOnCompletedWithValidResponse() throws Exception {
        Mockito.when(response.getStatusCode()).thenReturn(200);
        Mockito.when(response.hasResponseBody()).thenReturn(true);
        Mockito.when(response.getResponseBody()).thenReturn("Test Response");

        String result = handler.onCompleted(response);
        Assert.assertEquals(result, "Test Response");
    }

    @Test(expectedExceptions = HTTPException.class)
    public void testOnCompletedWithInvalidStatusCode() throws Exception {
        Mockito.when(response.getStatusCode()).thenReturn(404);

        handler.onCompleted(response);
    }

    @Test
    public void testOnCompletedWithEmptyResponseBody() throws Exception {
        Mockito.when(response.getStatusCode()).thenReturn(200);
        Mockito.when(response.hasResponseBody()).thenReturn(false);

        String result = handler.onCompleted(response);
        Assert.assertNull(result);
    }

    @Test
    public void testConstructorWithExpectedStatusCodes() {
        Set<Integer> expectedStatusCodes = new HashSet<>();
        expectedStatusCodes.add(200);
        expectedStatusCodes.add(201);
        
        DefaultAsyncCompletionHandler<String> handlerWithExpected = new DefaultAsyncCompletionHandler<>(String.class, expectedStatusCodes, objectMapper);
        Assert.assertNotNull(handlerWithExpected);
    }

    @Test
    public void testConstructorWithNullExpectedStatusCodes() {
        DefaultAsyncCompletionHandler<String> handlerWithNullExpected = new DefaultAsyncCompletionHandler<>(String.class, null, objectMapper);
        Assert.assertNotNull(handlerWithNullExpected);
    }

    @Test
    public void testConstructorWithEmptyExpectedStatusCodes() {
        Set<Integer> emptyExpectedStatusCodes = new HashSet<>();
        DefaultAsyncCompletionHandler<String> handlerWithEmptyExpected = new DefaultAsyncCompletionHandler<>(String.class, emptyExpectedStatusCodes, objectMapper);
        Assert.assertNotNull(handlerWithEmptyExpected);
    }
}
