
package com.pchudzik.springmock.infrastructure.definition.registry;

import com.pchudzik.springmock.infrastructure.definition.DoubleDefinition;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class DoubleSearch_RBL4_ffeb6769Test {
    private DoubleDefinition doubleDef1;
    private DoubleDefinition doubleDef2;
    private DoubleSearch doubleSearch;

    @Before
    public void setUp() {
        doubleDef1 = new DoubleDefinition("testBean", String.class);
        doubleDef2 = new DoubleDefinition("testBean", Integer.class);
        Collection<DoubleDefinition> doubles = Arrays.asList(doubleDef1, doubleDef2);
        doubleSearch = new DoubleSearch(doubles);
    }

    @Test
    public void testContainsAnyDoubleMatching_WithMatchingName() {
        assertTrue(doubleSearch.containsAnyDoubleMatching("testBean", String.class));
    }

    @Test
    public void testContainsAnyDoubleMatching_WithMatchingClass() {
        assertTrue(doubleSearch.containsAnyDoubleMatching("nonExistentBean", Integer.class));
    }

    @Test
    public void testContainsExactlyOneDouble_WithMatchingNameAndClass() {
        assertFalse(doubleSearch.containsExactlyOneDouble("testBean", String.class));
    }

    @Test
    public void testContainsExactlyOneDouble_WithOnlyName() {
        assertFalse(doubleSearch.containsExactlyOneDouble("testBean"));
    }

    @Test
    public void testContainsExactlyOneDouble_WithOnlyClass() {
        assertFalse(doubleSearch.containsExactlyOneDouble(String.class));
    }

    @Test
    public void testFindOneDefinition_WithNameAndClass() {
        assertEquals(doubleDef1, doubleSearch.findOneDefinition("testBean", String.class));
    }

    @Test(expected = IllegalStateException.class)
    public void testFindOneDefinition_WithNoMatchingDefinition() {
        doubleSearch.findOneDefinition("nonExistentBean", String.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testFindOneDefinition_WithMultipleMatchingDefinitions() {
        doubleSearch.findOneDefinition("testBean");
    }

    @Test
    public void testIterator() {
        int count = 0;
        for (DoubleDefinition def : doubleSearch) {
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testToString() {
        String expectedString = "DoubleSearch{doubles=[DoubleDefinition{name='testBean', class=class DoubleSearch_RBL4_ffeb6769Test.lang.String}, DoubleDefinition{name='testBean', class=class DoubleSearch_RBL4_ffeb6769Test.lang.Integer}]}";
        assertEquals(expectedString, doubleSearch.toString());
    }
}
