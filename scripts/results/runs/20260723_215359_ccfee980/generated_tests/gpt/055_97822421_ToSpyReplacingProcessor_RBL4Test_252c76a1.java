
package com.pchudzik.springmock.infrastructure.spring;

import com.pchudzik.springmock.infrastructure.DoubleFactory;
import com.pchudzik.springmock.infrastructure.MockConstants;
import com.pchudzik.springmock.infrastructure.definition.DoubleDefinition;
import com.pchudzik.springmock.infrastructure.definition.registry.DoubleRegistry;
import com.pchudzik.springmock.infrastructure.definition.registry.DoubleSearch;
import com.pchudzik.springmock.infrastructure.spring.util.ApplicationContextWalker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ToSpyReplacingProcessor_RBL4Test_252c76a1 {
    private ApplicationContext applicationContext;
    private DoubleRegistry doubleRegistry;
    private DoubleFactory doubleFactory;
    private DoubleDefinitionsRegistrationContext doubleDefinitionsRegistrationContext;
    private ToSpyReplacingProcessor processor;

    @Before
    public void setUp() {
        applicationContext = mock(ApplicationContext.class);
        doubleRegistry = mock(DoubleRegistry.class);
        doubleFactory = mock(DoubleFactory.class);
        doubleDefinitionsRegistrationContext = mock(DoubleDefinitionsRegistrationContext.class);
        processor = new ToSpyReplacingProcessor(applicationContext, doubleRegistry, doubleFactory, doubleDefinitionsRegistrationContext);
    }

    @Test
    public void testPostProcessAfterInitialization_withSingleSpyDefinition() throws BeansException {
        Object bean = new Object();
        String beanName = "testBean";
        DoubleDefinition spyDefinition = mock(DoubleDefinition.class);
        when(doubleRegistry.spySearch()).thenReturn(mock(DoubleSearch.class));
        when(doubleRegistry.spySearch().containsAnyDoubleMatching(beanName, bean.getClass())).thenReturn(true);
        when(doubleRegistry.spySearch().containsExactlyOneDouble(beanName, bean.getClass())).thenReturn(true);
        when(doubleRegistry.spySearch().findOneDefinition(beanName, bean.getClass())).thenReturn(spyDefinition);
        when(doubleFactory.createSpy(any(), any())).thenReturn(new Object());

        Object result = processor.postProcessAfterInitialization(bean, beanName);

        assertNotNull(result);
        verify(doubleFactory).createSpy(any(), eq(spyDefinition));
    }

    @Test
    public void testPostProcessAfterInitialization_withMultipleSpyDefinitions() throws BeansException {
        Object bean = new Object();
        String beanName = "testBean";
        when(doubleRegistry.spySearch()).thenReturn(mock(DoubleSearch.class));
        when(doubleRegistry.spySearch().containsAnyDoubleMatching(beanName, bean.getClass())).thenReturn(true);
        when(doubleRegistry.spySearch().containsExactlyOneDouble(beanName, bean.getClass())).thenReturn(false);
        when(doubleRegistry.spySearch().containsExactlyOneDouble(beanName)).thenReturn(false);
        when(doubleRegistry.spySearch().containsExactlyOneDouble(bean.getClass())).thenReturn(false);

        Object result = processor.postProcessAfterInitialization(bean, beanName);

        assertEquals(bean, result);
    }

    @Test
    public void testPostProcessAfterInitialization_withNoSpyDefinitions() throws BeansException {
        Object bean = new Object();
        String beanName = "testBean";
        when(doubleRegistry.spySearch()).thenReturn(mock(DoubleSearch.class));
        when(doubleRegistry.spySearch().containsAnyDoubleMatching(beanName, bean.getClass())).thenReturn(false);

        Object result = processor.postProcessAfterInitialization(bean, beanName);

        assertEquals(bean, result);
    }

    @Test
    public void testCreateSpy_whenSpyDefinitionRegistered() {
        Object bean = new Object();
        DoubleDefinition spyDefinition = mock(DoubleDefinition.class);
        when(doubleDefinitionsRegistrationContext.isBeanDefinitionRegisteredForDouble(spyDefinition)).thenReturn(true);

        Object result = processor.createSpy(bean, spyDefinition);

        assertEquals(bean, result);
        verify(doubleDefinitionsRegistrationContext, never()).registerSpyReplacement(spyDefinition);
    }

    @Test
    public void testCreateSpy_whenSpyDefinitionNotRegistered() {
        Object bean = new Object();
        DoubleDefinition spyDefinition = mock(DoubleDefinition.class);
        when(doubleDefinitionsRegistrationContext.isBeanDefinitionRegisteredForDouble(spyDefinition)).thenReturn(false);
        when(doubleFactory.createSpy(any(), any())).thenReturn(new Object());

        Object result = processor.createSpy(bean, spyDefinition);

        assertNotNull(result);
        verify(doubleDefinitionsRegistrationContext).registerSpyReplacement(spyDefinition);
    }

    @Test
    public void testResolveBeanClass_withAopProxy() {
        Object bean = mock(Advised.class);
        when(AopUtils.isAopProxy(bean)).thenReturn(true);
        when(((Advised) bean).getTargetSource().getTarget()).thenReturn(new Object());

        Class<?> result = processor.resolveBeanClass(bean);

        assertNotNull(result);
    }

    @Test
    public void testResolveBeanClass_withoutAopProxy() {
        Object bean = new Object();

        Class<?> result = processor.resolveBeanClass(bean);

        assertEquals(bean.getClass(), result);
    }
}
