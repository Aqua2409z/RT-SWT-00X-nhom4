
package org.minnal.instrument.resource;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Providers;

import org.activejpa.entity.Filter;
import org.minnal.instrument.MinnalInstrumentationException;
import org.minnal.instrument.entity.metadata.ParameterMetaData;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ResourceUtil_RBL4_be372e05Test {

    private MultivaluedMap<String, String> queryParams;
    private Providers providers;
    private HttpHeaders httpHeaders;

    @BeforeMethod
    public void setUp() {
        queryParams = mock(MultivaluedMap.class);
        providers = mock(Providers.class);
        httpHeaders = mock(HttpHeaders.class);
    }

    @Test
    public void testGetFilterWithPagination() {
        when(queryParams.getFirst(ResourceUtil.PER_PAGE)).thenReturn("10");
        when(queryParams.getFirst(ResourceUtil.PAGE_NO)).thenReturn("1");

        Filter filter = ResourceUtil.getFilter(queryParams);
        assertEquals(filter.getPerPage(), Integer.valueOf(10));
        assertEquals(filter.getPageNo(), Integer.valueOf(1));
    }

    @Test
    public void testGetFilterWithConditions() {
        when(queryParams.getFirst(ResourceUtil.PER_PAGE)).thenReturn("10");
        when(queryParams.getFirst(ResourceUtil.PAGE_NO)).thenReturn("1");
        when(queryParams.entrySet()).thenReturn(Map.of("name", List.of("John")).entrySet());

        Filter filter = ResourceUtil.getFilter(queryParams, Arrays.asList("name"));
        assertEquals(filter.getPerPage(), Integer.valueOf(10));
        assertEquals(filter.getPageNo(), Integer.valueOf(1));
        // Add assertions for conditions
    }

    @Test
    public void testIsCommaSeparated() {
        assertTrue(ResourceUtil.isCommaSeparated("value1,value2"));
        assertFalse(ResourceUtil.isCommaSeparated("value1"));
    }

    @Test
    public void testGetCommaSeparatedValues() {
        String[] values = ResourceUtil.getCommaSeparatedValues("value1,value2,value3");
        assertEquals(values, new String[]{"value1", "value2", "value3"});
    }

    @Test(expectedExceptions = MinnalInstrumentationException.class)
    public void testGetContentThrowsException() {
        byte[] raw = new byte[0];
        when(providers.getMessageBodyReader(any(), any(), any(), any())).thenThrow(new RuntimeException());
        ResourceUtil.getContent(raw, providers, httpHeaders, Object.class);
    }

    @Test
    public void testInvokeAction() throws Throwable {
        Object model = new TestModel();
        String actionName = "testAction";
        List<ParameterMetaData> parameters = Arrays.asList(new ParameterMetaData("param1", String.class));
        byte[] rawContent = "param1=value".getBytes();
        Map<String, Object> values = new HashMap<>();

        Object result = ResourceUtil.invokeAction(model, actionName, parameters, rawContent, providers, httpHeaders, values);
        assertEquals(result, "Received: value");
    }

    private static class ResourceUtil_RBL4_be372e05Test {
        public String testAction(String param1) {
            return "Received: " + param1;
        }
    }
}
