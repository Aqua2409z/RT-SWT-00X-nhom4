package com.vaadin.data.provider;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.data.provider.Query;
import com.vaadin.server.SerializableComparator;
import com.vaadin.server.SerializablePredicate;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ListDataProvider_RBL4Test_d31191e9 {

    private ListDataProvider<String> dataProvider;
    private List<String> items;

    @Before
    public void setUp() {
        items = new ArrayList<>();
        items.add("apple");
        items.add("banana");
        items.add("cherry");
        dataProvider = new ListDataProvider<>(items);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullCollection() {
        new ListDataProvider<>(null);
    }

    @Test
    public void testGetItems() {
        Collection<String> result = dataProvider.getItems();
        assertEquals(items, result);
    }

    @Test
    public void testFetch_NoFilterOrSort() {
        Query<String, SerializablePredicate<String>> query = new Query<>(0, 10, null, Optional.empty());
        List<String> result = dataProvider.fetch(query).toList();
        assertEquals(items, result);
    }

    @Test
    public void testFetch_WithFilter() {
        SerializablePredicate<String> filter = item -> item.startsWith("b");
        Query<String, SerializablePredicate<String>> query = new Query<>(0, 10, null, Optional.of(filter));
        dataProvider.setFilter(filter);
        List<String> result = dataProvider.fetch(query).toList();
        assertEquals(1, result.size());
        assertTrue(result.contains("banana"));
    }

    @Test
    public void testFetch_WithSorting() {
        SerializableComparator<String> comparator = Comparator.naturalOrder();
        dataProvider.setSortComparator(comparator);
        Query<String, SerializablePredicate<String>> query = new Query<>(0, 10, comparator, Optional.empty());
        List<String> result = dataProvider.fetch(query).toList();
        assertEquals(items, result);
    }

    @Test
    public void testSize_NoFilter() {
        Query<String, SerializablePredicate<String>> query = new Query<>(0, 10, null, Optional.empty());
        int size = dataProvider.size(query);
        assertEquals(items.size(), size);
    }

    @Test
    public void testSize_WithFilter() {
        SerializablePredicate<String> filter = item -> item.startsWith("c");
        Query<String, SerializablePredicate<String>> query = new Query<>(0, 10, null, Optional.of(filter));
        dataProvider.setFilter(filter);
        int size = dataProvider.size(query);
        assertEquals(1, size);
    }

    @Test
    public void testSetFilter() {
        SerializablePredicate<String> filter = item -> item.length() > 5;
        dataProvider.setFilter(filter);
        assertEquals(filter, dataProvider.getFilter());
    }

    @Test
    public void testSetSortComparator() {
        SerializableComparator<String> comparator = Comparator.reverseOrder();
        dataProvider.setSortComparator(comparator);
        assertEquals(comparator, dataProvider.getSortComparator());
    }
}
