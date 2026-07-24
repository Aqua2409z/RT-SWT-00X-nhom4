
package org.springframework.data.simpledb.query;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.repository.query.QueryMethod;
import org.springframework.data.simpledb.core.SimpleDbOperations;
import org.springframework.data.simpledb.query.executions.AbstractSimpleDbQueryExecution;
import org.springframework.data.simpledb.query.executions.MultipleResultExecution;
import org.springframework.data.simpledb.query.executions.PagedResultExecution;
import org.springframework.data.simpledb.query.executions.SingleResultExecution;

public class SimpleDbRepositoryQuery_RBL4_ec1e9b28Test {

    private SimpleDbQueryMethod mockMethod;
    private SimpleDbOperations mockSimpleDbOperations;
    private SimpleDbRepositoryQuery repositoryQuery;

    @Before
    public void setUp() {
        mockMethod = mock(SimpleDbQueryMethod.class);
        mockSimpleDbOperations = mock(SimpleDbOperations.class);
        repositoryQuery = new SimpleDbRepositoryQuery(mockMethod, mockSimpleDbOperations);
    }

    @Test
    public void testExecuteWithPagedQuery() {
        when(mockMethod.isPagedQuery()).thenReturn(true);
        when(mockMethod.getAnnotatedQuery()).thenReturn("SELECT * FROM test");

        AbstractSimpleDbQueryExecution execution = repositoryQuery.getExecution();
        assertTrue(execution instanceof PagedResultExecution);
    }

    @Test
    public void testExecuteWithCollectionQuery() {
        when(mockMethod.isCollectionQuery()).thenReturn(true);
        when(mockMethod.getAnnotatedQuery()).thenReturn("SELECT * FROM test");

        AbstractSimpleDbQueryExecution execution = repositoryQuery.getExecution();
        assertTrue(execution instanceof MultipleResultExecution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testExecuteWithModifyingQuery() {
        when(mockMethod.isModifyingQuery()).thenReturn(true);
        repositoryQuery.getExecution();
    }

    @Test
    public void testExecuteWithSingleResultQuery() {
        when(mockMethod.isCollectionQuery()).thenReturn(false);
        when(mockMethod.isPagedQuery()).thenReturn(false);
        when(mockMethod.getAnnotatedQuery()).thenReturn("SELECT * FROM test");

        AbstractSimpleDbQueryExecution execution = repositoryQuery.getExecution();
        assertTrue(execution instanceof SingleResultExecution);
    }

    @Test
    public void testFromQueryAnnotationWithAnnotatedQuery() {
        when(mockMethod.getAnnotatedQuery()).thenReturn("SELECT * FROM test");
        RepositoryQuery query = SimpleDbRepositoryQuery.fromQueryAnnotation(mockMethod, mockSimpleDbOperations);
        assertNotNull(query);
    }

    @Test
    public void testFromQueryAnnotationWithoutAnnotatedQuery() {
        when(mockMethod.getAnnotatedQuery()).thenReturn(null);
        RepositoryQuery query = SimpleDbRepositoryQuery.fromQueryAnnotation(mockMethod, mockSimpleDbOperations);
        assertNull(query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAssertNotHavingNestedQueryParametersWithNestedField() throws NoSuchFieldException {
        when(mockMethod.getAnnotatedQuery()).thenReturn("SELECT * FROM test WHERE nestedField = :nestedField");
        when(mockMethod.getDomainClazz()).thenReturn(TestDomainClass.class);
        repositoryQuery.assertNotHavingNestedQueryParameters("SELECT * FROM test WHERE nestedField = :nestedField");
    }

    @Test
    public void testAssertNotHavingNestedQueryParametersWithValidField() throws NoSuchFieldException {
        when(mockMethod.getAnnotatedQuery()).thenReturn("SELECT * FROM test WHERE validField = :validField");
        when(mockMethod.getDomainClazz()).thenReturn(TestDomainClass.class);
        repositoryQuery.assertNotHavingNestedQueryParameters("SELECT * FROM test WHERE validField = :validField");
    }

    private static class SimpleDbRepositoryQuery_RBL4_ec1e9b28Test {
        public String validField;
        public NestedClass nestedField;

        private static class SimpleDbRepositoryQuery_RBL4_ec1e9b28Test {
            public String nestedField;
        }
    }
}
