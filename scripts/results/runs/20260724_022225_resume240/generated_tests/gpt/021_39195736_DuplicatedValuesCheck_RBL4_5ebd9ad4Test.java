package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.KeyTree;
import org.sonar.plugins.jproperties.api.tree.PropertiesTree;
import org.sonar.plugins.jproperties.api.tree.PropertyTree;
import org.sonar.plugins.jproperties.api.visitors.issue.PreciseIssue;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class DuplicatedValuesCheck_RBL4_5ebd9ad4Test {

    private DuplicatedValuesCheck check;
    private PropertiesTree propertiesTree;

    @Before
    public void setUp() {
        check = new DuplicatedValuesCheck();
        propertiesTree = mock(PropertiesTree.class);
    }

    @Test
    public void testVisitPropertiesWithNoDuplicates() {
        PropertyTree property1 = createMockProperty("key1", "value1");
        PropertyTree property2 = createMockProperty("key2", "value2");

        when(propertiesTree.properties()).thenReturn(List.of(property1, property2));

        check.visitProperties(propertiesTree);

        // Verify that no issues are added
        verifyNoMoreInteractions(check);
    }

    @Test
    public void testVisitPropertiesWithDuplicates() {
        PropertyTree property1 = createMockProperty("key1", "value1");
        PropertyTree property2 = createMockProperty("key2", "value1");

        when(propertiesTree.properties()).thenReturn(List.of(property1, property2));

        check.visitProperties(propertiesTree);

        // Verify that an issue is added
        verify(check, times(1)).addPreciseIssue(any(KeyTree.class), anyString());
    }

    @Test
    public void testVisitPropertiesWithIgnoredValues() {
        check.setValuesToIgnore("value1");

        PropertyTree property1 = createMockProperty("key1", "value1");
        PropertyTree property2 = createMockProperty("key2", "value1");

        when(propertiesTree.properties()).thenReturn(List.of(property1, property2));

        check.visitProperties(propertiesTree);

        // Verify that no issues are added since value1 is ignored
        verifyNoMoreInteractions(check);
    }

    @Test(expected = IllegalStateException.class)
    public void testValidateParametersWithInvalidRegex() {
        check.setValuesToIgnore("[");
        check.validateParameters();
    }

    @Test
    public void testValidateParametersWithValidRegex() {
        check.setValuesToIgnore("validRegex");
        check.validateParameters(); // Should not throw
    }

    private PropertyTree createMockProperty(String key, String value) {
        PropertyTree propertyTree = mock(PropertyTree.class);
        KeyTree keyTree = mock(KeyTree.class);
        when(keyTree.text()).thenReturn(key);
        when(propertyTree.key()).thenReturn(keyTree);
        when(propertyTree.value()).thenReturn(mockValue(value));
        return propertyTree;
    }

    private PropertyTree mockValue(String value) {
        PropertyTree valueTree = mock(PropertyTree.class);
        when(valueTree.text()).thenReturn(value);
        return valueTree;
    }
}
