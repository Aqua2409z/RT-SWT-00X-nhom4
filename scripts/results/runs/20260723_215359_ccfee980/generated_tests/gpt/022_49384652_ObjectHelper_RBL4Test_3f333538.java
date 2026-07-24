
package com.zuoxiaolong.niubi.job.core.helper;

import org.junit.Test;
import java.lang.reflect.Field;
import static org.junit.Assert.*;

public class ObjectHelper_RBL4Test_3f333538 {

    private static class ObjectHelper_RBL4Test_3f333538 {
        @Transient
        private String transientId;

        private String regularId;

        public String getTransientId() {
            return transientId;
        }

        public String getRegularId() {
            return regularId;
        }
    }

    @Test
    public void testIsEmpty_NullObject() {
        assertTrue(ObjectHelper.isEmpty(null));
    }

    @Test
    public void testIsEmpty_EmptyString() {
        assertTrue(ObjectHelper.isEmpty(""));
    }

    @Test
    public void testIsEmpty_WhitespaceString() {
        assertTrue(ObjectHelper.isEmpty("   "));
    }

    @Test
    public void testIsEmpty_NonEmptyString() {
        assertFalse(ObjectHelper.isEmpty("Hello"));
    }

    @Test
    public void testIsTransientId_TransientField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("transientId");
        assertTrue(ObjectHelper.isTransientId(TestClass.class, field));
    }

    @Test
    public void testIsTransientId_NonTransientField() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("regularId");
        assertFalse(ObjectHelper.isTransientId(TestClass.class, field));
    }

    @Test
    public void testIsTransientId_NonMatchingFieldName() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("transientId");
        field.setName("someOtherId");
        assertFalse(ObjectHelper.isTransientId(TestClass.class, field));
    }
}
