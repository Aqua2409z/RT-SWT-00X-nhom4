package org.apache.calcite.avatica;

import org.apache.calcite.avatica.ConnectionConfigImpl;
import org.apache.calcite.avatica.ConnectionProperty;
import org.apache.calcite.avatica.ha.LBStrategy;
import org.apache.calcite.avatica.remote.AvaticaHttpClientFactory;
import org.apache.calcite.avatica.remote.Service;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Properties;

import static org.junit.Assert.*;

public class ConnectionConfigImpl_RBL4Test_625c2e75 {

    private Properties properties;
    private ConnectionConfigImpl connectionConfig;

    @Before
    public void setUp() {
        properties = new Properties();
        connectionConfig = new ConnectionConfigImpl(properties);
    }

    @Test
    public void testSchema() {
        properties.setProperty("schema", "testSchema");
        assertEquals("testSchema", connectionConfig.schema());
    }

    @Test
    public void testTimeZone() {
        properties.setProperty("timeZone", "UTC");
        assertEquals("UTC", connectionConfig.timeZone());
    }

    @Test
    public void testFactory() {
        properties.setProperty("factory", "org.apache.calcite.avatica.remote.ServiceFactory");
        assertNotNull(connectionConfig.factory());
    }

    @Test
    public void testUrl() {
        properties.setProperty("url", "jdbc:testdb");
        assertEquals("jdbc:testdb", connectionConfig.url());
    }

    @Test
    public void testSerialization() {
        properties.setProperty("serialization", "json");
        assertEquals("json", connectionConfig.serialization());
    }

    @Test
    public void testAuthentication() {
        properties.setProperty("authentication", "basic");
        assertEquals("basic", connectionConfig.authentication());
    }

    @Test
    public void testAvaticaUser() {
        properties.setProperty("avaticaUser", "user");
        assertEquals("user", connectionConfig.avaticaUser());
    }

    @Test
    public void testAvaticaPassword() {
        properties.setProperty("avaticaPassword", "password");
        assertEquals("password", connectionConfig.avaticaPassword());
    }

    @Test
    public void testHttpClientFactory() {
        properties.setProperty("httpClientFactory", "org.apache.calcite.avatica.remote.AvaticaHttpClientFactory");
        assertNotNull(connectionConfig.httpClientFactory());
    }

    @Test
    public void testHttpClientClass() {
        properties.setProperty("httpClientImpl", "org.apache.calcite.avatica.remote.HttpClient");
        assertEquals("org.apache.calcite.avatica.remote.HttpClient", connectionConfig.httpClientClass());
    }

    @Test
    public void testKerberosPrincipal() {
        properties.setProperty("principal", "user@EXAMPLE.COM");
        assertEquals("user@EXAMPLE.COM", connectionConfig.kerberosPrincipal());
    }

    @Test
    public void testKerberosKeytab() {
        properties.setProperty("keytab", "path/to/keytab");
        File keytabFile = connectionConfig.kerberosKeytab();
        assertNotNull(keytabFile);
        assertEquals("path/to/keytab", keytabFile.getPath());
    }

    @Test(expected = RuntimeException.class)
    public void testKerberosKeytabFileNotFound() {
        properties.setProperty("keytab", "invalid/path/to/keytab");
        connectionConfig.kerberosKeytab();
    }

    @Test
    public void testTruststore() {
        properties.setProperty("truststore", "path/to/truststore");
        File truststoreFile = connectionConfig.truststore();
        assertNotNull(truststoreFile);
        assertEquals("path/to/truststore", truststoreFile.getPath());
    }

    @Test
    public void testKeystore() {
        properties.setProperty("keystore", "path/to/keystore");
        File keystoreFile = connectionConfig.keystore();
        assertNotNull(keystoreFile);
        assertEquals("path/to/keystore", keystoreFile.getPath());
    }

    @Test
    public void testFetchSize() {
        properties.setProperty("fetchSize", "100");
        assertEquals(100, connectionConfig.fetchSize());
    }

    @Test
    public void testTransparentReconnectionEnabled() {
        properties.setProperty("transparentReconnection", "true");
        assertTrue(connectionConfig.transparentReconnectionEnabled());
    }

    @Test
    public void testUseClientSideLb() {
        properties.setProperty("useClientSideLb", "false");
        assertFalse(connectionConfig.useClientSideLb());
    }

    @Test
    public void testGetLBStrategy() {
        properties.setProperty("lbStrategy", "org.apache.calcite.avatica.ha.LBStrategy");
        assertNotNull(connectionConfig.getLBStrategy());
    }

    @Test
    public void testGetLBConnectionFailoverRetries() {
        properties.setProperty("lbConnectionFailoverRetries", "3");
        assertEquals(3, connectionConfig.getLBConnectionFailoverRetries());
    }

    @Test
    public void testGetHttpConnectionTimeout() {
        properties.setProperty("httpConnectionTimeout", "5000");
        assertEquals(5000, connectionConfig.getHttpConnectionTimeout());
    }

    @Test
    public void testGetBearerToken() {
        properties.setProperty("bearerToken", "token123");
        assertEquals("token123", connectionConfig.getBearerToken());
    }

    @Test
    public void testCustomPropertyValue() {
        ConnectionProperty customProperty = ConnectionProperty.create("customProperty", ConnectionProperty.Type.STRING, null);
        properties.setProperty("customProperty", "customValue");
        assertEquals("customValue", connectionConfig.customPropertyValue(customProperty).getString());
    }
}
