
package com.mbed.coap.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.mbed.coap.exception.CoapCodeException;
import com.mbed.coap.exception.CoapRequestEntityTooLarge;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.packet.Code;
import com.mbed.coap.server.internal.CoapMessaging;
import com.mbed.coap.transport.TransportContext;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

public class CoapServerTest {
    private CoapMessaging coapMessaging;
    private CoapServer coapServer;

    @Before
    public void setUp() {
        coapMessaging = mock(CoapMessaging.class);
        coapServer = new CoapServer(coapMessaging);
    }

    @Test
    public void testStart() throws IOException {
        coapServer.start();
        assertTrue(coapServer.isRunning());
    }

    @Test(expected = IllegalStateException.class)
    public void testStartAlreadyRunning() throws IOException {
        coapServer.start();
        coapServer.start(); // should throw exception
    }

    @Test
    public void testStop() {
        try {
            coapServer.start();
            coapServer.stop();
            assertFalse(coapServer.isRunning());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testStopNotRunning() {
        coapServer.stop(); // should throw exception
    }

    @Test
    public void testAddRequestHandler() {
        CoapHandler handler = mock(CoapHandler.class);
        coapServer.addRequestHandler("/test", handler);
        assertNotNull(coapServer.getResourceLinks());
    }

    @Test
    public void testRemoveRequestHandler() {
        CoapHandler handler = mock(CoapHandler.class);
        coapServer.addRequestHandler("/test", handler);
        coapServer.removeRequestHandler(handler);
        assertNull(coapServer.getResourceLinks());
    }

    @Test
    public void testMakeRequest() {
        CoapPacket requestPacket = mock(CoapPacket.class);
        when(coapMessaging.makeRequest(any(), any(), any())).thenReturn(CompletableFuture.completedFuture(requestPacket));

        CompletableFuture<CoapPacket> future = coapServer.makeRequest(requestPacket);
        assertNotNull(future);
        assertEquals(requestPacket, future.join());
    }

    @Test
    public void testSendNotification() {
        CoapPacket notifPacket = mock(CoapPacket.class);
        when(notifPacket.headers().getObserve()).thenReturn(1);
        when(notifPacket.getToken()).thenReturn(new byte[]{1});
        when(notifPacket.getCode()).thenReturn(Code.C205_CONTENT);

        coapServer.sendNotification(notifPacket, response -> {
            assertNotNull(response);
        }, TransportContext.NULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendNotificationWithoutObserveHeader() {
        CoapPacket notifPacket = mock(CoapPacket.class);
        when(notifPacket.headers().getObserve()).thenReturn(null);
        coapServer.sendNotification(notifPacket, response -> {}, TransportContext.NULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendNotificationWithoutToken() {
        CoapPacket notifPacket = mock(CoapPacket.class);
        when(notifPacket.headers().getObserve()).thenReturn(1);
        when(notifPacket.getToken()).thenReturn(new byte[]{});
        coapServer.sendNotification(notifPacket, response -> {}, TransportContext.NULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendNotificationWithInvalidCode() {
        CoapPacket notifPacket = mock(CoapPacket.class);
        when(notifPacket.headers().getObserve()).thenReturn(1);
        when(notifPacket.getToken()).thenReturn(new byte[]{1});
        when(notifPacket.getCode()).thenReturn(Code.C404_NOT_FOUND);
        coapServer.sendNotification(notifPacket, response -> {}, TransportContext.NULL);
    }

    @Test
    public void testPing() {
        InetSocketAddress address = new InetSocketAddress("localhost", 5683);
        coapServer.ping(address, response -> {
            assertNotNull(response);
        });
    }

    @Test
    public void testSetObservationHandler() {
        ObservationHandler handler = mock(ObservationHandler.class);
        coapServer.setObservationHandler(handler);
        assertNotNull(coapServer.observationHandler);
    }

    @Test
    public void testUseCriticalOptionTest() {
        coapServer.useCriticalOptionTest(true);
        assertTrue(coapServer.enabledCriticalOptTest);
    }
}
