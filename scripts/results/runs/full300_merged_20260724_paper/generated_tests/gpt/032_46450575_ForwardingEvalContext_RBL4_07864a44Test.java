
package com.spotify.flo.context;

import com.spotify.flo.EvalContext;
import com.spotify.flo.Fn;
import com.spotify.flo.Task;
import com.spotify.flo.TaskId;
import com.spotify.flo.TaskOperator.Listener;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ForwardingEvalContext_RBL4_07864a44Test {

    private EvalContext mockDelegate;
    private ForwardingEvalContext forwardingEvalContext;

    @Before
    public void setUp() {
        mockDelegate = mock(EvalContext.class);
        forwardingEvalContext = new ForwardingEvalContext(mockDelegate) {};
    }

    @Test
    public void testEvaluateInternal() {
        Task<String> mockTask = mock(Task.class);
        EvalContext mockContext = mock(EvalContext.class);
        Value<String> mockValue = mock(Value.class);
        
        when(mockDelegate.evaluateInternal(mockTask, mockContext)).thenReturn(mockValue);
        
        Value<String> result = forwardingEvalContext.evaluateInternal(mockTask, mockContext);
        
        verify(mockDelegate).evaluateInternal(mockTask, mockContext);
        assertSame(mockValue, result);
    }

    @Test
    public void testInvokeProcessFn() {
        TaskId mockTaskId = mock(TaskId.class);
        Fn<String> mockProcessFn = mock(Fn.class);
        Value<String> mockValue = mock(Value.class);
        
        when(mockDelegate.invokeProcessFn(mockTaskId, mockProcessFn)).thenReturn(mockValue);
        
        Value<String> result = forwardingEvalContext.invokeProcessFn(mockTaskId, mockProcessFn);
        
        verify(mockDelegate).invokeProcessFn(mockTaskId, mockProcessFn);
        assertSame(mockValue, result);
    }

    @Test
    public void testValue() {
        Fn<String> mockValueFn = mock(Fn.class);
        Value<String> mockValue = mock(Value.class);
        
        when(mockDelegate.value(mockValueFn)).thenReturn(mockValue);
        
        Value<String> result = forwardingEvalContext.value(mockValueFn);
        
        verify(mockDelegate).value(mockValueFn);
        assertSame(mockValue, result);
    }

    @Test
    public void testImmediateValue() {
        String value = "test";
        Value<String> mockValue = mock(Value.class);
        
        when(mockDelegate.immediateValue(value)).thenReturn(mockValue);
        
        Value<String> result = forwardingEvalContext.immediateValue(value);
        
        verify(mockDelegate).immediateValue(value);
        assertSame(mockValue, result);
    }

    @Test
    public void testPromise() {
        Promise<String> mockPromise = mock(Promise.class);
        
        when(mockDelegate.promise()).thenReturn(mockPromise);
        
        Promise<String> result = forwardingEvalContext.promise();
        
        verify(mockDelegate).promise();
        assertSame(mockPromise, result);
    }

    @Test
    public void testListener() {
        Listener mockListener = mock(Listener.class);
        
        when(mockDelegate.listener()).thenReturn(mockListener);
        
        Listener result = forwardingEvalContext.listener();
        
        verify(mockDelegate).listener();
        assertSame(mockListener, result);
    }
}
