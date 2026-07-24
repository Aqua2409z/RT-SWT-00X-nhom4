
package com.spotify.flo;

import org.junit.Test;
import static org.junit.Assert.*;

public class TaskIds_RBL4Test_b7d06a10 {

    @Test
    public void testCreateValidTaskId() {
        TaskId taskId = TaskIds.create("taskName", "arg1", "arg2");
        assertNotNull(taskId);
        assertEquals("taskName(arg1,arg2)#" + String.format("%08x", ("taskName".hashCode() * 1000003 ^ Objects.hash("arg1", "arg2"))), taskId.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateTaskIdWithParenthesisInName() {
        TaskIds.create("task(Name)", "arg1");
    }

    @Test
    public void testParseValidStringId() {
        TaskId taskId = TaskIds.parse("taskName(arg1,arg2)#deadbeef");
        assertNotNull(taskId);
        assertEquals("taskName(arg1,arg2)#deadbeef", taskId.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStringIdMissingParenthesis() {
        TaskIds.parse("taskNamearg1,arg2#deadbeef");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStringIdMissingHash() {
        TaskIds.parse("taskName(arg1,arg2)");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidStringIdWithParenthesisInName() {
        TaskIds.parse("task(Name)(arg1,arg2)#deadbeef");
    }

    @Test
    public void testToString() {
        TaskId taskId = TaskIds.create("taskName", "arg1", "arg2");
        assertEquals("taskName(arg1,arg2)#" + String.format("%08x", ("taskName".hashCode() * 1000003 ^ Objects.hash("arg1", "arg2"))), taskId.toString());
    }
}
