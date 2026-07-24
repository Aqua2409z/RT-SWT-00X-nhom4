
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

    @Before
    public void setUp() {
        // Setup any necessary preconditions for the tests
    }

    @Test
    public void testGetAllJobs() {
        List<StandbyJob> mockJobs = new ArrayList<>();
        when(baseDao.getAll(StandbyJob.class)).thenReturn(mockJobs);

        List<StandbyJob> jobs = standbyJobService.getAllJobs();
        assertNotNull(jobs);
        assertEquals(mockJobs, jobs);
        verify(baseDao, times(1)).getAll(StandbyJob.class);
    }

    @Test
    public void testGetJob() {
        StandbyJob mockJob = new StandbyJob();
        when(baseDao.getUnique(StandbyJob.class, any(StandbyJob.class))).thenReturn(mockJob);

        StandbyJob job = standbyJobService.getJob("group1", "job1", "file.jar");
        assertNotNull(job);
        verify(baseDao, times(1)).getUnique(StandbyJob.class, any(StandbyJob.class));
    }

    @Test(expected = ServiceException.class)
    public void testSaveJob_ThrowsException_WhenJobAlreadyExists() {
        StandbyJob existingJob = new StandbyJob();
        existingJob.setJarFileName("file.jar");
        List<StandbyJob> existingJobs = new ArrayList<>();
        existingJobs.add(existingJob);
        when(baseDao.getList(StandbyJob.class, any(StandbyJob.class))).thenReturn(existingJobs);

        standbyJobService.saveJob("path/to/file.jar", "com.example");
    }

    @Test
    public void testSaveJob() {
        when(baseDao.getList(StandbyJob.class, any(StandbyJob.class))).thenReturn(new ArrayList<>());

        // Mocking the behavior of the job scanner
        JobDescriptor mockDescriptor = mock(JobDescriptor.class);
        when(mockDescriptor.group()).thenReturn("group1");
        when(mockDescriptor.name()).thenReturn("job1");

        // Mocking the job scanner and its behavior
        JobScanner mockJobScanner = mock(JobScanner.class);
        when(mockJobScanner.getJobDescriptorList()).thenReturn(List.of(mockDescriptor));
        when(mockJobScanner.hasSpringEnvironment()).thenReturn(true);

        // Mocking the ApplicationClassLoader and its factory
        ApplicationClassLoader mockClassLoader = mock(ApplicationClassLoader.class);
        when(ApplicationClassLoaderFactory.createNormalApplicationClassLoader(any(), anyString())).thenReturn(mockClassLoader);
        when(JobScannerFactory.createJarFileJobScanner(any(), anyString(), anyString())).thenReturn(mockJobScanner);

        standbyJobService.saveJob("path/to/file.jar", "com.example");

        verify(baseDao, times(1)).save(any(StandbyJob.class));
        verify(baseDao, times(1)).save(any(StandbyJobSummary.class));
    }

    @Test
    public void testGetJarFileNameList() {
        List<StandbyJob> mockJobs = new ArrayList<>();
        StandbyJob job = new StandbyJob();
        job.setJarFileName("file.jar");
        mockJobs.add(job);
        when(baseDao.getList(StandbyJob.class, any(StandbyJob.class), eq(false))).thenReturn(mockJobs);

        List<String> jarFileNames = standbyJobService.getJarFileNameList("group1", "job1");
        assertNotNull(jarFileNames);
        assertEquals(1, jarFileNames.size());
        assertEquals("file.jar", jarFileNames.get(0));
        verify(baseDao, times(1)).getList(StandbyJob.class, any(StandbyJob.class), eq(false));
    }
}
