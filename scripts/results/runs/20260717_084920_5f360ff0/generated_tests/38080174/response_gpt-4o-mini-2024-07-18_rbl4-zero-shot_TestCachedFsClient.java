package io.datakernel.remotefs;

import io.datakernel.async.function.AsyncSupplier;
import io.datakernel.bytebuf.ByteBuf;
import io.datakernel.common.MemSize;
import io.datakernel.csp.ChannelConsumer;
import io.datakernel.csp.ChannelSupplier;
import io.datakernel.promise.Promise;
import io.datakernel.promise.Promises;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class TestCachedFsClient {
    private CachedFsClient cachedFsClient;
    private FsClient mainClient;
    private FsClient cacheClient;

    @Before
    public void setUp() {
        mainClient = mock(FsClient.class);
        cacheClient = mock(FsClient.class);
        cachedFsClient = CachedFsClient.create(mainClient, cacheClient, CachedFsClient.lruCompare());
    }

    @Test
    public void testSetCacheSizeLimit() {
        MemSize cacheSizeLimit = MemSize.of(100);
        Promise<Void> promise = cachedFsClient.setCacheSizeLimit(cacheSizeLimit);
        promise.blockingGet();
        assertEquals(cacheSizeLimit, cachedFsClient.getCacheSizeLimit());
    }

    @Test
    public void testGetTotalCacheSize() {
        when(cacheClient.list("**")).thenReturn(Promise.of(List.of(new FileMetadata("file1", 50), new FileMetadata("file2", 30))));
        Promise<MemSize> promise = cachedFsClient.getTotalCacheSize();
        MemSize totalSize = promise.blockingGet();
        assertEquals(MemSize.of(80), totalSize);
    }

    @Test
    public void testStartWithoutCacheSizeLimit() {
        try {
            cachedFsClient.start().blockingGet();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Cannot start cached client without specifying cache size limit", e.getMessage());
        }
    }

    @Test
    public void testStartWithCacheSizeLimit() {
        MemSize cacheSizeLimit = MemSize.of(100);
        cachedFsClient.setCacheSizeLimit(cacheSizeLimit).blockingGet();
        when(cacheClient.list("**")).thenReturn(Promise.of(List.of()));
        Promise<Void> promise = cachedFsClient.start();
        promise.blockingGet();
        assertNotNull(cachedFsClient.getTotalCacheSize());
    }

    @Test
    public void testDownloadFromCache() {
        String fileName = "testFile";
        when(cacheClient.getMetadata(fileName)).thenReturn(Promise.of(new FileMetadata(fileName, 100)));
        when(cacheClient.download(fileName, 0, 100)).thenReturn(Promise.of(mock(ChannelSupplier.class)));
        Promise<ChannelSupplier<ByteBuf>> promise = cachedFsClient.download(fileName, 0, 100);
        assertNotNull(promise.blockingGet());
    }

    @Test
    public void testDownloadFromMainClient() {
        String fileName = "testFile";
        when(cacheClient.getMetadata(fileName)).thenReturn(Promise.of(null));
        when(mainClient.getMetadata(fileName)).thenReturn(Promise.of(new FileMetadata(fileName, 100)));
        when(mainClient.download(fileName, 0, 100)).thenReturn(Promise.of(mock(ChannelSupplier.class)));
        Promise<ChannelSupplier<ByteBuf>> promise = cachedFsClient.download(fileName, 0, 100);
        assertNotNull(promise.blockingGet());
    }

    @Test
    public void testDeleteFile() {
        String fileName = "testFile";
        when(cacheClient.delete(fileName, 0)).thenReturn(Promise.complete());
        when(mainClient.delete(fileName, 0)).thenReturn(Promise.complete());
        Promise<Void> promise = cachedFsClient.delete(fileName, 0);
        promise.blockingGet();
        verify(cacheClient).delete(fileName, 0);
        verify(mainClient).delete(fileName, 0);
    }

    @Test
    public void testStop() {
        Promise<Void> promise = cachedFsClient.stop();
        promise.blockingGet();
        assertNotNull(promise);
    }
}
