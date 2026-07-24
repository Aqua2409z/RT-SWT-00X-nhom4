
package com.pchudzik.springmock.infrastructure.spring.util;

import com.pchudzik.springmock.infrastructure.definition.registry.DoubleRegistry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ApplicationContextWalker_RBL4_8573e9e6Test {
    private ApplicationContext applicationContext;
    private ApplicationContextWalker applicationContextWalker;
    private DoubleRegistry doubleRegistry;

    @Before
    public void setUp() {
        applicationContext = mock(ApplicationContext.class);
        doubleRegistry = mock(DoubleRegistry.class);
        when(applicationContext.getBean(DoubleRegistry.BEAN_NAME, DoubleRegistry.class)).thenReturn(doubleRegistry);
        applicationContextWalker = new ApplicationContextWalker(applicationContext);
    }

    @Test
    public void testGetBeanDefinition() {
        String beanName = "testBean";
        BeanDefinition beanDefinition = mock(BeanDefinition.class);
        BeanDefinitionFinder beanDefinitionFinder = mock(BeanDefinitionFinder.class);
        
        when(applicationContext.getBean(DoubleRegistry.BEAN_NAME, DoubleRegistry.class)).thenReturn(doubleRegistry);
        when(doubleRegistry.getBeanDefinitionFinder()).thenReturn(beanDefinitionFinder);
        when(beanDefinitionFinder.tryToFindBeanDefinition(beanName)).thenReturn(Optional.of(beanDefinition));

        BeanDefinition result = applicationContextWalker.getBeanDefinition(beanName);
        assertNotNull(result);
        assertEquals(beanDefinition, result);
    }

    @Test(expected = NoSuchBeanDefinitionException.class)
    public void testGetBeanDefinitionThrowsException() {
        String beanName = "nonExistentBean";
        BeanDefinitionFinder beanDefinitionFinder = mock(BeanDefinitionFinder.class);
        
        when(applicationContext.getBean(DoubleRegistry.BEAN_NAME, DoubleRegistry.class)).thenReturn(doubleRegistry);
        when(doubleRegistry.getBeanDefinitionFinder()).thenReturn(beanDefinitionFinder);
        when(beanDefinitionFinder.tryToFindBeanDefinition(beanName)).thenReturn(Optional.empty());

        applicationContextWalker.getBeanDefinition(beanName);
    }

    @Test
    public void testHasOnlyOneBeanOfClass() {
        Class<String> beanClass = String.class;
        Map<String, String> beans = new HashMap<>();
        beans.put("bean1", "test");
        
        when(applicationContext.getBeansOfType(beanClass)).thenReturn(beans);

        assertTrue(applicationContextWalker.hasOnlyOneBeanOfClass(beanClass));
    }

    @Test
    public void testHasOnlyOneBeanOfClassReturnsFalse() {
        Class<String> beanClass = String.class;
        Map<String, String> beans = new HashMap<>();
        beans.put("bean1", "test");
        beans.put("bean2", "test2");
        
        when(applicationContext.getBeansOfType(beanClass)).thenReturn(beans);

        assertFalse(applicationContextWalker.hasOnlyOneBeanOfClass(beanClass));
    }

    @Test
    public void testGetBeanDefinitionNames() {
        String[] beanNames = {"bean1", "bean2"};
        when(applicationContext.getBeanDefinitionNames()).thenReturn(beanNames);

        Collection<String> result = applicationContextWalker.getBeanDefinitionNames();
        assertEquals(2, result.size());
        assertTrue(result.contains("bean1"));
        assertTrue(result.contains("bean2"));
    }
}
