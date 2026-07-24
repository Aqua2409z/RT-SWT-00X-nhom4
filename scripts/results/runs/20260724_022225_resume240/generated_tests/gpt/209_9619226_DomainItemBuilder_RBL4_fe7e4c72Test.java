
package org.springframework.data.simpledb.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.simpledb.core.entity.EntityWrapper;
import org.springframework.data.simpledb.repository.support.entityinformation.SimpleDbEntityInformation;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectResult;

public class DomainItemBuilder_RBL4_fe7e4c72Test {

    private DomainItemBuilder<TestEntity> domainItemBuilder;

    @Mock
    private SimpleDbEntityInformation<TestEntity, String> entityInformation;

    @Mock
    private SelectResult selectResult;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        domainItemBuilder = new DomainItemBuilder<>();
    }

    @Test
    public void testPopulateDomainItems() {
        Item item1 = new Item().withName("item1")
                .withAttributes(Arrays.asList(new Attribute().withName("attr1").withValue("value1")));
        Item item2 = new Item().withName("item2")
                .withAttributes(Arrays.asList(new Attribute().withName("attr2").withValue("value2")));
        when(selectResult.getItems()).thenReturn(Arrays.asList(item1, item2));

        List<TestEntity> result = domainItemBuilder.populateDomainItems(entityInformation, selectResult);

        assertEquals(2, result.size());
        assertEquals("item1", result.get(0).getId());
        assertEquals("item2", result.get(1).getId());
    }

    @Test
    public void testPopulateDomainItem() {
        Item item = new Item().withName("item1")
                .withAttributes(Arrays.asList(new Attribute().withName("attr1").withValue("value1")));
        when(entityInformation.getJavaType()).thenReturn(TestEntity.class);

        TestEntity result = domainItemBuilder.populateDomainItem(entityInformation, item);

        assertNotNull(result);
        assertEquals("item1", result.getId());
    }

    @Test
    public void testConvertSimpleDbAttributes() {
        List<Attribute> attributes = Arrays.asList(
                new Attribute().withName("attr1").withValue("value1"),
                new Attribute().withName("attr1").withValue("value2")
        );

        Map<String, String> result = domainItemBuilder.convertSimpleDbAttributes(attributes);

        assertEquals(1, result.size());
        assertTrue(result.containsKey("attr1"));
        assertEquals("value1,value2", result.get("attr1"));
    }

    private static class DomainItemBuilder_RBL4_fe7e4c72Test extends EntityWrapper<TestEntity, String> {
        private String id;

        public TestEntity(SimpleDbEntityInformation<TestEntity, String> entityInformation) {
            super(entityInformation);
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
