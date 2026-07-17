
package com.nike.wingtips.zipkin2.util;

import com.nike.wingtips.Span;
import com.nike.wingtips.Span.SpanPurpose;
import com.nike.wingtips.Span.TimestampedAnnotation;
import org.junit.Before;
import org.junit.Test;
import zipkin2.Endpoint;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WingtipsToZipkinSpanConverterDefaultImplTest {

    private WingtipsToZipkinSpanConverterDefaultImpl converter;
    private Endpoint zipkinEndpoint;

    @Before
    public void setUp() {
        converter = new WingtipsToZipkinSpanConverterDefaultImpl();
        zipkinEndpoint = Endpoint.newBuilder().serviceName("testService").build();
    }

    @Test
    public void testConvertWingtipsSpanToZipkinSpan() {
        Span wingtipsSpan = createWingtipsSpan("spanId", "traceId", "parentId", "testSpan", SpanPurpose.SERVER);
        zipkin2.Span zipkinSpan = converter.convertWingtipsSpanToZipkinSpan(wingtipsSpan, zipkinEndpoint);

        assertEquals("spanId", zipkinSpan.id());
        assertEquals("traceId", zipkinSpan.traceId());
        assertEquals("parentId", zipkinSpan.parentId());
        assertEquals("testSpan", zipkinSpan.name());
        assertEquals(1000L, zipkinSpan.duration());
        assertEquals(zipkinEndpoint, zipkinSpan.localEndpoint());
        assertEquals(zipkin2.Span.Kind.SERVER, zipkinSpan.kind());
        assertTrue(zipkinSpan.tags().isEmpty());
    }

    @Test
    public void testConvertWingtipsSpanWithTags() {
        Span wingtipsSpan = createWingtipsSpan("spanId", "traceId", "parentId", "testSpan", SpanPurpose.CLIENT);
        wingtipsSpan.putTag("key1", "value1");
        wingtipsSpan.putTag("key2", "value2");

        zipkin2.Span zipkinSpan = converter.convertWingtipsSpanToZipkinSpan(wingtipsSpan, zipkinEndpoint);

        assertEquals("value1", zipkinSpan.tags().get("key1"));
        assertEquals("value2", zipkinSpan.tags().get("key2"));
    }

    @Test
    public void testSanitizeId() {
        converter = new WingtipsToZipkinSpanConverterDefaultImpl(true);
        Span wingtipsSpan = createWingtipsSpan("invalidSpanId", "invalidTraceId", null, "testSpan", SpanPurpose.SERVER);

        zipkin2.Span zipkinSpan = converter.convertWingtipsSpanToZipkinSpan(wingtipsSpan, zipkinEndpoint);

        assertNotEquals("invalidSpanId", zipkinSpan.id());
        assertNotEquals("invalidTraceId", zipkinSpan.traceId());
        assertTrue(wingtipsSpan.getTags().containsKey("sanitized_span_id"));
        assertTrue(wingtipsSpan.getTags().containsKey("sanitized_trace_id"));
        assertTrue(wingtipsSpan.getTags().containsKey("invalid.span_id"));
        assertTrue(wingtipsSpan.getTags().containsKey("invalid.trace_id"));
    }

    @Test
    public void testNullSafePutTag() {
        Span wingtipsSpan = createWingtipsSpan("spanId", "traceId", "parentId", "testSpan", SpanPurpose.SERVER);
        zipkin2.Span.Builder spanBuilder = zipkin2.Span.newBuilder();

        converter.nullSafePutTag(spanBuilder, null, null);
        assertEquals("NULL_KEY", spanBuilder.tags().keySet().iterator().next());
        assertEquals("NULL_VALUE", spanBuilder.tags().values().iterator().next());
    }

    @Test
    public void testNullSafeAddAnnotation() {
        Span wingtipsSpan = createWingtipsSpan("spanId", "traceId", "parentId", "testSpan", SpanPurpose.SERVER);
        zipkin2.Span.Builder spanBuilder = zipkin2.Span.newBuilder();

        converter.nullSafeAddAnnotation(spanBuilder, 123456789L, null);
        assertEquals("NULL_VALUE", spanBuilder.annotations().get(0).value());
    }

    private Span createWingtipsSpan(String spanId, String traceId, String parentId, String spanName, SpanPurpose purpose) {
        Span span = new Span(spanId, traceId, parentId, spanName, 1000L, System.currentTimeMillis() * 1000, purpose);
        span.putTag("key", "value");
        span.addTimestampedAnnotation(new TimestampedAnnotation(System.currentTimeMillis() * 1000, "annotation"));
        return span;
    }
}
