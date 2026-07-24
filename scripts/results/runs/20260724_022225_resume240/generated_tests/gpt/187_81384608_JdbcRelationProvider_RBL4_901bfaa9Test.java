
package com.thinkbiganalytics.kylo.catalog.spark.sources.jdbc;

import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.sources.BaseRelation;
import org.apache.spark.sql.sources.DataSourceRegister;
import org.apache.spark.sql.sources.RelationProvider;
import org.apache.spark.util.Utils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import scala.collection.immutable.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JdbcRelationProvider_RBL4_901bfaa9Test {

    private JdbcRelationProvider jdbcRelationProvider;
    private SQLContext sqlContext;
    private Map<String, String> parameters;

    @Before
    public void setUp() {
        jdbcRelationProvider = new JdbcRelationProvider();
        sqlContext = mock(SQLContext.class);
        parameters = mock(Map.class);
    }

    @Test
    public void testCreateRelation_Success() {
        // Arrange
        RelationProvider mockProvider = mock(RelationProvider.class);
        BaseRelation mockRelation = mock(BaseRelation.class);
        when(mockProvider.createRelation(sqlContext, parameters)).thenReturn(mockRelation);
        
        // Mock the ServiceLoader to return our mockProvider
        ServiceLoader<DataSourceRegister> mockServiceLoader = mock(ServiceLoader.class);
        when(mockServiceLoader.iterator()).thenReturn(java.util.Collections.singletonList(mockProvider).iterator());
        Utils.setServiceLoader(mockServiceLoader);

        // Act
        BaseRelation result = jdbcRelationProvider.createRelation(sqlContext, parameters);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof JdbcRelation);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateRelation_NoProviderFound() {
        // Arrange
        // Mock the ServiceLoader to return an empty iterator
        ServiceLoader<DataSourceRegister> mockServiceLoader = mock(ServiceLoader.class);
        when(mockServiceLoader.iterator()).thenReturn(java.util.Collections.emptyList().iterator());
        Utils.setServiceLoader(mockServiceLoader);

        // Act
        jdbcRelationProvider.createRelation(sqlContext, parameters);
    }

    @Test
    public void testFindProvider_Found() {
        // Arrange
        DataSourceRegister mockRegister = mock(DataSourceRegister.class);
        when(mockRegister.shortName()).thenReturn("jdbc");
        when(mockRegister instanceof RelationProvider).thenReturn(true);
        
        ServiceLoader<DataSourceRegister> mockServiceLoader = mock(ServiceLoader.class);
        when(mockServiceLoader.iterator()).thenReturn(java.util.Collections.singletonList(mockRegister).iterator());
        Utils.setServiceLoader(mockServiceLoader);

        // Act
        RelationProvider result = jdbcRelationProvider.findProvider();

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof RelationProvider);
    }

    @Test
    public void testFindProvider_NotFound() {
        // Arrange
        ServiceLoader<DataSourceRegister> mockServiceLoader = mock(ServiceLoader.class);
        when(mockServiceLoader.iterator()).thenReturn(java.util.Collections.emptyList().iterator());
        Utils.setServiceLoader(mockServiceLoader);

        // Act
        RelationProvider result = jdbcRelationProvider.findProvider();

        // Assert
        assertNull(result);
    }
}
