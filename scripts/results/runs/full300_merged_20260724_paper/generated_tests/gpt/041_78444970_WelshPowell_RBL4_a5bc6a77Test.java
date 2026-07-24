
package graph.algorithm.color;

import graph.model.Graph;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class WelshPowell_RBL4_a5bc6a77Test {

    private Graph<String, ?> graph;

    @Before
    public void setUp() {
        graph = new Graph<String, Object>() {
            private final Map<String, Set<String>> adjacencyList = new HashMap<>();

            @Override
            public Set<String> nodes() {
                return adjacencyList.keySet();
            }

            @Override
            public Set<String> adjacent(String node) {
                return adjacencyList.getOrDefault(node, Set.of());
            }

            @Override
            public boolean isAdjacent(String node1, String node2) {
                return adjacent(node1).contains(node2);
            }

            public void addNode(String node) {
                adjacencyList.putIfAbsent(node, Set.of());
            }

            public void addEdge(String node1, String node2) {
                adjacencyList.putIfAbsent(node1, Set.of());
                adjacencyList.putIfAbsent(node2, Set.of());
                adjacencyList.get(node1).add(node2);
                adjacencyList.get(node2).add(node1);
            }
        };
    }

    @Test
    public void testColorByWelshPowell() {
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");
        graph.addEdge("A", "C");
        graph.addEdge("B", "C");

        Map<String, Integer> colors = WelshPowell.colorByWelshPowell(graph);

        assertEquals(2, (int) colors.get("A"));
        assertEquals(2, (int) colors.get("B"));
        assertEquals(2, (int) colors.get("C"));
        assertEquals(0, (int) colors.get("D"));
    }

    @Test
    public void testColorWithDisconnectedGraph() {
        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addNode("D");
        graph.addEdge("A", "B");

        Map<String, Integer> colors = WelshPowell.colorByWelshPowell(graph);

        assertEquals(1, (int) colors.get("A"));
        assertEquals(1, (int) colors.get("B"));
        assertEquals(0, (int) colors.get("C"));
        assertEquals(0, (int) colors.get("D"));
    }

    @Test
    public void testColorWithSingleNode() {
        graph.addNode("A");

        Map<String, Integer> colors = WelshPowell.colorByWelshPowell(graph);

        assertEquals(0, (int) colors.get("A"));
    }

    @Test
    public void testColorWithNoNodes() {
        Map<String, Integer> colors = WelshPowell.colorByWelshPowell(graph);

        assertEquals(0, colors.size());
    }
}
