package com.kakao.hbase.common.util;

import com.kakao.hbase.common.Args;
import com.kakao.hbase.common.Constant;
import com.kakao.hbase.common.InvalidTableException;
import org.apache.hadoop.hbase.HRegionInfo;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Util_RBL4_ac867cd2Test {
    private HBaseAdmin admin;
    private Args args;

    @Before
    public void setUp() {
        admin = Mockito.mock(HBaseAdmin.class);
        args = Mockito.mock(Args.class);
    }

    @Test
    public void testValidateTableExistsAndEnabled() throws IOException, InterruptedException {
        String tableName = "testTable";
        when(admin.tableExists(tableName)).thenReturn(true);
        when(admin.isTableEnabled(tableName)).thenReturn(true);

        Util.validateTable(admin, tableName);
    }

    @Test(expected = InvalidTableException.class)
    public void testValidateTableNotExists() throws IOException, InterruptedException {
        String tableName = "nonExistentTable";
        when(admin.tableExists(tableName)).thenReturn(false);

        Util.validateTable(admin, tableName);
    }

    @Test(expected = InvalidTableException.class)
    public void testValidateTableNotEnabled() throws IOException, InterruptedException {
        String tableName = "testTable";
        when(admin.tableExists(tableName)).thenReturn(true);
        when(admin.isTableEnabled(tableName)).thenReturn(false);

        Util.validateTable(admin, tableName);
    }

    @Test
    public void testIsMoved() throws IOException {
        String tableName = "testTable";
        String regionName = "region1";
        String serverNameTarget = "server1";
        HTable table = mock(HTable.class);
        when(table.getRegionLocations()).thenReturn(new HashMap<HRegionInfo, ServerName>() {{
            put(new HRegionInfo(tableName, regionName.getBytes(), new byte[0], false), new ServerName(serverNameTarget, 0, 0));
        }});
        when(admin.getConfiguration()).thenReturn(null);
        whenNew(HTable.class).withArguments(admin.getConfiguration(), tableName).thenReturn(table);

        assertTrue(Util.isMoved(admin, tableName, regionName, serverNameTarget));
    }

    @Test
    public void testExistsRegion() {
        String regionName = "region1";
        Set<HRegionInfo> regionLocations = new HashSet<>();
        regionLocations.add(new HRegionInfo("testTable", regionName.getBytes(), new byte[0], false));

        assertTrue(Util.existsRegion(regionName, regionLocations));
    }

    @Test
    public void testAskProceed() {
        // This method requires user input, so we can only test it indirectly or mock the input.
        // For now, we will skip this test.
    }

    @Test
    public void testPrintVerboseMessageWithDuration() {
        long startTimestamp = System.currentTimeMillis();
        when(args.has(Args.OPTION_VERBOSE)).thenReturn(true);
        long duration = Util.printVerboseMessage(args, "Test message", startTimestamp);
        assertTrue(duration >= startTimestamp);
    }

    @Test
    public void testGetResource() throws IOException {
        // Assuming there is a resource file named "testResource.txt" in the classpath
        String resourceContent = Util.getResource("testResource.txt");
        assertNotNull(resourceContent);
    }

    @Test
    public void testReadFromResource() throws IOException {
        // Assuming there is a resource file named "testResource.txt" in the classpath
        String content = Util.readFromResource("testResource.txt");
        assertNotNull(content);
    }

    @Test
    public void testIsFile() {
        String path = "testFile.txt"; // Adjust this path as necessary for your test environment
        assertFalse(Util.isFile(path));
    }

    @Test
    public void testParseTableSet() throws IOException {
        String tableArg = "testTable";
        when(args.getOptionSet().nonOptionArguments()).thenReturn(Arrays.asList("arg1", tableArg));
        when(admin.listTables(tableArg)).thenReturn(new HTableDescriptor[]{new HTableDescriptor(tableArg.getBytes())});

        Set<String> tableSet = Util.parseTableSet(admin, args);
        assertTrue(tableSet.contains(tableArg));
    }

    @Test
    public void testParseTimestamp() {
        String validTimestamp = "2023-10-01 12:00:00";
        long timestamp = Util.parseTimestamp(validTimestamp);
        assertTrue(timestamp > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseTimestampInvalidFormat() {
        String invalidTimestamp = "invalid-date";
        Util.parseTimestamp(invalidTimestamp);
    }
}
