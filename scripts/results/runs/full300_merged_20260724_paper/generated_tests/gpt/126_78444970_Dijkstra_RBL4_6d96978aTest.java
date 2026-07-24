
package graph.algorithm.path;

import graph.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.Assert.assertEquals;

public class Dijkstra_RBL4_6d96978aTest {
    private Graph<String, Integer> graph;
    private DijkstraOutput<String> output;

    @Before
    public void setUp() {
        graph = new Graph<>();
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 1);
    }

    @Test
    public void testDijkstra() {
        ToIntFunction<Integer> weighter = edge -> edge;

        output = Dijkstra.dijkstra(graph, "A", weighter);

        assertEquals(0, output.getDistance("A"));
        assertEquals(1, output.getDistance("B"));
        assertEquals(3, output.getDistance("C"));
        assertEquals(4, output.getDistance("D"));

        assertEquals("A", output.getPrevious("B"));
        assertEquals("B", output.getPrevious("C"));
        assertEquals("C", output.getPrevious("D"));
    }

    @Test
    public void testDijkstraWithDisconnectedGraph() {
        graph.addEdge("E", "F", 2); // Adding a disconnected edge

        ToIntFunction<Integer> weighter = edge -> edge;

        output = Dijkstra.dijkstra(graph, "A", weighter);

        assertEquals(0, output.getDistance("A"));
        assertEquals(1, output.getDistance("B"));
        assertEquals(3, output.getDistance("C"));
        assertEquals(4, output.getDistance("D"));
        assertEquals(Integer.MAX_VALUE, output.getDistance("E"));
        assertEquals(Integer.MAX_VALUE, output.getDistance("F"));
    }

    @Test
    public void testDijkstraWithSingleNode() {
        Graph<String, Integer> singleNodeGraph = new Graph<>();
        singleNodeGraph.addNode("A");

        ToIntFunction<Integer> weighter = edge -> edge;

        output = Dijkstra.dijkstra(singleNodeGraph, "A", weighter);

        assertEquals(0, output.getDistance("A"));
        assertEquals(null, output.getPrevious("A"));
    }
}
