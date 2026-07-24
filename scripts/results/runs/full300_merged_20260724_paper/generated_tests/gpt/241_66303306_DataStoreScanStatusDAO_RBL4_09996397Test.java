
package com.bazaarvoice.emodb.web.scanner.scanstatus;

import com.bazaarvoice.emodb.sor.api.DataStore;
import com.bazaarvoice.emodb.sor.api.TableOptionsBuilder;
import com.bazaarvoice.emodb.sor.condition.Conditions;
import com.bazaarvoice.emodb.sor.delta.Deltas;
import com.bazaarvoice.emodb.sor.db.ScanRange;
import com.google.common.collect.ImmutableMap;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class DataStoreScanStatusDAO_RBL4_09996397Test {

    private DataStore dataStore;
    private DataStoreScanStatusDAO dao;

    @BeforeMethod
    public void setUp() {
        dataStore = mock(DataStore.class);
        dao = new DataStoreScanStatusDAO(dataStore, "scanStatusTable", "scanStatusPlacement");
    }

    @Test
    public void testGetTableCreatesTableIfNotExists() {
        when(dataStore.getTableExists("scanStatusTable")).thenReturn(false);

        dao.getTable();

        verify(dataStore).createTable(eq("scanStatusTable"), any(TableOptionsBuilder.class), anyMap(), any());
    }

    @Test
    public void testGetTableDoesNotCreateTableIfExists() {
        when(dataStore.getTableExists("scanStatusTable")).thenReturn(true);

        dao.getTable();

        verify(dataStore, never()).createTable(anyString(), any(), anyMap(), any());
    }

    @Test
    public void testListReturnsScanStatus() {
        Map<String, Object> mockMap = ImmutableMap.of("canceled", false, "options", new ScanOptions(), "ranges", ImmutableMap.of());
        when(dataStore.scan(anyString(), anyString(), anyLong(), anyBoolean(), any())).thenReturn(List.of(mockMap).iterator());

        Iterator<ScanStatus> result = dao.list(null, 10);

        assert result.hasNext();
        ScanStatus status = result.next();
        assert status != null;
    }

    @Test
    public void testUpdateScanStatus() {
        ScanStatus status = new ScanStatus("scanId", new ScanOptions(), false, false, new Date(), List.of(), List.of(), List.of(), null);
        dao.updateScanStatus(status);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        verify(dataStore).update(eq("scanStatusTable"), eq("scanId"), any(), captor.capture(), any());
        Map<String, Object> capturedMap = captor.getValue();
        assert capturedMap.containsKey("options");
        assert capturedMap.containsKey("ranges");
    }

    @Test
    public void testGetScanStatusReturnsNullForDeleted() {
        Map<String, Object> mockMap = ImmutableMap.of("canceled", true);
        when(dataStore.get(anyString(), anyString())).thenReturn(mockMap);

        ScanStatus result = dao.getScanStatus("scanId");

        assert result == null;
    }

    @Test
    public void testSetScanRangeTaskQueued() {
        Date queuedTime = new Date();
        dao.setScanRangeTaskQueued("scanId", 1, queuedTime);

        verify(dataStore).update(eq("scanStatusTable"), eq("scanId"), any(), any(), any());
    }

    @Test
    public void testSetScanRangeTaskActive() {
        Date startTime = new Date();
        dao.setScanRangeTaskActive("scanId", 1, startTime);

        verify(dataStore).update(eq("scanStatusTable"), eq("scanId"), any(), any(), any());
    }

    @Test
    public void testSetScanRangeTaskComplete() {
        Date completeTime = new Date();
        dao.setScanRangeTaskComplete("scanId", 1, completeTime);

        verify(dataStore).update(eq("scanStatusTable"), eq("scanId"), any(), any(), any());
    }

    @Test
    public void testSetCanceled() {
        dao.setCanceled("scanId");

        verify(dataStore).update(eq("scanStatusTable"), eq("scanId"), any(), any(), any());
    }

    @Test
    public void testSetTableSnapshotCreated() {
        dao.setTableSnapshotCreated("scanId");

        verify(dataStore).update(eq("scanStatusTable"), eq("scanId"), any(), any(), any());
    }
}
