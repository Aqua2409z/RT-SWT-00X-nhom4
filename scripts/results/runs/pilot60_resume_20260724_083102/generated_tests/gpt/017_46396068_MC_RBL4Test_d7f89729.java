package com.kakao.hbase.manager.command;

import com.kakao.hbase.common.Args;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MC_RBL4Test_d7f89729 {
    private HBaseAdmin admin;
    private Args args;
    private MC mc;

    @Before
    public void setUp() {
        admin = Mockito.mock(HBaseAdmin.class);
        args = Mockito.mock(Args.class);
        when(args.getOptionSet()).thenReturn(Collections.singletonList("test").stream().collect(Collectors.toSet()));
        mc = new MC(admin, args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidArguments() {
        when(args.getOptionSet().nonOptionArguments()).thenReturn(Collections.singletonList("onlyOneArg").stream().collect(Collectors.toSet()));
        new MC(admin, args);
    }

    @Test
    public void testUsage() {
        String usage = MC.usage();
        assertNotNull(usage);
        assertTrue(usage.contains("Run major compaction on tables."));
    }

    @Test
    public void testRunWithTableLevel() throws Exception {
        when(args.has(Args.OPTION_REGION_SERVER)).thenReturn(false);
        when(args.has(Args.OPTION_LOCALITY_THRESHOLD)).thenReturn(false);
        when(args.has(Args.OPTION_CF)).thenReturn(false);
        when(args.isForceProceed()).thenReturn(true);
        when(Args.tables(args, admin)).thenReturn(new HashSet<>(Collections.singletonList("testTable")));

        mc.run();

        assertTrue(mc.isTableLevel());
        assertNotNull(mc.getTargets());
        assertFalse(mc.getTargets().isEmpty());
    }

    @Test
    public void testRunWithRegionLevel() throws Exception {
        when(args.has(Args.OPTION_REGION_SERVER)).thenReturn(true);
        when(args.has(Args.OPTION_LOCALITY_THRESHOLD)).thenReturn(false);
        when(args.has(Args.OPTION_CF)).thenReturn(false);
        when(args.isForceProceed()).thenReturn(true);
        when(Args.tables(args, admin)).thenReturn(new HashSet<>(Collections.singletonList("testTable")));

        mc.run();

        assertFalse(mc.isTableLevel());
        assertNotNull(mc.getTargets());
        assertFalse(mc.getTargets().isEmpty());
    }

    @Test
    public void testMcCounterIncrement() throws Exception {
        when(args.has(Args.OPTION_CF)).thenReturn(false);
        when(args.isForceProceed()).thenReturn(true);
        when(Args.tables(args, admin)).thenReturn(new HashSet<>(Collections.singletonList("testTable")));
        mc.run();
        int initialCounter = mc.getMcCounter();
        mc.run();
        assertEquals(initialCounter + 1, mc.getMcCounter());
    }

    @Test
    public void testGetRegionInfo() {
        byte[] regionName = "testRegion".getBytes();
        mc.regionTableMap.put(regionName, "testTable");
        mc.regionRSMap.put(regionName, "testRS");
        mc.regionLocalityMap.put(regionName, 0.5f);
        mc.regionSizeMap.put(regionName, 10);

        String regionInfo = mc.getRegionInfo(regionName);
        assertTrue(regionInfo.contains("Table: testTable"));
        assertTrue(regionInfo.contains("RS: testRS"));
        assertTrue(regionInfo.contains("Locality: 50.00%"));
        assertTrue(regionInfo.contains("SizeMB: 10"));
    }

    @Test
    public void testWaitUntilFinish() throws Exception {
        when(args.has(Args.OPTION_WAIT_UNTIL_FINISH)).thenReturn(true);
        when(Args.tables(args, admin)).thenReturn(new HashSet<>(Collections.singletonList("testTable")));
        mc.waitUntilFinish(Args.tables(args, admin));
        // Verify that the method completes without exceptions
    }

    @Test
    public void testFilterWithLocalityOnly() throws IOException {
        when(args.has(Args.OPTION_LOCALITY_THRESHOLD)).thenReturn(true);
        when(args.valueOf(Args.OPTION_LOCALITY_THRESHOLD)).thenReturn(50.0);
        mc.filterWithLocalityOnly(new HashSet<>(), "testTable");
        // Verify that the method completes without exceptions
    }

    @Test
    public void testFilterWithRsAndLocality() throws IOException {
        when(args.has(Args.OPTION_REGION_SERVER)).thenReturn(true);
        when(args.valueOf(Args.OPTION_REGION_SERVER)).thenReturn("testRS");
        mc.filterWithRsAndLocality(new HashSet<>(), "testTable");
        // Verify that the method completes without exceptions
    }
}
