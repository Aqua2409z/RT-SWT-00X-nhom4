
package com.spotify.flo.context;

import com.spotify.flo.EvalContext;
import com.spotify.flo.Task;
import com.spotify.flo.TaskId;
import com.spotify.flo.Value;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.*;

public class MemoizingContext_RBL4_c253e1f9Test {

    private EvalContext baseContext;
    private MemoizingContext memoizingContext;

    @Before
    public void setUp() {
        baseContext = mock(EvalContext.class);
        memoizingContext = (MemoizingContext) MemoizingContext.composeWith(baseContext);
    }

    @Test
    public void testEvaluateInternalFirstTime() {
        Task<String> task = mock(Task.class);
        TaskId taskId = mock(TaskId.class);
        when(task.id()).thenReturn(taskId);
        
        Value<String> value = mock(Value.class);
        when(baseContext.evaluateInternal(task, baseContext)).thenReturn(value);
        when(value.onFail(any())).thenReturn(value);
        when(value.consume(any())).thenReturn(value);
        
        Value<String> result = memoizingContext.evaluateInternal(task, baseContext);
        
        assertNotNull(result);
        verify(baseContext).evaluateInternal(task, baseContext);
        verify(value).onFail(any());
        verify(value).consume(any());
    }

    @Test
    public void testEvaluateInternalSecondTime() {
        Task<String> task = mock(Task.class);
        TaskId taskId = mock(TaskId.class);
        when(task.id()).thenReturn(taskId);
        
        Value<String> value = mock(Value.class);
        when(baseContext.evaluateInternal(task, baseContext)).thenReturn(value);
        when(value.onFail(any())).thenReturn(value);
        when(value.consume(any())).thenReturn(value);
        
        // First evaluation
        memoizingContext.evaluateInternal(task, baseContext);
        
        // Second evaluation should return the cached result
        Value<String> result = memoizingContext.evaluateInternal(task, baseContext);
        
        assertNotNull(result);
        verify(baseContext, times(1)).evaluateInternal(task, baseContext);
    }

    @Test
    public void testEvaluateInternalFailure() {
        Task<String> task = mock(Task.class);
        TaskId taskId = mock(TaskId.class);
        when(task.id()).thenReturn(taskId);
        
        Value<String> value = mock(Value.class);
        when(baseContext.evaluateInternal(task, baseContext)).thenThrow(new RuntimeException("Test Exception"));
        
        Value<String> result = memoizingContext.evaluateInternal(task, baseContext);
        
        assertNotNull(result);
        verify(value, never()).onFail(any());
        verify(value, never()).consume(any());
    }
}
