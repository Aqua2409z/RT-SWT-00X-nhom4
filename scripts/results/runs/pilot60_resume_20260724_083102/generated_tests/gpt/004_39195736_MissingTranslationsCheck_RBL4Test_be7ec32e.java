package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.KeyTree;
import org.sonar.plugins.jproperties.api.tree.PropertiesTree;
import org.sonar.plugins.jproperties.api.visitors.Context;

import java.io.File;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class MissingTranslationsCheck_RBL4Test_be7ec32e {

    private MissingTranslationsCheck check;
    private Context context;
    private PropertiesTree propertiesTree;
    private KeyTree keyTree;

    @Before
    public void setUp() {
        check = new MissingTranslationsCheck();
        context = mock(Context.class);
        propertiesTree = mock(PropertiesTree.class);
        keyTree = mock(KeyTree.class);
    }

    @Test
    public void testVisitProperties() {
        File file = new File("test.properties");
        when(context.getFile()).thenReturn(file);
        check.setContext(context);

        check.visitProperties(propertiesTree);

        Map<File, Set<String>> fileKeys = check.getFileKeys();
        assertEquals(1, fileKeys.size());
        assertEquals(fileKeys.get(file).size(), 0);
    }

    @Test
    public void testVisitKey() {
        File file = new File("test.properties");
        when(context.getFile()).thenReturn(file);
        check.setContext(context);
        check.visitProperties(propertiesTree);

        when(keyTree.text()).thenReturn("testKey");
        check.visitKey(keyTree);

        Map<File, Set<String>> fileKeys = check.getFileKeys();
        assertEquals(1, fileKeys.size());
        assertEquals(1, fileKeys.get(file).size());
        assertEquals(true, fileKeys.get(file).contains("testKey"));
    }

    @Test
    public void testMultipleKeys() {
        File file = new File("test.properties");
        when(context.getFile()).thenReturn(file);
        check.setContext(context);
        check.visitProperties(propertiesTree);

        when(keyTree.text()).thenReturn("key1");
        check.visitKey(keyTree);
        when(keyTree.text()).thenReturn("key2");
        check.visitKey(keyTree);

        Map<File, Set<String>> fileKeys = check.getFileKeys();
        assertEquals(1, fileKeys.size());
        assertEquals(2, fileKeys.get(file).size());
        assertEquals(true, fileKeys.get(file).contains("key1"));
        assertEquals(true, fileKeys.get(file).contains("key2"));
    }
}
