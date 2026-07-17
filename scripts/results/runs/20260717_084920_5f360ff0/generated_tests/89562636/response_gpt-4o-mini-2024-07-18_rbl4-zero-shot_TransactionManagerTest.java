
package com.mbed.coap.server.internal;

import com.mbed.coap.exception.TooManyRequestsForEndpointException;
import com.mbed.coap.packet.CoapPacket;
import com.mbed.coap.packet.MessageType;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.Optional;

import static org.junit.Assert.*;

public class TransactionManagerTest {

    private TransactionManager transactionManager;

    @Before
    public void setUp() {
        transactionManager = new TransactionManager();
    }

    @Test
    public void testSetMaximumEndpointQueueSize_Valid() {
        transactionManager.setMaximumEndpointQueueSize(50);
        assertEquals(50, transactionManager.maximumEndpointQueueSize);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximumEndpointQueueSize_LessThanOne() {
        transactionManager.setMaximumEndpointQueueSize(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaximumEndpointQueueSize_MoreThan65536() {
        transactionManager.setMaximumEndpointQueueSize(70000);
    }

    @Test
    public void testAddTransactionAndGetReadyToSend_Success() throws TooManyRequestsForEndpointException {
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        boolean result = transactionManager.addTransactionAndGetReadyToSend(transaction);
        assertTrue(result);
    }

    @Test(expected = TooManyRequestsForEndpointException.class)
    public void testAddTransactionAndGetReadyToSend_TooManyRequests() throws TooManyRequestsForEndpointException {
        transactionManager.setMaximumEndpointQueueSize(1);
        CoapTransaction transaction1 = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        CoapTransaction transaction2 = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction1);
        transactionManager.addTransactionAndGetReadyToSend(transaction2);
    }

    @Test
    public void testRemoveAndLock_Success() {
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction);
        Optional<CoapTransaction> removedTransaction = transactionManager.removeAndLock(transaction.getTransactionId());
        assertTrue(removedTransaction.isPresent());
        assertEquals(transaction, removedTransaction.get());
    }

    @Test
    public void testUnlockOrRemoveAndGetNext_Success() {
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction);
        Optional<CoapTransaction> nextTransaction = transactionManager.unlockOrRemoveAndGetNext(transaction.getTransactionId());
        assertFalse(nextTransaction.isPresent());
    }

    @Test
    public void testGetNumberOfTransactions() {
        assertEquals(0, transactionManager.getNumberOfTransactions());
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction);
        assertEquals(1, transactionManager.getNumberOfTransactions());
    }

    @Test
    public void testFindMatchAndRemoveForSeparateResponse_Success() {
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction);
        CoapPacket request = new CoapPacket();
        request.setRemoteAddress(new InetSocketAddress("localhost", 5683));
        request.setMessageType(MessageType.Confirmable);
        request.setToken(new byte[]{1});
        Optional<CoapTransaction> foundTransaction = transactionManager.findMatchAndRemoveForSeparateResponse(request);
        assertTrue(foundTransaction.isPresent());
        assertEquals(transaction, foundTransaction.get());
    }

    @Test
    public void testFindTimeoutTransactions() {
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction);
        assertTrue(transactionManager.findTimeoutTransactions(System.currentTimeMillis()).isEmpty());
    }

    @Test
    public void testClose() {
        CoapTransaction transaction = new CoapTransaction(new CoapTransactionId(new InetSocketAddress("localhost", 5683)));
        transactionManager.addTransactionAndGetReadyToSend(transaction);
        transactionManager.close();
        assertEquals(0, transactionManager.getNumberOfTransactions());
    }
}
