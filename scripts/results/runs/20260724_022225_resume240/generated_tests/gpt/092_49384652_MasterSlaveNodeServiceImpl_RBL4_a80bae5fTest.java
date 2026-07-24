package com.zuoxiaolong.niubi.job.service.impl;

import com.zuoxiaolong.niubi.job.api.data.MasterSlaveNodeData;
import com.zuoxiaolong.niubi.job.core.helper.LoggerHelper;
import com.zuoxiaolong.niubi.job.core.helper.ReflectHelper;
import com.zuoxiaolong.niubi.job.service.view.MasterNodeView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MasterSlaveNodeServiceImpl_RBL4_a80bae5fTest {

    private MasterSlaveNodeServiceImpl masterSlaveNodeService;
    private MasterSlaveApiFactory mockMasterSlaveApiFactory;
    private NodeApi mockNodeApi;

    @Before
    public void setUp() {
        masterSlaveNodeService = new MasterSlaveNodeServiceImpl();
        mockMasterSlaveApiFactory = mock(MasterSlaveApiFactory.class);
        mockNodeApi = mock(NodeApi.class);
        masterSlaveNodeService.setMasterSlaveApiFactory(mockMasterSlaveApiFactory);
    }

    @Test
    public void testGetAllNodes_WhenApiReturnsNodes() {
        List<MasterSlaveNodeData> mockNodeDataList = new ArrayList<>();
        MasterSlaveNodeData nodeData = new MasterSlaveNodeData();
        nodeData.setId("1");
        nodeData.setData(new Object()); // Assuming data is an Object for this example
        mockNodeDataList.add(nodeData);

        when(mockNodeApi.getAllNodes()).thenReturn(mockNodeDataList);
        when(mockMasterSlaveApiFactory.nodeApi()).thenReturn(mockNodeApi);

        List<MasterNodeView> result = masterSlaveNodeService.getAllNodes();

        assertEquals(1, result.size());
        assertEquals("1", result.get(0).getId());
    }

    @Test
    public void testGetAllNodes_WhenApiThrowsException() {
        when(mockNodeApi.getAllNodes()).thenThrow(new RuntimeException("API error"));
        when(mockMasterSlaveApiFactory.nodeApi()).thenReturn(mockNodeApi);

        List<MasterNodeView> result = masterSlaveNodeService.getAllNodes();

        assertEquals(0, result.size());
        // Verify that the warning was logged
        verify(LoggerHelper.class);
    }

    @Test
    public void testGetAllNodes_WhenApiReturnsNull() {
        when(mockNodeApi.getAllNodes()).thenReturn(null);
        when(mockMasterSlaveApiFactory.nodeApi()).thenReturn(mockNodeApi);

        List<MasterNodeView> result = masterSlaveNodeService.getAllNodes();

        assertEquals(0, result.size());
    }

    @Test
    public void testGetAllNodes_WhenNodeDataIsEmpty() {
        when(mockNodeApi.getAllNodes()).thenReturn(new ArrayList<>());
        when(mockMasterSlaveApiFactory.nodeApi()).thenReturn(mockNodeApi);

        List<MasterNodeView> result = masterSlaveNodeService.getAllNodes();

        assertEquals(0, result.size());
    }
}
