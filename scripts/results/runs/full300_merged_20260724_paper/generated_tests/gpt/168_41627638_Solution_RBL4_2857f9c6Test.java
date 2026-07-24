
package com.github.MoveZeroes;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Solution_RBL4_2857f9c6Test {

    @Test
    public void testMoveZeroesWithMixedNumbers() {
        Solution solution = new Solution();
        int[] nums = {0, 1, 0, 3, 12};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{1, 3, 12, 0, 0}, nums);
    }

    @Test
    public void testMoveZeroesWithAllZeros() {
        Solution solution = new Solution();
        int[] nums = {0, 0, 0, 0};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{0, 0, 0, 0}, nums);
    }

    @Test
    public void testMoveZeroesWithNoZeros() {
        Solution solution = new Solution();
        int[] nums = {1, 2, 3, 4};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{1, 2, 3, 4}, nums);
    }

    @Test
    public void testMoveZeroesWithLeadingZeros() {
        Solution solution = new Solution();
        int[] nums = {0, 0, 1, 2, 3};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0}, nums);
    }

    @Test
    public void testMoveZeroesWithTrailingZeros() {
        Solution solution = new Solution();
        int[] nums = {1, 2, 3, 0, 0};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0}, nums);
    }

    @Test
    public void testMoveZeroesWithSingleElement() {
        Solution solution = new Solution();
        int[] nums = {0};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{0}, nums);

        nums = new int[]{1};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{1}, nums);
    }

    @Test
    public void testMoveZeroesWithMultipleZeros() {
        Solution solution = new Solution();
        int[] nums = {0, 1, 0, 2, 0, 3};
        solution.moveZeroes(nums);
        assertArrayEquals(new int[]{1, 2, 3, 0, 0, 0}, nums);
    }
}
