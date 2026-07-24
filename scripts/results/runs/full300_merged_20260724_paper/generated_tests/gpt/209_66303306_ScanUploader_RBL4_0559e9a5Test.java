
package com.bazaarvoice.emodb.web.scanner;

import com.bazaarvoice.emodb.datacenter.api.DataCenters;
import com.bazaarvoice.emodb.plugin.stash.StashStateListener;
import com.bazaarvoice.emodb.sor.api.CompactionControlSource;
import com.bazaarvoice.emodb.sor.core.DataTools;
import com.bazaarvoice.emodb.web.scanner.control.ScanWorkflow;
import com.bazaarvoice.emodb.web.scanner.scanstatus.ScanStatus;
import com.bazaarvoice.emodb.web.scanner.scanstatus.ScanStatusDAO;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;

public class ScanUploader_RBL4_0559e9a5Test {

    private DataTools dataTools;
    private ScanWorkflow scanWorkflow;
    private ScanStatusDAO scanStatusDAO;
    private StashStateListener stashStateListener;
    private CompactionControlSource compactionControlSource;
    private DataCenters dataCenters;
    private ScanUploader scanUploader;

    @BeforeMethod
    public void setUp() {
        dataTools = mock(DataTools.class);
        scanWorkflow = mock(ScanWorkflow.class);
        scanStatusDAO = mock(ScanStatusDAO.class);
        stashStateListener = mock(StashStateListener.class);
        compactionControlSource = mock(CompactionControlSource.class);
        dataCenters = mock(DataCenters.class);
        scanUploader = new ScanUploader(dataTools, scanWorkflow, scanStatusDAO, stashStateListener, compactionControlSource, dataCenters);
    }

    @Test
    public void testScanAndUpload() {
        String scanId = "scan1";
        ScanOptions options = mock(ScanOptions.class);
        when(options.getPlacements()).thenReturn(Collections.singleton("placement1"));
        when(options.getRangeScanSplitSize()).thenReturn(100);
        when(options.isTemporalEnabled()).thenReturn(false);

        ScanStatus status = scanUploader.scanAndUpload(scanId, options).start();

        Assert.assertNotNull(status);
        Assert.assertEquals(status.getScanId(), scanId);
        verify(scanWorkflow).scanStatusUpdated(scanId);
        verify(stashStateListener).stashStarted(any());
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testStartFromUnknownScan() {
        String scanId = "scan2";
        ScanOptions options = mock(ScanOptions.class);
        when(options.getPlacements()).thenReturn(Collections.singleton("placement1"));
        when(options.getRangeScanSplitSize()).thenReturn(100);
        when(options.isTemporalEnabled()).thenReturn(false);

        scanUploader.scanAndUpload(scanId, options).usePlanFromStashId("unknownId").start();
    }

    @Test
    public void testResubmitWorkflowTasks() {
        String scanId = "scan3";
        ScanStatus status = mock(ScanStatus.class);
        when(scanStatusDAO.getScanStatus(scanId)).thenReturn(status);
        when(status.getCompleteTime()).thenReturn(null);
        when(status.getActiveScanRanges()).thenReturn(Collections.emptyList());

        scanUploader.resubmitWorkflowTasks(scanId);

        verify(scanWorkflow).scanStatusUpdated(scanId);
    }

    @Test
    public void testCancel() {
        String scanId = "scan4";
        scanUploader.cancel(scanId);

        verify(scanStatusDAO).setCanceled(scanId);
        verify(scanWorkflow).scanStatusUpdated(scanId);
        verify(compactionControlSource).deleteStashTime(scanId, dataCenters.getSelf().getName());
    }

    @Test
    public void testDoExceptionTasks() {
        String scanId = "scan5";
        Exception exception = new Exception("Test Exception");

        scanUploader.doExceptionTasks(scanId, exception);

        verify(scanStatusDAO).setCanceled(scanId);
        verify(compactionControlSource).deleteStashTime(scanId, dataCenters.getSelf().getName());
    }
}
