
package turin.relations;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class OneToManyRelation_RBL4_0ba40af0Test {

    private OneToManyRelation<String, Integer> relation;

    @Before
    public void setUp() {
        relation = new OneToManyRelation<>();
    }

    @Test
    public void testLink() {
        relation.link("A", 1);
        assertTrue(relation.areLinked("A", 1));
        assertFalse(relation.areLinked("A", 2));
    }

    @Test
    public void testLinkWithSubset() {
        OneToManyRelation<String, Integer>.Subset subset = relation.newBSubset();
        relation.link("A", 1, subset);
        assertTrue(relation.areLinked("A", 1));
    }

    @Test
    public void testUnlink() {
        relation.link("A", 1);
        relation.unlink("A", 1);
        assertFalse(relation.areLinked("A", 1));
    }

    @Test
    public void testLinkingSameBToDifferentA() {
        relation.link("A", 1);
        relation.link("B", 1);
        assertTrue(relation.areLinked("A", 1));
        assertTrue(relation.areLinked("B", 1));
        assertFalse(relation.areLinked("A", 2));
    }

    @Test
    public void testGetReferenceForB() {
        relation.link("A", 1);
        ReferenceSingleEndpoint reference = relation.getReferenceForB(1);
        assertNotNull(reference);
        assertEquals(1, reference.getB());
    }

    @Test
    public void testGetReferenceForA() {
        relation.link("A", 1);
        ReferenceMultipleEndpoint reference = relation.getReferenceForA("A");
        assertNotNull(reference);
        List<Integer> linkedBs = reference.getLinkedBs();
        assertTrue(linkedBs.contains(1));
    }

    @Test
    public void testGetReferenceForSubsetB() {
        relation.link("A", 1);
        ReferenceSingleEndpoint reference = relation.getReferenceForSubsetB(1);
        assertNotNull(reference);
        assertEquals(1, reference.getB());
    }

    @Test
    public void testGetReferenceForAWithSubset() {
        OneToManyRelation<String, Integer>.Subset subset = relation.newBSubset();
        relation.link("A", 1, subset);
        ReferenceMultipleEndpoint reference = relation.getReferenceForA("A", subset);
        assertNotNull(reference);
        List<Integer> linkedBs = reference.getLinkedBs();
        assertTrue(linkedBs.contains(1));
    }
}
