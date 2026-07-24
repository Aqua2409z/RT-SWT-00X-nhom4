package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.KeyTree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class KeyNamingConventionCheck_RBL4_e39f864dTest {

    private KeyNamingConventionCheck check;

    @Before
    public void setUp() {
        check = new KeyNamingConventionCheck();
    }

    @Test
    public void testDefaultFormat() {
        assertEquals("^[A-Za-z][.A-Za-z0-9]*$", check.getFormat());
    }

    @Test
    public void testValidKey() {
        KeyTree keyTree = Mockito.mock(KeyTree.class);
        Mockito.when(keyTree.text()).thenReturn("valid.key");

        check.visitKey(keyTree);
        assertTrue(check.getIssues().isEmpty());
    }

    @Test
    public void testInvalidKey() {
        KeyTree keyTree = Mockito.mock(KeyTree.class);
        Mockito.when(keyTree.text()).thenReturn("1invalid.key");

        check.visitKey(keyTree);
        assertEquals(1, check.getIssues().size());
        assertEquals("Rename key \"1invalid.key\" to match the regular expression: " + check.getFormat(), check.getIssues().get(0).message());
    }

    @Test(expected = IllegalStateException.class)
    public void testInvalidRegexFormat() {
        check.setFormat("[");
        check.validateParameters();
    }

    @Test
    public void testCustomFormat() {
        check.setFormat("^[a-z]+$");
        KeyTree validKeyTree = Mockito.mock(KeyTree.class);
        Mockito.when(validKeyTree.text()).thenReturn("validkey");
        KeyTree invalidKeyTree = Mockito.mock(KeyTree.class);
        Mockito.when(invalidKeyTree.text()).thenReturn("InvalidKey");

        check.visitKey(validKeyTree);
        assertTrue(check.getIssues().isEmpty());

        check.visitKey(invalidKeyTree);
        assertEquals(1, check.getIssues().size());
        assertEquals("Rename key \"InvalidKey\" to match the regular expression: " + check.getFormat(), check.getIssues().get(0).message());
    }
}
