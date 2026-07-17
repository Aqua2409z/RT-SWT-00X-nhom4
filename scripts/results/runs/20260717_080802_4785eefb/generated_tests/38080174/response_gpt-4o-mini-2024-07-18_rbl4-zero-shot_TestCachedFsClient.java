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
import org.mockito.Mockito;

import java.util.Comparator;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestCachedFsClient {
    private CachedFsClient cachedFsClient;
    private FsClient mainClient;
    private FsClient cacheClient;
    private MemSize cacheSizeLimit;

    @Before
    public void setUp() {
        mainClient = mock(FsClient.class);
        cacheClient = mock(FsClient.class);
        cacheSizeLimit = MemSize.of(1024 * 1024); // 1 MB
        cachedFsClient = CachedFsClient.create(mainClient, cacheClient, CachedFsClient.lruCompare())
                .with(cacheSizeLimit);
    }

    @Test
    public void testSetCacheSizeLimit() {
        MemSize newSizeLimit = MemSize.of(2048 * 1024); // 2 MB
        Promise<Void> promise = cachedFsClient.setCacheSizeLimit(newSizeLimit);
        promise.blockingGet();
        assertEquals(newSizeLimit, cachedFsClient.getCacheSizeLimit());
    }

    @Test
    public void testGetTotalCacheSize() {
        when(cacheClient.list("**")).thenReturn(Promise.of(List.of(new FileMetadata("file1", 500), new FileMetadata("file2", 300))));
        Promise<MemSize> promise = cachedFsClient.getTotalCacheSize();
        assertEquals(MemSize.of(800), promise.blockingGet());
    }

    @Test
    public void testStartWithoutCacheSizeLimit() {
        CachedFsClient client = CachedFsClient.create(mainClient, cacheClient, CachedFsClient.lruCompare());
        Promise<Void> promise = client.start();
        assertThrows(IllegalStateException.class, promise::blockingGet);
    }

    @Test
    public void testDownloadFromCache() {
        FileMetadata cachedFile = new FileMetadata("file1", 500);
        when(cacheClient.getMetadata("file1")).thenReturn(Promise.of(cachedFile));
        when(cacheClient.download("file1", 0, 500)).thenReturn(Promise.of(mock(ChannelSupplier.class)));

        Promise<ChannelSupplier<ByteBuf>> promise = cachedFsClient.download("file1", 0, 500);
        assertNotNull(promise.blockingGet());
    }

    @Test
    public void testDownloadFromMainClient() {
        FileMetadata mainFile = new FileMetadata("file1", 500);
        when(cacheClient.getMetadata("file1")).thenReturn(Promise.of(null));
        when(mainClient.getMetadata("file1")).thenReturn(Promise.of(mainFile));
        when(mainClient.download("file1", 0, 500)).thenReturn(Promise.of(mock(ChannelSupplier.class)));

        Promise<ChannelSupplier<ByteBuf>> promise = cachedFsClient.download("file1", 0, 500);
        assertNotNull(promise.blockingGet());
    }

    @Test
    public void testDeleteFile() {
        when(cacheClient.delete("file1", 0)).thenReturn(Promise.complete());
        when(mainClient.delete("file1", 0)).thenReturn(Promise.complete());

        Promise<Void> promise = cachedFsClient.delete("file1", 0);
        promise.blockingGet();
        verify(cacheClient).delete("file1", 0);
        verify(mainClient).delete("file1", 0);
    }

    @Test
    public void testListEntities() {
        when(cacheClient.listEntities("**")).thenReturn(Promise.of(List.of(new FileMetadata("file1", 500))));
        when(mainClient.listEntities("**")).thenReturn(Promise.of(List.of(new FileMetadata("file2", 300))));

        Promise<List<FileMetadata>> promise = cachedFsClient.listEntities("**");
        List<FileMetadata> result = promise.blockingGet();
        assertEquals(2, result.size());
    }

    @Test
    public void testStop() {
        Promise<Void> promise = cachedFsClient.stop();
        promise.blockingGet();
        // Ensure that ensureSpace is called
        verify(cachedFsClient, times(1)).ensureSpace();
    }
}
