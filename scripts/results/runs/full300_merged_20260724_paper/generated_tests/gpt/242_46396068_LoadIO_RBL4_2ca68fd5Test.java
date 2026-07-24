package com.kakao.hbase.stat.load;

import com.kakao.hbase.common.Args;
import com.kakao.hbase.common.LoadEntry;
import com.kakao.hbase.stat.load.Load;
import com.kakao.hbase.stat.load.LoadIO;
import com.kakao.hbase.stat.load.LoadRecord;
import com.kakao.hbase.specific.Level;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LoadIO_RBL4_2ca68fd5Test {
    private Load load;
    private LoadIO loadIO;
    private Args args;

    @Before
    public void setUp() {
        load = Mockito.mock(Load.class);
        loadIO = new LoadIO(load);
        args = Mockito.mock(Args.class);
    }

    @Test
    public void testSave() {
        Mockito.when(args.getZookeeperQuorum()).thenReturn("localhost");
        Mockito.when(args.getTableName()).thenReturn("testTable");
        Mockito.when(load.getTimestampIteration()).thenReturn(System.currentTimeMillis());

        String result = loadIO.save(args);
        assertTrue(result.contains("is saved."));
    }

    @Test
    public void testSaveOutput() throws IOException {
        Mockito.when(args.has(Args.OPTION_OUTPUT)).thenReturn(true);
        Mockito.when(args.valueOf(Args.OPTION_OUTPUT)).thenReturn("output.csv");

        FileWriter writer = new FileWriter("output.csv");
        writer.write("Test data");
        writer.close();

        loadIO.saveOutput(args);
        File file = new File("output.csv");
        assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testLoad() throws IOException {
        Mockito.when(args.getZookeeperQuorum()).thenReturn("localhost");
        Mockito.when(args.getTableName()).thenReturn("testTable");
        Mockito.when(load.getTimestampIteration()).thenReturn(System.currentTimeMillis());

        // Create a sample CSV file for loading
        String fileName = "stat_saved/localhost_testTable_20230101000000000.csv";
        File dir = new File("stat_saved");
        dir.mkdirs();
        FileWriter writer = new FileWriter(fileName);
        writer.write("Level,Timestamp,LoadEntry1,LoadEntry2\n");
        writer.write("Level1,1672537600000,100,200\n");
        writer.close();

        loadIO.load(args, "0");
        assertTrue(new File(fileName).exists());
        new File(fileName).delete();
        dir.delete();
    }

    @Test
    public void testShowSavedFiles() {
        Mockito.when(args.getZookeeperQuorum()).thenReturn("localhost");
        Mockito.when(args.getTableName()).thenReturn("testTable");

        String result = loadIO.showSavedFiles(args);
        assertEquals("There is no saved file.\n", result);
    }

    @Test
    public void testGetSavedFileName() {
        String result = loadIO.getSavedFileName("0");
        assertEquals(null, result);
    }

    @Test
    public void testFilename() {
        Mockito.when(args.getZookeeperQuorum()).thenReturn("localhost");
        Mockito.when(args.getTableName()).thenReturn("testTable");
        Mockito.when(load.getTimestampIteration()).thenReturn(System.currentTimeMillis());

        String fileName = loadIO.filename(args);
        assertTrue(fileName.startsWith("stat_saved/localhost_testTable_"));
        assertTrue(fileName.endsWith(".csv"));
    }
}
