
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
        attributeValue = new AttributeValue("Test");
        assertEquals("Test", attributeValue.getValue());
        assertFalse(attributeValue.isHidden());
    }

    @Test
    public void testConstructorWithValueAndHidden() {
        attributeValue = new AttributeValue("Test", true);
        assertEquals("Test", attributeValue.getValue());
        assertTrue(attributeValue.isHidden());
    }

    @Test
    public void testSetValue() {
        attributeValue.setValue("New Value");
        assertEquals("New Value", attributeValue.getValue());
    }

    @Test
    public void testSetInteger() {
        attributeValue.setInteger(10);
        assertEquals(Integer.valueOf(10), attributeValue.getInteger());
    }

    @Test
    public void testSetDouble() {
        attributeValue.setDouble(10.5);
        assertEquals(Double.valueOf(10.5), attributeValue.getDouble());
    }

    @Test
    public void testSetString() {
        attributeValue.setString("Hello");
        assertEquals("Hello", attributeValue.getString());
    }

    @Test
    public void testSetHidden() {
        attributeValue.setHidden(true);
        assertTrue(attributeValue.isHidden());
    }

    @Test
    public void testToString() {
        attributeValue.setValue("Test");
        attributeValue.setHidden(true);
        assertEquals("Value: Test Hidden: true", attributeValue.toString());
    }

    @Test
    public void testClone() {
        attributeValue.setValue("Clone Test");
        attributeValue.setHidden(false);
        AttributeValue cloned = (AttributeValue) attributeValue.clone();
        assertEquals(attributeValue, cloned);
        assertNotSame(attributeValue, cloned);
    }

    @Test
    public void testEquals() {
        AttributeValue another = new AttributeValue("Test", false);
        assertTrue(attributeValue.equals(another));
        assertFalse(attributeValue.equals(null));
        assertFalse(attributeValue.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        attributeValue.setValue("Test");
        attributeValue.setHidden(false);
        int expectedHashCode = 79 * 3 + ("Test".hashCode()) + 0;
        assertEquals(expectedHashCode, attributeValue.hashCode());
    }
}
