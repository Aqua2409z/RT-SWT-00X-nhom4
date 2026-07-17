
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
        AccessTokenRequest accessTokenRequest = new ClientCredentialsGrantRequest();
        AccessTokenResponse tokenResponse = mock(AccessTokenResponse.class);

        // Setting up mocks
        when(tokenResponse.getIdToken()).thenReturn("mockIdToken");
        when(tokenEndpoint.requestToken(accessTokenRequest)).thenReturn(tokenResponse);
        when(HereAccount.getTokenEndpoint(any(), any())).thenReturn(tokenEndpoint);
        when(tutorial.getCredentials(any())).thenReturn(credentialsProvider);

        // Call the method
        String idToken = tutorial.getToken();

        // Verify results
        assertNotNull(idToken);
        assertEquals("mockIdToken", idToken);
    }

    @Test(expected = RuntimeException.class)
    public void testGetTokenIdTokenNull() throws Exception {
        // Mocking dependencies
        OAuth1ClientCredentialsProvider credentialsProvider = mock(OAuth1ClientCredentialsProvider.class);
        TokenEndpoint tokenEndpoint = mock(TokenEndpoint.class);
        AccessTokenRequest accessTokenRequest = new ClientCredentialsGrantRequest();
        AccessTokenResponse tokenResponse = mock(AccessTokenResponse.class);

        // Setting up mocks
        when(tokenResponse.getIdToken()).thenReturn(null);
        when(tokenEndpoint.requestToken(accessTokenRequest)).thenReturn(tokenResponse);
        when(HereAccount.getTokenEndpoint(any(), any())).thenReturn(tokenEndpoint);
        when(tutorial.getCredentials(any())).thenReturn(credentialsProvider);

        // Call the method
        tutorial.getToken();
    }

    @Test
    public void testParseArgsValid() {
        Args parsedArgs = tutorial.parseArgs(args);
        assertTrue(parsedArgs.isVerbose());
        assertEquals("path/to/credentials", parsedArgs.getFilePath());
    }

    @Test(expected = RuntimeException.class)
    public void testParseArgsInvalid() {
        String[] invalidArgs = new String[]{"-idToken", "-v", "extraArg"};
        tutorial.parseArgs(invalidArgs);
    }

    @Test
    public void testPrintUsageAndExit() {
        try {
            tutorial.printUsageAndExit();
        } catch (Exception e) {
            // Expected exception due to System.exit call
            assertTrue(e instanceof RuntimeException);
        }
    }
}
