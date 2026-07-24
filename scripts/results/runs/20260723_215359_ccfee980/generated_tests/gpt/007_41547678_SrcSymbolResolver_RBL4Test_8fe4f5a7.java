
package me.tomassetti.turin.resolvers;

import me.tomassetti.turin.definitions.ContextDefinition;
import me.tomassetti.turin.definitions.TypeDefinition;
import me.tomassetti.turin.parser.ast.*;
import me.tomassetti.turin.parser.ast.context.ContextDefinitionNode;
import me.tomassetti.turin.parser.ast.expressions.FunctionCall;
import me.tomassetti.turin.parser.ast.invokables.FunctionDefinitionNode;
import me.tomassetti.turin.parser.ast.properties.PropertyDefinition;
import me.tomassetti.turin.parser.ast.properties.PropertyReference;
import me.tomassetti.turin.parser.analysis.exceptions.UnsolvedMethodException;
import me.tomassetti.turin.symbols.Symbol;
import me.tomassetti.turin.typesystem.ReferenceTypeUsage;
import me.tomassetti.turin.typesystem.TypeUsage;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SrcSymbolResolver_RBL4Test_8fe4f5a7 {

    private SrcSymbolResolver resolver;
    private TurinFile turinFile;

    @Before
    public void setUp() {
        PropertyDefinition propertyDefinition = new PropertyDefinition("contextName.propertyName", "propertyType");
        FunctionDefinitionNode functionDefinition = new FunctionDefinitionNode("contextName.functionName", "returnType");
        TypeDefinitionNode typeDefinition = new TypeDefinitionNode("contextName.typeName");
        Program program = new Program("contextName.programName");
        ContextDefinitionNode contextDefinition = new ContextDefinitionNode("contextName");

        turinFile = new TurinFile(Arrays.asList(typeDefinition), Arrays.asList(propertyDefinition), Arrays.asList(program), Arrays.asList(functionDefinition), Arrays.asList(contextDefinition));
        resolver = new SrcSymbolResolver(Collections.singletonList(turinFile));
    }

    @Test
    public void testFindDefinition() {
        PropertyReference propertyReference = new PropertyReference("contextName", "propertyName");
        Optional<PropertyDefinition> result = resolver.findDefinition(propertyReference);
        assertTrue(result.isPresent());
        assertEquals("contextName.propertyName", result.get().getQualifiedName());
    }

    @Test
    public void testFindTypeDefinitionIn() {
        Optional<TypeDefinition> result = resolver.findTypeDefinitionIn("contextName.typeName", null, resolver);
        assertTrue(result.isPresent());
        assertEquals("contextName.typeName", result.get().getQualifiedName());
    }

    @Test
    public void testFindTypeUsageIn() {
        Optional<TypeUsage> result = resolver.findTypeUsageIn("contextName.typeName", null, resolver);
        assertTrue(result.isPresent());
        assertTrue(result.get() instanceof ReferenceTypeUsage);
    }

    @Test(expected = UnsolvedMethodException.class)
    public void testFindJvmDefinition() {
        FunctionCall functionCall = new FunctionCall("contextName.functionName");
        resolver.findJvmDefinition(functionCall);
    }

    @Test
    public void testFindSymbol() {
        Optional<Symbol> result = resolver.findSymbol("contextName.propertyName", null);
        assertTrue(result.isPresent());
        assertEquals("contextName.propertyName", result.get().getQualifiedName());
    }

    @Test
    public void testExistPackage() {
        assertTrue(resolver.existPackage("contextName"));
        assertFalse(resolver.existPackage("nonExistentPackage"));
    }

    @Test
    public void testFindContextSymbol() {
        Optional<ContextDefinition> result = resolver.findContextSymbol("contextName", null);
        assertTrue(result.isPresent());
        assertEquals("contextName", result.get().getQualifiedName());
    }

    @Test
    public void testSetParent() {
        SrcSymbolResolver parentResolver = new SrcSymbolResolver(Collections.emptyList());
        resolver.setParent(parentResolver);
        assertEquals(parentResolver, resolver.getParent());
    }

    @Test
    public void testGetParent() {
        assertNull(resolver.getParent());
        SrcSymbolResolver parentResolver = new SrcSymbolResolver(Collections.emptyList());
        resolver.setParent(parentResolver);
        assertEquals(parentResolver, resolver.getParent());
    }
}
