package com.linkedin.d2.balancer.util;

import com.linkedin.common.callback.Callback;
import com.linkedin.common.util.None;
import com.linkedin.d2.balancer.clients.DynamicClient;
import com.linkedin.r2.message.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URI;
import java.util.*;

public class LoadBalancerUtil_RBL4Test_fa60eb27 {

    private static final Logger log = LoggerFactory.getLogger(LoadBalancerUtilTest.class);

    @Test
    public void testFindOriginalThrowable() {
        Throwable cause = new Throwable("Cause");
        Throwable throwable = new Throwable("Main", cause);
        Throwable original = LoadBalancerUtil.findOriginalThrowable(throwable);
        Assert.assertEquals(original, cause);
    }

    @Test
    public void testJoin() {
        Collection<String> toJoin = Arrays.asList("a", "b", "c");
        String result = LoadBalancerUtil.join(toJoin, ",");
        Assert.assertEquals(result, "a,b,c");
    }

    @Test
    public void testJoinWithNull() {
        String result = LoadBalancerUtil.join(null, ",");
        Assert.assertNull(result);
    }

    @Test
    public void testGetServiceNameFromUri() {
        URI uri = URI.create("http://example.com");
        String serviceName = LoadBalancerUtil.getServiceNameFromUri(uri);
        Assert.assertEquals(serviceName, "example.com");
    }

    @Test
    public void testGetPathFromUri() {
        URI uri = URI.create("http://example.com/path/to/resource");
        String path = LoadBalancerUtil.getPathFromUri(uri);
        Assert.assertEquals(path, "/path/to/resource");
    }

    @Test
    public void testGetRawPathFromUri() {
        URI uri = URI.create("http://example.com/path/to/resource?query=1");
        String rawPath = LoadBalancerUtil.getRawPathFromUri(uri);
        Assert.assertEquals(rawPath, "/path/to/resource");
    }

    @Test
    public void testGetSubProperties() throws IOException {
        String propertiesString = "serviceA.key1=value1\nserviceA.key2=value2\nserviceB.key1=value3";
        Map<String, Map<String, String>> result = LoadBalancerUtil.getSubProperties("service", propertiesString);
        Assert.assertEquals(result.size(), 2);
        Assert.assertEquals(result.get("A").get("key1"), "value1");
        Assert.assertEquals(result.get("A").get("key2"), "value2");
        Assert.assertEquals(result.get("B").get("key1"), "value3");
    }

    @Test
    public void testGetOrElseWithMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        String result = LoadBalancerUtil.getOrElse(map, "key1", "default");
        Assert.assertEquals(result, "value1");
    }

    @Test
    public void testGetOrElseWithMapDefault() {
        Map<String, String> map = new HashMap<>();
        String result = LoadBalancerUtil.getOrElse(map, "key1", "default");
        Assert.assertEquals(result, "default");
    }

    @Test
    public void testGetOrElseWithList() {
        List<String> list = Arrays.asList("value1", "value2");
        List<String> result = LoadBalancerUtil.getOrElse(list);
        Assert.assertEquals(result.size(), 2);
    }

    @Test
    public void testGetOrElseWithNullList() {
        List<String> result = LoadBalancerUtil.getOrElse(null);
        Assert.assertEquals(result.size(), 0);
    }

    @Test
    public void testCreateTempDirectory() throws IOException {
        File tempDir = LoadBalancerUtil.createTempDirectory("test");
        Assert.assertTrue(tempDir.exists());
        Assert.assertTrue(tempDir.isDirectory());
        Assert.assertTrue(tempDir.delete());
    }

    @Test
    public void testSyncShutdownClient() throws InterruptedException {
        DynamicClient mockClient = new DynamicClient() {
            @Override
            public void shutdown(Callback<None> callback) {
                callback.onSuccess(None.instance());
            }
        };
        LoadBalancerUtil.syncShutdownClient(mockClient, log);
    }

    @Test
    public void testTargetHints() {
        RequestContext context = new RequestContext();
        URI targetService = URI.create("http://example.com");
        LoadBalancerUtil.TargetHints.setRequestContextTargetService(context, targetService);
        URI retrievedService = LoadBalancerUtil.TargetHints.getRequestContextTargetService(context);
        Assert.assertEquals(retrievedService, targetService);
    }
}
