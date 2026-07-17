
package com.nike.wingtips.servlet;

import com.nike.wingtips.Span;
import com.nike.wingtips.Tracer;
import com.nike.wingtips.tags.HttpTagAndSpanNamingAdapter;
import com.nike.wingtips.tags.HttpTagAndSpanNamingStrategy;
import com.nike.wingtips.util.TracingState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;

public class WingtipsRequestSpanCompletionAsyncListenerTest {

    private TracingState tracingState;
    private HttpTagAndSpanNamingStrategy<HttpServletRequest, HttpServletResponse> tagStrategy;
    private HttpTagAndSpanNamingAdapter<HttpServletRequest, HttpServletResponse> tagAdapter;
    private WingtipsRequestSpanCompletionAsyncListener listener;
    private AsyncEvent asyncEvent;
    private HttpServletRequest httpRequest;
    private HttpServletResponse httpResponse;
    private AsyncContext asyncContext;

    @Before
    public void setUp() {
        tracingState = mock(TracingState.class);
        tagStrategy = mock(HttpTagAndSpanNamingStrategy.class);
        tagAdapter = mock(HttpTagAndSpanNamingAdapter.class);
        listener = new WingtipsRequestSpanCompletionAsyncListener(tracingState, tagStrategy, tagAdapter);
        
        asyncEvent = mock(AsyncEvent.class);
        httpRequest = mock(HttpServletRequest.class);
        httpResponse = mock(HttpServletResponse.class);
        asyncContext = mock(AsyncContext.class);
    }

    @Test
    public void testOnComplete() throws Exception {
        when(asyncEvent.getSuppliedRequest()).thenReturn(httpRequest);
        when(asyncEvent.getSuppliedResponse()).thenReturn(httpResponse);
        
        listener.onComplete(asyncEvent);
        
        ArgumentCaptor<Runnable> runnableCaptor = ArgumentCaptor.forClass(Runnable.class);
        verify(tagStrategy).handleResponseTaggingAndFinalSpanName(any(Span.class), eq(httpRequest), eq(httpResponse), isNull(), eq(tagAdapter));
        verify(Tracer.getInstance()).completeRequestSpan();
    }

    @Test
    public void testOnTimeout() throws Exception {
        listener.onTimeout(asyncEvent);
        // No action expected, just ensure no exceptions are thrown
    }

    @Test
    public void testOnError() throws Exception {
        listener.onError(asyncEvent);
        // No action expected, just ensure no exceptions are thrown
    }

    @Test
    public void testOnStartAsync() throws Exception {
        when(asyncEvent.getAsyncContext()).thenReturn(asyncContext);
        when(asyncEvent.getSuppliedRequest()).thenReturn(httpRequest);
        when(asyncEvent.getSuppliedResponse()).thenReturn(httpResponse);
        
        listener.onStartAsync(asyncEvent);
        
        verify(asyncContext).addListener(listener, httpRequest, httpResponse);
    }

    @Test
    public void testCompleteRequestSpan() throws Exception {
        when(asyncEvent.getSuppliedRequest()).thenReturn(httpRequest);
        when(asyncEvent.getSuppliedResponse()).thenReturn(httpResponse);
        
        listener.onComplete(asyncEvent);
        listener.onComplete(asyncEvent); // Call again to test atomicity
        
        verify(tagStrategy, times(1)).handleResponseTaggingAndFinalSpanName(any(Span.class), eq(httpRequest), eq(httpResponse), isNull(), eq(tagAdapter));
        verify(Tracer.getInstance(), times(1)).completeRequestSpan();
    }
}
