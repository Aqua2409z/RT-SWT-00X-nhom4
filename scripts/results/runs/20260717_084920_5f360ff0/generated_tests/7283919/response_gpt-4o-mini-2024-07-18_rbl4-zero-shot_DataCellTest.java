
package dbfit.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class DataCellTest {

    @Test
    public void testCreateDataCellWithNonNullRow() {
        DataRow row = new DataRow(); // Assuming DataRow has a default constructor
        String columnName = "testColumn";
        DataCell cell = DataCell.createDataCell(row, columnName);
        assertNotNull(cell);
    }

    @Test
    public void testCreateDataCellWithNullRow() {
        DataCell cell = DataCell.createDataCell(null, "testColumn");
        assertNull(cell);
    }

    @Test
    public void testToString() {
        DataRow row = new DataRow();
        row.setValue("testColumn", "testValue"); // Assuming setValue method exists
        DataCell cell = new DataCell(row, "testColumn");
        assertEquals("testValue", cell.toString());
    }

    @Test
    public void testEqualToWithEqualCells() {
        DataRow row1 = new DataRow();
        row1.setValue("testColumn", "testValue");
        DataCell cell1 = new DataCell(row1, "testColumn");

        DataRow row2 = new DataRow();
        row2.setValue("testColumn", "testValue");
        DataCell cell2 = new DataCell(row2, "testColumn");

        assertTrue(cell1.equalTo(cell2));
    }

    @Test
    public void testEqualToWithDifferentCells() {
        DataRow row1 = new DataRow();
        row1.setValue("testColumn", "value1");
        DataCell cell1 = new DataCell(row1, "testColumn");

        DataRow row2 = new DataRow();
        row2.setValue("testColumn", "value2");
        DataCell cell2 = new DataCell(row2, "testColumn");

        assertFalse(cell1.equalTo(cell2));
    }

    @Test
    public void testEqualToWithNullCell() {
        DataRow row = new DataRow();
        DataCell cell = new DataCell(row, "testColumn");
        assertFalse(cell.equalTo(null));
    }

    @Test
    public void testEqualToWithSameInstance() {
        DataRow row = new DataRow();
        DataCell cell = new DataCell(row, "testColumn");
        assertTrue(cell.equalTo(cell));
    }
}
