
package org.springframework.data.simpledb.query;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import java.util.*;

public class SimpleDbResultConverter_RBL4_c6463d40Test {

    private static class SimpleDbResultConverter_RBL4_c6463d40Test {
        private String name;
        private int age;

        public TestEntity(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    public void testFilterNamedAttributesAsList() {
        List<TestEntity> entities = Arrays.asList(new TestEntity("Alice", 30), new TestEntity("Bob", 25));
        List<Object> result = SimpleDbResultConverter.filterNamedAttributesAsList(entities, "name");
        Assert.assertEquals(Arrays.asList("Alice", "Bob"), result);
    }

    @Test
    public void testFilterNamedAttributesAsSet() {
        List<TestEntity> entities = Arrays.asList(new TestEntity("Alice", 30), new TestEntity("Bob", 25), new TestEntity("Alice", 40));
        Set<Object> result = SimpleDbResultConverter.filterNamedAttributesAsSet(entities, "name");
        Assert.assertEquals(new LinkedHashSet<>(Arrays.asList("Alice", "Bob")), result);
    }

    @Test
    public void testToListOfListOfObject() {
        List<TestEntity> entities = Arrays.asList(new TestEntity("Alice", 30), new TestEntity("Bob", 25));
        List<String> fields = Arrays.asList("name", "age");
        List<List<Object>> result = SimpleDbResultConverter.toListOfListOfObject(entities, fields);
        List<List<Object>> expected = Arrays.asList(Arrays.asList("Alice", 30), Arrays.asList("Bob", 25));
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToListOfListOfObjectEmptyList() {
        List<TestEntity> entities = Collections.emptyList();
        List<String> fields = Arrays.asList("name", "age");
        List<List<Object>> result = SimpleDbResultConverter.toListOfListOfObject(entities, fields);
        Assert.assertTrue(result.isEmpty());
    }
}
