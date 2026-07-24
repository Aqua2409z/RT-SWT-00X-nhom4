
package com.github.MaximumDepthofBinaryTree;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution_RBL4_194905a6Test {

    @Test
    public void testMaxDepth_NullTree() {
        Solution solution = new Solution();
        TreeNode root = null;
        assertEquals(0, solution.maxDepth(root));
    }

    @Test
    public void testMaxDepth_OneNode() {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        assertEquals(1, solution.maxDepth(root));
    }

    @Test
    public void testMaxDepth_TwoLevels() {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        assertEquals(2, solution.maxDepth(root));
    }

    @Test
    public void testMaxDepth_ThreeLevels() {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        assertEquals(3, solution.maxDepth(root));
    }

    @Test
    public void testMaxDepth_ImbalancedTree() {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(3);
        root.left.left.left = new TreeNode(4);
        assertEquals(4, solution.maxDepth(root));
    }

    @Test
    public void testMaxDepth_BalancedTree() {
        Solution solution = new Solution();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
        assertEquals(3, solution.maxDepth(root));
    }
}
