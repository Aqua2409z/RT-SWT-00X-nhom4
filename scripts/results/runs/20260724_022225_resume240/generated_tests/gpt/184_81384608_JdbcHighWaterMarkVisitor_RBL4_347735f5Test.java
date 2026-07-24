
package com.thinkbiganalytics.kylo.catalog.spark.sources.jdbc;

import org.apache.spark.Accumulable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import scala.Function1;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JdbcHighWaterMarkVisitor_RBL4_347735f5Test {

    private Accumulable<JdbcHighWaterMark, Long> accumulable;
    private Function1<Integer, Long> toLong;
    private JdbcHighWaterMarkVisitor<Integer> visitor;

    @Before
    public void setUp() {
        accumulable = Mockito.mock(Accumulable.class);
        toLong = Mockito.mock(Function1.class);
        visitor = new JdbcHighWaterMarkVisitor<>(accumulable, toLong);
    }

    @Test
    public void testApplyWithNonNullValue() {
        Integer inputValue = 5;
        Long convertedValue = 5L;

        when(toLong.apply(inputValue)).thenReturn(convertedValue);
        when(accumulable.$plus$eq(convertedValue)).thenReturn(accumulable);

        Integer result = visitor.apply(inputValue);

        verify(toLong).apply(inputValue);
        verify(accumulable).$plus$eq(convertedValue);
        assertEquals(inputValue, result);
    }

    @Test
    public void testApplyWithNullValue() {
        Integer inputValue = null;

        when(toLong.apply(inputValue)).thenReturn(null);
        when(accumulable.$plus$eq(null)).thenReturn(accumulable);

        Integer result = visitor.apply(inputValue);

        verify(toLong).apply(inputValue);
        verify(accumulable).$plus$eq(null);
        assertEquals(inputValue, result);
    }
}
