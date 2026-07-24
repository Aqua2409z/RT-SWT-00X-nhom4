package com.ebayopensource.webrex.resource;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ResourceUrn_RBL4_ef2a07abTest {

    private ResourceUrn resourceUrn;

    @Before
    public void setUp() {
        resourceUrn = new ResourceUrn("type", "namespace", "/path");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidResourcePath() {
        new ResourceUrn("type", "namespace", "path"); // path does not start with '/'
    }

    @Test
    public void testGetType() {
        assertEquals("type", resourceUrn.getType());
    }

    @Test
    public void testGetNamespace() {
        assertEquals("namespace", resourceUrn.getNamespace());
    }

    @Test
    public void testGetPath() {
        assertEquals("/path", resourceUrn.getPath());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(resourceUrn.equals(resourceUrn));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(resourceUrn.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(resourceUrn.equals("string"));
    }

    @Test
    public void testEquals_EqualObjects() {
        ResourceUrn other = new ResourceUrn("type", "namespace", "/path");
        assertTrue(resourceUrn.equals(other));
    }

    @Test
    public void testEquals_DifferentPath() {
        ResourceUrn other = new ResourceUrn("type", "namespace", "/differentPath");
        assertFalse(resourceUrn.equals(other));
    }

    @Test
    public void testEquals_DifferentType() {
        ResourceUrn other = new ResourceUrn("differentType", "namespace", "/path");
        assertFalse(resourceUrn.equals(other));
    }

    @Test
    public void testEquals_DifferentNamespace() {
        ResourceUrn other = new ResourceUrn("type", "differentNamespace", "/path");
        assertFalse(resourceUrn.equals(other));
    }

    @Test
    public void testHashCode() {
        ResourceUrn other = new ResourceUrn("type", "namespace", "/path");
        assertEquals(resourceUrn.hashCode(), other.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("type.namespace:/path", resourceUrn.toString());
    }
}
