
package com.github.IslandPerimeter;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution_RBL4_562560bfTest {

    @Test
    public void testIslandPerimeter_EmptyGrid() {
        Solution solution = new Solution();
        int[][] grid = {};
        assertEquals(0, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_NoLand() {
        Solution solution = new Solution();
        int[][] grid = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
        };
        assertEquals(0, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_SingleLand() {
        Solution solution = new Solution();
        int[][] grid = {
            {1}
        };
        assertEquals(4, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_SingleRowLand() {
        Solution solution = new Solution();
        int[][] grid = {
            {1, 0, 1}
        };
        assertEquals(8, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_SingleColumnLand() {
        Solution solution = new Solution();
        int[][] grid = {
            {1},
            {0},
            {1}
        };
        assertEquals(8, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_ComplexShape() {
        Solution solution = new Solution();
        int[][] grid = {
            {1, 1, 1, 0},
            {1, 0, 0, 0},
            {1, 1, 1, 0}
        };
        assertEquals(12, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_AdjacentLands() {
        Solution solution = new Solution();
        int[][] grid = {
            {1, 1},
            {1, 1}
        };
        assertEquals(8, solution.islandPerimeter(grid));
    }

    @Test
    public void testIslandPerimeter_IslandWithHoles() {
        Solution solution = new Solution();
        int[][] grid = {
            {1, 1, 1, 0},
            {1, 0, 1, 0},
            {1, 1, 1, 0}
        };
        assertEquals(12, solution.islandPerimeter(grid));
    }
}
