package io.datakernel.uikernel;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import io.datakernel.common.parse.ParseException;
import io.datakernel.http.HttpRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class ReadSettingsTest {
    private Gson gson;
    private HttpRequest request;

    @Before
    public void setUp() {
        gson = new Gson();
        request = new HttpRequest();
    }

    @Test
    public void testFromWithValidParameters() throws ParseException {
        request.setQueryParameter("fields", "[\"field1\", \"field2\"]");
        request.setQueryParameter("offset", "10");
        request.setQueryParameter("limit", "100");
        request.setQueryParameter("filters", "{\"filter1\":\"value1\"}");
        request.setQueryParameter("sort", "[[\"field1\", \"asc\"], [\"field2\", \"desc\"]]");
        request.setQueryParameter("extra", "[\"extra1\", \"extra2\"]");

        ReadSettings<String> settings = ReadSettings.from(gson, request);

        assertEquals(Arrays.asList("field1", "field2"), settings.getFields());
        assertEquals(10, settings.getOffset());
        assertEquals(100, settings.getLimit());
        assertEquals(Collections.singletonMap("filter1", "value1"), settings.getFilters());
        assertEquals(Collections.singletonMap("field1", ReadSettings.SortOrder.ASCENDING), settings.getSort());
        assertEquals(new HashSet<>(Arrays.asList("extra1", "extra2")), settings.getExtra());
    }

    @Test
    public void testFromWithEmptyParameters() throws ParseException {
        ReadSettings<String> settings = ReadSettings.from(gson, request);

        assertTrue(settings.getFields().isEmpty());
        assertEquals(0, settings.getOffset());
        assertEquals(Integer.MAX_VALUE, settings.getLimit());
        assertTrue(settings.getFilters().isEmpty());
        assertTrue(settings.getSort().isEmpty());
        assertTrue(settings.getExtra().isEmpty());
    }

    @Test
    public void testFromWithInvalidOffset() throws ParseException {
        request.setQueryParameter("offset", "invalid");
        ReadSettings<String> settings = ReadSettings.from(gson, request);
        assertEquals(0, settings.getOffset());
    }

    @Test
    public void testFromWithInvalidLimit() throws ParseException {
        request.setQueryParameter("limit", "invalid");
        ReadSettings<String> settings = ReadSettings.from(gson, request);
        assertEquals(Integer.MAX_VALUE, settings.getLimit());
    }

    @Test
    public void testOfMethod() {
        List<String> fields = Arrays.asList("field1", "field2");
        int offset = 5;
        int limit = 50;
        Map<String, String> filters = Collections.singletonMap("filter1", "value1");
        Map<String, ReadSettings.SortOrder> sort = Collections.singletonMap("field1", ReadSettings.SortOrder.ASCENDING);
        Set<String> extra = new HashSet<>(Arrays.asList("extra1", "extra2"));

        ReadSettings<String> settings = ReadSettings.of(fields, offset, limit, filters, sort, extra);

        assertEquals(fields, settings.getFields());
        assertEquals(offset, settings.getOffset());
        assertEquals(limit, settings.getLimit());
        assertEquals(filters, settings.getFilters());
        assertEquals(sort, settings.getSort());
        assertEquals(extra, settings.getExtra());
    }

    @Test
    public void testToString() {
        List<String> fields = Arrays.asList("field1", "field2");
        Map<String, String> filters = Collections.singletonMap("filter1", "value1");
        Map<String, ReadSettings.SortOrder> sort = Collections.singletonMap("field1", ReadSettings.SortOrder.ASCENDING);
        Set<String> extra = new HashSet<>(Arrays.asList("extra1", "extra2"));

        ReadSettings<String> settings = ReadSettings.of(fields, 0, 10, filters, sort, extra);
        String expectedString = "ReadSettings{fields=" + fields + ", offset=0, limit=10, filters=" + filters + ", sort=" + sort + ", extra=" + extra + '}';
        assertEquals(expectedString, settings.toString());
    }
}
