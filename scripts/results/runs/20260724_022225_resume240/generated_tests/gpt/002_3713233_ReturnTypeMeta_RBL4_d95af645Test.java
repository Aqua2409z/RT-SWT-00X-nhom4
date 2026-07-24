package org.junithelper.core.meta;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ReturnTypeMeta_RBL4_d95af645Test {

    @Test
    public void testGetGenericsAsString_EmptyGenerics() {
        ReturnTypeMeta returnTypeMeta = new ReturnTypeMeta();
        assertEquals("", returnTypeMeta.getGenericsAsString());
    }

    @Test
    public void testGetGenericsAsString_SingleGeneric() {
        ReturnTypeMeta returnTypeMeta = new ReturnTypeMeta();
        returnTypeMeta.generics.add("T");
        assertEquals("<T>", returnTypeMeta.getGenericsAsString());
    }

    @Test
    public void testGetGenericsAsString_MultipleGenerics() {
        ReturnTypeMeta returnTypeMeta = new ReturnTypeMeta();
        returnTypeMeta.generics.addAll(Arrays.asList("T", "U", "V"));
        assertEquals("<T, U, V>", returnTypeMeta.getGenericsAsString());
    }

    @Test
    public void testGetGenericsAsString_OnlyOneGeneric() {
        ReturnTypeMeta returnTypeMeta = new ReturnTypeMeta();
        returnTypeMeta.generics.add("String");
        assertEquals("<String>", returnTypeMeta.getGenericsAsString());
    }

    @Test
    public void testGetGenericsAsString_TwoGenerics() {
        ReturnTypeMeta returnTypeMeta = new ReturnTypeMeta();
        returnTypeMeta.generics.add("Integer");
        returnTypeMeta.generics.add("Double");
        assertEquals("<Integer, Double>", returnTypeMeta.getGenericsAsString());
    }
}
