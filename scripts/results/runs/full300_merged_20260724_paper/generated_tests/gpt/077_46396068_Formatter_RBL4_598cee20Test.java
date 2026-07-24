package com.kakao.hbase.stat.print;

import com.kakao.hbase.stat.load.Load;
import com.kakao.hbase.stat.print.Formatter;
import com.kakao.hbase.stat.print.PrintEntry;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class Formatter_RBL4_598cee20Test {
    private Load mockLoad;
    private Formatter formatter;

    @Before
    public void setUp() {
        mockLoad = new Load() {
            // Mock implementation of Load methods
            @Override
            public boolean toggleShowChangedOnly() {
                return true;
            }

            @Override
            public boolean toggleDiffFromStart() {
                return true;
            }

            @Override
            public void initializeTimestamp() {
                // Mock implementation
            }

            @Override
            public long getTimestampIteration() {
                return System.currentTimeMillis();
            }

            @Override
            public long getTotalDuration() {
                return 10;
            }

            @Override
            public boolean isDiffFromStart() {
                return true;
            }

            @Override
            public boolean isShowChangedOnly() {
                return true;
            }

            @Override
            public boolean isShowRate() {
                return true;
            }

            @Override
            public String getSortKeyInfo() {
                return "SortKeyInfo";
            }

            @Override
            public boolean isUpdating() {
                return false;
            }
        };
        formatter = new Formatter("TestTable", mockLoad);
    }

    @Test
    public void testToggleShowChangedOnly() {
        assertTrue(formatter.toggleShowChangedOnly());
    }

    @Test
    public void testToggleDiffFromStart() {
        assertTrue(formatter.toggleDiffFromStart());
    }

    @Test
    public void testBuildStringWithRunInformation() {
        String result = formatter.buildString(true, Formatter.Type.ANSI);
        assertNotNull(result);
        assertTrue(result.contains("TestTable"));
        assertTrue(result.contains("DiffFromStart: true"));
        assertTrue(result.contains("ShowChangedOnly: true"));
    }

    @Test
    public void testBuildStringWithoutRunInformation() {
        String result = formatter.buildString(false, Formatter.Type.ANSI);
        assertNotNull(result);
        assertFalse(result.contains("TestTable"));
    }

    @Test
    public void testToHtmlString() {
        String result = formatter.toHtmlString();
        assertNotNull(result);
        assertTrue(result.contains("SortKeyInfo"));
    }

    @Test
    public void testToString() {
        String result = formatter.toString();
        assertNotNull(result);
        assertTrue(result.contains("SortKeyInfo"));
    }
}
