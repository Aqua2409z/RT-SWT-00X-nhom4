
package com.pchudzik.springmock.infrastructure.definition;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

public class DoubleName_RBL4_a3466d37Test {

    @Test
    public void testGetName() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertEquals("mainName", doubleName.getName());
    }

    @Test
    public void testGetAliases() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertEquals(Collections.unmodifiableCollection(Arrays.asList("alias1", "alias2")), doubleName.getAliases());
    }

    @Test
    public void testEquals_SameObject() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertTrue(doubleName.equals(doubleName));
    }

    @Test
    public void testEquals_DifferentClass() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertFalse(doubleName.equals(new Object()));
    }

    @Test
    public void testEquals_EqualObjects() {
        DoubleName doubleName1 = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        DoubleName doubleName2 = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertTrue(doubleName1.equals(doubleName2));
    }

    @Test
    public void testEquals_NonEqualObjects() {
        DoubleName doubleName1 = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        DoubleName doubleName2 = new DoubleName("otherName", Arrays.asList("alias3"));
        assertFalse(doubleName1.equals(doubleName2));
    }

    @Test
    public void testHashCode_EqualObjects() {
        DoubleName doubleName1 = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        DoubleName doubleName2 = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertEquals(doubleName1.hashCode(), doubleName2.hashCode());
    }

    @Test
    public void testHashCode_NonEqualObjects() {
        DoubleName doubleName1 = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        DoubleName doubleName2 = new DoubleName("otherName", Arrays.asList("alias3"));
        assertNotEquals(doubleName1.hashCode(), doubleName2.hashCode());
    }

    @Test
    public void testToString() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        String expectedString = "DoubleName{name=mainName, aliases=[alias1, alias2]}";
        assertTrue(doubleName.toString().contains("name=mainName"));
        assertTrue(doubleName.toString().contains("aliases=[alias1, alias2]"));
    }

    @Test
    public void testHasName_WithName() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertTrue(doubleName.hasName("mainName"));
    }

    @Test
    public void testHasName_WithAlias() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertTrue(doubleName.hasName("alias1"));
    }

    @Test
    public void testHasName_WithNonExistentName() {
        DoubleName doubleName = new DoubleName("mainName", Arrays.asList("alias1", "alias2"));
        assertFalse(doubleName.hasName("nonExistentAlias"));
    }
}
