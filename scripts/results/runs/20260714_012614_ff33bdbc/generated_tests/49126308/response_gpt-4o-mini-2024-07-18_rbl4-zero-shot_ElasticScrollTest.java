
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

import java.io.IOException;
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
        elasticScroll = new ElasticScroll(client, searchRequestBuilder, TimeValue.timeValueMinutes(1));
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
        assertEquals(TimeValue.timeValueSeconds(5), elasticScroll.scrollTime.get());
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
    public void testScroll() throws IOException {
        SearchHit hit1 = mock(SearchHit.class);
        SearchHit hit2 = mock(SearchHit.class);
        SearchHit[] hits = new SearchHit[]{hit1, hit2};
        SearchResponse response = mock(SearchResponse.class);
        
        when(searchRequestBuilder.addSort(any(FieldSortBuilder.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setScroll(any(TimeValue.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.execute()).thenReturn(response);
        when(response.getScrollId()).thenReturn("scrollId");
        when(response.getHits()).thenReturn(mock(org.elasticsearch.search.SearchHits.class));
        when(response.getHits().getHits()).thenReturn(hits);
        
        elasticScroll.scroll(databaseScroll);
        
        verify(databaseScroll, times(1)).onStartBatch();
        verify(databaseScroll, times(1)).accept(hit1);
        verify(databaseScroll, times(1)).accept(hit2);
        verify(databaseScroll, times(1)).onEndBatch();
    }

    @Test
    public void testScrollEmptyHits() throws IOException {
        SearchResponse response = mock(SearchResponse.class);
        
        when(searchRequestBuilder.addSort(any(FieldSortBuilder.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.setScroll(any(TimeValue.class))).thenReturn(searchRequestBuilder);
        when(searchRequestBuilder.execute()).thenReturn(response);
        when(response.getScrollId()).thenReturn("scrollId");
        when(response.getHits()).thenReturn(mock(org.elasticsearch.search.SearchHits.class));
        when(response.getHits().getHits()).thenReturn(new SearchHit[0]);
        
        elasticScroll.scroll(databaseScroll);
        
        verify(databaseScroll, never()).accept(any());
    }
}
