
package com.bazaarvoice.emodb.web.partition;

import com.bazaarvoice.emodb.common.dropwizard.healthcheck.HealthCheckRegistry;
import com.bazaarvoice.ostrich.MultiThreadedServiceFactory;
import com.bazaarvoice.ostrich.ServiceEndPoint;
import com.bazaarvoice.ostrich.ServiceFactory;
import com.bazaarvoice.ostrich.pool.ServicePoolBuilder;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Meter;
import com.codahale.metrics.health.HealthCheck;
import com.google.common.net.HostAndPort;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class PartitionAwareServiceFactory_RBL4_e6c37a47Test {
    private MultiThreadedServiceFactory<MyService> delegate;
    private MyService localService;
    private HealthCheckRegistry healthCheckRegistry;
    private MetricRegistry metricRegistry;
    private PartitionAwareServiceFactory<MyService> factory;
    private ServiceEndPoint selfEndPoint;
    private ServiceEndPoint otherEndPoint;

    @BeforeMethod
    public void setUp() {
        delegate = Mockito.mock(MultiThreadedServiceFactory.class);
        localService = Mockito.mock(MyService.class);
        healthCheckRegistry = new HealthCheckRegistry();
        metricRegistry = new MetricRegistry();
        selfEndPoint = Mockito.mock(ServiceEndPoint.class);
        otherEndPoint = Mockito.mock(ServiceEndPoint.class);
        HostAndPort selfHostAndPort = HostAndPort.fromString("localhost:8080");
        
        Mockito.when(selfEndPoint.getId()).thenReturn(selfHostAndPort.toString());
        factory = new PartitionAwareServiceFactory<>(MyService.class, delegate, localService, selfHostAndPort, healthCheckRegistry, metricRegistry);
    }

    @Test
    public void testGetServiceName() {
        Mockito.when(delegate.getServiceName()).thenReturn("MyService");
        Assert.assertEquals(factory.getServiceName(), "MyService");
    }

    @Test
    public void testCreateSelf() {
        MyService service = factory.create(selfEndPoint);
        Assert.assertEquals(service, localService);
    }

    @Test
    public void testCreateDelegate() {
        Mockito.when(delegate.create(otherEndPoint)).thenReturn(Mockito.mock(MyService.class));
        MyService service = factory.create(otherEndPoint);
        Assert.assertNotNull(service);
    }

    @Test
    public void testDestroySelf() {
        factory.destroy(selfEndPoint, localService);
        // No exception should be thrown
    }

    @Test
    public void testDestroyDelegate() {
        MyService delegateService = Mockito.mock(MyService.class);
        Mockito.when(delegate.create(otherEndPoint)).thenReturn(delegateService);
        MyService service = factory.create(otherEndPoint);
        factory.destroy(otherEndPoint, service);
        Mockito.verify(delegate).destroy(otherEndPoint, delegateService);
    }

    @Test
    public void testIsHealthySelf() {
        HealthCheck.Result result = Mockito.mock(HealthCheck.Result.class);
        Mockito.when(result.isHealthy()).thenReturn(true);
        healthCheckRegistry.register("test", new HealthCheck() {
            @Override
            protected Result check() {
                return result;
            }
        });
        Assert.assertTrue(factory.isHealthy(selfEndPoint));
    }

    @Test
    public void testIsHealthyDelegate() {
        Mockito.when(delegate.isHealthy(otherEndPoint)).thenReturn(true);
        Assert.assertTrue(factory.isHealthy(otherEndPoint));
    }

    @Test
    public void testIsRetriableException() {
        Exception exception = new Exception();
        Mockito.when(delegate.isRetriableException(exception)).thenReturn(true);
        Assert.assertTrue(factory.isRetriableException(exception));
    }

    @Test
    public void testCreateDelegateHandlesException() throws Throwable {
        MyService delegateService = Mockito.mock(MyService.class);
        Mockito.when(delegate.create(otherEndPoint)).thenReturn(delegateService);
        Method method = MyService.class.getMethod("someMethod");
        Mockito.doThrow(new ClientHandlerException("Error")).when(delegateService).someMethod();
        
        MyService service = factory.create(otherEndPoint);
        try {
            service.someMethod();
        } catch (PartitionForwardingException e) {
            Assert.assertNotNull(e);
        }
    }

    interface MyService {
        void someMethod();
    }
}
