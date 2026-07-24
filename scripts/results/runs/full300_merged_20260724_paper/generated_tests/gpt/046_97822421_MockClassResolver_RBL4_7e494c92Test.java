
package com.pchudzik.springmock.infrastructure.spring;

import com.pchudzik.springmock.infrastructure.DoubleFactory;
import com.pchudzik.springmock.infrastructure.MockConstants;
import com.pchudzik.springmock.infrastructure.definition.DoubleDefinition;
import com.pchudzik.springmock.infrastructure.definition.registry.DoubleRegistry;
import com.pchudzik.springmock.infrastructure.definition.registry.DoubleSearch;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MockClassResolver_RBL4_7e494c92Test {
    private DoubleRegistry doubleRegistry;
    private MockClassResolver mockClassResolver;

    @Before
    public void setUp() {
        doubleRegistry = Mockito.mock(DoubleRegistry.class);
        mockClassResolver = new MockClassResolver(doubleRegistry);
    }

    @Test
    public void testPredictBeanType_WhenExactlyOneDoubleExists_ShouldReturnDoubleClass() {
        String beanName = "testBean";
        Class<?> expectedClass = String.class; // Example class
        DoubleDefinition doubleDefinition = Mockito.mock(DoubleDefinition.class);
        Mockito.when(doubleDefinition.getDoubleClass()).thenReturn(expectedClass);

        DoubleSearch mockSearch = Mockito.mock(DoubleSearch.class);
        Mockito.when(mockSearch.containsExactlyOneDouble(beanName)).thenReturn(true);
        Mockito.when(mockSearch.findOneDefinition(beanName)).thenReturn(doubleDefinition);
        Mockito.when(doubleRegistry.mockSearch()).thenReturn(mockSearch);

        Class<?> result = mockClassResolver.predictBeanType(String.class, beanName);

        assertEquals(expectedClass, result);
    }

    @Test
    public void testPredictBeanType_WhenNoDoubleExists_ShouldReturnNull() {
        String beanName = "testBean";

        DoubleSearch mockSearch = Mockito.mock(DoubleSearch.class);
        Mockito.when(mockSearch.containsExactlyOneDouble(beanName)).thenReturn(false);
        Mockito.when(doubleRegistry.mockSearch()).thenReturn(mockSearch);

        Class<?> result = mockClassResolver.predictBeanType(String.class, beanName);

        assertNull(result);
    }
}
