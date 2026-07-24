
package com.github.InvertBinaryTree;

import org.junit.Test;
import static org.junit.Assert.*;

public class Solution_RBL4_900e296fTest {

    @Test
    public void testInvertTree() {
        Solution solution = new Solution();

        // Test case 1: Simple tree
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        TreeNode inverted1 = solution.invertTree(root1);
        assertEquals(1, inverted1.val);
        assertEquals(3, inverted1.left.val);
        assertEquals(2, inverted1.right.val);

        // Test case 2: Tree with only left children
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.left.left = new TreeNode(3);
        TreeNode inverted2 = solution.invertTree(root2);
        assertEquals(1, inverted2.val);
        assertEquals(null, inverted2.left);
        assertEquals(2, inverted2.right.val);
        assertEquals(null, inverted2.right.left);
        assertEquals(3, inverted2.right.right.val);

        // Test case 3: Tree with only right children
        TreeNode root3 = new TreeNode(1);
        root3.right = new TreeNode(2);
        root3.right.right = new TreeNode(3);
        TreeNode inverted3 = solution.invertTree(root3);
        assertEquals(1, inverted3.val);
        assertEquals(2, inverted3.left.val);
        assertEquals(null, inverted3.left.left);
        assertEquals(3, inverted3.left.right.val);

        // Test case 4: Empty tree
        TreeNode root4 = null;
        TreeNode inverted4 = solution.invertTree(root4);
        assertNull(inverted4);

        // Test case 5: Single node tree
        TreeNode root5 = new TreeNode(1);
        TreeNode inverted5 = solution.invertTree(root5);
        assertEquals(1, inverted5.val);
        assertNull(inverted5.left);
        assertNull(inverted5.right);
    }
}
