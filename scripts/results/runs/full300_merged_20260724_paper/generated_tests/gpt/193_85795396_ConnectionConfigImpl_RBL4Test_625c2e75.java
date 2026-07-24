package org.apache.calcite.avatica;

import org.apache.calcite.avatica.ConnectionConfig;
import org.apache.calcite.avatica.ConnectionConfigImpl;
import org.apache.calcite.avatica.ConnectionProperty;
import org.apache.calcite.avatica.BuiltInConnectionProperty;
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
        properties.setProperty(BuiltInConnectionProperty.SCHEMA.name(), "testSchema");
        assertEquals("testSchema", connectionConfig.schema());
    }

    @Test
    public void testTimeZone() {
        properties.setProperty(BuiltInConnectionProperty.TIME_ZONE.name(), "UTC");
        assertEquals("UTC", connectionConfig.timeZone());
    }

    @Test
    public void testFactory() {
        // Assuming a mock or a real factory class ConnectionConfigImpl_RBL4Test_625c2e75 available
        // properties.setProperty(BuiltInConnectionProperty.FACTORY.name(), "mockFactory");
        // assertNotNull(connectionConfig.factory());
    }

    @Test
    public void testUrl() {
        properties.setProperty(BuiltInConnectionProperty.URL.name(), "jdbc:testdb");
        assertEquals("jdbc:testdb", connectionConfig.url());
    }

    @Test
    public void testSerialization() {
        properties.setProperty(BuiltInConnectionProperty.SERIALIZATION.name(), "json");
        assertEquals("json", connectionConfig.serialization());
    }

    @Test
    public void testAuthentication() {
        properties.setProperty(BuiltInConnectionProperty.AUTHENTICATION.name(), "basic");
        assertEquals("basic", connectionConfig.authentication());
    }

    @Test
    public void testAvaticaUser() {
        properties.setProperty(BuiltInConnectionProperty.AVATICA_USER.name(), "user");
        assertEquals("user", connectionConfig.avaticaUser());
    }

    @Test
    public void testAvaticaPassword() {
        properties.setProperty(BuiltInConnectionProperty.AVATICA_PASSWORD.name(), "password");
        assertEquals("password", connectionConfig.avaticaPassword());
    }

    @Test
    public void testHttpClientFactory() {
        // Assuming a mock or a real factory class ConnectionConfigImpl_RBL4Test_625c2e75 available
        // properties.setProperty(BuiltInConnectionProperty.HTTP_CLIENT_FACTORY.name(), "mockHttpClientFactory");
        // assertNotNull(connectionConfig.httpClientFactory());
    }

    @Test
    public void testKerberosKeytab() {
        properties.setProperty(BuiltInConnectionProperty.KEYTAB.name(), "path/to/keytab");
        File keytabFile = connectionConfig.kerberosKeytab();
        assertNotNull(keytabFile);
        assertEquals("path/to/keytab", keytabFile.getPath());
    }

    @Test(expected = RuntimeException.class)
    public void testKerberosKeytabFileNotFound() {
        properties.setProperty(BuiltInConnectionProperty.KEYTAB.name(), "invalid/path/to/keytab");
        connectionConfig.kerberosKeytab();
    }

    @Test
    public void testTruststore() {
        properties.setProperty(BuiltInConnectionProperty.TRUSTSTORE.name(), "path/to/truststore");
        File truststoreFile = connectionConfig.truststore();
        assertNotNull(truststoreFile);
        assertEquals("path/to/truststore", truststoreFile.getPath());
    }

    @Test
    public void testKeystore() {
        properties.setProperty(BuiltInConnectionProperty.KEYSTORE.name(), "path/to/keystore");
        File keystoreFile = connectionConfig.keystore();
        assertNotNull(keystoreFile);
        assertEquals("path/to/keystore", keystoreFile.getPath());
    }

    @Test
    public void testFetchSize() {
        properties.setProperty(BuiltInConnectionProperty.FETCH_SIZE.name(), "100");
        assertEquals(100, connectionConfig.fetchSize());
    }

    @Test
    public void testTransparentReconnectionEnabled() {
        properties.setProperty(BuiltInConnectionProperty.TRANSPARENT_RECONNECTION.name(), "true");
        assertTrue(connectionConfig.transparentReconnectionEnabled());
    }

    @Test
    public void testUseClientSideLb() {
        properties.setProperty(BuiltInConnectionProperty.USE_CLIENT_SIDE_LB.name(), "false");
        assertFalse(connectionConfig.useClientSideLb());
    }

    @Test
    public void testGetLBURLs() {
        properties.setProperty(BuiltInConnectionProperty.LB_URLS.name(), "url1,url2");
        assertEquals("url1,url2", connectionConfig.getLbURLs());
    }

    @Test
    public void testGetLBConnectionFailoverRetries() {
        properties.setProperty(BuiltInConnectionProperty.LB_CONNECTION_FAILOVER_RETRIES.name(), "3");
        assertEquals(3, connectionConfig.getLBConnectionFailoverRetries());
    }

    @Test
    public void testGetHttpConnectionTimeout() {
        properties.setProperty(BuiltInConnectionProperty.HTTP_CONNECTION_TIMEOUT.name(), "5000");
        assertEquals(5000, connectionConfig.getHttpConnectionTimeout());
    }

    @Test
    public void testGetBearerToken() {
        properties.setProperty(BuiltInConnectionProperty.BEARER_TOKEN.name(), "token123");
        assertEquals("token123", connectionConfig.getBearerToken());
    }

    @Test
    public void testCustomPropertyValue() {
        ConnectionProperty customProperty = BuiltInConnectionProperty.SCHEMA; // Example property
        properties.setProperty(customProperty.name(), "customValue");
        assertEquals("customValue", connectionConfig.customPropertyValue(customProperty).getString());
    }
}
