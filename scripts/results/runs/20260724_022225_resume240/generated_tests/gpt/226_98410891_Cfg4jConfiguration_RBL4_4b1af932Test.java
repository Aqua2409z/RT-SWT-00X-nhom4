
package com.softavail.comms.demo.application.impl;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.classpath.ClasspathConfigurationSource;
import org.cfg4j.source.context.environment.ImmutableEnvironment;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class Cfg4jConfiguration_RBL4_4b1af932Test {

    private Cfg4jConfiguration cfg4jConfiguration;
    private ConfigurationProvider mockProvider;

    @Before
    public void setUp() {
        mockProvider = Mockito.mock(ConfigurationProvider.class);
        ConfigurationSource mockSource = Mockito.mock(ConfigurationSource.class);
        ImmutableEnvironment mockEnvironment = Mockito.mock(ImmutableEnvironment.class);

        // Mocking the ConfigurationProvider behavior
        when(mockProvider.getProperty("app.callbackBaseUrl", String.class)).thenReturn("http://callback.url");
        when(mockProvider.getProperty("app.nexmoCallbackBaseUrl", String.class)).thenReturn("http://nexmo.callback.url");
        when(mockProvider.getProperty("app.phone", String.class)).thenReturn("+123456789");
        when(mockProvider.getProperty("comms.routerUrl", String.class)).thenReturn("http://router.url");
        when(mockProvider.getProperty("comms.routerId", String.class)).thenReturn("routerId");
        when(mockProvider.getProperty("nexmo.appId", String.class)).thenReturn("appId");
        when(mockProvider.getProperty("nexmo.appPrivateKey", String.class)).thenReturn("privateKey");
        when(mockProvider.getProperty("app.musicOnHoldUrl", String.class)).thenReturn("http://music.url");
        when(mockProvider.getProperty("comms.queueId", String.class)).thenReturn("queueId");
        when(mockProvider.getProperty("comms.planId", String.class)).thenReturn("planId");

        // Create an instance of Cfg4jConfiguration with the mocked provider
        cfg4jConfiguration = new Cfg4jConfiguration() {
            @Override
            protected ConfigurationProvider createProvider(ConfigurationSource source, ImmutableEnvironment environment) {
                return mockProvider;
            }
        };
    }

    @Test
    public void testCallbackBaseUrl() {
        assertEquals("http://callback.url", cfg4jConfiguration.callbackBaseUrl());
    }

    @Test
    public void testNexmoCallbackBaseUrl() {
        assertEquals("http://nexmo.callback.url", cfg4jConfiguration.nexmoCallbackBaseUrl());
    }

    @Test
    public void testPhone() {
        assertEquals("+123456789", cfg4jConfiguration.phone());
    }

    @Test
    public void testCommsRouterUrl() {
        assertEquals("http://router.url", cfg4jConfiguration.commsRouterUrl());
    }

    @Test
    public void testCommsRouterId() {
        assertEquals("routerId", cfg4jConfiguration.commsRouterId());
    }

    @Test
    public void testAppId() {
        assertEquals("appId", cfg4jConfiguration.appId());
    }

    @Test
    public void testAppPrivateKey() {
        assertEquals("privateKey", cfg4jConfiguration.appPrivateKey());
    }

    @Test
    public void testMusicOnHoldUrl() {
        assertEquals("http://music.url", cfg4jConfiguration.musicOnHoldUrl());
    }

    @Test
    public void testCommsQueueId() {
        assertEquals("queueId", cfg4jConfiguration.commsQueueId());
    }

    @Test
    public void testCommsPlanId() {
        assertEquals("planId", cfg4jConfiguration.commsPlanId());
    }
}
