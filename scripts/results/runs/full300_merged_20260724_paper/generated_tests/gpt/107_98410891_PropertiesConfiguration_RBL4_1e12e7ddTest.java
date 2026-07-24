
package com.softavail.comms.demo.application.impl;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class PropertiesConfiguration_RBL4_1e12e7ddTest {

    private PropertiesConfiguration propertiesConfiguration;
    private static final String TEST_PROPERTIES_FILE = "test-application.properties";

    @Before
    public void setUp() throws IOException {
        createTestPropertiesFile();
        System.setProperty("config.path", new File(".").getAbsolutePath());
        propertiesConfiguration = new PropertiesConfiguration();
    }

    private void createTestPropertiesFile() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("app.callbackBaseUrl", "http://localhost/callback");
        properties.setProperty("app.nexmoCallbackBaseUrl", "http://localhost/nexmo/callback");
        properties.setProperty("app.phone", "1234567890");
        properties.setProperty("app.musicOnHoldUrl", "http://localhost/music");
        properties.setProperty("nexmo.appId", "testAppId");
        properties.setProperty("nexmo.appPrivateKey", "testPrivateKey");
        properties.setProperty("comms.routerUrl", "http://localhost/router");
        properties.setProperty("comms.routerId", "routerId");
        properties.setProperty("comms.queueId", "queueId");
        properties.setProperty("comms.planId", "planId");

        try (OutputStream output = new FileOutputStream(TEST_PROPERTIES_FILE)) {
            properties.store(output, null);
        }
    }

    @Test
    public void testCallbackBaseUrl() {
        assertEquals("http://localhost/callback", propertiesConfiguration.callbackBaseUrl());
    }

    @Test
    public void testNexmoCallbackBaseUrl() {
        assertEquals("http://localhost/nexmo/callback", propertiesConfiguration.nexmoCallbackBaseUrl());
    }

    @Test
    public void testPhone() {
        assertEquals("1234567890", propertiesConfiguration.phone());
    }

    @Test
    public void testMusicOnHoldUrl() {
        assertEquals("http://localhost/music", propertiesConfiguration.musicOnHoldUrl());
    }

    @Test
    public void testAppId() {
        assertEquals("testAppId", propertiesConfiguration.appId());
    }

    @Test
    public void testAppPrivateKey() {
        assertEquals("testPrivateKey", propertiesConfiguration.appPrivateKey());
    }

    @Test
    public void testCommsRouterUrl() {
        assertEquals("http://localhost/router", propertiesConfiguration.commsRouterUrl());
    }

    @Test
    public void testCommsRouterId() {
        assertEquals("routerId", propertiesConfiguration.commsRouterId());
    }

    @Test
    public void testCommsQueueId() {
        assertEquals("queueId", propertiesConfiguration.commsQueueId());
    }

    @Test
    public void testCommsPlanId() {
        assertEquals("planId", propertiesConfiguration.commsPlanId());
    }
}
