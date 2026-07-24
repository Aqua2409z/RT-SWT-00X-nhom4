package com.vaadin.data.provider;

import com.vaadin.data.provider.DataCommunicator;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.data.ValueProvider;
import com.vaadin.shared.Range;
import com.vaadin.shared.data.DataCommunicatorClientRpc;
import com.vaadin.ui.ComboBox;
import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DataCommunicator_RBL4_9bb6c751Test {

    private DataCommunicator<TestData> dataCommunicator;
    private DataProvider<TestData, String> dataProvider;
    private TestData testData1;
    private TestData testData2;
    private DataCommunicatorClientRpc rpc;

    @Before
    public void setUp() {
        dataProvider = mock(DataProvider.class);
        rpc = mock(DataCommunicatorClientRpc.class);
        dataCommunicator = new DataCommunicator<>();
        dataCommunicator.setDataProvider(dataProvider, null);
        dataCommunicator.registerRpc(rpc);
        
        testData1 = new TestData(1, "Test 1");
        testData2 = new TestData(2, "Test 2");
    }

    @Test
    public void testSetDataProvider() {
        dataCommunicator.setDataProvider(dataProvider, null);
        assertNotNull(dataCommunicator.getDataProvider());
    }

    @Test
    public void testFetchItemsWithRange() {
        List<TestData> dataList = new ArrayList<>();
        dataList.add(testData1);
        dataList.add(testData2);
        
        when(dataProvider.fetch(any())).thenReturn(dataList.stream());
        when(dataProvider.size(any())).thenReturn(dataList.size());

        List<TestData> fetchedItems = dataCommunicator.fetchItemsWithRange(0, 2);
        assertEquals(2, fetchedItems.size());
        assertEquals(testData1, fetchedItems.get(0));
        assertEquals(testData2, fetchedItems.get(1));
    }

    @Test
    public void testPushData() {
        List<TestData> dataList = new ArrayList<>();
        dataList.add(testData1);
        dataList.add(testData2);
        
        dataCommunicator.pushData(0, dataList);
        
        ArgumentCaptor<JsonArray> jsonArrayCaptor = ArgumentCaptor.forClass(JsonArray.class);
        verify(rpc).setData(eq(0), jsonArrayCaptor.capture());
        
        JsonArray jsonArray = jsonArrayCaptor.getValue();
        assertEquals(2, jsonArray.length());
    }

    @Test
    public void testOnRequestRows() {
        dataCommunicator.setMaximumAllowedRows(10);
        dataCommunicator.onRequestRows(0, 5, 0, 0);
        assertEquals(Range.withLength(0, 5), dataCommunicator.getPushRows());
    }

    @Test(expected = IllegalStateException.class)
    public void testOnRequestRowsExceedsLimit() {
        dataCommunicator.setMaximumAllowedRows(5);
        dataCommunicator.onRequestRows(0, 10, 0, 0);
    }

    @Test
    public void testAddDataGenerator() {
        TestDataGenerator generator = new TestDataGenerator();
        dataCommunicator.addDataGenerator(generator);
        assertTrue(dataCommunicator.getActiveDataHandler().getGenerators().contains(generator));
    }

    @Test
    public void testRemoveDataGenerator() {
        TestDataGenerator generator = new TestDataGenerator();
        dataCommunicator.addDataGenerator(generator);
        dataCommunicator.removeDataGenerator(generator);
        assertFalse(dataCommunicator.getActiveDataHandler().getGenerators().contains(generator));
    }

    @Test
    public void testSetInMemorySorting() {
        Comparator<TestData> comparator = Comparator.comparing(TestData::getName);
        dataCommunicator.setInMemorySorting(comparator);
        assertEquals(comparator, dataCommunicator.getInMemorySorting());
    }

    @Test
    public void testReset() {
        dataCommunicator.reset();
        assertTrue(dataCommunicator.getUpdatedData().isEmpty());
    }

    private static class DataCommunicator_RBL4_9bb6c751Test {
        private final int id;
        private final String name;

        public TestData(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    private static class DataCommunicator_RBL4_9bb6c751Test implements DataGenerator<TestData> {
        @Override
        public void generateData(TestData data, JsonObject jsonObject) {
            jsonObject.put("name", data.getName());
        }

        @Override
        public void destroyData(TestData data) {
            // No-op
        }

        @Override
        public void destroyAllData() {
            // No-op
        }

        @Override
        public void refreshData(TestData data) {
            // No-op
        }
    }
}
