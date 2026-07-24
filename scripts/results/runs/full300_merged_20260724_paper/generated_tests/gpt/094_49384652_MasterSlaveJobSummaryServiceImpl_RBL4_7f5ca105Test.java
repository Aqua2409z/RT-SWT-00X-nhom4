package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.api.data.MasterSlaveJobData;
import com.zuoxiaolong.niubi.job.persistent.BaseDao;
import com.zuoxiaolong.niubi.job.persistent.entity.MasterSlaveJob;
import com.zuoxiaolong.niubi.job.persistent.entity.MasterSlaveJobSummary;
import com.zuoxiaolong.niubi.job.service.MasterSlaveJobLogService;
import com.zuoxiaolong.niubi.job.service.MasterSlaveJobService;
import com.zuoxiaolong.niubi.job.service.ServiceException;
import com.zuoxiaolong.niubi.job.service.impl.MasterSlaveJobSummaryServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MasterSlaveJobSummaryServiceImpl_RBL4_7f5ca105Test {

    @InjectMocks
    private MasterSlaveJobSummaryServiceImpl masterSlaveJobSummaryService;

    @Mock
    private BaseDao baseDao;

    @Mock
    private MasterSlaveJobService masterSlaveJobService;

    @Mock
    private MasterSlaveJobLogService masterSlaveJobLogService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllJobSummaries() {
        List<MasterSlaveJobSummary> summaries = new ArrayList<>();
        when(baseDao.getAll(MasterSlaveJobSummary.class)).thenReturn(summaries);

        List<MasterSlaveJobSummary> result = masterSlaveJobSummaryService.getAllJobSummaries();
        assertEquals(summaries, result);
        verify(baseDao, times(1)).getAll(MasterSlaveJobSummary.class);
    }

    @Test
    public void testSaveJobSummary() {
        MasterSlaveJobSummary summary = new MasterSlaveJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        summary.setJarFileName("jar1");

        MasterSlaveJobData masterSlaveJobData = new MasterSlaveJobData();
        MasterSlaveJobData.Data data = new MasterSlaveJobData.Data();
        data.setGroupName("group1");
        data.setJobName("job1");
        when(masterSlaveJobService.getJob("group1", "job1", "jar1")).thenReturn(new MasterSlaveJob());
        when(masterSlaveJobLogService.saveJobLog(summary)).thenReturn(1L);
        when(baseDao.getUnique(MasterSlaveJobSummary.class, summary)).thenReturn(summary);

        masterSlaveJobSummaryService.saveJobSummary(summary);

        verify(masterSlaveJobLogService, times(1)).saveJobLog(summary);
        verify(baseDao, times(1)).update(summary);
    }

    @Test
    public void testUpdateJobSummaryWithData() {
        MasterSlaveJobData.Data data = new MasterSlaveJobData.Data();
        data.setGroupName("group1");
        data.setJobName("job1");

        MasterSlaveJobSummary summary = new MasterSlaveJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.getUnique(MasterSlaveJobSummary.class, summary)).thenReturn(summary);

        masterSlaveJobSummaryService.updateJobSummary(data);

        verify(baseDao, times(1)).update(summary);
    }

    @Test
    public void testUpdateJobSummaryWithId() {
        String id = "1";
        MasterSlaveJobSummary summary = new MasterSlaveJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.get(MasterSlaveJobSummary.class, id)).thenReturn(summary);

        MasterSlaveJobData masterSlaveJobData = new MasterSlaveJobData();
        MasterSlaveJobData.Data data = new MasterSlaveJobData.Data();
        data.setGroupName("group1");
        data.setJobName("job1");
        when(masterSlaveJobService.getJob("group1", "job1")).thenReturn(new MasterSlaveJob());
        when(masterSlaveJobLogService.saveJobLog(summary)).thenReturn(1L);

        masterSlaveJobSummaryService.updateJobSummary(id);

        verify(baseDao, times(1)).update(summary);
    }

    @Test
    public void testGetJobSummary() {
        String id = "1";
        MasterSlaveJobSummary summary = new MasterSlaveJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.get(MasterSlaveJobSummary.class, id)).thenReturn(summary);

        MasterSlaveJobData masterSlaveJobData = new MasterSlaveJobData();
        MasterSlaveJobData.Data data = new MasterSlaveJobData.Data();
        data.setJarFileName("jar1");
        when(masterSlaveJobService.getJarFileNameList("group1", "job1")).thenReturn(new ArrayList<>());

        MasterSlaveJobSummary result = masterSlaveJobSummaryService.getJobSummary(id);

        assertNotNull(result);
        assertEquals("jar1", result.getOriginalJarFileName());
    }

    @Test(expected = ServiceException.class)
    public void testGetJobSummaryThrowsException() {
        String id = "1";
        MasterSlaveJobSummary summary = new MasterSlaveJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.get(MasterSlaveJobSummary.class, id)).thenReturn(summary);
        when(masterSlaveJobService.getJarFileNameList("group1", "job1")).thenReturn(new ArrayList<>());

        masterSlaveJobSummaryService.getJobSummary(id);
    }
}
