package org.sonar.jproperties.checks.generic;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.sonar.plugins.jproperties.api.tree.KeyTree;
import org.sonar.plugins.jproperties.api.tree.PropertiesTree;
import org.sonar.plugins.jproperties.api.visitors.issue.FileIssue;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class TooManyKeysCheck_RBL4_3d24a470Test {

    private TooManyKeysCheck check;
    private PropertiesTree propertiesTree;
    private KeyTree keyTree;

    @Before
    public void setUp() {
        check = new TooManyKeysCheck();
        propertiesTree = mock(PropertiesTree.class);
        keyTree = mock(KeyTree.class);
    }

    @Test
    public void testVisitKey() {
        check.visitKey(keyTree);
        // Verify that the key is added to the keyTrees list
        assertEquals(1, check.keyTrees.size());
    }

    @Test
    public void testVisitPropertiesWithLessKeys() {
        check.setNumberKeys(5);
        for (int i = 0; i < 4; i++) {
            check.visitKey(keyTree);
        }
        check.visitProperties(propertiesTree);
        // Verify that no issues are added
        assertEquals(0, check.getFileIssues().size());
    }

    @Test
    public void testVisitPropertiesWithMoreKeys() {
        check.setNumberKeys(5);
        for (int i = 0; i < 6; i++) {
            check.visitKey(keyTree);
        }
        check.visitProperties(propertiesTree);
        // Verify that one issue is added
        assertEquals(1, check.getFileIssues().size());
        FileIssue issue = check.getFileIssues().get(0);
        assertEquals("Reduce the number of keys. The number of keys is 6, greater than 5 authorized.", issue.message());
        assertEquals(1, issue.secondaryIssues().size());
    }

    @Test
    public void testVisitPropertiesWithExactKeys() {
        check.setNumberKeys(5);
        for (int i = 0; i < 5; i++) {
            check.visitKey(keyTree);
        }
        check.visitProperties(propertiesTree);
        // Verify that no issues are added
        assertEquals(0, check.getFileIssues().size());
    }

    @Test
    public void testSetNumberKeys() {
        check.setNumberKeys(100);
        assertEquals(100, check.numberKeys);
    }
}
