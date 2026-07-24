
package com.ebayopensource.webrex.resource;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

public class ResourceArgumentsUrn_RBL4_4484f2f5Test {

    private ResourceArgumentsUrn resourceArgumentsUrn1;
    private ResourceArgumentsUrn resourceArgumentsUrn2;
    private ResourceArgumentsUrn resourceArgumentsUrn3;
    private Map<String, Object> arguments;

    @Before
    public void setUp() {
        arguments = new HashMap<>();
        arguments.put("key1", "value1");
        arguments.put("key2", 2);
        resourceArgumentsUrn1 = new ResourceArgumentsUrn("type1", "namespace1", "resourcePath1", arguments);
        resourceArgumentsUrn2 = new ResourceArgumentsUrn("type1", "namespace1", "resourcePath1", arguments);
        resourceArgumentsUrn3 = new ResourceArgumentsUrn("type1", "namespace1", "resourcePath1", null);
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(resourceArgumentsUrn1.equals(resourceArgumentsUrn1));
    }

    @Test
    public void testEquals_SameValues() {
        assertTrue(resourceArgumentsUrn1.equals(resourceArgumentsUrn2));
    }

    @Test
    public void testEquals_DifferentArguments() {
        Map<String, Object> differentArguments = new HashMap<>();
        differentArguments.put("key1", "value1");
        differentArguments.put("key2", 3);
        ResourceArgumentsUrn differentUrn = new ResourceArgumentsUrn("type1", "namespace1", "resourcePath1", differentArguments);
        assertFalse(resourceArgumentsUrn1.equals(differentUrn));
    }

    @Test
    public void testEquals_NullArguments() {
        assertFalse(resourceArgumentsUrn1.equals(resourceArgumentsUrn3));
    }

    @Test
    public void testHashCode_SameValues() {
        assertEquals(resourceArgumentsUrn1.hashCode(), resourceArgumentsUrn2.hashCode());
    }

    @Test
    public void testHashCode_DifferentArguments() {
        Map<String, Object> differentArguments = new HashMap<>();
        differentArguments.put("key1", "value1");
        differentArguments.put("key2", 3);
        ResourceArgumentsUrn differentUrn = new ResourceArgumentsUrn("type1", "namespace1", "resourcePath1", differentArguments);
        assertNotEquals(resourceArgumentsUrn1.hashCode(), differentUrn.hashCode());
    }

    @Test
    public void testGetArgument() {
        assertEquals(arguments, resourceArgumentsUrn1.getArgument());
    }

    @Test
    public void testGetArgument_Null() {
        assertNull(resourceArgumentsUrn3.getArgument());
    }
}
