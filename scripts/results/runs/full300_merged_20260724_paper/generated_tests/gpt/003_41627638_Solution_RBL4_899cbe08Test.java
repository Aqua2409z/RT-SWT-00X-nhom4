
package com.github.SingleNumber;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution_RBL4_899cbe08Test {

    @Test
    public void testSingleNumberWithUniqueElement() {
        Solution solution = new Solution();
        int[] nums = {4, 1, 2, 1, 2};
        assertEquals(4, solution.singleNumber(nums));
    }

    @Test
    public void testSingleNumberWithNegativeAndPositive() {
        Solution solution = new Solution();
        int[] nums = {-1, -1, 2, 2, 3};
        assertEquals(3, solution.singleNumber(nums));
    }

    @Test
    public void testSingleNumberWithAllNegative() {
        Solution solution = new Solution();
        int[] nums = {-2, -2, -3, -3, -1};
        assertEquals(-1, solution.singleNumber(nums));
    }

    @Test
    public void testSingleNumberWithSingleElement() {
        Solution solution = new Solution();
        int[] nums = {5};
        assertEquals(5, solution.singleNumber(nums));
    }

    @Test
    public void testSingleNumberWithMultipleDuplicates() {
        Solution solution = new Solution();
        int[] nums = {7, 8, 7, 8, 9, 10, 10};
        assertEquals(9, solution.singleNumber(nums));
    }
}
