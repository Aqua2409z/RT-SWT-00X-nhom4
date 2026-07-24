
package com.github.battleship;

import org.junit.Test;
import static org.junit.Assert.*;

public class Solution_RBL4Test_d71b298e {

    @Test
    public void testCountBattleshipsWithSingleShip() {
        char[][] board = {
            {'X', 'X', '.', 'X'},
            {'.', '.', '.', 'X'},
            {'.', '.', '.', '.'}
        };
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(3, result);
    }

    @Test
    public void testCountBattleshipsWithNoShips() {
        char[][] board = {
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'},
            {'.', '.', '.', '.'}
        };
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(0, result);
    }

    @Test
    public void testCountBattleshipsWithMultipleShips() {
        char[][] board = {
            {'X', '.', 'X'},
            {'.', '.', '.'},
            {'X', 'X', 'X'}
        };
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(3, result);
    }

    @Test
    public void testCountBattleshipsWithAdjacentShips() {
        char[][] board = {
            {'X', 'X', '.', 'X'},
            {'X', '.', '.', 'X'},
            {'.', '.', '.', '.'}
        };
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(3, result);
    }

    @Test
    public void testCountBattleshipsWithComplexBoard() {
        char[][] board = {
            {'X', '.', 'X', 'X'},
            {'.', 'X', '.', '.'},
            {'X', '.', 'X', 'X'},
            {'.', '.', '.', 'X'}
        };
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(5, result);
    }

    @Test
    public void testCountBattleshipsWithEmptyBoard() {
        char[][] board = {};
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(0, result);
    }

    @Test
    public void testCountBattleshipsWithSingleCellShip() {
        char[][] board = {
            {'X'}
        };
        Solution solution = new Solution();
        int result = solution.countBattleships(board);
        assertEquals(1, result);
    }
}
