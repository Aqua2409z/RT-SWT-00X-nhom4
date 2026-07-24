
package com.spotify.flo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import java.util.Optional;

public class EvalContextWithTask_RBL4_ef73684bTest {

    private EvalContext mockDelegate;
    private Task<String> mockTask;
    private EvalContextWithTask evalContextWithTask;

    @Before
    public void setUp() {
        mockDelegate = new EvalContext() {
            @Override
            public Optional<Task<?>> currentTask() {
                return Optional.empty();
            }
        };
        mockTask = new Task<String>() {
            @Override
            public String toString() {
                return "MockTask";
            }
        };
        evalContextWithTask = EvalContextWithTask.withTask(mockDelegate, mockTask);
    }

    @Test
    public void testWithTask() {
        assertNotNull(evalContextWithTask);
    }

    @Test
    public void testCurrentTask() {
        Optional<Task<?>> currentTask = evalContextWithTask.currentTask();
        assertTrue(currentTask.isPresent());
        assertEquals(mockTask, currentTask.get());
    }

    @Test(expected = NullPointerException.class)
    public void testWithTask_NullTask() {
        EvalContextWithTask.withTask(mockDelegate, null);
    }

    @Test(expected = NullPointerException.class)
    public void testWithTask_NullDelegate() {
        EvalContextWithTask.withTask(null, mockTask);
    }
}
