
package graph.algorithm.path;

import graph.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class FloydWarshall_RBL4_592f2b06Test {

    private Graph<String, Integer> graph;
    private ToIntFunction<Integer> weighter;

    @Before
    public void setUp() {
        graph = new Graph<>();
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 2);
        graph.addEdge("A", "C", 4);
        graph.addEdge("C", "D", 1);
        graph.addEdge("B", "D", 5);

        weighter = edge -> edge; // Assuming the edge weight is the integer value itself
    }

    @Test
    public void testCalculateShortestPaths() {
        FloydWarshallOutput<String> output = FloydWarshall.calculate(graph, weighter);

        // Test minimal distances
        assertEquals(Optional.of(1), output.getMinimalDistance("A", "B"));
        assertEquals(Optional.of(3), output.getMinimalDistance("A", "C"));
        assertEquals(Optional.of(4), output.getMinimalDistance("B", "D"));
        assertEquals(Optional.of(1), output.getMinimalDistance("C", "D"));
        assertEquals(Optional.empty(), output.getMinimalDistance("A", "E")); // Unknown node
    }

    @Test
    public void testGetShortestPath() {
        FloydWarshallOutput<String> output = FloydWarshall.calculate(graph, weighter);

        // Test shortest paths
        assertEquals(Optional.of(Arrays.asList("A", "B")), output.getShortestPath("A", "B"));
        assertEquals(Optional.of(Arrays.asList("A", "B", "C")), output.getShortestPath("A", "C"));
        assertEquals(Optional.of(Arrays.asList("B", "C", "D")), output.getShortestPath("B", "D"));
        assertEquals(Optional.of(Arrays.asList("C", "D")), output.getShortestPath("C", "D"));
        assertEquals(Optional.empty(), output.getShortestPath("A", "E")); // Unknown node
    }

    @Test
    public void testUnknownNodes() {
        FloydWarshallOutput<String> output = FloydWarshall.calculate(graph, weighter);

        // Test with unknown nodes
        assertEquals(Optional.empty(), output.getMinimalDistance("A", "E"));
        assertEquals(Optional.empty(), output.getShortestPath("A", "E"));
    }
}
