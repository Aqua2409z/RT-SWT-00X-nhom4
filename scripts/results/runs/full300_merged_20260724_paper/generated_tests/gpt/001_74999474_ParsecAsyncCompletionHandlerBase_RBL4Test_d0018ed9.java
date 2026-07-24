package com.yahoo.parsec.clients;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.Response;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ParsecAsyncCompletionHandlerBase_RBL4Test_d0018ed9 {

    @Test
    public void testOnCompleted() throws Exception {
        // Arrange
        ParsecAsyncCompletionHandlerBase handler = new ParsecAsyncCompletionHandlerBase();
        com.ning.http.client.Response ningResponse = Mockito.mock(com.ning.http.client.Response.class);
        
        // Mock the behavior of ParsecHttpUtil.getResponse
        Response expectedResponse = Mockito.mock(Response.class);
        Mockito.when(ParsecHttpUtil.getResponse(ningResponse)).thenReturn(expectedResponse);
        
        // Act
        Response actualResponse = handler.onCompleted(ningResponse);
        
        // Assert
        Assert.assertEquals(actualResponse, expectedResponse);
    }
}
