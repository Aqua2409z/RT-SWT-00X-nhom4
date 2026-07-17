package io.datakernel.http;

import io.datakernel.bytebuf.ByteBuf;
import io.datakernel.http.ContentType;
import io.datakernel.http.HttpCharset;
import io.datakernel.http.MediaType;
import io.datakernel.common.parse.ParseException;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class ContentTypeTest {

    @Test
    public void testOfWithMediaType() {
        MediaType mediaType = MediaType.of("text/plain");
        ContentType contentType = ContentType.of(mediaType);
        assertEquals(mediaType, contentType.getMediaType());
        assertNull(contentType.getCharset());
    }

    @Test
    public void testOfWithMediaTypeAndCharset() {
        MediaType mediaType = MediaType.of("text/html");
        ContentType contentType = ContentType.of(mediaType, StandardCharsets.UTF_8);
        assertEquals(mediaType, contentType.getMediaType());
        assertNotNull(contentType.getCharset());
        assertEquals(StandardCharsets.UTF_8, contentType.getCharset());
    }

    @Test
    public void testParseValidContentType() throws ParseException {
        byte[] bytes = "text/html; charset=UTF-8".getBytes();
        ContentType contentType = ContentType.parse(bytes, 0, bytes.length);
        assertEquals(MediaType.of("text/html"), contentType.getMediaType());
        assertEquals(HttpCharset.of(StandardCharsets.UTF_8), contentType.charset);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidContentType() throws ParseException {
        byte[] bytes = "invalid/content-type".getBytes();
        ContentType.parse(bytes, 0, bytes.length);
    }

    @Test
    public void testRenderWithoutCharset() {
        MediaType mediaType = MediaType.of("application/json");
        ContentType contentType = ContentType.of(mediaType);
        ByteBuf buf = ByteBuf.allocate(contentType.size());
        ContentType.render(contentType, buf);
        String result = new String(buf.array(), 0, buf.tail());
        assertEquals("application/json", result);
    }

    @Test
    public void testRenderWithCharset() {
        MediaType mediaType = MediaType.of("text/xml");
        ContentType contentType = ContentType.of(mediaType, StandardCharsets.UTF_8);
        ByteBuf buf = ByteBuf.allocate(contentType.size());
        ContentType.render(contentType, buf);
        String result = new String(buf.array(), 0, buf.tail());
        assertEquals("text/xml; charset=UTF-8", result);
    }

    @Test
    public void testEqualsAndHashCode() {
        MediaType mediaType = MediaType.of("image/png");
        ContentType contentType1 = ContentType.of(mediaType);
        ContentType contentType2 = ContentType.of(mediaType);
        assertEquals(contentType1, contentType2);
        assertEquals(contentType1.hashCode(), contentType2.hashCode());

        ContentType contentType3 = ContentType.of(mediaType, StandardCharsets.UTF_8);
        assertNotEquals(contentType1, contentType3);
    }

    @Test
    public void testToString() {
        MediaType mediaType = MediaType.of("application/pdf");
        ContentType contentType = ContentType.of(mediaType, StandardCharsets.ISO_8859_1);
        String expected = "ContentType{type=application/pdf, charset=ISO-8859-1}";
        assertEquals(expected, contentType.toString());
    }
}
