
package me.tomassetti.turin.compiler;

import me.tomassetti.turin.definitions.TypeDefinition;
import me.tomassetti.turin.parser.analysis.Property;
import me.tomassetti.turin.parser.ast.Node;
import me.tomassetti.turin.parser.ast.expressions.ActualParam;
import me.tomassetti.turin.parser.ast.expressions.Creation;
import me.tomassetti.turin.parser.ast.expressions.Expression;
import me.tomassetti.turin.parser.ast.expressions.InstanceMethodInvokation;
import me.tomassetti.turin.parser.ast.expressions.literals.StringLiteral;
import me.tomassetti.turin.symbols.FormalParameter;
import me.tomassetti.turin.typesystem.TypeUsage;
import me.tomassetti.turin.util.Either;
import me.tomassetti.turin.resolvers.SymbolResolver;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ParamUtils_RBL4_6e5d9f86Test {

    @Test
    public void testVerifyOrder() {
        List<ActualParam> params = new ArrayList<>();
        params.add(new ActualParam("param1", false));
        params.add(new ActualParam("param2", true));
        params.add(new ActualParam("param3", false));
        assertFalse(ParamUtils.verifyOrder(params));

        params.clear();
        params.add(new ActualParam("param1", true));
        params.add(new ActualParam("param2", false));
        assertTrue(ParamUtils.verifyOrder(params));
    }

    @Test
    public void testUnnamedParams() {
        List<ActualParam> params = new ArrayList<>();
        params.add(new ActualParam("param1", false));
        params.add(new ActualParam("param2", true));
        params.add(new ActualParam("param3", false));
        List<ActualParam> unnamed = ParamUtils.unnamedParams(params);
        assertEquals(2, unnamed.size());
    }

    @Test
    public void testNamedParams() {
        List<ActualParam> params = new ArrayList<>();
        params.add(new ActualParam("param1", false));
        params.add(new ActualParam("param2", true));
        params.add(new ActualParam("param3", false));
        List<ActualParam> named = ParamUtils.namedParams(params);
        assertEquals(1, named.size());
    }

    @Test
    public void testHasDefaultParams() {
        List<FormalParameter> params = new ArrayList<>();
        params.add(new FormalParameter("param1", true));
        params.add(new FormalParameter("param2", false));
        assertTrue(ParamUtils.hasDefaultParams(params));

        params.clear();
        params.add(new FormalParameter("param1", false));
        assertFalse(ParamUtils.hasDefaultParams(params));
    }

    @Test
    public void testDesugarizeAsteriskParam() {
        // Mock objects for testing
        List<FormalParameter> formalParams = new ArrayList<>();
        formalParams.add(new FormalParameter("param1", true));
        Expression value = new Creation("SomeType", Collections.emptyList());
        SymbolResolver resolver = new SymbolResolver();
        Node parent = new Node();

        Either<String, List<ActualParam>> result = ParamUtils.desugarizeAsteriskParam(formalParams, value, resolver, parent);
        assertTrue(result.isRight());
        assertNotNull(result.right().get());
    }

    @Test
    public void testGetterName() {
        FormalParameter param = new FormalParameter("param1", false);
        SymbolResolver resolver = new SymbolResolver();
        String getterName = ParamUtils.getterName(param, resolver);
        assertNotNull(getterName);
    }
}
