
package com.linkedin.d2.balancer.util;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.LoadBalancerWithFacilities;
import com.linkedin.d2.balancer.LoadBalancerWithFacilitiesDelegator;
import com.linkedin.d2.balancer.ServiceUnavailableException;
import com.linkedin.d2.balancer.WarmUpService;
import com.linkedin.d2.balancer.dualread.DualReadStateManager;
import com.linkedin.d2.balancer.properties.ServiceProperties;
import com.linkedin.d2.balancer.simple.SimpleLoadBalancer;
import com.linkedin.d2.balancer.util.downstreams.DownstreamServicesFetcher;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class WarmUpLoadBalancer_RBL4_7142b9faTest {
    private LoadBalancerWithFacilities mockLoadBalancer;
    private WarmUpService mockWarmUpService;
    private DownstreamServicesFetcher mockDownstreamServicesFetcher;
    private DualReadStateManager mockDualReadStateManager;
    private ScheduledExecutorService executorService;
    private WarmUpLoadBalancer warmUpLoadBalancer;

    @BeforeMethod
    public void setUp() {
        mockLoadBalancer = Mockito.mock(LoadBalancerWithFacilities.class);
        mockWarmUpService = Mockito.mock(WarmUpService.class);
        mockDownstreamServicesFetcher = Mockito.mock(DownstreamServicesFetcher.class);
        mockDualReadStateManager = Mockito.mock(DualReadStateManager.class);
        executorService = Executors.newSingleThreadScheduledExecutor();
        warmUpLoadBalancer = new WarmUpLoadBalancer(mockLoadBalancer, mockWarmUpService, executorService,
                "d2FsDirPath", "d2ServicePath", mockDownstreamServicesFetcher, 60, 1, mockDualReadStateManager, false);
    }

    @Test
    public void testStartSuccess() {
        when(mockLoadBalancer.start(any(Callback.class))).thenAnswer(invocation -> {
            Callback<None> callback = invocation.getArgument(0);
            callback.onSuccess(None.none());
            return null;
        });

        warmUpLoadBalancer.start(new Callback<None>() {
            @Override
            public void onError(Throwable e) {
                assert false : "Should not have failed";
            }

            @Override
            public void onSuccess(None result) {
                // Verify that the warm-up process continues
                verify(mockDownstreamServicesFetcher).getServiceNames(any(Callback.class));
            }
        });
    }

    @Test
    public void testStartError() {
        when(mockLoadBalancer.start(any(Callback.class))).thenAnswer(invocation -> {
            Callback<None> callback = invocation.getArgument(0);
            callback.onError(new Exception("Load balancer error"));
            return null;
        });

        warmUpLoadBalancer.start(new Callback<None>() {
            @Override
            public void onError(Throwable e) {
                assert e.getMessage().equals("Load balancer error");
            }

            @Override
            public void onSuccess(None result) {
                assert false : "Should not have succeeded";
            }
        });
    }

    @Test
    public void testWarmUpServices() {
        when(mockLoadBalancer.start(any(Callback.class))).thenAnswer(invocation -> {
            Callback<None> callback = invocation.getArgument(0);
            callback.onSuccess(None.none());
            return null;
        });

        when(mockDownstreamServicesFetcher.getServiceNames(any(Callback.class))).thenAnswer(invocation -> {
            Callback<List<String>> callback = invocation.getArgument(0);
            callback.onSuccess(List.of("service1", "service2"));
            return null;
        });

        warmUpLoadBalancer.start(new Callback<None>() {
            @Override
            public void onError(Throwable e) {
                assert false : "Should not have failed";
            }

            @Override
            public void onSuccess(None result) {
                // Verify that warm-up services are called
                verify(mockWarmUpService, times(2)).warmUpService(anyString(), any(Callback.class));
            }
        });
    }

    @Test
    public void testShutdown() {
        warmUpLoadBalancer.shutdown(null);
        // Verify that the load balancer's shutdown method is called
        verify(mockLoadBalancer).shutdown(any());
    }

    @Test
    public void testGetClient() throws ServiceUnavailableException {
        // Mocking the behavior of the load balancer
        when(mockLoadBalancer.getClient(any(), any())).thenReturn(null);

        warmUpLoadBalancer.getClient(null, null);
        // Verify that the service name is added to used services
        // This would require additional mocking to check the internal state
    }
}
