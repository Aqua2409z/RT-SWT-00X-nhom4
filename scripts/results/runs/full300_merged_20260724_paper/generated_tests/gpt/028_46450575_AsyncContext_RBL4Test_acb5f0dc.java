
package com.spotify.flo.context;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.spotify.flo.Fn;
import com.spotify.flo.Task;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AsyncContext_RBL4Test_acb5f0dc {

    private Executor executor;
    private AsyncContext asyncContext;

    @Before
    public void setUp() {
        executor = Executors.newSingleThreadExecutor();
        asyncContext = (AsyncContext) AsyncContext.create(executor);
    }

    @Test
    public void testEvaluateInternal() {
        Task<Integer> task = mock(Task.class);
        EvalContext context = mock(EvalContext.class);
        when(context.evaluateInternal(task, context)).thenReturn(new Value<Integer>() {
            @Override
            public void consume(Consumer<Integer> consumer) {
                consumer.accept(42);
            }

            @Override
            public void onFail(Consumer<Throwable> consumer) {
                // No failure for this test
            }
        });

        Value<Integer> result = asyncContext.evaluateInternal(task, context);
        assertNotNull(result);
        assertEquals(Integer.valueOf(42), result.get());
    }

    @Test
    public void testValue() {
        Fn<Integer> fn = () -> 10;
        Value<Integer> result = asyncContext.value(fn);
        assertNotNull(result);
        assertEquals(Integer.valueOf(10), result.get());
    }

    @Test
    public void testImmediateValue() {
        Value<Integer> result = asyncContext.immediateValue(5);
        assertNotNull(result);
        assertEquals(Integer.valueOf(5), result.get());
    }

    @Test
    public void testPromise() {
        Promise<Integer> promise = asyncContext.promise();
        assertNotNull(promise);
        assertFalse(promise.isDone());

        promise.set(100);
        assertTrue(promise.isDone());
        assertEquals(Integer.valueOf(100), promise.value().get());
    }
}
