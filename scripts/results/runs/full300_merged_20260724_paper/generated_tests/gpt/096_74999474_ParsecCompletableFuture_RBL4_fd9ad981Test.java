
package com.yahoo.parsec.clients;

import com.ning.http.client.ListenableFuture;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ParsecCompletableFuture_RBL4_fd9ad981Test {

    private Future<String> mockFuture;
    private ParsecCompletableFuture<String> parsecCompletableFuture;

    @BeforeMethod
    public void setUp() {
        mockFuture = Mockito.mock(Future.class);
        parsecCompletableFuture = new ParsecCompletableFuture<>(mockFuture);
    }

    @Test
    public void testCancel() {
        Mockito.when(mockFuture.cancel(true)).thenReturn(true);
        boolean result = parsecCompletableFuture.cancel(true);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCancelled() {
        Mockito.when(mockFuture.isCancelled()).thenReturn(true);
        Assert.assertTrue(parsecCompletableFuture.isCancelled());
    }

    @Test
    public void testIsDone() {
        Mockito.when(mockFuture.isDone()).thenReturn(true);
        Assert.assertTrue(parsecCompletableFuture.isDone());
    }

    @Test
    public void testGet() throws InterruptedException, ExecutionException {
        Mockito.when(mockFuture.get()).thenReturn("result");
        String result = parsecCompletableFuture.get();
        Assert.assertEquals(result, "result");
    }

    @Test(expectedExceptions = ExecutionException.class)
    public void testGetThrowsExecutionException() throws InterruptedException, ExecutionException {
        Mockito.when(mockFuture.get()).thenThrow(new ExecutionException(new Throwable()));
        parsecCompletableFuture.get();
    }

    @Test(expectedExceptions = InterruptedException.class)
    public void testGetThrowsInterruptedException() throws InterruptedException, ExecutionException {
        Mockito.when(mockFuture.get()).thenThrow(new InterruptedException());
        parsecCompletableFuture.get();
    }

    @Test
    public void testGetWithTimeout() throws InterruptedException, ExecutionException, TimeoutException {
        Mockito.when(mockFuture.get(1, TimeUnit.SECONDS)).thenReturn("result");
        String result = parsecCompletableFuture.get(1, TimeUnit.SECONDS);
        Assert.assertEquals(result, "result");
    }

    @Test(expectedExceptions = TimeoutException.class)
    public void testGetWithTimeoutThrowsTimeoutException() throws InterruptedException, ExecutionException, TimeoutException {
        Mockito.when(mockFuture.get(1, TimeUnit.SECONDS)).thenThrow(new TimeoutException());
        parsecCompletableFuture.get(1, TimeUnit.SECONDS);
    }
}
