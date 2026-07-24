package org.apache.calcite.avatica;

import org.apache.calcite.avatica.QueryState;
import org.apache.calcite.avatica.proto.Common;
import org.apache.calcite.avatica.remote.MetaDataOperation;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QueryState_RBL4_22b34a0aTest {

    private QueryState sqlQueryState;
    private QueryState metaDataQueryState;
    private MetaDataOperation metaDataOperationMock;
    private Object[] operationArgs;
    private Connection connectionMock;
    private Statement statementMock;
    private DatabaseMetaData databaseMetaDataMock;

    @Before
    public void setUp() {
        sqlQueryState = new QueryState("SELECT * FROM table");
        metaDataOperationMock = mock(MetaDataOperation.class);
        operationArgs = new Object[]{"arg1", "arg2", "arg3", "arg4"};
        metaDataQueryState = new QueryState(metaDataOperationMock, operationArgs);
        connectionMock = mock(Connection.class);
        statementMock = mock(Statement.class);
        databaseMetaDataMock = mock(DatabaseMetaData.class);
    }

    @Test
    public void testSqlQueryStateConstructor() {
        assertEquals(QueryState.StateType.SQL, sqlQueryState.getType());
        assertEquals("SELECT * FROM table", sqlQueryState.getSql());
        assertNull(sqlQueryState.getMetaDataOperation());
        assertNull(sqlQueryState.getOperationArgs());
    }

    @Test
    public void testMetaDataQueryStateConstructor() {
        assertEquals(QueryState.StateType.METADATA, metaDataQueryState.getType());
        assertNull(metaDataQueryState.getSql());
        assertEquals(metaDataOperationMock, metaDataQueryState.getMetaDataOperation());
        assertArrayEquals(operationArgs, metaDataQueryState.getOperationArgs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidStateTypeInConstructor() {
        new QueryState(null, "invalid sql", metaDataOperationMock, operationArgs);
    }

    @Test
    public void testInvokeSql() throws SQLException {
        when(statementMock.execute("SELECT * FROM table")).thenReturn(true);
        ResultSet resultSetMock = mock(ResultSet.class);
        when(statementMock.getResultSet()).thenReturn(resultSetMock);

        ResultSet resultSet = sqlQueryState.invoke(connectionMock, statementMock);
        assertNotNull(resultSet);
        verify(statementMock).execute("SELECT * FROM table");
    }

    @Test
    public void testInvokeMetaData() throws SQLException {
        when(connectionMock.getMetaData()).thenReturn(databaseMetaDataMock);
        when(databaseMetaDataMock.getAttributes("arg1", "arg2", "arg3", "arg4"))
                .thenReturn(mock(ResultSet.class));

        ResultSet resultSet = metaDataQueryState.invoke(connectionMock, statementMock);
        assertNotNull(resultSet);
        verify(databaseMetaDataMock).getAttributes("arg1", "arg2", "arg3", "arg4");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvokeInvalidType() {
        QueryState invalidState = new QueryState();
        invalidState.invoke(connectionMock, statementMock);
    }

    @Test
    public void testToProto() {
        Common.QueryState protoState = sqlQueryState.toProto();
        assertEquals(Common.StateType.SQL, protoState.getType());
        assertEquals("SELECT * FROM table", protoState.getSql());
        assertFalse(protoState.getHasOp());
        assertFalse(protoState.getHasArgs());
    }

    @Test
    public void testFromProto() {
        Common.QueryState protoState = sqlQueryState.toProto();
        QueryState reconstructedState = QueryState.fromProto(protoState);
        assertEquals(sqlQueryState, reconstructedState);
    }

    @Test
    public void testEqualsAndHashCode() {
        QueryState anotherSqlQueryState = new QueryState("SELECT * FROM table");
        assertTrue(sqlQueryState.equals(anotherSqlQueryState));
        assertEquals(sqlQueryState.hashCode(), anotherSqlQueryState.hashCode());
    }

    @Test
    public void testNotEquals() {
        QueryState differentSqlQueryState = new QueryState("SELECT * FROM another_table");
        assertFalse(sqlQueryState.equals(differentSqlQueryState));
    }
}
