
package com.softavail.commsrouter.api.dto.model.skill;

import com.softavail.commsrouter.api.exception.BadValueException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class NumberAttributeDomainDto_RBL4_a597dd0fTest {

    private NumberAttributeDomainDto numberAttributeDomainDto;

    @Before
    public void setUp() {
        numberAttributeDomainDto = new NumberAttributeDomainDto();
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(numberAttributeDomainDto.equals(numberAttributeDomainDto));
    }

    @Test
    public void testEquals_DifferentObject() {
        assertFalse(numberAttributeDomainDto.equals(new Object()));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(numberAttributeDomainDto.equals(null));
    }

    @Test
    public void testEquals_EqualObjects() {
        NumberInterval interval1 = new NumberInterval(1, 5);
        NumberInterval interval2 = new NumberInterval(6, 10);
        NumberAttributeDomainDto dto1 = new NumberAttributeDomainDto(Arrays.asList(interval1, interval2));
        NumberAttributeDomainDto dto2 = new NumberAttributeDomainDto(Arrays.asList(interval1, interval2));
        assertTrue(dto1.equals(dto2));
    }

    @Test
    public void testEquals_NonEqualObjects() {
        NumberInterval interval1 = new NumberInterval(1, 5);
        NumberInterval interval2 = new NumberInterval(6, 10);
        NumberInterval interval3 = new NumberInterval(11, 15);
        NumberAttributeDomainDto dto1 = new NumberAttributeDomainDto(Arrays.asList(interval1, interval2));
        NumberAttributeDomainDto dto2 = new NumberAttributeDomainDto(Arrays.asList(interval1, interval3));
        assertFalse(dto1.equals(dto2));
    }

    @Test
    public void testHashCode() {
        NumberInterval interval1 = new NumberInterval(1, 5);
        NumberInterval interval2 = new NumberInterval(6, 10);
        NumberAttributeDomainDto dto = new NumberAttributeDomainDto(Arrays.asList(interval1, interval2));
        assertEquals(dto.hashCode(), dto.hashCode());
    }

    @Test
    public void testGetType() {
        assertEquals(AttributeType.number, numberAttributeDomainDto.getType());
    }

    @Test
    public void testAccept() {
        MockVisitor visitor = new MockVisitor();
        numberAttributeDomainDto.accept(visitor);
        assertTrue(visitor.isHandled());
    }

    @Test
    public void testValidate_ValidIntervals() throws BadValueException {
        NumberInterval interval1 = new NumberInterval(1, 5);
        NumberInterval interval2 = new NumberInterval(6, 10);
        numberAttributeDomainDto.setIntervals(Arrays.asList(interval1, interval2));
        numberAttributeDomainDto.validate(); // Should not throw
    }

    @Test(expected = BadValueException.class)
    public void testValidate_OverlappingIntervals() throws BadValueException {
        NumberInterval interval1 = new NumberInterval(1, 5);
        NumberInterval interval2 = new NumberInterval(4, 10);
        numberAttributeDomainDto.setIntervals(Arrays.asList(interval1, interval2));
        numberAttributeDomainDto.validate(); // Should throw BadValueException
    }

    @Test
    public void testSetIntervals() {
        NumberInterval interval1 = new NumberInterval(1, 5);
        numberAttributeDomainDto.setIntervals(Collections.singletonList(interval1));
        assertEquals(1, numberAttributeDomainDto.getIntervals().size());
    }

    @Test
    public void testGetIntervals() {
        NumberInterval interval1 = new NumberInterval(1, 5);
        numberAttributeDomainDto.setIntervals(Collections.singletonList(interval1));
        assertEquals(1, numberAttributeDomainDto.getIntervals().size());
        assertEquals(interval1, numberAttributeDomainDto.getIntervals().get(0));
    }

    private class NumberAttributeDomainDto_RBL4_a597dd0fTest implements AttributeDomainDtoVisitor {
        private boolean handled = false;

        @Override
        public void handleNumberIntervals(List<NumberInterval> intervals) {
            handled = true;
        }

        public boolean isHandled() {
            return handled;
        }
    }
}
