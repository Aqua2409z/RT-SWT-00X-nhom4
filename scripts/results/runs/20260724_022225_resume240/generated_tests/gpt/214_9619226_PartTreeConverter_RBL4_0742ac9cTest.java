
package org.springframework.data.simpledb.query;

import org.junit.Test;
import org.springframework.data.mapping.model.MappingException;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.data.repository.query.parser.PartTree;
import org.springframework.data.repository.query.parser.PartTree.OrPart;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartTreeConverter_RBL4_0742ac9cTest {

    @Test
    public void testToIndexedQuerySingleSimpleProperty() {
        Part part = mock(Part.class);
        when(part.getProperty().getSegment()).thenReturn("name");
        when(part.getType()).thenReturn(Part.Type.SIMPLE_PROPERTY);

        OrPart orPart = mock(OrPart.class);
        when(orPart.iterator()).thenReturn(Collections.singletonList(part).iterator());

        PartTree tree = mock(PartTree.class);
        when(tree.iterator()).thenReturn(Collections.singletonList(orPart).iterator());

        String result = PartTreeConverter.toIndexedQuery(tree);
        assertEquals("name = ?", result);
    }

    @Test
    public void testToIndexedQueryMultipleParts() {
        Part part1 = mock(Part.class);
        when(part1.getProperty().getSegment()).thenReturn("age");
        when(part1.getType()).thenReturn(Part.Type.GREATER_THAN);

        Part part2 = mock(Part.class);
        when(part2.getProperty().getSegment()).thenReturn("salary");
        when(part2.getType()).thenReturn(Part.Type.LESS_THAN);

        OrPart orPart = mock(OrPart.class);
        when(orPart.iterator()).thenReturn(Arrays.asList(part1, part2).iterator());

        PartTree tree = mock(PartTree.class);
        when(tree.iterator()).thenReturn(Collections.singletonList(orPart).iterator());

        String result = PartTreeConverter.toIndexedQuery(tree);
        assertEquals("age > ? AND salary < ?", result);
    }

    @Test
    public void testToIndexedQueryMultipleOrParts() {
        Part part1 = mock(Part.class);
        when(part1.getProperty().getSegment()).thenReturn("name");
        when(part1.getType()).thenReturn(Part.Type.SIMPLE_PROPERTY);

        Part part2 = mock(Part.class);
        when(part2.getProperty().getSegment()).thenReturn("age");
        when(part2.getType()).thenReturn(Part.Type.GREATER_THAN);

        OrPart orPart1 = mock(OrPart.class);
        when(orPart1.iterator()).thenReturn(Arrays.asList(part1).iterator());

        OrPart orPart2 = mock(OrPart.class);
        when(orPart2.iterator()).thenReturn(Arrays.asList(part2).iterator());

        PartTree tree = mock(PartTree.class);
        when(tree.iterator()).thenReturn(Arrays.asList(orPart1, orPart2).iterator());

        String result = PartTreeConverter.toIndexedQuery(tree);
        assertEquals("name = ? OR age > ?", result);
    }

    @Test(expected = MappingException.class)
    public void testConvertOperatorThrowsExceptionForUnknownType() {
        Part.Type unknownType = mock(Part.Type.class);
        PartTreeConverter.convertOperator(unknownType);
    }
}
