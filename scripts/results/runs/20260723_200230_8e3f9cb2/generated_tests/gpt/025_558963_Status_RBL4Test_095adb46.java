package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.Status;
import org.junit.Test;
import static org.junit.Assert.*;

public class Status_RBL4Test_095adb46 {

    @Test
    public void testGetCode() {
        assertEquals(200, Status.OK.getCode());
        assertEquals(404, Status.NOT_FOUND.getCode());
    }

    @Test
    public void testGetName() {
        assertEquals("OK", Status.OK.getName());
        assertEquals("Not Found", Status.NOT_FOUND.getName());
    }

    @Test
    public void testGetCategory() {
        assertEquals(Status.Category.SUCCESS, Status.OK.getCategory());
        assertEquals(Status.Category.CLIENT_ERROR, Status.NOT_FOUND.getCategory());
    }

    @Test
    public void testIsClientError() {
        assertTrue(Status.BAD_REQUEST.isClientError());
        assertFalse(Status.OK.isClientError());
    }

    @Test
    public void testIsServerError() {
        assertTrue(Status.INTERNAL_SERVER_ERROR.isServerError());
        assertFalse(Status.OK.isServerError());
    }

    @Test
    public void testIsBodyContentAllowed() {
        assertFalse(Status.NO_CONTENT.isBodyContentAllowed());
        assertTrue(Status.OK.isBodyContentAllowed());
    }

    @Test
    public void testCompareTo() {
        assertTrue(Status.OK.compareTo(Status.NOT_FOUND) < 0);
        assertTrue(Status.NOT_FOUND.compareTo(Status.OK) > 0);
        assertEquals(0, Status.OK.compareTo(Status.OK));
    }

    @Test
    public void testEquals() {
        assertEquals(Status.OK, Status.OK);
        assertNotEquals(Status.OK, Status.NOT_FOUND);
    }

    @Test
    public void testHashCode() {
        assertEquals(Status.OK.hashCode(), Status.OK.hashCode());
        assertNotEquals(Status.OK.hashCode(), Status.NOT_FOUND.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("200 OK", Status.OK.toString());
        assertEquals("404 Not Found", Status.NOT_FOUND.toString());
    }

    @Test
    public void testValueOf() {
        assertEquals(Status.OK, Status.valueOf(200));
        assertEquals(Status.NOT_FOUND, Status.valueOf(404));
        assertEquals(new Status(999, "Unknown"), Status.valueOf(999));
    }

    @Test
    public void testValues() {
        Status[] statuses = Status.values();
        assertTrue(statuses.length > 0);
        assertTrue(java.util.Arrays.asList(statuses).contains(Status.OK));
        assertTrue(java.util.Arrays.asList(statuses).contains(Status.NOT_FOUND));
    }
}
