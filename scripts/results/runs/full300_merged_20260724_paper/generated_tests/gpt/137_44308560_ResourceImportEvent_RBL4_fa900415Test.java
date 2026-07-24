
package org.rf.ide.core.execution.agent.event;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ResourceImportEvent_RBL4_fa900415Test {

    @Test
    void testFromValidEventMap() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("resource_import", List.of("arg1", Map.of("source", "file:///path/to/resource", "importer", "file:///path/to/importer")));

        ResourceImportEvent event = ResourceImportEvent.from(eventMap);

        assertEquals(URI.create("file:///path/to/resource"), event.getPath());
        assertEquals(URI.create("file:///path/to/importer"), event.getImporterPath().orElse(null));
        assertFalse(event.isDynamicallyImported());
    }

    @Test
    void testFromValidEventMapWithNullImporter() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("resource_import", List.of("arg1", Map.of("source", "file:///path/to/resource", "importer", null)));

        ResourceImportEvent event = ResourceImportEvent.from(eventMap);

        assertEquals(URI.create("file:///path/to/resource"), event.getPath());
        assertTrue(event.getImporterPath().isEmpty());
        assertTrue(event.isDynamicallyImported());
    }

    @Test
    void testFromInvalidEventMapMissingPath() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("resource_import", List.of("arg1", Map.of("source", null, "importer", "file:///path/to/importer")));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ResourceImportEvent.from(eventMap));
        assertEquals("Resource import event has to contain path to imported resource", exception.getMessage());
    }

    @Test
    void testEqualsAndHashCode() {
        ResourceImportEvent event1 = new ResourceImportEvent(URI.create("file:///path/to/resource"), URI.create("file:///path/to/importer"));
        ResourceImportEvent event2 = new ResourceImportEvent(URI.create("file:///path/to/resource"), URI.create("file:///path/to/importer"));
        ResourceImportEvent event3 = new ResourceImportEvent(URI.create("file:///path/to/resource"), null);

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);
        assertNotEquals(event2, event3);
        assertEquals(event1.hashCode(), event2.hashCode());
        assertNotEquals(event1.hashCode(), event3.hashCode());
    }

    @Test
    void testGetImporterPath() {
        ResourceImportEvent eventWithImporter = new ResourceImportEvent(URI.create("file:///path/to/resource"), URI.create("file:///path/to/importer"));
        ResourceImportEvent eventWithoutImporter = new ResourceImportEvent(URI.create("file:///path/to/resource"), null);

        assertTrue(eventWithImporter.getImporterPath().isPresent());
        assertEquals(URI.create("file:///path/to/importer"), eventWithImporter.getImporterPath().get());

        assertFalse(eventWithoutImporter.getImporterPath().isPresent());
    }
}
