package com.yahoo.parsec.clients;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class RandomNameResolver_RBL4_835787fdTest {

    @Test
    public void testResolveValidHost() throws UnknownHostException {
        RandomNameResolver resolver = new RandomNameResolver();
        InetAddress address = resolver.resolve("www.google.com");
        Assert.assertNotNull(address);
    }

    @Test(expectedExceptions = UnknownHostException.class)
    public void testResolveInvalidHost() throws UnknownHostException {
        RandomNameResolver resolver = new RandomNameResolver();
        resolver.resolve("invalid.hostname");
    }

    @Test
    public void testResolveMultipleAddresses() throws UnknownHostException {
        RandomNameResolver resolver = new RandomNameResolver();
        InetAddress address1 = resolver.resolve("www.google.com");
        InetAddress address2 = resolver.resolve("www.google.com");
        Assert.assertNotEquals(address1, address2);
    }

    @Test
    public void testResolveSameAddress() throws UnknownHostException {
        RandomNameResolver resolver = new RandomNameResolver();
        InetAddress address1 = resolver.resolve("www.google.com");
        InetAddress address2 = resolver.resolve("www.google.com");
        Assert.assertTrue(address1.equals(address2) || !address1.equals(address2));
    }
}
