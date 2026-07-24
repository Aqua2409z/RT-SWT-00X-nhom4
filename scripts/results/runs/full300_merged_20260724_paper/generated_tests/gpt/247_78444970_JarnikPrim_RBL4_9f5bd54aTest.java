
package graph.algorithm.mst;

import graph.model.MutableUndirectedGraph;
import graph.model.UndirectedGraph;
import org.junit.Test;

import java.util.function.ToIntFunction;

import static org.junit.Assert.*;

public class JarnikPrim_RBL4_9f5bd54aTest {

    @Test
    public void testComputeWithConnectedGraph() {
        MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");
        graph.addEdge("B", "C");
        graph.addEdge("A", "C");

        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> {
            if (edge.either().equals("A") && edge.another().equals("B")) return 1;
            if (edge.either().equals("B") && edge.another().equals("C")) return 2;
            if (edge.either().equals("A") && edge.another().equals("C")) return 3;
            return 0;
        };

        UndirectedGraph<String> mst = JarnikPrim.compute(graph, weighter);

        assertEquals(3, mst.nodes().size());
        assertEquals(2, mst.edges().size());
        assertTrue(mst.edges().stream().anyMatch(edge -> edge.either().equals("A") && edge.another().equals("B")));
        assertTrue(mst.edges().stream().anyMatch(edge -> edge.either().equals("B") && edge.another().equals("C")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testComputeWithDisconnectedGraph() {
        MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("A", "B");

        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> 1;

        JarnikPrim.compute(graph, weighter);
    }

    @Test
    public void testComputeWithSingleNodeGraph() {
        MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<>();
        graph.addNode("A");

        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> 1;

        UndirectedGraph<String> mst = JarnikPrim.compute(graph, weighter);

        assertEquals(1, mst.nodes().size());
        assertEquals(0, mst.edges().size());
    }

    @Test
    public void testComputeWithMultipleEdgesSameWeight() {
        MutableUndirectedGraph<String> graph = new MutableUndirectedGraph<>();
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");
        graph.addEdge("C", "D");

        ToIntFunction<UndirectedGraph.Edge<String>> weighter = edge -> 1;

        UndirectedGraph<String> mst = JarnikPrim.compute(graph, weighter);

        assertEquals(4, mst.nodes().size());
        assertEquals(3, mst.edges().size());
    }
}
