
package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.persistent.BaseDao;
import com.zuoxiaolong.niubi.job.persistent.entity.StandbyJob;
import com.zuoxiaolong.niubi.job.persistent.entity.StandbyJobSummary;
import com.zuoxiaolong.niubi.job.scanner.job.JobDescriptor;
import com.zuoxiaolong.niubi.job.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandbyJobServiceImpl_RBL4Test_06be1a5a {

    @InjectMocks
    private StandbyJobServiceImpl standbyJobService;

    @Mock
    private BaseDao baseDao;

    private StandbyJob standbyJob;
    private StandbyJobSummary standbyJobSummary;

    @Before
    public void setUp() {
        standbyJob = new StandbyJob();
        standbyJob.setGroupName("group1");
        standbyJob.setJobName("job1");
        standbyJob.setJarFileName("test.jar");

        standbyJobSummary = new StandbyJobSummary();
        standbyJobSummary.setGroupName("group1");
        standbyJobSummary.setJobName("job1");
    }

    @Test
    public void testGetAllJobs() {
        List<StandbyJob> jobList = new ArrayList<>();
        jobList.add(standbyJob);
        when(baseDao.getAll(StandbyJob.class)).thenReturn(jobList);

        List<StandbyJob> result = standbyJobService.getAllJobs();
        assertEquals(1, result.size());
        assertEquals("job1", result.get(0).getJobName());
    }

    @Test
    public void testGetJob() {
        when(baseDao.getUnique(StandbyJob.class, standbyJob)).thenReturn(standbyJob);

        StandbyJob result = standbyJobService.getJob("group1", "job1", "test.jar");
        assertNotNull(result);
        assertEquals("job1", result.getJobName());
    }

    @Test(expected = ServiceException.class)
    public void testSaveJob_AlreadyExists() {
        List<StandbyJob> existingJobs = new ArrayList<>();
        existingJobs.add(standbyJob);
        when(baseDao.getList(StandbyJob.class, standbyJob)).thenReturn(existingJobs);

        standbyJobService.saveJob("path/to/test.jar", "com.example");
    }

    @Test
    public void testSaveJob_NewJob() {
        when(baseDao.getList(StandbyJob.class, standbyJob)).thenReturn(new ArrayList<>());
        when(baseDao.getUnique(StandbyJobSummary.class, standbyJobSummary)).thenReturn(null);

        standbyJobService.saveJob("path/to/test.jar", "com.example");

        verify(baseDao, times(1)).save(any(StandbyJob.class));
        verify(baseDao, times(1)).save(any(StandbyJobSummary.class));
    }

    @Test
    public void testGetJarFileNameList() {
        List<StandbyJob> jobList = new ArrayList<>();
        jobList.add(standbyJob);
        when(baseDao.getList(StandbyJob.class, standbyJob, false)).thenReturn(jobList);

        List<String> result = standbyJobService.getJarFileNameList("group1", "job1");
        assertEquals(1, result.size());
        assertEquals("test.jar", result.get(0));
    }

    @Test
    public void testGetJarFileNameList_NoJobs() {
        when(baseDao.getList(StandbyJob.class, standbyJob, false)).thenReturn(new ArrayList<>());

        List<String> result = standbyJobService.getJarFileNameList("group1", "job1");
        assertTrue(result.isEmpty());
    }
}
