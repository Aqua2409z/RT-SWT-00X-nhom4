
package com.thinkbiganalytics.kylo.catalog.spark.sources.jdbc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class JdbcHighWaterMarkAccumulableParam_RBL4_1975b3e4Test {

    private JdbcHighWaterMarkAccumulableParam accumulableParam;
    private JdbcHighWaterMark accumulator;
    private JdbcHighWaterMark right;

    @Before
    public void setUp() {
        accumulableParam = new JdbcHighWaterMarkAccumulableParam();
        accumulator = new JdbcHighWaterMark();
        right = new JdbcHighWaterMark();
    }

    @Test
    public void testAddAccumulator() {
        Long value = 10L;
        accumulator.accumulate(value);
        JdbcHighWaterMark result = accumulableParam.addAccumulator(accumulator, value);
        assertEquals(accumulator.getValue(), result.getValue());
    }

    @Test
    public void testAddInPlace() {
        Long value1 = 10L;
        Long value2 = 20L;
        accumulator.accumulate(value1);
        right.accumulate(value2);
        JdbcHighWaterMark result = accumulableParam.addInPlace(accumulator, right);
        assertEquals((Long) (value1 + value2), result.getValue());
    }

    @Test
    public void testZero() {
        JdbcHighWaterMark result = accumulableParam.zero(accumulator);
        assertEquals(0L, result.getValue().longValue());
    }
}
