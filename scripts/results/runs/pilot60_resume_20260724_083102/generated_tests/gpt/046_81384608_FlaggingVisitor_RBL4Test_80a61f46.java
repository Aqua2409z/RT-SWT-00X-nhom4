
package com.thinkbiganalytics.kylo.catalog.spark.sources.spark;

import org.apache.spark.Accumulable;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class FlaggingVisitor_RBL4Test_80a61f46 {

    private Accumulable<Boolean, Boolean> mockAccumulable;
    private FlaggingVisitor flaggingVisitor;

    @Before
    public void setUp() {
        mockAccumulable = mock(Accumulable.class);
        flaggingVisitor = new FlaggingVisitor(mockAccumulable);
    }

    @Test
    public void testApplyCallsAccumulable() {
        Object testValue = new Object();
        flaggingVisitor.apply(testValue);
        verify(mockAccumulable).$plus$eq(Boolean.TRUE);
    }

    @Test
    public void testApplyReturnsInputValue() {
        Object testValue = new Object();
        Object result = flaggingVisitor.apply(testValue);
        assertSame(testValue, result);
    }

    @Test
    public void testApplyReturnsNull() {
        Object result = flaggingVisitor.apply(null);
        assertNull(result);
    }
}
