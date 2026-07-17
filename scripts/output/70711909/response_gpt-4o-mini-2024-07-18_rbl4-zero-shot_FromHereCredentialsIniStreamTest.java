
package com.here.account.auth.provider;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.here.account.auth.JwtClientAssertionProvider;
import com.here.account.util.Clock;
import com.here.account.util.SettableSystemClock;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.Before;
import org.junit.Test;

public class FromHereCredentialsIniStreamTest {

    private Clock clock;
    private InputStream inputStream;
    private FromHereCredentialsIniStream fromHereCredentialsIniStream;

    @Before
    public void setUp() {
        clock = new SettableSystemClock();
        String iniContent = "[default]\n" +
                "access_key_id=sampleAccessKeyId\n" +
                "access_key_secret=sampleAccessKeySecret\n" +
                "token_endpoint_url=https://example.com/token\n";
        inputStream = new ByteArrayInputStream(iniContent.getBytes());
        fromHereCredentialsIniStream = new FromHereCredentialsIniStream(clock, inputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullInputStream() {
        new FromHereCredentialsIniStream(clock, null);
    }

    @Test
    public void testGetTokenEndpointUrl() {
        String tokenEndpointUrl = fromHereCredentialsIniStream.getTokenEndpointUrl();
        assertEquals("https://example.com/token", tokenEndpointUrl);
    }

    @Test
    public void testGetScope() {
        String scope = fromHereCredentialsIniStream.getScope();
        assertNull(scope); // Assuming scope is not set in the ini file
    }

    @Test
    public void testGetClientAuthorizer() {
        assertNotNull(fromHereCredentialsIniStream.getClientAuthorizer());
    }

    @Test
    public void testGetNewAccessTokenRequest() {
        assertNotNull(fromHereCredentialsIniStream.getNewAccessTokenRequest());
    }

    @Test
    public void testGetPropertiesFromIni() throws Exception {
        Properties properties = FromHereCredentialsIniStream.getPropertiesFromIni(inputStream, "default");
        assertEquals("sampleAccessKeyId", properties.getProperty("access_key_id"));
        assertEquals("sampleAccessKeySecret", properties.getProperty("access_key_secret"));
        assertEquals("https://example.com/token", properties.getProperty("token_endpoint_url"));
    }

    @Test(expected = RequestProviderException.class)
    public void testGetClientCredentialsProviderWithIOException() throws Exception {
        InputStream faultyInputStream = mock(InputStream.class);
        when(faultyInputStream.read()).thenThrow(new IOException("Mocked IOException"));
        FromHereCredentialsIniStream.getClientCredentialsProvider(clock, faultyInputStream, "default");
    }

    @Test(expected = RequestProviderException.class)
    public void testGetClientCredentialsProviderWithConfigurationException() throws Exception {
        InputStream faultyInputStream = new ByteArrayInputStream("invalid content".getBytes());
        FromHereCredentialsIniStream.getClientCredentialsProvider(clock, faultyInputStream, "default");
    }
}
