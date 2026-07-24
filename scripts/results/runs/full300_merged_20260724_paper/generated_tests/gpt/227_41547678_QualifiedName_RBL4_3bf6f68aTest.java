
package me.tomassetti.turin.parser.ast;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;

public class QualifiedName_RBL4_3bf6f68aTest {

    @Test(expected = IllegalArgumentException.class)
    public void testCreateEmptyList() {
        QualifiedName.create(Collections.emptyList());
    }

    @Test
    public void testCreateSingleName() {
        QualifiedName qn = QualifiedName.create(Collections.singletonList("name"));
        assertEquals("name", qn.getName());
        assertTrue(qn.isSimpleName());
    }

    @Test
    public void testCreateQualifiedName() {
        QualifiedName qn = QualifiedName.create(Arrays.asList("base", "name"));
        assertEquals("name", qn.getName());
        assertFalse(qn.isSimpleName());
        assertEquals("base.name", qn.qualifiedName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNameInConstructor() {
        new QualifiedName("invalid name");
    }

    @Test
    public void testValidNameInConstructor() {
        QualifiedName qn = new QualifiedName("validName");
        assertEquals("validName", qn.getName());
        assertTrue(qn.isSimpleName());
    }

    @Test
    public void testQualifiedNameConstructor() {
        QualifiedName base = new QualifiedName("base");
        QualifiedName qn = new QualifiedName(base, "name");
        assertEquals("name", qn.getName());
        assertFalse(qn.isSimpleName());
        assertEquals("base.name", qn.qualifiedName());
    }

    @Test
    public void testGetChildren() {
        QualifiedName base = new QualifiedName("base");
        QualifiedName qn = new QualifiedName(base, "name");
        assertEquals(1, ((Iterable<Node>) qn.getChildren()).spliterator().getExactSizeIfKnown());
    }

    @Test
    public void testQualifiedNameToString() {
        QualifiedName qn = QualifiedName.create(Arrays.asList("base", "name"));
        assertEquals("base.name", qn.toString());
    }

    @Test
    public void testFirstSegment() {
        QualifiedName qn = QualifiedName.create(Arrays.asList("base", "name"));
        assertEquals("base", qn.firstSegment());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRestOnSimpleName() {
        QualifiedName qn = new QualifiedName("name");
        qn.rest();
    }

    @Test
    public void testRestOnQualifiedName() {
        QualifiedName base = QualifiedName.create(Arrays.asList("base", "name"));
        QualifiedName rest = base.rest();
        assertEquals("name", rest.getName());
        assertTrue(rest.isSimpleName());
    }

    @Test
    public void testEqualsAndHashCode() {
        QualifiedName qn1 = QualifiedName.create(Arrays.asList("base", "name"));
        QualifiedName qn2 = QualifiedName.create(Arrays.asList("base", "name"));
        QualifiedName qn3 = QualifiedName.create(Arrays.asList("base", "otherName"));

        assertEquals(qn1, qn2);
        assertNotEquals(qn1, qn3);
        assertEquals(qn1.hashCode(), qn2.hashCode());
        assertNotEquals(qn1.hashCode(), qn3.hashCode());
    }
}
