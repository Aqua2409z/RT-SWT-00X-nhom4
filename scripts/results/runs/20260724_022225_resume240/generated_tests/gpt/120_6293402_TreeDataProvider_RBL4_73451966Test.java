package com.vaadin.data.provider;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.server.SerializableComparator;
import com.vaadin.server.SerializablePredicate;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class TreeDataProvider_RBL4_73451966Test {

    private TreeData<String> treeData;
    private TreeDataProvider<String> dataProvider;

    @Before
    public void setUp() {
        treeData = new TreeData<>();
        treeData.addItems(null, "Parent1", "Parent2");
        treeData.addItems("Parent1", "Child1", "Child2");
        treeData.addItems("Parent2", "Child3");
        dataProvider = new TreeDataProvider<>(treeData);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullTreeData() {
        new TreeDataProvider<>(null);
    }

    @Test
    public void testGetTreeData() {
        assertEquals(treeData, dataProvider.getTreeData());
    }

    @Test
    public void testHasChildren() {
        assertTrue(dataProvider.hasChildren("Parent1"));
        assertFalse(dataProvider.hasChildren("Child1"));
        assertFalse(dataProvider.hasChildren("NonExistent"));
    }

    @Test
    public void testGetChildCount() {
        HierarchicalQuery<String, SerializablePredicate<String>> query = new HierarchicalQuery<>("Parent1", 0, 10, null, null);
        assertEquals(2, dataProvider.getChildCount(query));
    }

    @Test
    public void testFetchChildren() {
        HierarchicalQuery<String, SerializablePredicate<String>> query = new HierarchicalQuery<>("Parent1", 0, 10, null, null);
        Stream<String> children = dataProvider.fetchChildren(query);
        assertEquals(Arrays.asList("Child1", "Child2"), children.collect(Collectors.toList()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFetchChildren_NonExistentParent() {
        HierarchicalQuery<String, SerializablePredicate<String>> query = new HierarchicalQuery<>("NonExistent", 0, 10, null, null);
        dataProvider.fetchChildren(query);
    }

    @Test
    public void testSetFilter() {
        SerializablePredicate<String> filter = item -> item.startsWith("Child");
        dataProvider.setFilter(filter);
        assertEquals(filter, dataProvider.getFilter());
    }

    @Test
    public void testSetSortComparator() {
        SerializableComparator<String> comparator = Comparator.naturalOrder();
        dataProvider.setSortComparator(comparator);
        assertEquals(comparator, dataProvider.getSortComparator());
    }

    @Test
    public void testWithConvertedFilter() {
        SerializableFunction<String, SerializablePredicate<String>> filterConverter = item -> (s) -> s.equals(item);
        TreeDataProvider<String> convertedProvider = dataProvider.withConvertedFilter(filterConverter);
        HierarchicalQuery<String, SerializablePredicate<String>> query = new HierarchicalQuery<>("Parent1", 0, 10, null, Optional.of("Child1"));
        assertEquals(1, convertedProvider.size(query));
    }
}
