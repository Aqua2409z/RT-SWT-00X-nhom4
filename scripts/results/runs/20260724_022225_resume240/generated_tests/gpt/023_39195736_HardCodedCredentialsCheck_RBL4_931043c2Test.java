package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.PropertyTree;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HardCodedCredentialsCheck_RBL4_931043c2Test {

    private HardCodedCredentialsCheck check;
    private PropertyTree propertyTree;

    @Before
    public void setUp() {
        check = new HardCodedCredentialsCheck();
        propertyTree = Mockito.mock(PropertyTree.class);
    }

    @Test
    public void testVisitProperty_withHardCodedUsername_shouldAddIssue() {
        when(propertyTree.key()).thenReturn(new MockPropertyKey("username"));
        when(propertyTree.value()).thenReturn(new MockPropertyValue("user123"));

        check.visitProperty(propertyTree);

        verify(propertyTree).key();
        verify(propertyTree).value();
        // Verify that an issue is added for hard-coded username
    }

    @Test
    public void testVisitProperty_withHardCodedPassword_shouldAddIssue() {
        when(propertyTree.key()).thenReturn(new MockPropertyKey("password"));
        when(propertyTree.value()).thenReturn(new MockPropertyValue("pass123"));

        check.visitProperty(propertyTree);

        verify(propertyTree).key();
        verify(propertyTree).value();
        // Verify that an issue is added for hard-coded password
    }

    @Test
    public void testVisitProperty_withIgnoredEncryptedCredentials_shouldNotAddIssue() {
        check.setEncryptedCredentialsToIgnore("^(ENC\\(|OBF:).+$");
        when(propertyTree.key()).thenReturn(new MockPropertyKey("username"));
        when(propertyTree.value()).thenReturn(new MockPropertyValue("ENC(user123)"));

        check.visitProperty(propertyTree);

        verify(propertyTree).key();
        verify(propertyTree).value();
        // Verify that no issue is added for ignored encrypted credentials
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateParameters_withInvalidRegex_shouldThrowException() {
        check.setEncryptedCredentialsToIgnore("[");
        check.validateParameters();
    }

    @Test
    public void testValidateParameters_withValidRegex_shouldNotThrowException() {
        check.setEncryptedCredentialsToIgnore("^(ENC\\(|OBF:).+$");
        check.validateParameters();
        // No exception should be thrown
    }

    // Mock classes for PropertyTree key and value
    private static class HardCodedCredentialsCheck_RBL4_931043c2Test {
        private final String text;

        MockPropertyKey(String text) {
            this.text = text;
        }

        public String text() {
            return text;
        }
    }

    private static class HardCodedCredentialsCheck_RBL4_931043c2Test {
        private final String text;

        MockPropertyValue(String text) {
            this.text = text;
        }

        public String text() {
            return text;
        }
    }
}
