
package com.pchudzik.springmock.infrastructure.annotation;

import org.junit.Test;
import static org.junit.Assert.*;

public class NoDoubleClassDefined_RBL4_f739e0feTest {

    @Test
    public void testIsDoubleClassDefinitionMissing_NullClass() {
        assertTrue(NoDoubleClassDefined.isDoubleClassDefinitionMissing(null));
    }

    @Test
    public void testIsDoubleClassDefinitionMissing_EqualClass() {
        assertTrue(NoDoubleClassDefined.isDoubleClassDefinitionMissing(NoDoubleClassDefined.class));
    }

    @Test
    public void testIsDoubleClassDefinitionMissing_DifferentClass() {
        assertFalse(NoDoubleClassDefined.isDoubleClassDefinitionMissing(String.class));
    }

    @Test
    public void testIsDoubleClassDefinitionMissing_AnotherClass() {
        assertFalse(NoDoubleClassDefined.isDoubleClassDefinitionMissing(Integer.class));
    }
}
