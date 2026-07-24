
package com.spotify.flo.contrib.bigquery;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.cloud.bigquery.JobInfo;
import com.spotify.flo.EvalContext;
import org.junit.Before;
import org.junit.Test;

public class BigQueryOperator_RBL4_6f1aba76Test {

    private BigQueryOperator<String> operator;
    private BigQueryOperation<String> operation;
    private BigQueryOperation.Provider<String> provider;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        operator = BigQueryOperator.create();
        operation = mock(BigQueryOperation.class);
        provider = mock(BigQueryOperation.Provider.class);
        evalContext = mock(EvalContext.class);
    }

    @Test
    public void testPerformWithJobRequest() {
        JobInfo jobInfo = mock(JobInfo.class);
        when(operation.jobRequest).thenReturn(() -> jobInfo);
        when(operation.success).thenReturn(result -> "Success");

        String result = operator.perform(operation, null);

        assertEquals("Success", result);
        verify(operation).jobRequest();
        verify(operation).success();
    }

    @Test(expected = AssertionError.class)
    public void testPerformWithoutJobRequest() {
        when(operation.jobRequest).thenReturn(null);
        operator.perform(operation, null);
    }

    @Test
    public void testProvide() {
        when(evalContext.get()).thenReturn(provider);
        Provider<String> result = operator.provide(evalContext);
        assertNotNull(result);
    }
}
