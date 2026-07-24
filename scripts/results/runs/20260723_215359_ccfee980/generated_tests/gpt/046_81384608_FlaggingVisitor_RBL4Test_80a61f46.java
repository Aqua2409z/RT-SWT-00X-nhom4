
package com.thinkbiganalytics.kylo.catalog.spark.sources.spark;

import org.apache.spark.Accumulable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class FlaggingVisitor_RBL4Test_80a61f46 {

    private Accumulable<Boolean, Boolean> mockAccumulable;
    private FlaggingVisitor flaggingVisitor;

    @Before
    public void setUp() {
        mockAccumulable = Mockito.mock(Accumulable.class);
        flaggingVisitor = new FlaggingVisitor(mockAccumulable);
    }

    @Test
    public void testApplyIncrementsAccumulator() {
        // Act
        Object result = flaggingVisitor.apply(null);

        // Assert
        verify(mockAccumulable).$plus$eq(Boolean.TRUE);
        assertNull(result);
    }

    @Test
    public void testApplyWithValue() {
        // Act
        Object testValue = new Object();
        Object result = flaggingVisitor.apply(testValue);

        // Assert
        verify(mockAccumulable).$plus$eq(Boolean.TRUE);
        assertEquals(testValue, result);
    }
}
