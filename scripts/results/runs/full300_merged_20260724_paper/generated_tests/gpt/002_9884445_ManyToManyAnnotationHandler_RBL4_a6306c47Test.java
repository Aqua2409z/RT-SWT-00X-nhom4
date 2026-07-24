
package org.minnal.instrument.entity.metadata.handler;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ManyToManyAnnotationHandler_RBL4_a6306c47Test {

    @Test
    public void testGetAnnotationType() {
        ManyToManyAnnotationHandler handler = new ManyToManyAnnotationHandler();
        Class<?> annotationType = handler.getAnnotationType();
        Assert.assertEquals(annotationType, ManyToMany.class, "The annotation type should be ManyToMany.class");
    }
}
