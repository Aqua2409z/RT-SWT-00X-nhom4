package com.here.account.oauth2;

import com.here.account.auth.ClientAuthorizationRequestProvider;
import com.here.account.client.Client;
import com.here.account.http.HttpProvider;
import com.here.account.oauth2.AccessTokenRequest;
import com.here.account.oauth2.AccessTokenResponse;
import com.here.account.oauth2.TokenEndpoint;
import com.here.account.oauth2.HereAccount;
import com.here.account.oauth2.retry.NoRetryPolicy;
import com.here.account.util.Clock;
import com.here.account.util.SettableClock;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class HereAccountTest {

    private HttpProvider httpProvider;
    private ClientAuthorizationRequestProvider clientAuthorizationRequestProvider;
    private TokenEndpoint tokenEndpoint;

    @Before
    public void setUp() {
        httpProvider = mock(HttpProvider.class);
        clientAuthorizationRequestProvider = mock(ClientAuthorizationRequestProvider.class);
        tokenEndpoint = HereAccount.getTokenEndpoint(httpProvider, clientAuthorizationRequestProvider);
    }

    @Test
    public void testGetTokenEndpointWithClientCredentialsProvider() {
        assertNotNull(tokenEndpoint);
    }

    @Test
    public void testGetTokenEndpointWithAuthorizationRequestProvider() {
        TokenEndpoint tokenEndpointWithAuth = HereAccount.getTokenEndpoint(httpProvider, clientAuthorizationRequestProvider);
        assertNotNull(tokenEndpointWithAuth);
    }

    @Test
    public void testRequestToken() throws Exception {
        AccessTokenRequest request = mock(AccessTokenRequest.class);
        AccessTokenResponse response = mock(AccessTokenResponse.class);
        when(tokenEndpoint.requestToken(request)).thenReturn(response);

        AccessTokenResponse result = tokenEndpoint.requestToken(request);
        assertNotNull(result);
        assertEquals(response, result);
    }

    @Test(expected = Exception.class)
    public void testRequestTokenThrowsException() throws Exception {
        AccessTokenRequest request = mock(AccessTokenRequest.class);
        when(tokenEndpoint.requestToken(request)).thenThrow(new Exception("Token request failed"));

        tokenEndpoint.requestToken(request);
    }

    @Test
    public void testRequestAutoRefreshingToken() throws Exception {
        AccessTokenRequest request = mock(AccessTokenRequest.class);
        Fresh<AccessTokenResponse> freshToken = tokenEndpoint.requestAutoRefreshingToken(request);
        assertNotNull(freshToken);
        AccessTokenResponse response = freshToken.get();
        assertNotNull(response);
    }

    @Test
    public void testReuseClock() {
        Clock clock = new SettableClock();
        assertNotNull(HereAccount.reuseClock(clientAuthorizationRequestProvider));
    }

    @Test
    public void testNullSafeCloseThrowingUnchecked() {
        Closeable closeable = mock(Closeable.class);
        doNothing().when(closeable).close();
        HereAccount.nullSafeCloseThrowingUnchecked(closeable);
    }

    @Test(expected = UncheckedIOException.class)
    public void testNullSafeCloseThrowingUncheckedThrowsIOException() {
        Closeable closeable = mock(Closeable.class);
        doThrow(new IOException("Close failed")).when(closeable).close();
        HereAccount.nullSafeCloseThrowingUnchecked(closeable);
    }
}
