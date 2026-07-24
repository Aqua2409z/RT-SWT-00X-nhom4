
package com.spotify.flo.contrib.bigquery;

import com.google.cloud.bigquery.DatasetId;
import com.google.cloud.bigquery.DatasetInfo;
import com.google.cloud.bigquery.TableId;
import com.spotify.flo.EvalContext;
import com.spotify.flo.Task;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BigQueryOutput_RBL4_ea016342Test {

    private FloBigQueryClient mockBigQueryClient;
    private BigQueryOutput bigQueryOutput;
    private TableId tableId;
    private EvalContext evalContext;

    @Before
    public void setUp() {
        mockBigQueryClient = Mockito.mock(FloBigQueryClient.class);
        tableId = TableId.of("project", "dataset", "table");
        bigQueryOutput = new BigQueryOutput(() -> mockBigQueryClient, tableId);
        evalContext = Mockito.mock(EvalContext.class);
    }

    @Test
    public void testCreateWithTableId() {
        BigQueryOutput output = BigQueryOutput.create(tableId);
        assertNotNull(output);
        assertEquals(tableId, output.tableId());
    }

    @Test
    public void testCreateWithProjectDatasetTable() {
        BigQueryOutput output = BigQueryOutput.create("project", "dataset", "table");
        assertNotNull(output);
        assertEquals(tableId, output.tableId());
    }

    @Test
    public void testProvideCreatesStagingDataset() {
        DatasetInfo datasetInfo = DatasetInfo.newBuilder(DatasetId.of("project", "dataset")).setLocation("US").build();
        when(mockBigQueryClient.getDataset(any(DatasetId.class))).thenReturn(datasetInfo);
        when(mockBigQueryClient.createStagingTableId(any(TableId.class), anyString())).thenReturn(TableId.of("project", "staging_dataset", "staging_table"));
        when(mockBigQueryClient.getDataset(any(DatasetId.class))).thenReturn(null);

        StagingTableId stagingTableId = bigQueryOutput.provide(evalContext);
        assertNotNull(stagingTableId);
        verify(mockBigQueryClient).create(any(DatasetInfo.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDatasetOrThrowThrowsException() {
        when(mockBigQueryClient.getDataset(any(DatasetId.class))).thenReturn(null);
        bigQueryOutput.provide(evalContext);
    }

    @Test
    public void testLookupReturnsEmptyIfTableDoesNotExist() {
        when(mockBigQueryClient.getDataset(any(DatasetId.class))).thenReturn(DatasetInfo.newBuilder(DatasetId.of("project", "dataset")).setLocation("US").build());
        when(mockBigQueryClient.tableExists(any(TableId.class))).thenReturn(false);

        Optional<TableId> result = bigQueryOutput.lookup(Mockito.mock(Task.class));
        assertFalse(result.isPresent());
    }

    @Test
    public void testLookupReturnsTableIdIfTableExists() {
        when(mockBigQueryClient.getDataset(any(DatasetId.class))).thenReturn(DatasetInfo.newBuilder(DatasetId.of("project", "dataset")).setLocation("US").build());
        when(mockBigQueryClient.tableExists(any(TableId.class))).thenReturn(true);

        Optional<TableId> result = bigQueryOutput.lookup(Mockito.mock(Task.class));
        assertTrue(result.isPresent());
        assertEquals(tableId, result.get());
    }

    @Test
    public void testPublishCallsPublishMethod() {
        StagingTableId stagingTableId = StagingTableId.of(bigQueryOutput, TableId.of("project", "staging_dataset", "staging_table"));
        bigQueryOutput.publish(stagingTableId);
        verify(mockBigQueryClient).publish(stagingTableId, tableId);
    }
}
