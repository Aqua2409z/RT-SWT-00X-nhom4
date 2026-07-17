
package com.jeromeloisel.database.scroll.elastic;

import com.jeromeloisel.db.scroll.api.DatabaseScroll;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ElasticScrollTest {

    private Client client;
    private SearchRequestBuilder searchRequestBuilder;
    private ElasticScroll elasticScroll;
    private DatabaseScroll databaseScroll;

    @Before
    public void setUp() {
        client = mock(Client.class);
        searchRequestBuilder = mock(SearchRequestBuilder.class);
        databaseScroll = mock(DatabaseScroll.class);
        TimeValue timeValue = TimeValue.timeValueMinutes(1);
        elasticScroll = new ElasticScroll(client, searchRequestBuilder, timeValue);
    }

    @Test
    public void testWithSort() {
        FieldSortBuilder sortBuilder = mock(FieldSortBuilder.class);
        elasticScroll.withSort(sortBuilder);
        assertEquals(sortBuilder, elasticScroll.sort.get());
    }

    @Test
    public void testWithScrollSize() {
        int size = 10;
        elasticScroll.withScrollSize(size);
        verify(searchRequestBuilder).setSize(size);
    }

    @Test
    public void testWithKeepAlive() {
        long time = 5;
        TimeUnit unit = TimeUnit.SECONDS;
        elasticScroll.withKeepAlive(time, unit);
        assertEquals(TimeValue.timeValueMillis(unit.toMillis(time)), elasticScroll.scrollTime.get());
    }

    @Test
    public void testWithTypes() {
        String[] types = {"type1", "type2"};
        elasticScroll.withTypes(types);
        verify(searchRequestBuilder).setTypes(types);
    }

    @Test
    public void testWithFetchSource() {
        elasticScroll.withFetchSource(true);
        verify(searchRequestBuilder).setFetchSource(true);
    }

    @Test
    public void testWithQuery() {
        elasticScroll.withQuery(QueryBuilders.matchAllQuery());
        verify(searchRequestBuilder).setQuery(QueryBuilders.matchAllQuery());
    }

    @Test
    public void testScroll() throws Exception {
        SearchHit[] hits = new SearchHit[2];
        hits[0] = mock(SearchHit.class);
        hits[1] = mock(SearchHit.class);
        
        SearchResponse response = mock(SearchResponse.class);
        when(response.getScrollId()).thenReturn("scrollId");
        when(response.getHits()).thenReturn(mock(org.elasticsearch.search.SearchHits.class));
        when(response.getHits().getHits()).thenReturn(hits);
        
        when(searchRequestBuilder.addSort(any(FieldSortBuilder.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setScroll(any(TimeValue.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.execute()).thenReturn(mock(org.elasticsearch.action.ActionFuture.class));
        when(searchRequestBuilder.execute().actionGet()).thenReturn(response);
        
        elasticScroll.scroll(databaseScroll);
        
        verify(databaseScroll, times(1)).onStartBatch();
        verify(databaseScroll, times(1)).accept(hits[0]);
        verify(databaseScroll, times(1)).accept(hits[1]);
        verify(databaseScroll, times(1)).onEndBatch();
    }

    @Test
    public void testScrollEmptyHits() throws Exception {
        SearchResponse response = mock(SearchResponse.class);
        when(response.getScrollId()).thenReturn("scrollId");
        when(response.getHits()).thenReturn(mock(org.elasticsearch.search.SearchHits.class));
        when(response.getHits().getHits()).thenReturn(new SearchHit[0]);
        
        when(searchRequestBuilder.addSort(any(FieldSortBuilder.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setScroll(any(TimeValue.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.execute()).thenReturn(mock(org.elasticsearch.action.ActionFuture.class));
        when(searchRequestBuilder.execute().actionGet()).thenReturn(response);
        
        elasticScroll.scroll(databaseScroll);
        
        verify(databaseScroll, never()).accept(any());
    }
}
