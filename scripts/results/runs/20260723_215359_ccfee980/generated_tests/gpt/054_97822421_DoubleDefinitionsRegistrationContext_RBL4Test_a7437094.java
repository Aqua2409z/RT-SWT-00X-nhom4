
package com.pchudzik.springmock.infrastructure.spring;

import com.pchudzik.springmock.infrastructure.definition.DoubleDefinition;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DoubleDefinitionsRegistrationContext_RBL4Test_a7437094 {
    private DoubleDefinitionsRegistrationContext context;
    private BeanDefinitionRegistry registry;
    private DoubleDefinition mockDefinition;
    private DoubleDefinition spyDefinition;

    @Before
    public void setUp() {
        context = new DoubleDefinitionsRegistrationContext();
        registry = mock(BeanDefinitionRegistry.class);
        mockDefinition = mock(DoubleDefinition.class);
        spyDefinition = mock(DoubleDefinition.class);
    }

    @Test
    public void testRegisterMockWithName() {
        when(mockDefinition.getName()).thenReturn("mockName");
        when(mockDefinition.getDoubleClass()).thenReturn(Object.class);
        
        context.registerMock(registry, "mockName", mockDefinition);
        
        verify(registry).registerBeanDefinition(eq("mockName"), any(BeanDefinition.class));
        assertTrue(context.isBeanDefinitionRegisteredForDouble(mockDefinition));
    }

    @Test
    public void testRegisterMockWithoutName() {
        when(mockDefinition.getName()).thenReturn("mockName");
        when(mockDefinition.getDoubleClass()).thenReturn(Object.class);
        
        context.registerMock(registry, mockDefinition);
        
        verify(registry).registerBeanDefinition(eq("mockName"), any(BeanDefinition.class));
        assertTrue(context.isBeanDefinitionRegisteredForDouble(mockDefinition));
    }

    @Test
    public void testRegisterSpy() {
        when(spyDefinition.getName()).thenReturn("spyName");
        when(spyDefinition.getDoubleClass()).thenReturn(Object.class);
        
        context.registerSpy(registry, spyDefinition);
        
        verify(registry).registerBeanDefinition(eq("spyName"), any(BeanDefinition.class));
        assertTrue(context.isBeanDefinitionRegisteredForDouble(spyDefinition));
    }

    @Test
    public void testIsBeanDefinitionRegisteredForDouble() {
        when(mockDefinition.getName()).thenReturn("mockName");
        context.registerSpyReplacement(mockDefinition);
        
        assertTrue(context.isBeanDefinitionRegisteredForDouble(mockDefinition));
    }

    @Test
    public void testIsBeanDefinitionNotRegisteredForDouble() {
        when(mockDefinition.getName()).thenReturn("mockName");
        
        assertFalse(context.isBeanDefinitionRegisteredForDouble(mockDefinition));
    }

    @Test
    public void testRegisterBeanDefinitionRemovesExistingDefinition() {
        when(mockDefinition.getName()).thenReturn("mockName");
        when(mockDefinition.getDoubleClass()).thenReturn(Object.class);
        when(registry.containsBeanDefinition("mockName")).thenReturn(true);
        
        context.registerMock(registry, "mockName", mockDefinition);
        
        verify(registry).removeBeanDefinition("mockName");
        verify(registry).registerBeanDefinition(eq("mockName"), any(BeanDefinition.class));
    }
}
