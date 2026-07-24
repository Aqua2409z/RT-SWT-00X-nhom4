package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.persistent.BaseDao;
import com.zuoxiaolong.niubi.job.persistent.entity.MasterSlaveJob;
import com.zuoxiaolong.niubi.job.persistent.entity.MasterSlaveJobSummary;
import com.zuoxiaolong.niubi.job.scanner.ApplicationClassLoader;
import com.zuoxiaolong.niubi.job.scanner.JobScanner;
import com.zuoxiaolong.niubi.job.service.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MasterSlaveJobServiceImpl_RBL4_d070942aTest {

    @InjectMocks
    private MasterSlaveJobServiceImpl masterSlaveJobService;

    @Mock
    private BaseDao baseDao;

    @Mock
    private ApplicationClassLoader applicationClassLoader;

    @Mock
    private JobScanner jobScanner;

    @Before
    public void setUp() {
        // Setup mock behavior for jobScanner
        when(jobScanner.getJobDescriptorList()).thenReturn(new ArrayList<>());
        when(jobScanner.hasSpringEnvironment()).thenReturn(false);
    }

    @Test
    public void testGetAllJobs() {
        List<MasterSlaveJob> jobs = new ArrayList<>();
        when(baseDao.getAll(MasterSlaveJob.class)).thenReturn(jobs);

        List<MasterSlaveJob> result = masterSlaveJobService.getAllJobs();
        assertEquals(jobs, result);
        verify(baseDao, times(1)).getAll(MasterSlaveJob.class);
    }

    @Test
    public void testGetJob() {
        MasterSlaveJob job = new MasterSlaveJob();
        when(baseDao.getUnique(MasterSlaveJob.class, job)).thenReturn(job);

        MasterSlaveJob result = masterSlaveJobService.getJob("group", "name", "jarFileName");
        assertEquals(job, result);
        verify(baseDao, times(1)).getUnique(MasterSlaveJob.class, job);
    }

    @Test(expected = ServiceException.class)
    public void testSaveJob_AlreadyExists() {
        MasterSlaveJob existingJob = new MasterSlaveJob();
        existingJob.setJarFileName("existing.jar");
        when(baseDao.getList(MasterSlaveJob.class, existingJob)).thenReturn(Collections.singletonList(existingJob));

        masterSlaveJobService.saveJob("path/to/existing.jar", "com.example");
    }

    @Test
    public void testSaveJob_NewJob() {
        when(baseDao.getList(any(), any())).thenReturn(Collections.emptyList());
        when(baseDao.getUnique(MasterSlaveJobSummary.class, any())).thenReturn(null);
        when(baseDao.save(any(MasterSlaveJob.class))).thenReturn(null);
        when(baseDao.save(any(MasterSlaveJobSummary.class))).thenReturn(null);

        masterSlaveJobService.saveJob("path/to/new.jar", "com.example");

        verify(baseDao, times(1)).save(any(MasterSlaveJob.class));
        verify(baseDao, times(1)).save(any(MasterSlaveJobSummary.class));
    }

    @Test
    public void testGetJarFileNameList() {
        MasterSlaveJob job = new MasterSlaveJob();
        job.setJarFileName("test.jar");
        List<MasterSlaveJob> jobs = Collections.singletonList(job);
        when(baseDao.getList(MasterSlaveJob.class, job, false)).thenReturn(jobs);

        List<String> result = masterSlaveJobService.getJarFileNameList("group", "name");
        assertEquals(Collections.singletonList("test.jar"), result);
        verify(baseDao, times(1)).getList(MasterSlaveJob.class, job, false);
    }

    @Test
    public void testGetJarFileNameList_NoJobs() {
        MasterSlaveJob job = new MasterSlaveJob();
        List<MasterSlaveJob> jobs = Collections.emptyList();
        when(baseDao.getList(MasterSlaveJob.class, job, false)).thenReturn(jobs);

        List<String> result = masterSlaveJobService.getJarFileNameList("group", "name");
        assertTrue(result.isEmpty());
        verify(baseDao, times(1)).getList(MasterSlaveJob.class, job, false);
    }
}
