package io.datakernel.jmx;

import io.datakernel.di.Key;
import io.datakernel.di.module.UniqueQualifierImpl;
import io.datakernel.jmx.DynamicMBeanFactory;
import io.datakernel.jmx.JmxRegistry;
import io.datakernel.jmx.JmxBeanSettings;
import io.datakernel.worker.WorkerPool;
import org.junit.Before;
import org.junit.Test;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class JmxRegistryTest {
    private MBeanServer mbs;
    private DynamicMBeanFactory mbeanFactory;
    private JmxRegistry jmxRegistry;
    private Key<Object> key;
    private Object singletonInstance;
    private JmxBeanSettings settings;
    private WorkerPool workerPool;

    @Before
    public void setUp() {
        mbs = mock(MBeanServer.class);
        mbeanFactory = mock(DynamicMBeanFactory.class);
        jmxRegistry = JmxRegistry.create(mbs, mbeanFactory);
        key = Key.of(Object.class);
        singletonInstance = new Object();
        settings = JmxBeanSettings.of(Collections.emptyList(), new HashMap<>());
        workerPool = mock(WorkerPool.class);
    }

    @Test
    public void testRegisterSingleton() throws Exception {
        when(mbeanFactory.createDynamicMBean(anyList(), any(), anyBoolean())).thenReturn(mock(Object.class));
        jmxRegistry.registerSingleton(key, singletonInstance, settings);
        verify(mbs, times(1)).registerMBean(any(), any(ObjectName.class));
    }

    @Test
    public void testUnregisterSingleton() throws Exception {
        jmxRegistry.registerSingleton(key, singletonInstance, settings);
        jmxRegistry.unregisterSingleton(key, singletonInstance);
        verify(mbs, times(1)).unregisterMBean(any(ObjectName.class));
    }

    @Test
    public void testRegisterWorkers() throws Exception {
        List<Object> poolInstances = Collections.singletonList(new Object());
        when(mbeanFactory.createDynamicMBean(anyList(), any(), anyBoolean())).thenReturn(mock(Object.class));
        jmxRegistry.registerWorkers(workerPool, key, poolInstances, settings);
        verify(mbs, times(1)).registerMBean(any(), any(ObjectName.class));
    }

    @Test
    public void testUnregisterWorkers() throws Exception {
        List<Object> poolInstances = Collections.singletonList(new Object());
        jmxRegistry.registerWorkers(workerPool, key, poolInstances, settings);
        jmxRegistry.unregisterWorkers(workerPool, key, poolInstances);
        verify(mbs, times(1)).unregisterMBean(any(ObjectName.class));
    }

    @Test
    public void testUnregisterAll() throws Exception {
        jmxRegistry.registerSingleton(key, singletonInstance, settings);
        jmxRegistry.unregisterAll();
        verify(mbs, times(1)).unregisterMBean(any(ObjectName.class));
    }

    @Test
    public void testGetRegisteredSingletons() {
        jmxRegistry.registerSingleton(key, singletonInstance, settings);
        assert jmxRegistry.getRegisteredSingletons() == 1;
    }

    @Test
    public void testGetRegisteredPools() {
        List<Object> poolInstances = Collections.singletonList(new Object());
        jmxRegistry.registerWorkers(workerPool, key, poolInstances, settings);
        assert jmxRegistry.getRegisteredPools() == 1;
    }

    @Test
    public void testGetTotallyRegisteredMBeans() {
        jmxRegistry.registerSingleton(key, singletonInstance, settings);
        assert jmxRegistry.getTotallyRegisteredMBeans() == 1;
    }

    @Test
    public void testSetRefreshPeriod() {
        jmxRegistry.setRefreshPeriod("1000");
        verify(mbeanFactory, times(1)).setRefreshPeriod(anyLong());
    }

    @Test
    public void testSetMaxRefreshesPerOneCycle() {
        jmxRegistry.setMaxRefreshesPerOneCycle(10);
        verify(mbeanFactory, times(1)).setMaxJmxRefreshesPerOneCycle(10);
    }
}
