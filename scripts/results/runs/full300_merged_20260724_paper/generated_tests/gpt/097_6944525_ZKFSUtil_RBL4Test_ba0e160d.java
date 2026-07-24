package com.linkedin.d2.balancer.zkfs;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ZKFSUtil_RBL4Test_ba0e160d {

    @Test
    public void testServicePathWithDefault() {
        String basePath = "/example";
        String expected = "/example/services";
        String actual = ZKFSUtil.servicePath(basePath);
        Assert.assertEquals(actual, expected, "Service path with default should match");
    }

    @Test
    public void testServicePathWithCustomServicePath() {
        String basePath = "/example";
        String servicePath = "customService";
        String expected = "/example/customService";
        String actual = ZKFSUtil.servicePath(basePath, servicePath);
        Assert.assertEquals(actual, expected, "Service path with custom service path should match");
    }

    @Test
    public void testServicePathWithEmptyServicePath() {
        String basePath = "/example";
        String expected = "/example/services";
        String actual = ZKFSUtil.servicePath(basePath, "");
        Assert.assertEquals(actual, expected, "Service path with empty service path should default to 'services'");
    }

    @Test
    public void testServicePathWithNullServicePath() {
        String basePath = "/example";
        String expected = "/example/services";
        String actual = ZKFSUtil.servicePath(basePath, null);
        Assert.assertEquals(actual, expected, "Service path with null service path should default to 'services'");
    }

    @Test
    public void testClusterPath() {
        String basePath = "/example";
        String expected = "/example/clusters";
        String actual = ZKFSUtil.clusterPath(basePath);
        Assert.assertEquals(actual, expected, "Cluster path should match");
    }

    @Test
    public void testUriPath() {
        String basePath = "/example";
        String expected = "/example/uris";
        String actual = ZKFSUtil.uriPath(basePath);
        Assert.assertEquals(actual, expected, "URI path should match");
    }

    @Test
    public void testNormalizeBasePath() {
        String basePath = "/example/";
        String expected = "/example";
        String actual = ZKFSUtil.servicePath(basePath);
        Assert.assertEquals(actual, expected, "Normalized base path should not have trailing slash");
    }

    @Test
    public void testNormalizeBasePathWithMultipleTrailingSlashes() {
        String basePath = "/example////";
        String expected = "/example";
        String actual = ZKFSUtil.servicePath(basePath);
        Assert.assertEquals(actual, expected, "Normalized base path should not have multiple trailing slashes");
    }
}
