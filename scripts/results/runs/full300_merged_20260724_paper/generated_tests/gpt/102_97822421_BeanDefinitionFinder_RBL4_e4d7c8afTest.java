
package com.pchudzik.springmock.infrastructure.spring.util;

import com.pchudzik.springmock.infrastructure.definition.registry.DoubleRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BeanDefinitionFinder_RBL4_e4d7c8afTest {
    private ConfigurableListableBeanFactory definitionRegistry;
    private DoubleRegistry doubleRegistry;
    private BeanDefinitionFinder beanDefinitionFinder;

    @Before
    public void setUp() {
        definitionRegistry = mock(ConfigurableListableBeanFactory.class);
        doubleRegistry = mock(DoubleRegistry.class);
        beanDefinitionFinder = new BeanDefinitionFinder(definitionRegistry, doubleRegistry);
    }

    @Test
    public void testTryToFindBeanDefinition_WhenBeanExists() {
        String beanName = "testBean";
        BeanDefinition beanDefinition = mock(BeanDefinition.class);
        when(definitionRegistry.getBeanDefinition(beanName)).thenReturn(beanDefinition);

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindBeanDefinition(beanName);

        assertTrue(result.isPresent());
        assertEquals(beanDefinition, result.get());
    }

    @Test
    public void testTryToFindBeanDefinition_WhenBeanDoesNotExist() {
        String beanName = "nonExistentBean";
        when(definitionRegistry.getBeanDefinition(beanName)).thenThrow(new NoSuchBeanDefinitionException(beanName));

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindBeanDefinition(beanName);

        assertFalse(result.isPresent());
    }

    @Test
    public void testTryToFindBeanDefinition_WithDoubleClass_WhenBeanExists() {
        String beanName = "testBean";
        Class<?> doubleClass = Object.class;
        BeanDefinition beanDefinition = mock(BeanDefinition.class);
        when(definitionRegistry.getBeanDefinition(beanName)).thenReturn(beanDefinition);
        when(doubleRegistry.doublesSearch().containsExactlyOneDouble(beanName, doubleClass)).thenReturn(false);

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindBeanDefinition(beanName, doubleClass);

        assertTrue(result.isPresent());
        assertEquals(beanDefinition, result.get());
    }

    @Test
    public void testTryToFindBeanDefinition_WithDoubleClass_WhenBeanDoesNotExist() {
        String beanName = "nonExistentBean";
        Class<?> doubleClass = Object.class;
        when(definitionRegistry.getBeanDefinition(beanName)).thenThrow(new NoSuchBeanDefinitionException(beanName));
        when(doubleRegistry.doublesSearch().containsExactlyOneDouble(anyString(), eq(doubleClass))).thenReturn(false);

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindBeanDefinition(beanName, doubleClass);

        assertFalse(result.isPresent());
    }

    @Test
    public void testTryToFindSingleBeanDefinition_WhenSingleBeanExists() {
        Class<?> doubleClass = Object.class;
        String beanName = "singleBean";
        BeanDefinition beanDefinition = mock(BeanDefinition.class);
        when(definitionRegistry.getBeanNamesForType(doubleClass)).thenReturn(new String[]{beanName});
        when(definitionRegistry.getBeanDefinition(beanName)).thenReturn(beanDefinition);
        when(doubleRegistry.doublesSearch().containsExactlyOneDouble(beanName, doubleClass)).thenReturn(false);

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindSingleBeanDefinition(doubleClass);

        assertTrue(result.isPresent());
        assertEquals(beanDefinition, result.get());
    }

    @Test
    public void testTryToFindSingleBeanDefinition_WhenNoBeansExist() {
        Class<?> doubleClass = Object.class;
        when(definitionRegistry.getBeanNamesForType(doubleClass)).thenReturn(new String[]{});

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindSingleBeanDefinition(doubleClass);

        assertFalse(result.isPresent());
    }

    @Test
    public void testTryToFindSingleBeanDefinition_WhenMultipleBeansExist() {
        Class<?> doubleClass = Object.class;
        when(definitionRegistry.getBeanNamesForType(doubleClass)).thenReturn(new String[]{"bean1", "bean2"});

        Optional<BeanDefinition> result = beanDefinitionFinder.tryToFindSingleBeanDefinition(doubleClass);

        assertFalse(result.isPresent());
    }

    @Test
    public void testDoubleRegistryContainsDouble() {
        String doubleName = "doubleBean";
        Class<?> doubleClass = Object.class;
        when(doubleRegistry.doublesSearch().containsExactlyOneDouble(doubleName, doubleClass)).thenReturn(true);

        boolean result = beanDefinitionFinder.doubleRegistryContainsDouble(doubleName, doubleClass);

        assertTrue(result);
    }
}
