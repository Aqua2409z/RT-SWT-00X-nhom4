
package com.spotify.flo;

import org.junit.Test;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class Serialization_RBL4_0293e98eTest {

    private static class Serialization_RBL4_0293e98eTest implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void testSerializeAndDeserialize() throws Exception {
        TestObject original = new TestObject("TestName");
        byte[] serialized = Serialization.serialize(original);
        TestObject deserialized = Serialization.deserialize(serialized);
        assertEquals(original.getName(), deserialized.getName());
    }

    @Test
    public void testSerializeToFileAndDeserialize() throws Exception {
        TestObject original = new TestObject("TestName");
        Path tempFile = Files.createTempFile("testSerialization", ".ser");
        Serialization.serialize(original, tempFile);
        TestObject deserialized = Serialization.deserialize(tempFile);
        assertEquals(original.getName(), deserialized.getName());
        Files.delete(tempFile);
    }

    @Test(expected = SerializationException.class)
    public void testSerializeNullObject() throws Exception {
        Serialization.serialize(null);
    }

    @Test(expected = SerializationException.class)
    public void testDeserializeInvalidData() throws Exception {
        byte[] invalidData = new byte[]{1, 2, 3};
        Serialization.deserialize(invalidData);
    }

    @Test
    public void testRequireSerializable() {
        TestObject original = new TestObject("TestName");
        TestObject result = Serialization.requireSerializable(original, "TestObject");
        assertEquals(original.getName(), result.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRequireSerializableThrowsException() {
        Object nonSerializableObject = new Object();
        Serialization.requireSerializable(nonSerializableObject, "NonSerializableObject");
    }
}
