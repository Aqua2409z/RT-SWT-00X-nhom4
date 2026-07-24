package com.ebayopensource.webrex.resource.cache;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.ebayopensource.webrex.resource.api.IResource;
import com.ebayopensource.webrex.resource.api.IResourceLocale;
import com.ebayopensource.webrex.resource.api.IResourceUrn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceCache_RBL4_57adf17bTest {

    private ResourceCache resourceCache;
    private IResourceUrn mockUrn;
    private IResourceLocale mockLocale;
    private IResource mockResource;

    @Before
    public void setUp() {
        resourceCache = new ResourceCache();
        mockUrn = Mockito.mock(IResourceUrn.class);
        mockLocale = Mockito.mock(IResourceLocale.class);
        mockResource = Mockito.mock(IResource.class);
    }

    @Test
    public void testPutAndGetCache() {
        Mockito.when(mockUrn.toString()).thenReturn("urn:test");
        Mockito.when(mockLocale.toExternal()).thenReturn("en_US");

        resourceCache.putCache(mockUrn, mockLocale, true, mockResource);
        IResource retrievedResource = resourceCache.getCache(mockUrn, mockLocale, true);

        assertNotNull(retrievedResource);
        assertEquals(mockResource, retrievedResource);
    }

    @Test
    public void testPutCaches() {
        Mockito.when(mockUrn.toString()).thenReturn("urn:test");
        Mockito.when(mockLocale.toExternal()).thenReturn("en_US");

        List<IResource> resources = new ArrayList<>();
        resources.add(mockResource);
        Map<String, List<IResource>> map = new HashMap<>();
        map.put("en_US", resources);

        resourceCache.putCaches("namespace", map, true);
        IResource retrievedResource = resourceCache.getCache(mockUrn, mockLocale, true);

        assertNotNull(retrievedResource);
        assertEquals(mockResource, retrievedResource);
    }

    @Test
    public void testGetCacheWithNonExistentKey() {
        IResource retrievedResource = resourceCache.getCache(mockUrn, mockLocale, false);
        assertNull(retrievedResource);
    }

    @Test
    public void testCacheKeyEqualsAndHashCode() {
        CacheKey key1 = new ResourceCache.CacheKey(mockLocale, mockUrn, true);
        CacheKey key2 = new ResourceCache.CacheKey(mockLocale, mockUrn, true);
        CacheKey key3 = new ResourceCache.CacheKey(mockLocale, mockUrn, false);

        assertEquals(key1, key2);
        assertNotEquals(key1, key3);
        assertEquals(key1.hashCode(), key2.hashCode());
        assertNotEquals(key1.hashCode(), key3.hashCode());
    }

    @Test
    public void testCacheKeyWithNullLocale() {
        CacheKey key1 = new ResourceCache.CacheKey(null, mockUrn, true);
        CacheKey key2 = new ResourceCache.CacheKey(null, mockUrn, true);

        assertEquals(key1, key2);
    }

    @Test
    public void testCacheKeyWithNullUrn() {
        CacheKey key1 = new ResourceCache.CacheKey(mockLocale, null, true);
        CacheKey key2 = new ResourceCache.CacheKey(mockLocale, null, true);

        assertEquals(key1, key2);
    }
}
