
package graph.algorithm.traversal;

import graph.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class Dfs_RBL4_4340daa1Test {

    private Graph<String, ?> graph;

    @Before
    public void setUp() {
        graph = mock(Graph.class);
    }

    @Test
    public void testTraverseDepthFirst() {
        when(graph.nodes()).thenReturn(new HashSet<>(Arrays.asList("A", "B", "C", "D")));
        when(graph.successors("A")).thenReturn(Arrays.asList("B", "C"));
        when(graph.successors("B")).thenReturn(Arrays.asList("D"));
        when(graph.successors("C")).thenReturn(Arrays.asList());
        when(graph.successors("D")).thenReturn(Arrays.asList());

        List<String> result = Dfs.traverseDepthFirst(graph, "A");

        assertEquals(Arrays.asList("A", "C", "B", "D"), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTraverseDepthFirstWithInvalidStartingNode() {
        when(graph.nodes()).thenReturn(new HashSet<>(Arrays.asList("A", "B", "C")));

        Dfs.traverseDepthFirst(graph, "D");
    }

    @Test
    public void testTraverseDepthFirstWithSingleNode() {
        when(graph.nodes()).thenReturn(new HashSet<>(Arrays.asList("A")));
        when(graph.successors("A")).thenReturn(Arrays.asList());

        List<String> result = Dfs.traverseDepthFirst(graph, "A");

        assertEquals(Arrays.asList("A"), result);
    }

    @Test
    public void testTraverseDepthFirstWithDisconnectedGraph() {
        when(graph.nodes()).thenReturn(new HashSet<>(Arrays.asList("A", "B", "C", "D")));
        when(graph.successors("A")).thenReturn(Arrays.asList("B"));
        when(graph.successors("B")).thenReturn(Arrays.asList());
        when(graph.successors("C")).thenReturn(Arrays.asList("D"));
        when(graph.successors("D")).thenReturn(Arrays.asList());

        List<String> result = Dfs.traverseDepthFirst(graph, "A");

        assertEquals(Arrays.asList("A", "B"), result);
    }
}
