package org.apache.calcite.avatica;

import org.apache.calcite.avatica.ConnectionPropertiesImpl;
import org.apache.calcite.avatica.Meta;
import org.apache.calcite.avatica.proto.Common;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ConnectionPropertiesImpl_RBL4_5c0f04f0Test {
    private ConnectionPropertiesImpl connectionProperties;

    @Before
    public void setUp() {
        connectionProperties = new ConnectionPropertiesImpl();
    }

    @Test
    public void testDefaultConstructor() {
        assertTrue(connectionProperties.isEmpty());
        assertFalse(connectionProperties.isDirty());
    }

    @Test
    public void testParameterizedConstructor() {
        Connection mockConnection = new MockConnection(true, false, 1, "catalog", "schema");
        ConnectionPropertiesImpl properties = new ConnectionPropertiesImpl(mockConnection);
        assertFalse(properties.isEmpty());
        assertEquals(Boolean.TRUE, properties.isAutoCommit());
        assertEquals(Boolean.FALSE, properties.isReadOnly());
        assertEquals(Integer.valueOf(1), properties.getTransactionIsolation());
        assertEquals("catalog", properties.getCatalog());
        assertEquals("schema", properties.getSchema());
    }

    @Test
    public void testSetDirty() {
        connectionProperties.setDirty(true);
        assertTrue(connectionProperties.isDirty());
    }

    @Test
    public void testMerge() {
        ConnectionPropertiesImpl other = new ConnectionPropertiesImpl(true, false, 2, "catalog2", "schema2");
        connectionProperties.merge(other);
        assertTrue(connectionProperties.isDirty());
        assertEquals(Boolean.TRUE, connectionProperties.isAutoCommit());
        assertEquals(Boolean.FALSE, connectionProperties.isReadOnly());
        assertEquals(Integer.valueOf(2), connectionProperties.getTransactionIsolation());
        assertEquals("catalog2", connectionProperties.getCatalog());
        assertEquals("schema2", connectionProperties.getSchema());
    }

    @Test
    public void testSetAutoCommit() {
        connectionProperties.setAutoCommit(false);
        assertEquals(Boolean.FALSE, connectionProperties.isAutoCommit());
        assertTrue(connectionProperties.isDirty());
    }

    @Test
    public void testSetReadOnly() {
        connectionProperties.setReadOnly(true);
        assertEquals(Boolean.TRUE, connectionProperties.isReadOnly());
        assertTrue(connectionProperties.isDirty());
    }

    @Test
    public void testSetTransactionIsolation() {
        connectionProperties.setTransactionIsolation(3);
        assertEquals(Integer.valueOf(3), connectionProperties.getTransactionIsolation());
        assertTrue(connectionProperties.isDirty());
    }

    @Test
    public void testSetCatalog() {
        connectionProperties.setCatalog("newCatalog");
        assertEquals("newCatalog", connectionProperties.getCatalog());
        assertTrue(connectionProperties.isDirty());
    }

    @Test
    public void testSetSchema() {
        connectionProperties.setSchema("newSchema");
        assertEquals("newSchema", connectionProperties.getSchema());
        assertTrue(connectionProperties.isDirty());
    }

    @Test
    public void testEqualsAndHashCode() {
        ConnectionPropertiesImpl other = new ConnectionPropertiesImpl(true, false, 1, "catalog", "schema");
        connectionProperties.merge(other);
        assertEquals(connectionProperties, other);
        assertEquals(connectionProperties.hashCode(), other.hashCode());
    }

    @Test
    public void testToProto() {
        connectionProperties.setAutoCommit(true);
        connectionProperties.setReadOnly(false);
        connectionProperties.setTransactionIsolation(1);
        connectionProperties.setCatalog("catalog");
        connectionProperties.setSchema("schema");
        Common.ConnectionProperties proto = connectionProperties.toProto();
        assertTrue(proto.getHasAutoCommit());
        assertFalse(proto.getHasReadOnly());
        assertEquals(1, proto.getTransactionIsolation());
        assertEquals("catalog", proto.getCatalog());
        assertEquals("schema", proto.getSchema());
    }

    @Test
    public void testFromProto() {
        Common.ConnectionProperties proto = Common.ConnectionProperties.newBuilder()
                .setHasAutoCommit(true)
                .setAutoCommit(true)
                .setHasReadOnly(false)
                .setReadOnly(false)
                .setTransactionIsolation(1)
                .setCatalog("catalog")
                .setSchema("schema")
                .setIsDirty(true)
                .build();
        ConnectionPropertiesImpl fromProto = ConnectionPropertiesImpl.fromProto(proto);
        assertEquals(Boolean.TRUE, fromProto.isAutoCommit());
        assertEquals(Boolean.FALSE, fromProto.isReadOnly());
        assertEquals(Integer.valueOf(1), fromProto.getTransactionIsolation());
        assertEquals("catalog", fromProto.getCatalog());
        assertEquals("schema", fromProto.getSchema());
        assertTrue(fromProto.isDirty());
    }

    private static class ConnectionPropertiesImpl_RBL4_5c0f04f0Test extends Connection {
        private final boolean autoCommit;
        private final boolean readOnly;
        private final int transactionIsolation;
        private final String catalog;
        private final String schema;

        public MockConnection(boolean autoCommit, boolean readOnly, int transactionIsolation, String catalog, String schema) {
            this.autoCommit = autoCommit;
            this.readOnly = readOnly;
            this.transactionIsolation = transactionIsolation;
            this.catalog = catalog;
            this.schema = schema;
        }

        @Override
        public boolean getAutoCommit() throws SQLException {
            return autoCommit;
        }

        @Override
        public boolean isReadOnly() throws SQLException {
            return readOnly;
        }

        @Override
        public int getTransactionIsolation() throws SQLException {
            return transactionIsolation;
        }

        @Override
        public String getCatalog() throws SQLException {
            return catalog;
        }

        @Override
        public String getSchema() throws SQLException {
            return schema;
        }

        // Other methods from Connection interface would need to be implemented or stubbed
    }
}
