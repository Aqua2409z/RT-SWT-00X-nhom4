
package com.zuoxiaolong.niubi.job.core.helper;

import org.junit.Test;
import static org.junit.Assert.*;

public class JsonHelper_RBL4_c3d480dcTest {

    private static class JsonHelper_RBL4_c3d480dcTest {
        private String name;
        private int age;

        public TestObject(String name, int age) {
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
    public void testToJson() {
        TestObject obj = new TestObject("John", 30);
        String json = JsonHelper.toJson(obj);
        assertNotNull(json);
        assertTrue(json.contains("John"));
        assertTrue(json.contains("30"));
    }

    @Test
    public void testToJson_NullObject() {
        String json = JsonHelper.toJson(null);
        assertNull(json);
    }

    @Test
    public void testFromJson() {
        String json = "{\"name\":\"John\",\"age\":30}";
        TestObject obj = JsonHelper.fromJson(json, TestObject.class);
        assertNotNull(obj);
        assertEquals("John", obj.getName());
        assertEquals(30, obj.getAge());
    }

    @Test
    public void testFromJson_NullJson() {
        TestObject obj = JsonHelper.fromJson(null, TestObject.class);
        assertNull(obj);
    }

    @Test
    public void testToBytes() {
        TestObject obj = new TestObject("John", 30);
        byte[] bytes = JsonHelper.toBytes(obj);
        assertNotNull(bytes);
        assertTrue(bytes.length > 0);
    }

    @Test
    public void testFromJson_ByteArray() {
        String json = "{\"name\":\"John\",\"age\":30}";
        byte[] bytes = json.getBytes();
        TestObject obj = JsonHelper.fromJson(bytes, TestObject.class);
        assertNotNull(obj);
        assertEquals("John", obj.getName());
        assertEquals(30, obj.getAge());
    }

    @Test
    public void testFromJson_ByteArray_Null() {
        TestObject obj = JsonHelper.fromJson((byte[]) null, TestObject.class);
        assertNull(obj);
    }
}
