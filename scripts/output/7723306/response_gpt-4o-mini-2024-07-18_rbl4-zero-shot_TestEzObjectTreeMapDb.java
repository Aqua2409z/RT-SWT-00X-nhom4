
package ezdb.treemap.object;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ezdb.comparator.ComparableComparator;
import ezdb.serde.Serde;
import ezdb.table.Table;
import ezdb.table.range.RangeTable;
import ezdb.treemap.object.table.ObjectTreeMapTable;
import ezdb.treemap.object.table.range.ObjectTreeMapRangeTable;

public class TestEzObjectTreeMapDb {
    private EzObjectTreeMapDb db;

    @Before
    public void setUp() {
        db = new EzObjectTreeMapDb();
    }

    @Test
    public void testDeleteTable() {
        String tableName = "testTable";
        Serde<String> hashKeySerde = mock(Serde.class);
        Serde<String> valueSerde = mock(Serde.class);
        
        db.getTable(tableName, hashKeySerde, valueSerde);
        db.deleteTable(tableName);
        
        assertNull(db.getTable(tableName, hashKeySerde, valueSerde));
    }

    @Test
    public void testGetTableCreatesNewTable() {
        String tableName = "testTable";
        Serde<String> hashKeySerde = mock(Serde.class);
        Serde<String> valueSerde = mock(Serde.class);
        
        Table<String, String> table = db.getTable(tableName, hashKeySerde, valueSerde);
        
        assertNotNull(table);
        assertTrue(table instanceof ObjectTreeMapTable);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetTableThrowsExceptionForWrongTableType() {
        String tableName = "testTable";
        Serde<String> hashKeySerde = mock(Serde.class);
        Serde<String> valueSerde = mock(Serde.class);
        
        // Create a mock table of a different type
        Table<?, ?> mockTable = mock(Table.class);
        db.cache.put(tableName, mockTable);
        
        db.getTable(tableName, hashKeySerde, valueSerde);
    }

    @Test
    public void testGetRangeTableCreatesNewRangeTable() {
        String tableName = "testRangeTable";
        Serde<String> hashKeySerde = mock(Serde.class);
        Serde<String> rangeKeySerde = mock(Serde.class);
        Serde<String> valueSerde = mock(Serde.class);
        
        RangeTable<String, String, String> rangeTable = db.getRangeTable(tableName, hashKeySerde, rangeKeySerde, valueSerde);
        
        assertNotNull(rangeTable);
        assertTrue(rangeTable instanceof ObjectTreeMapRangeTable);
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRangeTableThrowsExceptionForWrongRangeTableType() {
        String tableName = "testRangeTable";
        Serde<String> hashKeySerde = mock(Serde.class);
        Serde<String> rangeKeySerde = mock(Serde.class);
        Serde<String> valueSerde = mock(Serde.class);
        
        // Create a mock range table of a different type
        RangeTable<?, ?, ?> mockRangeTable = mock(RangeTable.class);
        db.cache.put(tableName, mockRangeTable);
        
        db.getRangeTable(tableName, hashKeySerde, rangeKeySerde, valueSerde);
    }
}
