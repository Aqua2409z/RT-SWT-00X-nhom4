package com.linkedin.d2.discovery.event;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.discovery.event.PropertyEventBus;
import com.linkedin.d2.discovery.event.PropertyEventBusRequestsThrottler;
import com.linkedin.d2.discovery.event.PropertyEventSubscriber;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

public class PropertyEventBusRequestsThrottler_RBL4_41aae381Test {

    private PropertyEventBus<String> mockEventBus;
    private PropertyEventSubscriber<String> mockExternalSubscriber;
    private PropertyEventBusRequestsThrottler<String> throttler;
    private Callback<None> mockCallback;

    @BeforeMethod
    public void setUp() {
        mockEventBus = Mockito.mock(PropertyEventBus.class);
        mockExternalSubscriber = Mockito.mock(PropertyEventSubscriber.class);
        mockCallback = Mockito.mock(Callback.class);
        List<String> keysToFetch = Arrays.asList("key1", "key2", "key3");
        throttler = new PropertyEventBusRequestsThrottler<>(mockEventBus, mockExternalSubscriber, keysToFetch, 2, true);
    }

    @Test
    public void testSendRequestsWithNoKeys() {
        List<String> emptyKeys = Arrays.asList();
        PropertyEventBusRequestsThrottler<String> emptyThrottler = new PropertyEventBusRequestsThrottler<>(mockEventBus, mockExternalSubscriber, emptyKeys, 2, true);
        emptyThrottler.sendRequests(mockCallback);
        Mockito.verify(mockCallback).onSuccess(None.none());
    }

    @Test
    public void testSendRequestsWithKeys() {
        throttler.sendRequests(mockCallback);
        Mockito.verify(mockEventBus, Mockito.times(2)).register(Mockito.anySet(), Mockito.eq(mockExternalSubscriber));
    }

    @Test
    public void testOnInitializeCallsNext() {
        throttler.sendRequests(mockCallback);
        throttler._eventBusUpdaterSubscriberSubscriber.onInitialize("key1", "value1");
        Mockito.verify(mockCallback, Mockito.never()).onSuccess(None.none());
        throttler._eventBusUpdaterSubscriberSubscriber.onInitialize("key2", "value2");
        throttler._eventBusUpdaterSubscriberSubscriber.onInitialize("key3", "value3");
        Mockito.verify(mockCallback).onSuccess(None.none());
    }

    @Test
    public void testOnAddCallsNext() {
        throttler.sendRequests(mockCallback);
        throttler._eventBusUpdaterSubscriberSubscriber.onAdd("key1", "value1");
        throttler._eventBusUpdaterSubscriberSubscriber.onAdd("key2", "value2");
        throttler._eventBusUpdaterSubscriberSubscriber.onAdd("key3", "value3");
        Mockito.verify(mockCallback).onSuccess(None.none());
    }

    @Test
    public void testOnRemoveCallsNext() {
        throttler.sendRequests(mockCallback);
        throttler._eventBusUpdaterSubscriberSubscriber.onRemove("key1");
        throttler._eventBusUpdaterSubscriberSubscriber.onRemove("key2");
        throttler._eventBusUpdaterSubscriberSubscriber.onRemove("key3");
        Mockito.verify(mockCallback).onSuccess(None.none());
    }

    @Test
    public void testMakeRequestsLimitsConcurrentRequests() {
        throttler.sendRequests(mockCallback);
        throttler._eventBusUpdaterSubscriberSubscriber.onInitialize("key1", "value1");
        throttler._eventBusUpdaterSubscriberSubscriber.onInitialize("key2", "value2");
        throttler._eventBusUpdaterSubscriberSubscriber.onInitialize("key3", "value3");
        Mockito.verify(mockEventBus, Mockito.times(3)).register(Mockito.anySet(), Mockito.eq(mockExternalSubscriber));
    }
}
