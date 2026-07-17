
package org.confucius.commons.lang.io.scanner;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Set;

public class SimpleClassScannerTest {

    private SimpleClassScanner scanner;

    @Before
    public void setUp() {
        scanner = SimpleClassScanner.INSTANCE;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScanWithNullClassLoader() {
        scanner.scan(null, "org.confucius.commons.lang");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testScanWithEmptyPackageName() {
        scanner.scan(getClass().getClassLoader(), "");
    }

    @Test
    public void testScanWithValidPackageName() {
        Set<Class<?>> classes = scanner.scan(getClass().getClassLoader(), "org.confucius.commons.lang");
        Assert.assertNotNull(classes);
        Assert.assertFalse(classes.isEmpty());
    }

    @Test
    public void testScanWithRecursive() {
        Set<Class<?>> classes = scanner.scan(getClass().getClassLoader(), "org.confucius.commons.lang", true);
        Assert.assertNotNull(classes);
        Assert.assertFalse(classes.isEmpty());
    }

    @Test
    public void testScanWithRequiredLoad() {
        Set<Class<?>> classes = scanner.scan(getClass().getClassLoader(), "org.confucius.commons.lang", false, true);
        Assert.assertNotNull(classes);
        Assert.assertFalse(classes.isEmpty());
    }

    @Test
    public void testScanWithNoClassesFound() {
        Set<Class<?>> classes = scanner.scan(getClass().getClassLoader(), "org.confucius.nonexistent");
        Assert.assertNotNull(classes);
        Assert.assertTrue(classes.isEmpty());
    }

    @Test
    public void testScanWithInvalidPackageName() {
        try {
            scanner.scan(getClass().getClassLoader(), "invalid.package.name");
            Assert.fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected exception
        }
    }
}
