package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.PropertyTree;
import org.sonar.plugins.jproperties.api.tree.SeparatorTree;

import static org.mockito.Mockito.*;

public class SeparatorConventionCheck_RBL4Test_f2dcb8d8 {

    private SeparatorConventionCheck check;
    private PropertyTree propertyTree;
    private SeparatorTree separatorTree;

    @Before
    public void setUp() {
        check = new SeparatorConventionCheck();
        propertyTree = mock(PropertyTree.class);
        separatorTree = mock(SeparatorTree.class);
    }

    @Test
    public void testVisitSeparator_ValidSeparator() {
        when(separatorTree.text()).thenReturn("=");
        check.setSeparator("=");
        check.visitSeparator(separatorTree);
        // No issues should be added
        verify(check, never()).addPreciseIssue(any(), anyString());
    }

    @Test
    public void testVisitSeparator_InvalidSeparator() {
        when(separatorTree.text()).thenReturn(":");
        check.setSeparator("=");
        check.visitSeparator(separatorTree);
        verify(check, times(1)).addPreciseIssue(eq(separatorTree), eq("Use '=' as separator instead."));
    }

    @Test
    public void testVisitProperty_WhitespaceBetweenKeyAndSeparator() {
        when(propertyTree.separator()).thenReturn(separatorTree);
        when(separatorTree.separatorToken()).thenReturn(mock(SeparatorTree.SeparatorToken.class));
        when(separatorTree.text()).thenReturn("=");
        when(propertyTree.key().value().endColumn()).thenReturn(5);
        when(separatorTree.separatorToken().column()).thenReturn(6);
        
        check.visitProperty(propertyTree);
        verify(check, times(1)).addPreciseIssue(eq(separatorTree), eq("Remove the whitespaces between the key and the separator."));
    }

    @Test
    public void testVisitProperty_WhitespaceBetweenSeparatorAndValue() {
        when(propertyTree.separator()).thenReturn(separatorTree);
        when(separatorTree.text()).thenReturn("=");
        when(propertyTree.value()).thenReturn(mock(PropertyTree.Value.class));
        when(propertyTree.value().value().column()).thenReturn(8);
        when(separatorTree.separatorToken().column()).thenReturn(6);
        
        check.visitProperty(propertyTree);
        verify(check, times(1)).addPreciseIssue(eq(separatorTree), eq("Remove the whitespaces between the separator and the value."));
    }

    @Test
    public void testValidateParameters_ValidSeparator() {
        check.setSeparator(":");
        check.validateParameters(); // Should not throw
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateParameters_InvalidSeparator() {
        check.setSeparator(";");
        check.validateParameters(); // Should throw IllegalStateException
    }

    @Test
    public void testSetSeparator() {
        check.setSeparator(":");
        // Verify that the separator is set correctly
        assertEquals(":", check.getSeparator());
    }
}
