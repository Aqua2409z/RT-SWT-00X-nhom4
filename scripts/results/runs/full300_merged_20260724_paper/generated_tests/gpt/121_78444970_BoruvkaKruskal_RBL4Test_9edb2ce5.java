
package graph.algorithm.mst;

import graph.model.MutableUndirectedGraph;
import graph.model.UndirectedGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.function.ToIntFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BoruvkaKruskal_RBL4Test_9edb2ce5 {
    private MutableUndirectedGraph<String> graph;

    @Before
    public void setUp() {
        graph = new MutableUndirectedGraph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 3);
        graph.addEdge("B", "C", 1);
        graph.addEdge("B", "D", 4);
        graph.addEdge("C", "D", 2);
    }

    @Test
    public void testComputeMinimumSpanningTree() {
        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> edge.weight();
        UndirectedGraph<String> mst = BoruvkaKruskal.compute(graph, weighter);

        // Check that the minimum spanning tree has the correct number of edges
        assertEquals(3, mst.edges().size());

        // Check that the edges in the minimum spanning tree are correct
        assertTrue(mst.edges().stream().anyMatch(edge -> edge.either().equals("A") && edge.another().equals("B")));
        assertTrue(mst.edges().stream().anyMatch(edge -> edge.either().equals("B") && edge.another().equals("C")));
        assertTrue(mst.edges().stream().anyMatch(edge -> edge.either().equals("C") && edge.another().equals("D")));
    }

    @Test
    public void testComputeMinimumSpanningTreeWithNoEdges() {
        MutableUndirectedGraph<String> emptyGraph = new MutableUndirectedGraph<>();
        emptyGraph.addNode("A");
        emptyGraph.addNode("B");

        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> edge.weight();
        UndirectedGraph<String> mst = BoruvkaKruskal.compute(emptyGraph, weighter);

        // Check that the minimum spanning tree has no edges
        assertEquals(0, mst.edges().size());
    }

    @Test
    public void testComputeMinimumSpanningTreeWithSingleNode() {
        MutableUndirectedGraph<String> singleNodeGraph = new MutableUndirectedGraph<>();
        singleNodeGraph.addNode("A");

        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> edge.weight();
        UndirectedGraph<String> mst = BoruvkaKruskal.compute(singleNodeGraph, weighter);

        // Check that the minimum spanning tree has no edges
        assertEquals(0, mst.edges().size());
    }
}
