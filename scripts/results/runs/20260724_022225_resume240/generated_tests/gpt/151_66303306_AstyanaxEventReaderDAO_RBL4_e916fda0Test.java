
package com.bazaarvoice.emodb.event.db.astyanax;

import com.bazaarvoice.emodb.common.cassandra.CassandraKeyspace;
import com.bazaarvoice.emodb.common.dropwizard.lifecycle.LifeCycleRegistry;
import com.bazaarvoice.emodb.common.dropwizard.metrics.MetricRegistry;
import com.bazaarvoice.emodb.event.db.EventSink;
import com.codahale.metrics.Meter;
import com.google.common.cache.CacheLoader;
import com.netflix.astyanax.model.Column;
import com.netflix.astyanax.model.ColumnList;
import com.netflix.astyanax.model.Row;
import com.netflix.astyanax.query.RowQuery;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import static org.mockito.Mockito.*;

public class AstyanaxEventReaderDAO_RBL4_e916fda0Test {
    private AstyanaxEventReaderDAO eventReaderDAO;
    private CassandraKeyspace keyspace;
    private ManifestPersister manifestPersister;
    private MetricRegistry metricRegistry;
    private EventSink eventSink;
    private LifeCycleRegistry lifeCycle;

    @BeforeMethod
    public void setUp() {
        keyspace = mock(CassandraKeyspace.class);
        manifestPersister = mock(ManifestPersister.class);
        metricRegistry = mock(MetricRegistry.class);
        eventSink = mock(EventSink.class);
        lifeCycle = mock(LifeCycleRegistry.class);
        eventReaderDAO = new AstyanaxEventReaderDAO(lifeCycle, keyspace, manifestPersister, "testMetricsGroup", metricRegistry);
    }

    @Test
    public void testListChannels() {
        Row<String, ByteBuffer> row = mock(Row.class);
        ColumnList<ByteBuffer> columns = mock(ColumnList.class);
        when(columns.isEmpty()).thenReturn(false);
        when(row.getColumns()).thenReturn(columns);
        when(row.getKey()).thenReturn("channel1");

        RowQuery<String, ByteBuffer> query = mock(RowQuery.class);
        when(query.getAllRows()).thenReturn(query);
        when(query.setRowLimit(1000)).thenReturn(query);
        when(query.withColumnRange(any())).thenReturn(query);
        when(keyspace.prepareQuery(any(), any())).thenReturn(query);
        when(query.iterator()).thenReturn(Collections.singletonList(row).iterator());

        Iterator<String> channels = eventReaderDAO.listChannels();
        assert channels.hasNext();
        assert "channel1".equals(channels.next());
    }

    @Test
    public void testCount() {
        long limit = 10;
        String channel = "channel1";
        Column<ByteBuffer> column = mock(Column.class);
        ByteBuffer slabId = ByteBuffer.allocate(16);
        when(column.getName()).thenReturn(slabId);
        when(column.getBooleanValue()).thenReturn(false);

        Iterator<Column<ByteBuffer>> manifestColumns = Collections.singletonList(column).iterator();
        when(keyspace.prepareQuery(any(), any())).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).getKey(channel)).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).withColumnRange(any())).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).autoPaginate(true)).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).getCount()).thenReturn(5);
        when(keyspace.prepareQuery(any(), any()).getKey(slabId)).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).withColumnRange(any(), any(), any(), any(), any())).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).getCount()).thenReturn(5);

        long count = eventReaderDAO.count(channel, limit);
        assert count == 5;
    }

    @Test
    public void testMoveIfFast() {
        String fromChannel = "fromChannel";
        String toChannel = "toChannel";
        Column<ByteBuffer> column = mock(Column.class);
        ByteBuffer slabId = ByteBuffer.allocate(16);
        when(column.getName()).thenReturn(slabId);
        when(column.getBooleanValue()).thenReturn(false);

        Iterator<Column<ByteBuffer>> manifestColumns = Collections.singletonList(column).iterator();
        when(keyspace.prepareQuery(any(), any())).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).getKey(fromChannel)).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).withColumnRange(any())).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).autoPaginate(true)).thenReturn(mock(RowQuery.class));
        when(keyspace.prepareQuery(any(), any()).getCount()).thenReturn(1);

        boolean moved = eventReaderDAO.moveIfFast(fromChannel, toChannel);
        assert !moved;
        verify(manifestPersister, times(1)).move(fromChannel, toChannel, Collections.singletonList(slabId), false);
    }

    @Test
    public void testReadAll() {
        String channel = "channel1";
        Date since = new Date();
        eventReaderDAO.readAll(channel, eventSink, since, false);
        verify(eventSink, times(1)).accept(any(), any());
    }

    @Test
    public void testMarkUnread() {
        String channel = "channel1";
        EventId eventId = mock(EventId.class);
        when(eventId.getChannel()).thenReturn(channel);
        when(eventId.getSlabId()).thenReturn(ByteBuffer.allocate(16));
        when(eventId.getEventIdx()).thenReturn(1);

        eventReaderDAO.markUnread(channel, Collections.singletonList(eventId));
        // Verify that the cursor is updated correctly
    }
}
