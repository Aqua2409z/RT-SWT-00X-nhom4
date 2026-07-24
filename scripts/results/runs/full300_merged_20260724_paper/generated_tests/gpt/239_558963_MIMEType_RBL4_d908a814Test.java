package org.codehaus.httpcache4j;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;

public class MIMEType_RBL4_d908a814Test {

    @Test
    public void testConstructorAndGetters() {
        MIMEType mimeType = new MIMEType("application", "json");
        assertEquals("application", mimeType.getPrimaryType());
        assertEquals("json", mimeType.getSubType());
        assertNull(mimeType.getCharset());
    }

    @Test
    public void testAddParameter() {
        MIMEType mimeType = new MIMEType("application", "json");
        MIMEType mimeTypeWithParam = mimeType.addParameter("charset", "utf-8");
        assertEquals("utf-8", mimeTypeWithParam.getCharset());
        assertNotEquals(mimeType, mimeTypeWithParam);
    }

    @Test
    public void testEquals() {
        MIMEType mimeType1 = new MIMEType("application", "json");
        MIMEType mimeType2 = new MIMEType("application", "json");
        MIMEType mimeType3 = new MIMEType("application", "xml");

        assertTrue(mimeType1.equals(mimeType2));
        assertFalse(mimeType1.equals(mimeType3));
    }

    @Test
    public void testEqualsWithoutParameters() {
        MIMEType mimeType1 = new MIMEType("application", "json");
        MIMEType mimeType2 = new MIMEType("application", "json").addParameter("charset", "utf-8");
        
        assertTrue(mimeType1.equalsWithoutParameters(mimeType2));
    }

    @Test
    public void testIncludes() {
        MIMEType mimeType1 = new MIMEType("application", "json");
        MIMEType mimeType2 = new MIMEType("application", "json");
        MIMEType mimeType3 = new MIMEType("application", "*");
        MIMEType mimeType4 = new MIMEType("text", "html");

        assertTrue(mimeType1.includes(mimeType2));
        assertTrue(mimeType1.includes(mimeType3));
        assertFalse(mimeType1.includes(mimeType4));
    }

    @Test
    public void testValueOf() {
        MIMEType mimeType = MIMEType.valueOf("application/json; charset=utf-8");
        assertEquals("application", mimeType.getPrimaryType());
        assertEquals("json", mimeType.getSubType());
        assertEquals("utf-8", mimeType.getParameter("charset"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfInvalid() {
        MIMEType.valueOf("invalid/mimeType");
    }

    @Test
    public void testToString() {
        MIMEType mimeType = new MIMEType("application", "json").addParameter("charset", "utf-8");
        assertEquals("application/json;charset=utf-8", mimeType.toString());
    }

    @Test
    public void testHashCode() {
        MIMEType mimeType1 = new MIMEType("application", "json");
        MIMEType mimeType2 = new MIMEType("application", "json");
        assertEquals(mimeType1.hashCode(), mimeType2.hashCode());
    }

    @Test
    public void testGetParameters() {
        MIMEType mimeType = new MIMEType("application", "json").addParameter("charset", "utf-8");
        assertEquals(1, mimeType.getParameters().size());
        assertEquals("charset", mimeType.getParameters().get(0).getName());
        assertEquals("utf-8", mimeType.getParameters().get(0).getValue());
    }
}
