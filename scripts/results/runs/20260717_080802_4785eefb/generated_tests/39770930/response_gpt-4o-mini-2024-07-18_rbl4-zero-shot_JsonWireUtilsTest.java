
package ru.qatools.gridrouter;

import org.junit.Test;
import org.mockito.Mockito;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;

public class JsonWireUtilsTest {

    @Test
    public void testIsUriValid() {
        assertTrue(JsonWireUtils.isUriValid("/wd/hub/session/12345678901234567890123456789012"));
        assertFalse(JsonWireUtils.isUriValid("/wd/hub/"));
        assertFalse(JsonWireUtils.isUriValid(""));
    }

    @Test
    public void testIsSessionDeleteRequest() {
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Mockito.when(request.getMethod()).thenReturn("DELETE");

        assertTrue(JsonWireUtils.isSessionDeleteRequest(request, "sessionId"));
        assertFalse(JsonWireUtils.isSessionDeleteRequest(request, "sessionId/extra"));
    }

    @Test
    public void testGetSessionHash() {
        String uri = "/wd/hub/session/12345678901234567890123456789012/command";
        assertEquals("12345678901234567890123456789012", JsonWireUtils.getSessionHash(uri));
    }

    @Test
    public void testGetFullSessionId() {
        String uri = "/wd/hub/session/12345678901234567890123456789012/command";
        assertEquals("12345678901234567890123456789012", JsonWireUtils.getFullSessionId(uri));

        String uriWithoutCommand = "/wd/hub/session/12345678901234567890123456789012";
        assertEquals("12345678901234567890123456789012", JsonWireUtils.getFullSessionId(uriWithoutCommand));
    }

    @Test
    public void testGetUriPrefixLength() {
        assertEquals(36, JsonWireUtils.getUriPrefixLength());
    }

    @Test
    public void testRedirectionUrl() throws Exception {
        String host = "http://localhost:4444";
        String command = "session";
        String expectedUrl = "http://localhost:4444/wd/hub/session/session";
        assertEquals(expectedUrl, JsonWireUtils.redirectionUrl(host, command));
    }

    @Test
    public void testGetCommand() throws Exception {
        String uri = "/wd/hub/session/12345678901234567890123456789012/command%20with%20spaces";
        assertEquals("command with spaces", JsonWireUtils.getCommand(uri));

        String uriInvalid = "/wd/hub/session/12345678901234567890123456789012/invalid%command";
        assertEquals("invalid%command", JsonWireUtils.getCommand(uriInvalid));
    }
}
