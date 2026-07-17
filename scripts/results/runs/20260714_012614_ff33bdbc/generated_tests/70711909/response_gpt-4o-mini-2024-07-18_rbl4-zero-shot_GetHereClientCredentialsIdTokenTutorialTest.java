
package com.here.account.oauth2.tutorial;

import com.here.account.auth.OAuth1ClientCredentialsProvider;
import com.here.account.http.apache.ApacheHttpClientProvider;
import com.here.account.oauth2.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class GetHereClientCredentialsIdTokenTutorialTest {

    private GetHereClientCredentialsIdTokenTutorial tutorial;
    private String[] args;

    @Before
    public void setUp() {
        args = new String[]{"-idToken", "-v", "path/to/credentials"};
        tutorial = new GetHereClientCredentialsIdTokenTutorial(args);
    }

    @Test
    public void testGetTokenSuccess() throws Exception {
        // Mocking dependencies
        OAuth1ClientCredentialsProvider credentialsProvider = mock(OAuth1ClientCredentialsProvider.class);
        TokenEndpoint tokenEndpoint = mock(TokenEndpoint.class);
        AccessTokenRequest accessTokenRequest = mock(AccessTokenRequest.class);
        AccessTokenResponse tokenResponse = mock(AccessTokenResponse.class);

        // Setting up mocks
        when(tutorial.getCredentials(any())).thenReturn(credentialsProvider);
        when(HereAccount.getTokenEndpoint(any(), any())).thenReturn(tokenEndpoint);
        when(tokenEndpoint.requestToken(any())).thenReturn(tokenResponse);
        when(tokenResponse.getIdToken()).thenReturn("mockIdToken1234567890");

        // Call the method
        String idToken = tutorial.getToken();

        // Verify results
        assertNotNull(idToken);
        assertEquals("mockIdToken1234567890", idToken);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTokenIdTokenNull() throws Exception {
        // Mocking dependencies
        OAuth1ClientCredentialsProvider credentialsProvider = mock(OAuth1ClientCredentialsProvider.class);
        TokenEndpoint tokenEndpoint = mock(TokenEndpoint.class);
        AccessTokenRequest accessTokenRequest = mock(AccessTokenRequest.class);
        AccessTokenResponse tokenResponse = mock(AccessTokenResponse.class);

        // Setting up mocks
        when(tutorial.getCredentials(any())).thenReturn(credentialsProvider);
        when(HereAccount.getTokenEndpoint(any(), any())).thenReturn(tokenEndpoint);
        when(tokenEndpoint.requestToken(any())).thenReturn(tokenResponse);
        when(tokenResponse.getIdToken()).thenReturn(null);

        // Call the method
        tutorial.getToken();
    }

    @Test
    public void testPrintUsageAndExit() {
        // This method is protected and does not return a value, 
        // we can only verify that it prints the usage message.
        tutorial.printUsageAndExit();
    }

    @Test
    public void testParseArgsValid() {
        Args parsedArgs = tutorial.parseArgs(new String[]{"-idToken", "-v", "path/to/credentials"});
        assertTrue(parsedArgs.isVerbose());
        assertEquals("path/to/credentials", parsedArgs.getFilePath());
    }

    @Test(expected = RuntimeException.class)
    public void testParseArgsInvalid() {
        tutorial.parseArgs(new String[]{"-idToken", "-v", "path/to/credentials", "extraArg"});
    }

    @Test(expected = RuntimeException.class)
    public void testParseArgsTooMany() {
        tutorial.parseArgs(new String[]{"-idToken", "-v", "path/to/credentials", "extraArg1", "extraArg2"});
    }
}
