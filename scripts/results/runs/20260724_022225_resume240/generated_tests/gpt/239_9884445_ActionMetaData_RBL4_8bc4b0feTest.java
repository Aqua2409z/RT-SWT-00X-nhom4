
package org.minnal.instrument.entity.metadata;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

public class ActionMetaData_RBL4_8bc4b0feTest {

    private ActionMetaData actionMetaData;
    private Method testMethod;

    @BeforeMethod
    public void setUp() throws NoSuchMethodException {
        testMethod = this.getClass().getMethod("testMethod");
        actionMetaData = new ActionMetaData("testAction", "/test/path", testMethod);
    }

    @Test
    public void testGetName() {
        Assert.assertEquals(actionMetaData.getName(), "testAction");
    }

    @Test
    public void testGetPath() {
        Assert.assertEquals(actionMetaData.getPath(), "/test/path");
    }

    @Test
    public void testGetMethod() {
        Assert.assertEquals(actionMetaData.getMethod(), testMethod);
    }

    @Test
    public void testAddParameter() {
        ParameterMetaData parameter = new ParameterMetaData("param1", String.class);
        actionMetaData.addParameter(parameter);
        Assert.assertEquals(actionMetaData.getParameters().size(), 1);
        Assert.assertEquals(actionMetaData.getParameters().get(0), parameter);
    }

    @Test
    public void testGetParameters() {
        Assert.assertTrue(actionMetaData.getParameters().isEmpty());
        ParameterMetaData parameter = new ParameterMetaData("param1", String.class);
        actionMetaData.addParameter(parameter);
        Assert.assertFalse(actionMetaData.getParameters().isEmpty());
    }

    @Test
    public void testEqualsAndHashCode() {
        ActionMetaData anotherActionMetaData = new ActionMetaData("testAction", "/test/path", testMethod);
        Assert.assertTrue(actionMetaData.equals(anotherActionMetaData));
        Assert.assertEquals(actionMetaData.hashCode(), anotherActionMetaData.hashCode());

        anotherActionMetaData = new ActionMetaData("differentAction", "/different/path", testMethod);
        Assert.assertFalse(actionMetaData.equals(anotherActionMetaData));
    }

    public void testMethod() {
        // This is a placeholder method for reflection
    }
}
