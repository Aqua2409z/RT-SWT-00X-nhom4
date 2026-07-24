
package com.bazaarvoice.emodb.event.core;

import com.bazaarvoice.emodb.event.api.EventData;
import com.bazaarvoice.emodb.event.api.EventSink;
import com.bazaarvoice.emodb.event.api.ScanSink;
import com.bazaarvoice.emodb.event.db.EventId;
import com.bazaarvoice.emodb.event.db.EventIdSerializer;
import com.bazaarvoice.emodb.event.db.EventReaderDAO;
import com.bazaarvoice.emodb.event.db.EventWriterDAO;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.*;

public class DefaultEventStore_RBL4_cf4261e0Test {
    private EventReaderDAO readerDao;
    private EventWriterDAO writerDao;
    private EventIdSerializer eventIdSerializer;
    private ClaimStore claimStore;
    private DefaultEventStore eventStore;

    @BeforeMethod
    public void setUp() {
        readerDao = Mockito.mock(EventReaderDAO.class);
        writerDao = Mockito.mock(EventWriterDAO.class);
        eventIdSerializer = Mockito.mock(EventIdSerializer.class);
        claimStore = Mockito.mock(ClaimStore.class);
        eventStore = new DefaultEventStore(readerDao, writerDao, eventIdSerializer, claimStore);
    }

    @Test
    public void testAddSingleEvent() {
        String channel = "testChannel";
        ByteBuffer event = ByteBuffer.wrap("testEvent".getBytes());

        eventStore.add(channel, event);

        Mockito.verify(writerDao).addAll(Mockito.any(), Mockito.isNull());
    }

    @Test
    public void testAddAllEvents() {
        String channel = "testChannel";
        Collection<ByteBuffer> events = Arrays.asList(ByteBuffer.wrap("event1".getBytes()), ByteBuffer.wrap("event2".getBytes()));

        eventStore.addAll(channel, events);

        Mockito.verify(writerDao).addAll(Mockito.any(), Mockito.isNull());
    }

    @Test
    public void testGetSizeEstimate() {
        String channel = "testChannel";
        long limit = 100;
        Mockito.when(readerDao.count(channel, limit)).thenReturn(10L);

        long sizeEstimate = eventStore.getSizeEstimate(channel, limit);

        Assert.assertEquals(sizeEstimate, 10L);
    }

    @Test
    public void testGetClaimCount() {
        String channel = "testChannel";
        Mockito.when(claimStore.withClaimSet(Mockito.any())).thenAnswer(invocation -> {
            Function<ClaimSet, Long> function = invocation.getArgument(0);
            return function.apply(Mockito.mock(ClaimSet.class));
        });

        long claimCount = eventStore.getClaimCount(channel);

        Assert.assertEquals(claimCount, 0L);
    }

    @Test
    public void testPeek() {
        String channel = "testChannel";
        int limit = 5;
        EventSink sink = Mockito.mock(EventSink.class);
        Mockito.when(sink.remaining()).thenReturn(limit);
        Mockito.when(readerDao.readAll(Mockito.any(), Mockito.any(), Mockito.isNull(), Mockito.eq(true))).thenAnswer(invocation -> {
            DaoEventSink daoSink = invocation.getArgument(1);
            daoSink.accept(Mockito.mock(EventId.class), ByteBuffer.wrap("eventData".getBytes()));
            return null;
        });

        List<EventData> events = eventStore.peek(channel, limit);

        Assert.assertNotNull(events);
    }

    @Test
    public void testPoll() {
        String channel = "testChannel";
        Duration claimTtl = Duration.ofSeconds(10);
        EventSink sink = Mockito.mock(EventSink.class);
        Mockito.when(sink.remaining()).thenReturn(5);
        Mockito.when(readerDao.readNewer(Mockito.any(), Mockito.any())).thenAnswer(invocation -> {
            DaoEventSink daoSink = invocation.getArgument(1);
            daoSink.accept(Mockito.mock(EventId.class), ByteBuffer.wrap("eventData".getBytes()));
            return null;
        });

        List<EventData> events = eventStore.poll(channel, claimTtl, 5);

        Assert.assertNotNull(events);
    }

    @Test
    public void testDelete() {
        String channel = "testChannel";
        Collection<String> eventIds = Arrays.asList("event1", "event2");

        eventStore.delete(channel, eventIds, true);

        Mockito.verify(writerDao).delete(Mockito.eq(channel), Mockito.any());
    }

    @Test
    public void testPurge() {
        String channel = "testChannel";

        eventStore.purge(channel);

        Mockito.verify(writerDao).deleteAll(channel);
    }

    @Test
    public void testCopy() {
        String fromChannel = "fromChannel";
        String toChannel = "toChannel";
        Predicate<ByteBuffer> filter = eventData -> true;

        eventStore.copy(fromChannel, toChannel, filter, new Date());

        Mockito.verify(writerDao, Mockito.atLeastOnce()).addAll(Mockito.any(), Mockito.isNull());
    }

    @Test
    public void testMove() {
        String fromChannel = "fromChannel";
        String toChannel = "toChannel";

        eventStore.move(fromChannel, toChannel);

        Mockito.verify(writerDao, Mockito.atLeastOnce()).addAll(Mockito.any(), Mockito.isNull());
    }
}
