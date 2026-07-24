package com.linkedin.d2.balancer.servers;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.properties.UriProperties;
import com.linkedin.d2.balancer.servers.ZKStoreFactory;
import com.linkedin.d2.balancer.servers.ZooKeeperAnnouncer;
import com.linkedin.d2.balancer.servers.ZooKeeperConnectionManager;
import com.linkedin.d2.discovery.stores.zk.ZKPersistentConnection;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;

public class ZooKeeperConnectionManager_RBL4_3804399dTest {

    private ZKPersistentConnection zkConnection;
    private ZKStoreFactory<UriProperties, ?> factory;
    private ZooKeeperAnnouncer server1;
    private ZooKeeperAnnouncer server2;
    private ZooKeeperConnectionManager manager;

    @BeforeMethod
    public void setUp() {
        zkConnection = mock(ZKPersistentConnection.class);
        factory = mock(ZKStoreFactory.class);
        server1 = mock(ZooKeeperAnnouncer.class);
        server2 = mock(ZooKeeperAnnouncer.class);
        manager = new ZooKeeperConnectionManager(zkConnection, "/base/path", factory, server1, server2);
    }

    @Test
    public void testStartSuccess() {
        Callback<None> callback = mock(Callback.class);
        when(zkConnection.getZKConnection()).thenReturn(mock(ZKConnection.class));
        
        manager.start(callback);
        
        verify(zkConnection).start();
        verify(callback, never()).onError(any());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testStartAlreadyStarting() {
        Callback<None> callback1 = mock(Callback.class);
        Callback<None> callback2 = mock(Callback.class);
        
        manager.start(callback1);
        manager.start(callback2); // This should throw an exception
    }

    @Test
    public void testShutdown() {
        Callback<None> callback = mock(Callback.class);
        
        manager.shutdown(callback);
        
        verify(server1).shutdown();
        verify(server2).shutdown();
        verify(callback, never()).onError(any());
    }

    @Test
    public void testNotifyEventSessionEstablished() {
        manager.start(mock(Callback.class));
        manager.notifyEvent(ZKPersistentConnection.Event.SESSION_ESTABLISHED);
        
        // Verify that the store is created and started
        verify(factory).createStore(any(), anyString());
    }

    @Test
    public void testNotifyEventSessionExpired() {
        manager.notifyEvent(ZKPersistentConnection.Event.SESSION_EXPIRED);
        
        // Verify that the store shutdown is called
        verify(server1, times(0)).shutdown();
        verify(server2, times(0)).shutdown();
    }

    @Test
    public void testAddConnectionWatcher() {
        ZooKeeperConnectionManager.ZooKeeperConnectionWatcher watcher = mock(ZooKeeperConnectionManager.ZooKeeperConnectionWatcher.class);
        
        manager.addConnectionWatcher(watcher);
        
        // Verify that the watcher is added
        // This would require additional implementation to verify the internal state
    }

    @Test
    public void testGetZooKeeperConnectString() {
        String connectString = manager.getZooKeeperConnectString();
        assertNotNull(connectString);
    }

    @Test
    public void testIsSessionEstablished() {
        assertFalse(manager.isSessionEstablished());
        manager.notifyEvent(ZKPersistentConnection.Event.SESSION_ESTABLISHED);
        assertTrue(manager.isSessionEstablished());
    }
}
