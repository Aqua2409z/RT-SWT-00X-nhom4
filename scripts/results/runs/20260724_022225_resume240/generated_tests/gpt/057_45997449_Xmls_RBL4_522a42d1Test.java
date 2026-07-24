package com.ebayopensource.webrex.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class Xmls_RBL4_522a42d1Test {

    @Test
    public void testIsEnclosedByCData_WithCData() {
        Xmls.Data data = Xmls.forData().trim();
        String xmlStr = "<![CDATA[Some data]]>";
        assertTrue(data.isEnclosedByCData(xmlStr));
    }

    @Test
    public void testIsEnclosedByCData_WithoutCData() {
        Xmls.Data data = Xmls.forData();
        String xmlStr = "Some data";
        assertFalse(data.isEnclosedByCData(xmlStr));
    }

    @Test
    public void testIsEnclosedByCData_NullInput() {
        Xmls.Data data = Xmls.forData();
        assertFalse(data.isEnclosedByCData(null));
    }

    @Test
    public void testTrimCData_WithCData() {
        Xmls.Data data = Xmls.forData().trim();
        String xmlStr = "<![CDATA[Some data]]>";
        String expected = "Some data";
        assertEquals(expected, data.trimCData(xmlStr));
    }

    @Test
    public void testTrimCData_WithoutCData() {
        Xmls.Data data = Xmls.forData();
        String xmlStr = "Some data";
        assertEquals(xmlStr, data.trimCData(xmlStr));
    }

    @Test
    public void testTrimCData_NullInput() {
        Xmls.Data data = Xmls.forData();
        assertNull(data.trimCData(null));
    }

    @Test
    public void testTrimCData_TrimEnabled() {
        Xmls.Data data = Xmls.forData().trim();
        String xmlStr = "   <![CDATA[Some data]]>   ";
        String expected = "Some data";
        assertEquals(expected, data.trimCData(xmlStr));
    }

    @Test
    public void testTrimCData_TrimDisabled() {
        Xmls.Data data = Xmls.forData();
        String xmlStr = "   <![CDATA[Some data]]>   ";
        String expected = "   <![CDATA[Some data]]>   ";
        assertEquals(expected, data.trimCData(xmlStr));
    }
}
