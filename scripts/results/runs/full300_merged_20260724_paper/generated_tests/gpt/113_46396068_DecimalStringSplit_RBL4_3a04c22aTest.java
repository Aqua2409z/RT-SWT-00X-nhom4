
package com.kakao.hbase.common.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DecimalStringSplit_RBL4_3a04c22aTest {

    @Test
    public void testDigits() {
        assertEquals(1, DecimalStringSplit.digits(0));
        assertEquals(1, DecimalStringSplit.digits(5));
        assertEquals(2, DecimalStringSplit.digits(10));
        assertEquals(3, DecimalStringSplit.digits(100));
        assertEquals(3, DecimalStringSplit.digits(-100));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSplitPointIndexOutOfBounds() {
        DecimalStringSplit.splitPoint(1, 10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitPointNumRegionsExceedsCardinality() {
        DecimalStringSplit.splitPoint(5, 3, 0);
    }

    @Test
    public void testSplitPoint() {
        assertEquals(0, DecimalStringSplit.splitPoint(3, 10, 0));
        assertEquals(3, DecimalStringSplit.splitPoint(3, 10, 1));
        assertEquals(6, DecimalStringSplit.splitPoint(3, 10, 2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSplitTooManyRegions() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.split(DecimalStringSplit.MAX_NUM_REGIONS + 1);
    }

    @Test
    public void testSplit() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        byte[][] splits = decimalStringSplit.split(3);
        assertEquals(2, splits.length);
        assertArrayEquals("0".getBytes(), splits[0]);
        assertArrayEquals("5".getBytes(), splits[1]);
    }

    @Test(expected = IllegalStateException.class)
    public void testFirstRowNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.firstRow();
    }

    @Test(expected = IllegalStateException.class)
    public void testLastRowNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.lastRow();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFirstRowStringNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.setFirstRow("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testSetLastRowStringNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.setLastRow("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testStrToRowNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.strToRow("test");
    }

    @Test(expected = IllegalStateException.class)
    public void testRowToStrNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.rowToStr("test".getBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void testSeparatorNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.separator();
    }

    @Test(expected = IllegalStateException.class)
    public void testSetFirstRowByteArrayNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.setFirstRow("test".getBytes());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetLastRowByteArrayNotImplemented() {
        DecimalStringSplit decimalStringSplit = new DecimalStringSplit(10);
        decimalStringSplit.setLastRow("test".getBytes());
    }
}
