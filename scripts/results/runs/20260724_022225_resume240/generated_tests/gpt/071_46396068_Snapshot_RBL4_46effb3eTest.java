package com.kakao.hbase.snapshot;

import com.kakao.hbase.SnapshotArgs;
import com.kakao.hbase.common.HBaseClient;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.SnapshotDescription;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class Snapshot_RBL4_46effb3eTest {
    private HBaseAdmin admin;
    private SnapshotArgs args;
    private Snapshot snapshot;

    @Before
    public void setUp() throws Exception {
        admin = mock(HBaseAdmin.class);
        args = mock(SnapshotArgs.class);
        snapshot = new Snapshot(admin, args);
    }

    @Test
    public void testRunWithExcludedTable() throws Exception {
        when(args.tableSet(admin)).thenReturn(List.of("excludedTable"));
        when(args.isExcluded("excludedTable")).thenReturn(true);

        snapshot.run();

        verify(admin, never()).snapshot(anyString(), anyString(), any());
    }

    @Test
    public void testRunWithSuccessfulSnapshot() throws Exception {
        when(args.tableSet(admin)).thenReturn(List.of("testTable"));
        when(args.isExcluded("testTable")).thenReturn(false);
        when(args.keepCount("testTable")).thenReturn(1);
        when(admin.tableExists("testTable")).thenReturn(true);
        when(admin.listSnapshots()).thenReturn(List.of());

        snapshot.run();

        verify(admin, times(1)).snapshot(anyString(), eq("testTable"), any());
    }

    @Test
    public void testRunWithFailedSnapshot() throws Exception {
        when(args.tableSet(admin)).thenReturn(List.of("testTable"));
        when(args.isExcluded("testTable")).thenReturn(false);
        when(admin.tableExists("testTable")).thenReturn(true);
        doThrow(new IOException("Snapshot failed")).when(admin).snapshot(anyString(), eq("testTable"), any());

        snapshot.run();

        verify(admin, times(1)).snapshot(anyString(), eq("testTable"), any());
    }

    @Test
    public void testDeleteOldSnapshots() throws IOException {
        when(args.keepCount("testTable")).thenReturn(1);
        SnapshotDescription snapshotDescription = mock(SnapshotDescription.class);
        when(snapshotDescription.getName()).thenReturn("testTable_Snapshot");
        when(snapshotDescription.getTable()).thenReturn("testTable");
        when(SnapshotAdapter.getSnapshotDescriptions(admin, "testTable_Snapshot\\d{14}$"))
                .thenReturn(List.of(snapshotDescription, snapshotDescription));

        snapshot.deleteOldSnapshots(admin, "testTable");

        verify(admin, times(1)).deleteSnapshot("testTable_Snapshot");
    }

    @Test
    public void testDeleteSnapshotsForNotExistingTables() throws IOException {
        when(args.has(anyString())).thenReturn(true);
        when(admin.listSnapshots()).thenReturn(List.of(mock(SnapshotDescription.class)));

        snapshot.deleteSnapshotsForNotExistingTables();

        verify(admin, times(1)).deleteSnapshot(anyString());
    }

    @Test
    public void testSnapshotExists() throws IOException {
        when(SnapshotAdapter.getSnapshotDescriptions(admin, "testSnapshot")).thenReturn(List.of());

        boolean exists = snapshot.exists(admin, "testSnapshot");

        assertFalse(exists);
    }

    @Test
    public void testSnapshotDoesNotExist() throws IOException {
        when(SnapshotAdapter.getSnapshotDescriptions(admin, "testSnapshot")).thenReturn(List.of(mock(SnapshotDescription.class)));

        boolean exists = snapshot.exists(admin, "testSnapshot");

        assertTrue(exists);
    }
}
