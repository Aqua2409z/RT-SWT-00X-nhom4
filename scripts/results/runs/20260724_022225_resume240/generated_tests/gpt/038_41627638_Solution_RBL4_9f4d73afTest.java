
package com.github.ReverseLinkedList;

import org.junit.Test;
import static org.junit.Assert.*;

public class Solution_RBL4_9f4d73afTest {

    @Test
    public void testReverseList_NullInput() {
        Solution solution = new Solution();
        ListNode result = solution.reverseList(null);
        assertNull(result);
    }

    @Test
    public void testReverseList_SingleNode() {
        Solution solution = new Solution();
        ListNode head = new ListNode(1);
        ListNode result = solution.reverseList(head);
        assertNotNull(result);
        assertEquals(1, result.val);
        assertNull(result.next);
    }

    @Test
    public void testReverseList_TwoNodes() {
        Solution solution = new Solution();
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        ListNode result = solution.reverseList(head);
        assertNotNull(result);
        assertEquals(2, result.val);
        assertNotNull(result.next);
        assertEquals(1, result.next.val);
        assertNull(result.next.next);
    }

    @Test
    public void testReverseList_MultipleNodes() {
        Solution solution = new Solution();
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        ListNode result = solution.reverseList(head);
        assertNotNull(result);
        assertEquals(4, result.val);
        assertNotNull(result.next);
        assertEquals(3, result.next.val);
        assertNotNull(result.next.next);
        assertEquals(2, result.next.next.val);
        assertNotNull(result.next.next.next);
        assertEquals(1, result.next.next.next.val);
        assertNull(result.next.next.next.next);
    }

    @Test
    public void testReverseList_EmptyList() {
        Solution solution = new Solution();
        ListNode head = new ListNode(0);
        head.next = null;
        ListNode result = solution.reverseList(head);
        assertNotNull(result);
        assertEquals(0, result.val);
        assertNull(result.next);
    }
}
