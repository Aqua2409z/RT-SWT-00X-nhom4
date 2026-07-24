package com.ebayopensource.webrex.resource.expression;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.ebayopensource.webrex.resource.ResourceFactory;
import com.ebayopensource.webrex.resource.api.IResource;
import com.ebayopensource.webrex.resource.expression.ResourceELEvaluator;
import com.ebayopensource.webrex.resource.expression.ResourceExpression;
import com.ebayopensource.webrex.resource.expression.ResourceExpressionType;

import java.util.HashMap;
import java.util.Map;

public class ResourceELEvaluator_RBL4Test_44ed24a7 {
    private ResourceELEvaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new ResourceELEvaluator();
    }

    @Test
    public void testEvaluateWithValidExpression() {
        ResourceExpression parent = new ResourceExpression(ResourceExpressionType.TYPE, "parentKey", null);
        ResourceExpression namespace = new ResourceExpression(ResourceExpressionType.NAMESPACE, "namespaceKey", parent);
        ResourceExpression expr = new ResourceExpression(ResourceExpressionType.RES, "resourceKey", namespace);

        Object result = evaluator.evaluate(expr);
        assertNotNull(result);
        assertTrue(result instanceof IResource);
    }

    @Test
    public void testEvaluateAsStringWithValidExpression() {
        ResourceExpression parent = new ResourceExpression(ResourceExpressionType.TYPE, "parentKey", null);
        ResourceExpression namespace = new ResourceExpression(ResourceExpressionType.NAMESPACE, "namespaceKey", parent);
        ResourceExpression expr = new ResourceExpression(ResourceExpressionType.RES, "resourceKey", namespace);

        String url = evaluator.evaluateAsString(expr);
        assertNotNull(url);
        assertFalse(url.isEmpty());
    }

    @Test
    public void testEvaluateWithNullExpression() {
        Object result = evaluator.evaluate(null);
        assertNull(result);
    }

    @Test
    public void testEvaluationAsEntrySet() {
        ResourceExpression expr = new ResourceExpression(ResourceExpressionType.RES, "resourceKey", null);
        assertTrue(evaluator.evaluationAsEntrySet(expr).isEmpty());
    }

    @Test
    public void testEvaluationAsKeySet() {
        ResourceExpression expr = new ResourceExpression(ResourceExpressionType.RES, "resourceKey", null);
        assertTrue(evaluator.evaluationAsKeySet(expr).isEmpty());
    }

    @Test
    public void testGetExpressionNamespace() {
        ResourceExpression parent = new ResourceExpression(ResourceExpressionType.TYPE, "parentKey", null);
        ResourceExpression namespace = new ResourceExpression(ResourceExpressionType.NAMESPACE, "namespaceKey", parent);
        ResourceExpression expr = new ResourceExpression(ResourceExpressionType.RES, "resourceKey", namespace);

        ResourceExpression result = evaluator.getExpressionNamespace(expr);
        assertNotNull(result);
        assertEquals(ResourceExpressionType.NAMESPACE, result.m_type);
    }

    @Test
    public void testConvertELToPath() {
        StringBuilder sb = new StringBuilder("resource_key");
        String result = evaluator.convertELToPath(sb, "type", "namespace");
        assertEquals("resource.key", result);
    }
}
