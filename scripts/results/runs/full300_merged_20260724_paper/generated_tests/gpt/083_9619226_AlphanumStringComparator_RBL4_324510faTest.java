
package org.springframework.data.simpledb.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class AlphanumStringComparator_RBL4_324510faTest {

    private final AlphanumStringComparator comparator = new AlphanumStringComparator();

    @Test
    public void testCompare_WithValidInputs_ShouldReturnPositive() {
        assertTrue(comparator.compare("10@attr", "2@attr") > 0);
    }

    @Test
    public void testCompare_WithValidInputs_ShouldReturnNegative() {
        assertTrue(comparator.compare("2@attr", "10@attr") < 0);
    }

    @Test
    public void testCompare_WithEqualNumbers_ShouldReturnZero() {
        assertEquals(0, comparator.compare("5@attr", "5@attr"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompare_WithMissingDigit_ShouldThrowException() {
        comparator.compare("attr", "2@attr");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCompare_WithMissingDigitInBoth_ShouldThrowException() {
        comparator.compare("attr1", "attr2");
    }

    @Test
    public void testCompare_WithDifferentAttributes_ShouldCompareByNumber() {
        assertTrue(comparator.compare("3@b", "3@a") == 0);
        assertTrue(comparator.compare("1@b", "1@c") == 0);
    }

    @Test
    public void testCompare_WithLeadingZeros_ShouldCompareCorrectly() {
        assertTrue(comparator.compare("02@attr", "2@attr") == 0);
        assertTrue(comparator.compare("10@attr", "0010@attr") == 0);
    }
}
