package com.linkedin.d2.discovery.stores.zk;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.discovery.event.PropertyEventBusImpl;
import com.linkedin.d2.discovery.stores.zk.ZKPersistentConnection;
import com.linkedin.d2.discovery.stores.zk.ZooKeeperConnectionAwareStore;
import com.linkedin.d2.discovery.stores.zk.ZooKeeperStore;
import com.linkedin.d2.discovery.stores.zk.builder.ZooKeeperStoreBuilder;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class ZooKeeperConnectionAwareStore_RBL4_0e47558fTest {

    private ZooKeeperStoreBuilder<ZooKeeperStore<String>> mockStoreBuilder;
    private ZKPersistentConnection mockZkPersistentConnection;
    private PropertyEventBusImpl<String> mockBus;
    private ZooKeeperConnectionAwareStore<String, ZooKeeperStore<String>> store;

    @BeforeMethod
    public void setUp() {
        mockStoreBuilder = Mockito.mock(ZooKeeperStoreBuilder.class);
        mockZkPersistentConnection = Mockito.mock(ZKPersistentConnection.class);
        mockBus = Mockito.mock(PropertyEventBusImpl.class);
        store = new ZooKeeperConnectionAwareStore<>(mockStoreBuilder, mockZkPersistentConnection);
    }

    @Test
    public void testSetBusImplWhenWrappedStoreIsNull() {
        store.setBusImpl(mockBus);
        Assert.assertTrue(store._pendingSetPublisher);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testSetBusWithInvalidBus() {
        PropertyEventBusImpl<String> invalidBus = Mockito.mock(PropertyEventBusImpl.class);
        store.setBus(invalidBus);
    }

    @Test
    public void testStart() {
        AtomicBoolean callbackInvoked = new AtomicBoolean(false);
        store.start(new Callback<None>() {
            @Override
            public void onSuccess(None result) {
                callbackInvoked.set(true);
            }

            @Override
            public void onError(Throwable e) {
                // Not expected to be called
            }
        });
        Assert.assertTrue(callbackInvoked.get());
    }

    @Test
    public void testShutdown() {
        AtomicBoolean callbackInvoked = new AtomicBoolean(false);
        store.shutdown(new Callback<None>() {
            @Override
            public void onSuccess(None result) {
                callbackInvoked.set(true);
            }

            @Override
            public void onError(Throwable e) {
                // Not expected to be called
            }
        });
        Assert.assertTrue(callbackInvoked.get());
    }

    @Test
    public void testSessionEstablished() {
        ZooKeeperStore<String> mockWrappedStore = Mockito.mock(ZooKeeperStore.class);
        Mockito.when(mockStoreBuilder.build()).thenReturn(mockWrappedStore);
        store.sessionEstablished(Mockito.mock(ZKPersistentConnection.Event.class));

        Mockito.verify(mockWrappedStore).start(Mockito.any());
        Assert.assertTrue(store._startupCompleted);
    }

    @Test
    public void testSessionExpired() {
        ZooKeeperStore<String> mockWrappedStore = Mockito.mock(ZooKeeperStore.class);
        Mockito.when(mockStoreBuilder.build()).thenReturn(mockWrappedStore);
        store.sessionEstablished(Mockito.mock(ZKPersistentConnection.Event.class));

        store.sessionExpired(Mockito.mock(ZKPersistentConnection.Event.class));
        Mockito.verify(mockWrappedStore).shutdown(Mockito.any());
    }
}
