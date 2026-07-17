
package org.confucius.commons.lang.io.scanner;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.net.URL;
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
    public void testFilterClassNames() {
        Set<String> classNames = Sets.newLinkedHashSet();
        classNames.add("org.confucius.commons.lang.TestClass");
        classNames.add("org.confucius.commons.lang.other.TestClass");
        
        Set<String> filteredNames = scanner.filterClassNames(classNames, "org.confucius.commons.lang", false);
        Assert.assertEquals(1, filteredNames.size());
        Assert.assertTrue(filteredNames.contains("org.confucius.commons.lang.TestClass"));
    }

    @Test
    public void testResolveClassPathURL() throws Exception {
        URL resourceURL = new URL("file:/path/to/classes/org/confucius/commons/lang/");
        String packageResourceName = "org/confucius/commons/lang/";
        URL resolvedURL = scanner.resolveClassPathURL(resourceURL, packageResourceName);
        Assert.assertNotNull(resolvedURL);
        Assert.assertEquals("file:/path/to/classes/", resolvedURL.toExternalForm());
    }
}
