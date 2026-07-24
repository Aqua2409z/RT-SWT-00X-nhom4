
package org.minnal.instrument.resource.metadata;

import org.minnal.instrument.metadata.MetaDataBuilder;
import org.minnal.instrument.resource.metadata.handler.AbstractResourceAnnotationHandler;
import org.minnal.utils.reflection.ClassUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.ws.rs.Path;

public class ResourceMetaDataBuilder_RBL4_c1478021Test {

    private Class<?> resourceClass;
    private ResourceMetaDataBuilder resourceMetaDataBuilder;

    @BeforeClass
    public void setUp() {
        resourceClass = TestResource.class;
        resourceMetaDataBuilder = new ResourceMetaDataBuilder(resourceClass);
    }

    @Test
    public void testResourceMetaDataBuilderInitialization() {
        Assert.assertNotNull(resourceMetaDataBuilder);
        Assert.assertEquals(resourceMetaDataBuilder.getMetaData().getResourceClass(), resourceClass);
        Assert.assertEquals(resourceMetaDataBuilder.getMetaData().getPath(), "/test");
    }

    @Test
    public void testBuildMethod() {
        ResourceMetaData metaData = resourceMetaDataBuilder.build();
        Assert.assertNotNull(metaData);
        Assert.assertEquals(metaData.getResourceClass(), resourceClass);
        Assert.assertFalse(metaData.getSubResources().isEmpty());
    }

    @Test
    public void testGetVisitingClass() {
        Class<?> visitingClass = resourceMetaDataBuilder.getVistingClass();
        Assert.assertEquals(visitingClass, resourceClass);
    }

    @Path("/test")
    public static class ResourceMetaDataBuilder_RBL4_c1478021Test {
        // Test resource implementation
    }
}
