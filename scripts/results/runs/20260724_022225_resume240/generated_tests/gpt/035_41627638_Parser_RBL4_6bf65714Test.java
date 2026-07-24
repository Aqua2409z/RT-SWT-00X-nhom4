
package com.github.battleship;

import org.junit.Test;
import static org.junit.Assert.*;

public class Parser_RBL4_6bf65714Test {

    @Test
    public void testBoardToRootCell_ValidBoard() {
        char[][] board = {
            {'A', 'B', 'C'},
            {'D', 'E', 'F'},
            {'G', 'H', 'I'}
        };
        
        Cell rootCell = Parser.boardToRootCell(board);
        
        assertNotNull(rootCell);
        assertEquals('A', rootCell.getValue());
        assertNotNull(rootCell.getRight());
        assertEquals('B', rootCell.getRight().getValue());
        assertNotNull(rootCell.getDown());
        assertEquals('D', rootCell.getDown().getValue());
    }

    @Test
    public void testBoardToRootCell_EmptyBoard() {
        char[][] board = new char[0][0];
        
        Cell rootCell = Parser.boardToRootCell(board);
        
        assertNotNull(rootCell);
        assertTrue(rootCell instanceof NotACell);
    }

    @Test
    public void testBoardToRootCell_SingleCellBoard() {
        char[][] board = {
            {'X'}
        };
        
        Cell rootCell = Parser.boardToRootCell(board);
        
        assertNotNull(rootCell);
        assertEquals('X', rootCell.getValue());
        assertNull(rootCell.getRight());
        assertNull(rootCell.getDown());
    }

    @Test
    public void testBoardToRootCell_NonRectangularBoard() {
        char[][] board = {
            {'A', 'B'},
            {'C'}
        };
        
        Cell rootCell = Parser.boardToRootCell(board);
        
        assertNotNull(rootCell);
        assertEquals('A', rootCell.getValue());
        assertNotNull(rootCell.getRight());
        assertEquals('B', rootCell.getRight().getValue());
        assertNotNull(rootCell.getDown());
        assertEquals('C', rootCell.getDown().getValue());
        assertNull(rootCell.getDown().getRight());
    }
}
