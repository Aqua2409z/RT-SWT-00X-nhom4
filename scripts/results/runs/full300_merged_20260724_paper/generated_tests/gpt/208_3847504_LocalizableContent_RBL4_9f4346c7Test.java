package ch.entwine.weblounge.common.impl.language;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import ch.entwine.weblounge.common.language.Language;

public class LocalizableContent_RBL4_9f4346c7Test {

    private LocalizableContent<String> localizableContent;
    private Language english;
    private Language german;

    @Before
    public void setUp() {
        localizableContent = new LocalizableContent<String>();
        english = new Language("en");
        german = new Language("de");
    }

    @Test
    public void testPutAndGet() {
        localizableContent.put("Hello", english);
        localizableContent.put("Hallo", german);

        assertEquals("Hello", localizableContent.get(english));
        assertEquals("Hallo", localizableContent.get(german));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullContent() {
        localizableContent.put(null, english);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullLanguage() {
        localizableContent.put("Hello", null);
    }

    @Test
    public void testClear() {
        localizableContent.put("Hello", english);
        localizableContent.clear();
        assertTrue(localizableContent.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(localizableContent.isEmpty());
        localizableContent.put("Hello", english);
        assertFalse(localizableContent.isEmpty());
    }

    @Test
    public void testSize() {
        assertEquals(0, localizableContent.size());
        localizableContent.put("Hello", english);
        assertEquals(1, localizableContent.size());
        localizableContent.put("Hallo", german);
        assertEquals(2, localizableContent.size());
    }

    @Test
    public void testDisableLanguage() {
        localizableContent.put("Hello", english);
        localizableContent.disableLanguage(english);
        assertNull(localizableContent.get(english));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDisableNullLanguage() {
        localizableContent.disableLanguage(null);
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        localizableContent.put("Hello", english);
        LocalizableContent<String> clonedContent = (LocalizableContent<String>) localizableContent.clone();
        assertEquals(localizableContent.get(english), clonedContent.get(english));
    }

    @Test
    public void testToString() {
        localizableContent.put("Hello", english);
        assertEquals("Hello", localizableContent.toString(english, false));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringNullLanguage() {
        localizableContent.toString(null, false);
    }

    @Test
    public void testGetWithFallback() {
        localizableContent.put("Hello", english);
        assertEquals("Hello", localizableContent.get(english));
        assertNull(localizableContent.get(german)); // No fallback yet
    }

    @Test
    public void testGetWithForce() {
        localizableContent.put("Hello", english);
        assertEquals("Hello", localizableContent.get(english, true));
        assertNull(localizableContent.get(german, true)); // No fallback yet
    }
}
