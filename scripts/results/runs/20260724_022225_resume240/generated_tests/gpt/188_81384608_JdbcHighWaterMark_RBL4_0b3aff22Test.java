
package com.thinkbiganalytics.kylo.catalog.spark.sources.jdbc;

import com.thinkbiganalytics.kylo.catalog.api.KyloCatalogClient;
import org.apache.spark.SparkContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class JdbcHighWaterMark_RBL4_0b3aff22Test {

    private JdbcHighWaterMark highWaterMark;
    private KyloCatalogClient<?> mockClient;

    @Before
    public void setUp() {
        mockClient = Mockito.mock(KyloCatalogClient.class);
        highWaterMark = new JdbcHighWaterMark("testHighWaterMark", mockClient);
    }

    @Test
    public void testConstructor() {
        assertNotNull(highWaterMark);
        assertEquals("testHighWaterMark", highWaterMark.getName());
        assertNull(highWaterMark.getValue());
    }

    @Test
    public void testAccumulateWithNullValue() {
        highWaterMark.accumulate(null);
        assertNull(highWaterMark.getValue());
        Mockito.verify(mockClient, Mockito.never()).setHighWaterMarks(Mockito.anyMap());
    }

    @Test
    public void testAccumulateWithLowerValue() {
        highWaterMark.accumulate(10L);
        assertEquals(Long.valueOf(10), highWaterMark.getValue());
        Mockito.verify(mockClient).setHighWaterMarks(Mockito.anyMap());
    }

    @Test
    public void testAccumulateWithHigherValue() {
        highWaterMark.accumulate(10L);
        highWaterMark.accumulate(20L);
        assertEquals(Long.valueOf(20), highWaterMark.getValue());
        Mockito.verify(mockClient, Mockito.times(2)).setHighWaterMarks(Mockito.anyMap());
    }

    @Test
    public void testAccumulateWithSameValue() {
        highWaterMark.accumulate(10L);
        highWaterMark.accumulate(10L);
        assertEquals(Long.valueOf(10), highWaterMark.getValue());
        Mockito.verify(mockClient, Mockito.times(1)).setHighWaterMarks(Mockito.anyMap());
    }

    @Test
    public void testEqualsAndHashCode() {
        JdbcHighWaterMark anotherHighWaterMark = new JdbcHighWaterMark("testHighWaterMark", mockClient);
        assertTrue(highWaterMark.equals(anotherHighWaterMark));
        assertEquals(highWaterMark.hashCode(), anotherHighWaterMark.hashCode());

        anotherHighWaterMark.accumulate(10L);
        assertFalse(highWaterMark.equals(anotherHighWaterMark));
    }

    @Test
    public void testSetFormatter() {
        highWaterMark.setFormatter(value -> "Formatted: " + value);
        highWaterMark.accumulate(10L);
        Mockito.verify(mockClient).setHighWaterMarks(Mockito.anyMap());
    }

    @Test
    public void testToString() {
        assertEquals("JdbcHighWaterMark{name='testHighWaterMark', value=null}", highWaterMark.toString());
        highWaterMark.accumulate(10L);
        assertEquals("JdbcHighWaterMark{name='testHighWaterMark', value=10}", highWaterMark.toString());
    }
}
