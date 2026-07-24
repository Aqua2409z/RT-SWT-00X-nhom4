
package org.rf.ide.core.execution.debug;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rf.ide.core.execution.agent.event.Variable;
import org.rf.ide.core.execution.agent.event.VariableTypedValue;
import org.rf.ide.core.testdata.model.table.variables.AVariable.VariableScope;

class StackFrameVariables_RBL4Test_996db0ed {

    private StackFrameVariables stackFrameVariables;

    @BeforeEach
    void setUp() {
        Map<String, StackFrameVariable> variables = new HashMap<>();
        variables.put("var1", new StackFrameVariable(VariableScope.LOCAL, false, "var1", "String", "value1"));
        variables.put("var2", new StackFrameVariable(VariableScope.GLOBAL, true, "var2", "Integer", 10));
        stackFrameVariables = new StackFrameVariables(variables);
    }

    @Test
    void testNewNonLocalVariables() {
        Map<Variable, VariableTypedValue> inputVars = new HashMap<>();
        Variable var = new Variable("var3", VariableScope.GLOBAL);
        VariableTypedValue typedValue = new VariableTypedValue("String", "value3");
        inputVars.put(var, typedValue);

        StackFrameVariables result = StackFrameVariables.newNonLocalVariables(inputVars);
        assertNotNull(result);
        assertEquals(1, result.getVariables().size());
        assertTrue(result.getVariables().containsKey("var3"));
    }

    @Test
    void testNewLocalVariables() {
        StackFrameVariables localVars = StackFrameVariables.newLocalVariables(stackFrameVariables, true);
        assertNotNull(localVars);
        assertEquals(2, localVars.getVariables().size());
        assertTrue(localVars.getVariables().containsKey("var1"));
        assertTrue(localVars.getVariables().containsKey("var2"));
    }

    @Test
    void testUpdateVariables() {
        Map<Variable, VariableTypedValue> updateVars = new HashMap<>();
        Variable var = new Variable("var1", VariableScope.LOCAL);
        VariableTypedValue typedValue = new VariableTypedValue("String", "newValue");
        updateVars.put(var, typedValue);

        StackVariablesDelta delta = stackFrameVariables.update(updateVars);
        assertNotNull(delta);
        assertTrue(delta.isChanged("var1"));
        assertFalse(delta.isAdded("var1"));
    }

    @Test
    void testComputeDelta() {
        Map<Variable, VariableTypedValue> incomingVars = new HashMap<>();
        Variable var = new Variable("var1", VariableScope.LOCAL);
        VariableTypedValue typedValue = new VariableTypedValue("String", "value1");
        incomingVars.put(var, typedValue);

        StackVariablesDelta delta = stackFrameVariables.update(incomingVars);
        assertTrue(delta.isUnchanged("var1"));
        assertFalse(delta.isRemoved("var1"));
    }

    @Test
    void testIsAutomatic() {
        assertTrue(StackFrameVariables.isAutomatic("${curdir}"));
        assertFalse(StackFrameVariables.isAutomatic("${unknown}"));
    }

    @Test
    void testIterator() {
        int count = 0;
        for (StackFrameVariable variable : stackFrameVariables) {
            count++;
        }
        assertEquals(2, count);
    }
}
