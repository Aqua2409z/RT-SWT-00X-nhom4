
package com.github.maxpointonline.plainloop;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution_RBL4Test_5e962fc1 {

    @Test
    public void testMaxPointsWithNoPoints() {
        Solution solution = new Solution();
        Point[] points = {};
        assertEquals(0, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithOnePoint() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1) };
        assertEquals(1, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithTwoPoints() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(2, 2) };
        assertEquals(2, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithThreeCollinearPoints() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(2, 2), new Point(3, 3) };
        assertEquals(3, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithThreeNonCollinearPoints() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(2, 2), new Point(3, 4) };
        assertEquals(2, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithDuplicatePoints() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(1, 1), new Point(2, 2) };
        assertEquals(3, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithVerticalLine() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(1, 2), new Point(1, 3) };
        assertEquals(3, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithHorizontalLine() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(2, 1), new Point(3, 1) };
        assertEquals(3, solution.maxPoints(points));
    }

    @Test
    public void testMaxPointsWithMixedPoints() {
        Solution solution = new Solution();
        Point[] points = { new Point(1, 1), new Point(2, 2), new Point(3, 3), new Point(1, 2), new Point(2, 3) };
        assertEquals(3, solution.maxPoints(points));
    }
}
