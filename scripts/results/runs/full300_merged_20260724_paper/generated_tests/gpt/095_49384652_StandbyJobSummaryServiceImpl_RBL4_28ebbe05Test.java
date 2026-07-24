package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.api.data.StandbyJobData;
import com.zuoxiaolong.niubi.job.persistent.BaseDao;
import com.zuoxiaolong.niubi.job.persistent.entity.StandbyJob;
import com.zuoxiaolong.niubi.job.persistent.entity.StandbyJobSummary;
import com.zuoxiaolong.niubi.job.service.ServiceException;
import com.zuoxiaolong.niubi.job.service.StandbyJobLogService;
import com.zuoxiaolong.niubi.job.service.StandbyJobService;
import com.zuoxiaolong.niubi.job.service.StandbyJobSummaryService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class StandbyJobSummaryServiceImpl_RBL4_28ebbe05Test {

    @InjectMocks
    private StandbyJobSummaryServiceImpl standbyJobSummaryService;

    @Mock
    private BaseDao baseDao;

    @Mock
    private StandbyJobService standbyJobService;

    @Mock
    private StandbyJobLogService standbyJobLogService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllJobSummaries() {
        List<StandbyJobSummary> summaries = new ArrayList<>();
        when(baseDao.getAll(StandbyJobSummary.class)).thenReturn(summaries);

        List<StandbyJobSummary> result = standbyJobSummaryService.getAllJobSummaries();

        verify(baseDao).getAll(StandbyJobSummary.class);
        assertSame(summaries, result);
    }

    @Test
    public void testSaveJobSummary() {
        StandbyJobSummary summary = new StandbyJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        summary.setJarFileName("jar1");

        StandbyJobData standbyJobData = new StandbyJobData();
        StandbyJobData.Data data = new StandbyJobData.Data();
        data.setGroupName("group1");
        data.setJobName("job1");
        when(standbyJobService.getJob("group1", "job1", "jar1")).thenReturn(new StandbyJob());
        when(standbyJobLogService.saveJobLog(summary)).thenReturn(1L);
        when(baseDao.getUnique(StandbyJobSummary.class, any())).thenReturn(summary);

        standbyJobSummaryService.saveJobSummary(summary);

        verify(baseDao).update(summary);
        verify(standbyJobLogService).saveJobLog(summary);
    }

    @Test
    public void testUpdateJobSummary() {
        StandbyJobData.Data data = new StandbyJobData.Data();
        data.setGroupName("group1");
        data.setJobName("job1");

        StandbyJobSummary summary = new StandbyJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.getUnique(StandbyJobSummary.class, any())).thenReturn(summary);

        standbyJobSummaryService.updateJobSummary(data);

        verify(baseDao).update(summary);
    }

    @Test
    public void testGetJobSummary() {
        StandbyJobSummary summary = new StandbyJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.get(StandbyJobSummary.class, "1")).thenReturn(summary);
        when(standbyJobService.getJarFileNameList("group1", "job1")).thenReturn(new ArrayList<>());

        try {
            standbyJobSummaryService.getJobSummary("1");
        } catch (ServiceException e) {
            assertEquals("job detail not found.", e.getMessage());
        }

        verify(baseDao).get(StandbyJobSummary.class, "1");
    }

    @Test
    public void testUpdateJobSummaryById() {
        StandbyJobSummary summary = new StandbyJobSummary();
        summary.setGroupName("group1");
        summary.setJobName("job1");
        when(baseDao.get(StandbyJobSummary.class, "1")).thenReturn(summary);

        StandbyJobData standbyJobData = new StandbyJobData();
        StandbyJobData.Data data = new StandbyJobData.Data();
        data.setGroupName("group1");
        data.setJobName("job1");
        when(standbyJobService.getJarFileNameList("group1", "job1")).thenReturn(new ArrayList<>());

        standbyJobSummaryService.updateJobSummary("1");

        verify(baseDao).update(summary);
    }
}
