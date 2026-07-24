package com.softavail.commsrouter.eval;

import com.softavail.commsrouter.api.exception.ExpressionException;
import com.softavail.commsrouter.domain.AttributeGroup;
import cz.jirutka.rsql.parser.ast.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RsqlEvaluatorFactory_RBL4_fb92d911Test {

    private CommsRouterEvaluatorFactory mockFactory;
    private RsqlEvaluatorFactory rsqlEvaluatorFactory;

    @Before
    public void setUp() {
        mockFactory = mock(CommsRouterEvaluatorFactory.class);
        rsqlEvaluatorFactory = new RsqlEvaluatorFactory(mockFactory);
    }

    @Test
    public void testParseValidExpression() {
        String expression = "name==John";
        Node node = rsqlEvaluatorFactory.parse(expression);
        assertNotNull(node);
    }

    @Test(expected = ExpressionException.class)
    public void testValidateInvalidExpression() throws ExpressionException {
        String invalidExpression = "name==";
        rsqlEvaluatorFactory.validate(invalidExpression);
    }

    @Test
    public void testValidateValidExpression() throws ExpressionException {
        String validExpression = "name==John";
        rsqlEvaluatorFactory.validate(validExpression);
    }

    @Test
    public void testCreateValidExpression() throws ExpressionException {
        String expression = "name==John";
        String routerRef = "router1";
        RsqlEvaluator evaluator = rsqlEvaluatorFactory.create(expression, routerRef);
        assertNotNull(evaluator);
    }

    @Test(expected = ExpressionException.class)
    public void testCreateInvalidExpression() throws ExpressionException {
        String invalidExpression = "name==";
        String routerRef = "router1";
        rsqlEvaluatorFactory.create(invalidExpression, routerRef);
    }

    @Test
    public void testEvaluateValidExpression() throws ExpressionException {
        String expression = "name==John";
        AttributeGroup attributeGroup = mock(AttributeGroup.class);
        String routerRef = "router1";
        RsqlEvaluator evaluator = mock(RsqlEvaluator.class);
        when(evaluator.evaluate(attributeGroup)).thenReturn(true);
        when(mockFactory.create(any(Node.class), eq(routerRef))).thenReturn(evaluator);

        boolean result = rsqlEvaluatorFactory.evaluate(expression, attributeGroup, routerRef);
        assertTrue(result);
    }

    @Test(expected = ExpressionException.class)
    public void testEvaluateInvalidExpression() throws ExpressionException {
        String invalidExpression = "name==";
        AttributeGroup attributeGroup = mock(AttributeGroup.class);
        String routerRef = "router1";
        rsqlEvaluatorFactory.evaluate(invalidExpression, attributeGroup, routerRef);
    }
}
