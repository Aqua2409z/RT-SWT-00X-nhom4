
package com.yahoo.parsec.clients;

import com.ning.http.client.Param;
import com.ning.http.client.cookie.Cookie;
import com.ning.http.client.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.IOException;
import java.util.*;

public class ParsecHttpUtil_RBL4_603d73bfTest {

    @Test
    public void testGetCookies() {
        Collection<Cookie> ningCookies = new ArrayList<>();
        ningCookies.add(new Cookie("name1", "value1", "/", "domain1", 3600, true, true));
        ningCookies.add(new Cookie("name2", "value2", "/", "domain2", 3600, false, false));

        List<NewCookie> newCookies = ParsecHttpUtil.getCookies(ningCookies);
        Assert.assertEquals(newCookies.size(), 2);
        Assert.assertEquals(newCookies.get(0).getName(), "name1");
        Assert.assertEquals(newCookies.get(1).getName(), "name2");
    }

    @Test
    public void testGetCookie() {
        Cookie ningCookie = new Cookie("name", "value", "/", "domain", 3600, true, true);
        NewCookie newCookie = ParsecHttpUtil.getCookie(ningCookie);
        Assert.assertEquals(newCookie.getName(), "name");
        Assert.assertEquals(newCookie.getValue(), "value");
        Assert.assertEquals(newCookie.getPath(), "/");
        Assert.assertEquals(newCookie.getDomain(), "domain");
        Assert.assertTrue(newCookie.isSecure());
        Assert.assertTrue(newCookie.isHttpOnly());
    }

    @Test
    public void testGetParamsMap() {
        List<Param> ningParams = new ArrayList<>();
        ningParams.add(new Param("param1", "value1"));
        ningParams.add(new Param("param1", "value2"));
        ningParams.add(new Param("param2", "value3"));

        Map<String, List<String>> paramsMap = ParsecHttpUtil.getParamsMap(ningParams);
        Assert.assertEquals(paramsMap.size(), 2);
        Assert.assertTrue(paramsMap.get("param1").contains("value1"));
        Assert.assertTrue(paramsMap.get("param1").contains("value2"));
        Assert.assertTrue(paramsMap.get("param2").contains("value3"));
    }

    @Test
    public void testGetResponse() throws IOException {
        Map<String, List<String>> headers = new HashMap<>();
        headers.put("header1", Arrays.asList("value1"));
        headers.put("header2", Arrays.asList("value2"));

        Response ningResponse = new Response(200, "text/plain", headers, "response body", new ArrayList<>());

        javax.ws.rs.core.Response response = ParsecHttpUtil.getResponse(ningResponse);
        Assert.assertEquals(response.getStatus(), 200);
        Assert.assertEquals(response.getMediaType().toString(), "text/plain");
        Assert.assertEquals(response.getEntity(), "response body");
        Assert.assertTrue(response.getCookies().size() == 0); // Assuming no cookies in the response
    }

    @Test
    public void testParseCharsetFromContentType() {
        String contentType = "text/html; charset=UTF-8";
        String charset = ParsecHttpUtil.parseCharsetFromContentType(contentType, "ISO-8859-1");
        Assert.assertEquals(charset, "UTF-8");

        charset = ParsecHttpUtil.parseCharsetFromContentType(null, "ISO-8859-1");
        Assert.assertEquals(charset, "ISO-8859-1");
    }
}
