package net.krotscheck.kangaroo.authz.common.util;

import net.krotscheck.kangaroo.authz.common.authenticator.AuthenticatorType;
import net.krotscheck.kangaroo.authz.common.database.entity.AbstractClientUri;
import net.krotscheck.kangaroo.authz.common.database.entity.ApplicationScope;
import net.krotscheck.kangaroo.authz.common.database.entity.Authenticator;
import net.krotscheck.kangaroo.authz.common.database.entity.Client;
import net.krotscheck.kangaroo.authz.common.database.entity.ClientRedirect;
import net.krotscheck.kangaroo.authz.common.database.entity.ClientType;
import net.krotscheck.kangaroo.authz.common.database.entity.Role;
import net.krotscheck.kangaroo.authz.oauth2.exception.RFC6749.InvalidRequestException;
import net.krotscheck.kangaroo.authz.oauth2.exception.RFC6749.InvalidScopeException;
import net.krotscheck.kangaroo.authz.oauth2.exception.RFC6749.UnsupportedResponseTypeException;
import org.junit.Test;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.Assert.*;

public class ValidationUtilTest {

    @Test(expected = UnsupportedResponseTypeException.class)
    public void testValidateResponseType_Unsupported() {
        Client client = new Client();
        client.setType(ClientType.AuthorizationGrant);
        ValidationUtil.validateResponseType(client, "token");
    }

    @Test
    public void testValidateResponseType_Supported() {
        Client client = new Client();
        client.setType(ClientType.AuthorizationGrant);
        ValidationUtil.validateResponseType(client, "code");
    }

    @Test(expected = InvalidRequestException.class)
    public void testRequireValidRedirect_NullRedirect() {
        List<ClientRedirect> redirects = new ArrayList<>();
        ValidationUtil.requireValidRedirect((URI) null, redirects);
    }

    @Test
    public void testRequireValidRedirect_ValidRedirect() throws URISyntaxException {
        List<ClientRedirect> redirects = new ArrayList<>();
        ClientRedirect redirect = new ClientRedirect();
        redirect.setUri(new URI("http://valid.redirect"));
        redirects.add(redirect);
        
        URI result = ValidationUtil.requireValidRedirect(new URI("http://valid.redirect"), redirects);
        assertNotNull(result);
    }

    @Test(expected = InvalidRequestException.class)
    public void testRequireValidRedirect_InvalidRedirect() throws URISyntaxException {
        List<ClientRedirect> redirects = new ArrayList<>();
        ClientRedirect redirect = new ClientRedirect();
        redirect.setUri(new URI("http://valid.redirect"));
        redirects.add(redirect);
        
        ValidationUtil.requireValidRedirect(new URI("http://invalid.redirect"), redirects);
    }

    @Test
    public void testValidateRedirect_ValidRedirect() throws URISyntaxException {
        List<ClientRedirect> redirects = new ArrayList<>();
        ClientRedirect redirect = new ClientRedirect();
        redirect.setUri(new URI("http://valid.redirect"));
        redirects.add(redirect);
        
        URI result = ValidationUtil.validateRedirect(new URI("http://valid.redirect"), redirects);
        assertNotNull(result);
    }

    @Test
    public void testValidateRedirect_InvalidRedirect() throws URISyntaxException {
        List<ClientRedirect> redirects = new ArrayList<>();
        ClientRedirect redirect = new ClientRedirect();
        redirect.setUri(new URI("http://valid.redirect"));
        redirects.add(redirect);
        
        URI result = ValidationUtil.validateRedirect(new URI("http://invalid.redirect"), redirects);
        assertNull(result);
    }

    @Test(expected = InvalidScopeException.class)
    public void testValidateScope_InvalidScopes() {
        String[] requestedScopes = {"scope1", "scope2"};
        SortedMap<String, ApplicationScope> validScopes = new TreeMap<>();
        ValidationUtil.validateScope(requestedScopes, validScopes);
    }

    @Test
    public void testValidateScope_ValidScopes() {
        String[] requestedScopes = {"scope1"};
        SortedMap<String, ApplicationScope> validScopes = new TreeMap<>();
        validScopes.put("scope1", new ApplicationScope());
        
        SortedMap<String, ApplicationScope> result = ValidationUtil.validateScope(requestedScopes, validScopes);
        assertEquals(1, result.size());
    }

    @Test(expected = InvalidRequestException.class)
    public void testValidateAuthenticator_NoAuthenticators() {
        ValidationUtil.validateAuthenticator(null, new ArrayList<>());
    }

    @Test
    public void testValidateAuthenticator_ValidAuthenticator() {
        AuthenticatorType type = AuthenticatorType.BASIC;
        List<Authenticator> authenticators = new ArrayList<>();
        Authenticator authenticator = new Authenticator();
        authenticator.setType(type);
        authenticators.add(authenticator);
        
        Authenticator result = ValidationUtil.validateAuthenticator(type, authenticators);
        assertNotNull(result);
    }

    @Test(expected = InvalidRequestException.class)
    public void testValidateAuthenticator_InvalidAuthenticator() {
        AuthenticatorType type = AuthenticatorType.BASIC;
        List<Authenticator> authenticators = new ArrayList<>();
        ValidationUtil.validateAuthenticator(type, authenticators);
    }
}
