package org.junithelper.core.meta;

import org.junit.Test;
import static org.junit.Assert.*;

import org.junithelper.core.meta.ArgTypeMeta;

import java.util.Arrays;

public class ArgTypeMeta_RBL4_5cccf4b4Test {

    @Test
    public void testGetGenericsAsString_EmptyGenerics() {
        ArgTypeMeta argTypeMeta = new ArgTypeMeta();
        assertEquals("", argTypeMeta.getGenericsAsString());
    }

    @Test
    public void testGetGenericsAsString_SingleGeneric() {
        ArgTypeMeta argTypeMeta = new ArgTypeMeta();
        argTypeMeta.generics.add("String");
        assertEquals("<String>", argTypeMeta.getGenericsAsString());
    }

    @Test
    public void testGetGenericsAsString_MultipleGenerics() {
        ArgTypeMeta argTypeMeta = new ArgTypeMeta();
        argTypeMeta.generics.addAll(Arrays.asList("String", "Integer", "Double"));
        assertEquals("<String, Integer, Double>", argTypeMeta.getGenericsAsString());
    }

    @Test(expected = IllegalStateException.class)
    public void testToString_ThrowsIllegalStateException() {
        ArgTypeMeta argTypeMeta = new ArgTypeMeta();
        argTypeMeta.toString();
    }

    @Test
    public void testNameInitialization() {
        ArgTypeMeta argTypeMeta = new ArgTypeMeta();
        argTypeMeta.name = "TestName";
        assertEquals("TestName", argTypeMeta.name);
    }

    @Test
    public void testNameInMethodNameInitialization() {
        ArgTypeMeta argTypeMeta = new ArgTypeMeta();
        argTypeMeta.nameInMethodName = "testNameInMethod";
        assertEquals("testNameInMethod", argTypeMeta.nameInMethodName);
    }
}
