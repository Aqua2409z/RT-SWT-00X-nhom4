package io.grpc;

import static org.junit.Assert.*;
import org.junit.Test;

public class StatusTest {

    @Test
    public void testStatusCodes() {
        assertEquals(Status.OK.getCode(), Status.Code.OK);
        assertEquals(Status.CANCELLED.getCode(), Status.Code.CANCELLED);
        assertEquals(Status.UNKNOWN.getCode(), Status.Code.UNKNOWN);
        assertEquals(Status.INVALID_ARGUMENT.getCode(), Status.Code.INVALID_ARGUMENT);
        assertEquals(Status.DEADLINE_EXCEEDED.getCode(), Status.Code.DEADLINE_EXCEEDED);
        assertEquals(Status.NOT_FOUND.getCode(), Status.Code.NOT_FOUND);
        assertEquals(Status.ALREADY_EXISTS.getCode(), Status.Code.ALREADY_EXISTS);
        assertEquals(Status.PERMISSION_DENIED.getCode(), Status.Code.PERMISSION_DENIED);
        assertEquals(Status.RESOURCE_EXHAUSTED.getCode(), Status.Code.RESOURCE_EXHAUSTED);
        assertEquals(Status.FAILED_PRECONDITION.getCode(), Status.Code.FAILED_PRECONDITION);
        assertEquals(Status.ABORTED.getCode(), Status.Code.ABORTED);
        assertEquals(Status.OUT_OF_RANGE.getCode(), Status.Code.OUT_OF_RANGE);
        assertEquals(Status.UNIMPLEMENTED.getCode(), Status.Code.UNIMPLEMENTED);
        assertEquals(Status.INTERNAL.getCode(), Status.Code.INTERNAL);
        assertEquals(Status.UNAVAILABLE.getCode(), Status.Code.UNAVAILABLE);
        assertEquals(Status.DATA_LOSS.getCode(), Status.Code.DATA_LOSS);
        assertEquals(Status.UNAUTHENTICATED.getCode(), Status.Code.UNAUTHENTICATED);
    }

    @Test
    public void testWithDescription() {
        Status status = Status.INVALID_ARGUMENT.withDescription("Invalid input");
        assertEquals("Invalid input", status.getDescription());
        assertEquals(Status.INVALID_ARGUMENT.getCode(), status.getCode());
    }

    @Test
    public void testWithCause() {
        Throwable cause = new Exception("Some error");
        Status status = Status.INTERNAL.withCause(cause);
        assertEquals(cause, status.getCause());
        assertEquals(Status.INTERNAL.getCode(), status.getCode());
    }

    @Test
    public void testIsOk() {
        assertTrue(Status.OK.isOk());
        assertFalse(Status.INVALID_ARGUMENT.isOk());
    }

    @Test
    public void testFromCodeValue() {
        assertEquals(Status.OK, Status.fromCodeValue(0));
        assertEquals(Status.INVALID_ARGUMENT, Status.fromCodeValue(3));
        assertEquals(Status.UNKNOWN, Status.fromCodeValue(100)); // Out of range
    }

    @Test
    public void testFromThrowable() {
        Throwable cause = new StatusRuntimeException(Status.INVALID_ARGUMENT);
        Status status = Status.fromThrowable(cause);
        assertEquals(Status.INVALID_ARGUMENT, status);
    }

    @Test
    public void testToString() {
        Status status = Status.NOT_FOUND.withDescription("File not found");
        String expected = "Status{code=NOT_FOUND, description=File not found, cause=null}";
        assertTrue(status.toString().contains("NOT_FOUND"));
        assertTrue(status.toString().contains("File not found"));
    }

    @Test
    public void testAugmentDescription() {
        Status status = Status.INVALID_ARGUMENT.withDescription("Invalid input");
        Status augmentedStatus = status.augmentDescription("Additional details");
        assertEquals("Invalid input\nAdditional details", augmentedStatus.getDescription());
    }
}
