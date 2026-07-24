
package com.spotify.flo.context;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.spotify.flo.EvalContext;
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
        Task<String> task = mock(Task.class);
        EvalContext context = mock(EvalContext.class);
        when(context.evaluateInternal(task, context)).thenReturn(new Value<String>() {
            @Override
            public void consume(Consumer<String> consumer) {
                consumer.accept("result");
            }

            @Override
            public void onFail(Consumer<Throwable> consumer) {
                // No failure
            }
        });

        Value<String> result = asyncContext.evaluateInternal(task, context);
        assertNotNull(result);
        assertEquals("result", result.get());
    }

    @Test
    public void testValue() {
        Fn<String> fn = () -> "testValue";
        Value<String> result = asyncContext.value(fn);
        assertNotNull(result);
        assertEquals("testValue", result.get());
    }

    @Test
    public void testImmediateValue() {
        Value<String> result = asyncContext.immediateValue("immediate");
        assertNotNull(result);
        assertEquals("immediate", result.get());
    }

    @Test
    public void testPromise() {
        Promise<String> promise = asyncContext.promise();
        assertNotNull(promise);
        assertFalse(promise.isDone());

        promise.set("promiseValue");
        assertTrue(promise.isDone());
        assertEquals("promiseValue", promise.value().get());
    }
}
