
package org.springframework.data.simpledb.query;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.simpledb.core.SimpleDbOperations;

public class SimpleDbQueryRunner_RBL4Test_6778b5de {

    private SimpleDbOperations simpleDbOperations;
    private SimpleDbQueryRunner queryRunner;
    private Class<?> domainClass;
    private String query;
    private Pageable pageable;

    @Before
    public void setUp() {
        simpleDbOperations = mock(SimpleDbOperations.class);
        domainClass = Object.class; // Replace with actual domain class SimpleDbQueryRunner_RBL4Test_6778b5de needed
        query = "SELECT * FROM test";
        pageable = mock(Pageable.class);
        queryRunner = new SimpleDbQueryRunner(simpleDbOperations, domainClass, query);
    }

    @Test
    public void testExecuteQuery() {
        List<Object> expectedResults = Arrays.asList(new Object(), new Object());
        when(simpleDbOperations.find(domainClass, query)).thenReturn(expectedResults);

        List<?> results = queryRunner.executeQuery();

        assertEquals(expectedResults, results);
        verify(simpleDbOperations).find(domainClass, query);
    }

    @Test
    public void testExecuteSingleResultQueryWithSingleResult() {
        List<Object> expectedResults = Collections.singletonList(new Object());
        when(simpleDbOperations.find(domainClass, query)).thenReturn(expectedResults);

        Object result = queryRunner.executeSingleResultQuery();

        assertNotNull(result);
        verify(simpleDbOperations).find(domainClass, query);
    }

    @Test
    public void testExecuteSingleResultQueryWithNoResult() {
        when(simpleDbOperations.find(domainClass, query)).thenReturn(Collections.emptyList());

        Object result = queryRunner.executeSingleResultQuery();

        assertNull(result);
        verify(simpleDbOperations).find(domainClass, query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteSingleResultQueryWithMultipleResults() {
        List<Object> expectedResults = Arrays.asList(new Object(), new Object());
        when(simpleDbOperations.find(domainClass, query)).thenReturn(expectedResults);

        queryRunner.executeSingleResultQuery();
    }

    @Test
    public void testExecuteCount() {
        long expectedCount = 5;
        when(simpleDbOperations.count(query, domainClass)).thenReturn(expectedCount);

        long count = queryRunner.executeCount();

        assertEquals(expectedCount, count);
        verify(simpleDbOperations).count(query, domainClass);
    }

    @Test
    public void testGetRequestedQueryFieldNames() {
        List<String> expectedFieldNames = Arrays.asList("field1", "field2");
        when(QueryUtils.getQueryPartialFieldNames(query)).thenReturn(expectedFieldNames);

        List<String> fieldNames = queryRunner.getRequestedQueryFieldNames();

        assertEquals(expectedFieldNames, fieldNames);
    }

    @Test
    public void testGetSingleQueryFieldName() {
        List<String> expectedFieldNames = Collections.singletonList("field1");
        when(QueryUtils.getQueryPartialFieldNames(query)).thenReturn(expectedFieldNames);

        String fieldName = queryRunner.getSingleQueryFieldName();

        assertEquals("field1", fieldName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSingleQueryFieldNameWithMultipleFieldNames() {
        List<String> expectedFieldNames = Arrays.asList("field1", "field2");
        when(QueryUtils.getQueryPartialFieldNames(query)).thenReturn(expectedFieldNames);

        queryRunner.getSingleQueryFieldName();
    }

    @Test
    public void testExecutePagedQuery() {
        Page<Object> expectedPage = mock(Page.class);
        when(simpleDbOperations.executePagedQuery(domainClass, query, pageable)).thenReturn(expectedPage);
        queryRunner = new SimpleDbQueryRunner(simpleDbOperations, domainClass, query, pageable);

        Page<?> page = queryRunner.executePagedQuery();

        assertEquals(expectedPage, page);
        verify(simpleDbOperations).executePagedQuery(domainClass, query, pageable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPageable() {
        new SimpleDbQueryRunner(simpleDbOperations, domainClass, query, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativePageNumber() {
        when(pageable.getPageNumber()).thenReturn(-1);
        new SimpleDbQueryRunner(simpleDbOperations, domainClass, query, pageable);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithZeroPageSize() {
        when(pageable.getPageNumber()).thenReturn(0);
        when(pageable.getPageSize()).thenReturn(0);
        new SimpleDbQueryRunner(simpleDbOperations, domainClass, query, pageable);
    }
}
