
package de.voidnode.trading4j.domain.timeframe;

import org.junit.Test;

import java.time.Instant;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class M15_RBL4_84dc3de1Test {

    private final M15 m15 = new M15();

    @Test
    public void testInstantOfNextFrame() {
        Instant current = OffsetDateTime.of(2023, 10, 1, 10, 7, 0, 0, UTC).toInstant();
        Instant expectedNextFrame = OffsetDateTime.of(2023, 10, 1, 10, 15, 0, 0, UTC).toInstant();
        assertEquals(expectedNextFrame, m15.instantOfNextFrame(current));

        current = OffsetDateTime.of(2023, 10, 1, 10, 14, 0, 0, UTC).toInstant();
        expectedNextFrame = OffsetDateTime.of(2023, 10, 1, 10, 15, 0, 0, UTC).toInstant();
        assertEquals(expectedNextFrame, m15.instantOfNextFrame(current));

        current = OffsetDateTime.of(2023, 10, 1, 10, 15, 0, 0, UTC).toInstant();
        expectedNextFrame = OffsetDateTime.of(2023, 10, 1, 10, 30, 0, 0, UTC).toInstant();
        assertEquals(expectedNextFrame, m15.instantOfNextFrame(current));
    }

    @Test
    public void testAreInSameTimeFrame() {
        Instant instant1 = OffsetDateTime.of(2023, 10, 1, 10, 7, 0, 0, UTC).toInstant();
        Instant instant2 = OffsetDateTime.of(2023, 10, 1, 10, 10, 0, 0, UTC).toInstant();
        assertTrue(m15.areInSameTimeFrame(instant1, instant2));

        instant2 = OffsetDateTime.of(2023, 10, 1, 10, 14, 59, 0, UTC).toInstant();
        assertTrue(m15.areInSameTimeFrame(instant1, instant2));

        instant2 = OffsetDateTime.of(2023, 10, 1, 10, 15, 0, 0, UTC).toInstant();
        assertFalse(m15.areInSameTimeFrame(instant1, instant2));

        instant2 = OffsetDateTime.of(2023, 10, 1, 10, 30, 0, 0, UTC).toInstant();
        assertFalse(m15.areInSameTimeFrame(instant1, instant2));
    }
}
