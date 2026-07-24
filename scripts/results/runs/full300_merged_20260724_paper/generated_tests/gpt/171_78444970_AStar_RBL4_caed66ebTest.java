
package graph.algorithm.path;

import graph.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

import static org.junit.Assert.assertEquals;

public class AStar_RBL4_caed66ebTest {
    private Graph<String, Integer> graph;
    private ToIntFunction<Integer> weighter;
    private ToIntFunction<String> heuristic;

    @Before
    public void setUp() {
        graph = new Graph<>();
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("B", "D", 5);
        graph.addEdge("C", "D", 1);

        weighter = edge -> edge; // weight is the edge value itself
        heuristic = node -> {
            Map<String, Integer> heuristicMap = new HashMap<>();
            heuristicMap.put("A", 7);
            heuristicMap.put("B", 6);
            heuristicMap.put("C", 2);
            heuristicMap.put("D", 0);
            return heuristicMap.getOrDefault(node, Integer.MAX_VALUE);
        };
    }

    @Test
    public void testAStarFindsOptimalPath() {
        AStarOutput<String> output = AStar.aStar(graph, "A", "D", weighter, heuristic);

        assertEquals(3, output.getDistanceSoFar("D"));
        assertEquals("C", output.getPrevious("D"));
        assertEquals("B", output.getPrevious("C"));
        assertEquals("A", output.getPrevious("B"));
    }

    @Test
    public void testAStarNoPath() {
        Graph<String, Integer> emptyGraph = new Graph<>();
        AStarOutput<String> output = AStar.aStar(emptyGraph, "A", "B", weighter, heuristic);

        assertEquals(Integer.MAX_VALUE, output.getDistanceSoFar("B"));
        assertEquals(null, output.getPrevious("B"));
    }

    @Test
    public void testAStarSingleNode() {
        Graph<String, Integer> singleNodeGraph = new Graph<>();
        singleNodeGraph.addNode("A");
        AStarOutput<String> output = AStar.aStar(singleNodeGraph, "A", "A", weighter, heuristic);

        assertEquals(0, output.getDistanceSoFar("A"));
        assertEquals(null, output.getPrevious("A"));
    }
}
