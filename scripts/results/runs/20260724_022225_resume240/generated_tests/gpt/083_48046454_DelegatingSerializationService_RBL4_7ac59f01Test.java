package com.hazelcast.jet.impl.serialization;

import com.hazelcast.internal.serialization.impl.SerializerAdapter;
import com.hazelcast.internal.serialization.impl.AbstractSerializationService;
import com.hazelcast.nio.serialization.Serializer;
import com.hazelcast.jet.impl.serialization.DelegatingSerializationService;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DelegatingSerializationService_RBL4_7ac59f01Test {

    private AbstractSerializationService delegate;
    private DelegatingSerializationService service;
    private Map<Class<?>, Serializer> serializers;

    @Before
    public void setUp() {
        delegate = mock(AbstractSerializationService.class);
        serializers = new HashMap<>();
        service = new DelegatingSerializationService(serializers, delegate);
    }

    @Test
    public void testConstructorWithEmptySerializers() {
        assertTrue(service.serializersByClass.isEmpty());
        assertTrue(service.serializersById.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidTypeId() {
        Serializer invalidSerializer = mock(Serializer.class);
        when(invalidSerializer.getTypeId()).thenReturn(0);
        serializers.put(String.class, invalidSerializer);
        new DelegatingSerializationService(serializers, delegate);
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithDuplicateTypeId() {
        Serializer serializer1 = mock(Serializer.class);
        when(serializer1.getTypeId()).thenReturn(1);
        serializers.put(String.class, serializer1);

        Serializer serializer2 = mock(Serializer.class);
        when(serializer2.getTypeId()).thenReturn(1);
        serializers.put(Integer.class, serializer2);

        new DelegatingSerializationService(serializers, delegate);
    }

    @Test
    public void testSerializerForObject() {
        Serializer serializer = mock(Serializer.class);
        when(serializer.getTypeId()).thenReturn(1);
        serializers.put(String.class, serializer);

        SerializerAdapter adapter = service.serializerFor("test");
        assertNotNull(adapter);
        assertEquals(serializer, adapter.getImpl());
    }

    @Test(expected = JetException.class)
    public void testSerializerForObjectWithNoSerializer() {
        service.serializerFor(new Object());
    }

    @Test
    public void testSerializerForTypeId() {
        Serializer serializer = mock(Serializer.class);
        when(serializer.getTypeId()).thenReturn(1);
        serializers.put(String.class, serializer);

        SerializerAdapter adapter = service.serializerFor(1);
        assertNotNull(adapter);
        assertEquals(serializer, adapter.getImpl());
    }

    @Test(expected = JetException.class)
    public void testSerializerForTypeIdWithNoSerializer() {
        service.serializerFor(2);
    }

    @Test
    public void testDispose() {
        SerializerAdapter adapter = mock(SerializerAdapter.class);
        serializers.put(String.class, adapter);
        service.dispose();
        verify(adapter).destroy();
        assertFalse(service.active);
    }

    @Test
    public void testFrom() {
        Map<String, String> serializerConfigs = new HashMap<>();
        serializerConfigs.put("com.example.MyClass", "com.example.MyClassSerializer");
        InternalSerializationService internalService = DelegatingSerializationService.from(delegate, serializerConfigs);
        assertNotNull(internalService);
    }
}
