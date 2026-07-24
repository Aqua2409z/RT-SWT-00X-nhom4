
package com.github.battleship;

import org.junit.Test;
import static org.junit.Assert.*;

public class Solution_RBL4Test_d71b298e {

    @Test
    public void testCountBattleshipsWithNoBattleships() {
        Solution solution = new Solution();
        char[][] board = {
            {'0', '0', '0'},
            {'0', '0', '0'},
            {'0', '0', '0'}
        };
        assertEquals(0, solution.countBattleships(board));
    }

    @Test
    public void testCountBattleshipsWithSingleBattleship() {
        Solution solution = new Solution();
        char[][] board = {
            {'X', '0', '0'},
            {'0', '0', '0'},
            {'0', '0', 'X'}
        };
        assertEquals(2, solution.countBattleships(board));
    }

    @Test
    public void testCountBattleshipsWithAdjacentBattleships() {
        Solution solution = new Solution();
        char[][] board = {
            {'X', 'X', '0'},
            {'0', '0', '0'},
            {'X', '0', 'X'}
        };
        assertEquals(3, solution.countBattleships(board));
    }

    @Test
    public void testCountBattleshipsWithComplexBoard() {
        Solution solution = new Solution();
        char[][] board = {
            {'X', '0', 'X', 'X'},
            {'0', '0', '0', '0'},
            {'X', '0', '0', 'X'}
        };
        assertEquals(4, solution.countBattleships(board));
    }

    @Test
    public void testCountBattleshipsWithNoGapBetweenShips() {
        Solution solution = new Solution();
        char[][] board = {
            {'X', 'X', 'X'},
            {'X', 'X', 'X'},
            {'0', '0', '0'}
        };
        assertEquals(1, solution.countBattleships(board));
    }
}
