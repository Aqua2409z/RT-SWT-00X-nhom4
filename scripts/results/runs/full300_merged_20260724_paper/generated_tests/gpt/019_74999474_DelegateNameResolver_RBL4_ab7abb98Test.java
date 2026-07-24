package com.yahoo.parsec.clients;

import com.yahoo.parsec.clients.DelegateNameResolver;
import com.yahoo.parsec.clients.ParsecNameResolver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class DelegateNameResolver_RBL4_ab7abb98Test {

    private ParsecNameResolver mockResolver;
    private DelegateNameResolver delegateNameResolver;

    @BeforeMethod
    public void setUp() {
        mockResolver = new ParsecNameResolver() {
            @Override
            public InetAddress resolve(String name) throws UnknownHostException {
                if ("localhost".equals(name)) {
                    return InetAddress.getByName("127.0.0.1");
                }
                throw new UnknownHostException("Host not found");
            }
        };
        delegateNameResolver = new DelegateNameResolver(mockResolver);
    }

    @Test(expectedExceptions = NullPointerException.class)
    public void testConstructorWithNullDelegate() {
        new DelegateNameResolver(null);
    }

    @Test
    public void testResolveValidHost() throws UnknownHostException {
        InetAddress address = delegateNameResolver.resolve("localhost");
        Assert.assertEquals(address.getHostAddress(), "127.0.0.1");
    }

    @Test(expectedExceptions = UnknownHostException.class)
    public void testResolveInvalidHost() throws UnknownHostException {
        delegateNameResolver.resolve("invalidhost");
    }

    @Test
    public void testGetDelegate() {
        Assert.assertNotNull(delegateNameResolver.getDelegate());
        Assert.assertEquals(delegateNameResolver.getDelegate(), mockResolver);
    }
}
