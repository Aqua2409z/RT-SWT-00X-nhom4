package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.api.data.StandbyJobData;
import com.zuoxiaolong.niubi.job.persistent.BaseDao;
import com.zuoxiaolong.niubi.job.persistent.entity.StandbyJobLog;
import com.zuoxiaolong.niubi.job.persistent.entity.StandbyJobSummary;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class StandbyJobLogServiceImpl_RBL4_c897b221Test {

    @InjectMocks
    private StandbyJobLogServiceImpl standbyJobLogService;

    @Mock
    private BaseDao baseDao;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllJobLogs() {
        List<StandbyJobLog> mockLogs = new ArrayList<>();
        when(baseDao.getAll(StandbyJobLog.class)).thenReturn(mockLogs);

        List<StandbyJobLog> logs = standbyJobLogService.getAllJobLogs();

        verify(baseDao, times(1)).getAll(StandbyJobLog.class);
        assertSame(mockLogs, logs);
    }

    @Test
    public void testSaveJobLog() {
        StandbyJobSummary summary = new StandbyJobSummary();
        String expectedId = "123";
        when(baseDao.save(any(StandbyJobLog.class))).thenReturn(expectedId);

        String actualId = standbyJobLogService.saveJobLog(summary);

        verify(baseDao, times(1)).save(any(StandbyJobLog.class));
        assertEquals(expectedId, actualId);
    }

    @Test
    public void testUpdateJobLog_WithValidId() {
        StandbyJobData.Data data = new StandbyJobData.Data();
        data.setJobOperationLogId("logId");
        StandbyJobLog existingLog = new StandbyJobLog();
        when(baseDao.get(StandbyJobLog.class, "logId")).thenReturn(existingLog);

        standbyJobLogService.updateJobLog(data);

        verify(baseDao, times(1)).get(StandbyJobLog.class, "logId");
        verify(baseDao, times(1)).update(existingLog);
        verify(data, times(1)).clearOperationLog();
    }

    @Test
    public void testUpdateJobLog_WithEmptyId() {
        StandbyJobData.Data data = new StandbyJobData.Data();
        data.setJobOperationLogId("");

        standbyJobLogService.updateJobLog(data);

        verify(baseDao, never()).get(any(), any());
    }

    @Test
    public void testUpdateJobLog_WithNonExistingLog() {
        StandbyJobData.Data data = new StandbyJobData.Data();
        data.setJobOperationLogId("logId");
        when(baseDao.get(StandbyJobLog.class, "logId")).thenReturn(null);

        standbyJobLogService.updateJobLog(data);

        verify(baseDao, times(1)).get(StandbyJobLog.class, "logId");
        verify(baseDao, never()).update(any());
    }
}
