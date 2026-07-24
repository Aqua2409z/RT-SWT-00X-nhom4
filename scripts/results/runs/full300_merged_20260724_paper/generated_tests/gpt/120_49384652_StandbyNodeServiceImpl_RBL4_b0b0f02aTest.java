package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.api.data.StandbyNodeData;
import com.zuoxiaolong.niubi.job.core.helper.LoggerHelper;
import com.zuoxiaolong.niubi.job.core.helper.ReflectHelper;
import com.zuoxiaolong.niubi.job.service.view.StandbyNodeView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StandbyNodeServiceImpl_RBL4_b0b0f02aTest {

    private StandbyNodeServiceImpl standbyNodeService;
    private StandbyApiFactory standbyApiFactoryMock;
    private NodeApi nodeApiMock;

    @Before
    public void setUp() {
        standbyNodeService = new StandbyNodeServiceImpl();
        standbyApiFactoryMock = mock(StandbyApiFactory.class);
        nodeApiMock = mock(NodeApi.class);
        standbyNodeService.setStandbyApiFactory(standbyApiFactoryMock);
        when(standbyApiFactoryMock.nodeApi()).thenReturn(nodeApiMock);
    }

    @Test
    public void testGetAllNodes_Success() {
        List<StandbyNodeData> standbyNodeDataList = new ArrayList<>();
        StandbyNodeData nodeData = new StandbyNodeData();
        nodeData.setId("1");
        nodeData.setData(new Object()); // Assuming data is an Object for this test
        standbyNodeDataList.add(nodeData);

        when(nodeApiMock.getAllNodes()).thenReturn(standbyNodeDataList);

        List<StandbyNodeView> result = standbyNodeService.getAllNodes();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
        // Additional assertions can be added here to verify the copied fields
    }

    @Test
    public void testGetAllNodes_EmptyList() {
        when(nodeApiMock.getAllNodes()).thenReturn(new ArrayList<>());

        List<StandbyNodeView> result = standbyNodeService.getAllNodes();

        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllNodes_ExceptionHandling() {
        when(nodeApiMock.getAllNodes()).thenThrow(new RuntimeException("API failure"));

        List<StandbyNodeView> result = standbyNodeService.getAllNodes();

        assertEquals(0, result.size());
        // Verify that the warning was logged
        verifyStatic(LoggerHelper.class);
        LoggerHelper.warn(contains("select all standby nodes failed"));
    }
}
