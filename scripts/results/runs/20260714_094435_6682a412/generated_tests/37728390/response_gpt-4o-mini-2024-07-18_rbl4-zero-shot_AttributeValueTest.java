package sim.util.geo;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeValueTest {

    private AttributeValue attributeValue;

    @Before
    public void setUp() {
        attributeValue = new AttributeValue();
    }

    @Test
    public void testDefaultConstructor() {
        assertNull(attributeValue.getValue());
        assertFalse(attributeValue.isHidden());
    }

    @Test
    public void testConstructorWithValue() {
        attributeValue = new AttributeValue(10);
        assertEquals(10, attributeValue.getInteger().intValue());
        assertFalse(attributeValue.isHidden());
    }

    @Test
    public void testConstructorWithValueAndHidden() {
        attributeValue = new AttributeValue("Test", true);
        assertEquals("Test", attributeValue.getString());
        assertTrue(attributeValue.isHidden());
    }

    @Test
    public void testToString() {
        attributeValue = new AttributeValue(5, true);
        assertEquals("Value: 5 Hidden: true", attributeValue.toString());
    }

    @Test
    public void testClone() {
        attributeValue = new AttributeValue("Clone Test", false);
        AttributeValue cloned = (AttributeValue) attributeValue.clone();
        assertEquals(attributeValue, cloned);
        assertNotSame(attributeValue, cloned);
    }

    @Test
    public void testEquals() {
        AttributeValue another = new AttributeValue(5, false);
        attributeValue = new AttributeValue(5, false);
        assertTrue(attributeValue.equals(another));
        
        another = new AttributeValue(5, true);
        assertFalse(attributeValue.equals(another));
        
        another = new AttributeValue(null, false);
        attributeValue = new AttributeValue(null, false);
        assertTrue(attributeValue.equals(another));
    }

    @Test
    public void testHashCode() {
        attributeValue = new AttributeValue(5, false);
        int expectedHashCode = 79 * 3 + (5 != null ? 5.hashCode() : 0) + (false ? 1 : 0);
        assertEquals(expectedHashCode, attributeValue.hashCode());
    }

    @Test
    public void testSetValueAndGetValue() {
        attributeValue.setValue("New Value");
        assertEquals("New Value", attributeValue.getValue());
    }

    @Test
    public void testSetIntegerAndGetInteger() {
        attributeValue.setInteger(42);
        assertEquals(Integer.valueOf(42), attributeValue.getInteger());
    }

    @Test
    public void testSetDoubleAndGetDouble() {
        attributeValue.setDouble(3.14);
        assertEquals(Double.valueOf(3.14), attributeValue.getDouble());
    }

    @Test
    public void testSetStringAndGetString() {
        attributeValue.setString("Hello");
        assertEquals("Hello", attributeValue.getString());
    }

    @Test
    public void testSetHiddenAndIsHidden() {
        attributeValue.setHidden(true);
        assertTrue(attributeValue.isHidden());
        attributeValue.setHidden(false);
        assertFalse(attributeValue.isHidden());
    }
}
