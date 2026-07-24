
package com.yahoo.parsec.clients;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ParsecAsyncProgressTimer_RBL4_9551429bTest {

    private ParsecAsyncProgress progress;

    @BeforeMethod
    public void setUp() {
        progress = new ParsecAsyncProgress();
    }

    @Test
    public void testProgressTime_StartSingle() {
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTSINGLE);
        Assert.assertTrue(progress.getStartSingleTime() > 0);
    }

    @Test
    public void testProgressTime_NameLookup() {
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTSINGLE);
        long startSingleTime = progress.getStartSingleTime();
        
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_NAMELOOKUP);
        Assert.assertEquals(progress.getNsLookupTime(), System.nanoTime() / DateUtils.MILLIS_PER_SECOND - startSingleTime);
    }

    @Test
    public void testProgressTime_Connect() {
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTSINGLE);
        long startSingleTime = progress.getStartSingleTime();
        
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_CONNECT);
        Assert.assertEquals(progress.getConnectTime(), System.nanoTime() / DateUtils.MILLIS_PER_SECOND - startSingleTime);
    }

    @Test
    public void testProgressTime_StartTransfer() {
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTSINGLE);
        long startSingleTime = progress.getStartSingleTime();
        
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTTRANSFER);
        Assert.assertEquals(progress.getStartTransferTime(), System.nanoTime() / DateUtils.MILLIS_PER_SECOND - startSingleTime);
    }

    @Test
    public void testProgressTime_PreTransfer() {
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTSINGLE);
        long startSingleTime = progress.getStartSingleTime();
        
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_PRETRANSFER);
        Assert.assertEquals(progress.getPreTransferTime(), System.nanoTime() / DateUtils.MILLIS_PER_SECOND - startSingleTime);
    }

    @Test
    public void testProgressTime_Total() {
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_STARTSINGLE);
        long startSingleTime = progress.getStartSingleTime();
        
        ParsecAsyncProgressTimer.progressTime(progress, ParsecAsyncProgressTimer.TimerOpCode.TIMER_TOTAL);
        Assert.assertEquals(progress.getTotalTime(), System.nanoTime() / DateUtils.MILLIS_PER_SECOND - startSingleTime);
    }

    @Test
    public void testProgressTime_UnknownOpCode() {
        ParsecAsyncProgressTimer.progressTime(progress, null);
        // Check that no exception is thrown and log warning is generated
    }
}
