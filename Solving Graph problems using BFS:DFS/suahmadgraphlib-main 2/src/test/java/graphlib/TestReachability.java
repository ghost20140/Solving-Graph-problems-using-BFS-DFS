package graphlib;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestReachability {

    // Method to convert a string to InputStream
    private InputStream stringToInputStream(String graphData) {
        return new ByteArrayInputStream(graphData.getBytes());
    }

    @Test
    public void testReachableNodesGraph() {
        String input = "A B\nA C\nB D";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Graph graph = Graph.readDirectedUnweightedGraph(stream);

        Map<String, Set<String>> reachableNodes = graph.getReachableNodes();

        assertEquals(Set.of("A", "B", "C", "D"), reachableNodes.get("A"));
        assertEquals(Set.of("B", "D"), reachableNodes.get("B"));
        assertEquals(Set.of("C"), reachableNodes.get("C"));
        assertEquals(Set.of("D"), reachableNodes.get("D"));
    }
    
    @Test
    public void testReachableNodesDisconnectedGraph() {
        String input = "A B\nC D";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Graph graph = Graph.readDirectedUnweightedGraph(stream);

        Map<String, Set<String>> reachableNodes = graph.getReachableNodes();

        assertEquals(Set.of("A", "B"), reachableNodes.get("A"));
        assertEquals(Set.of("B"), reachableNodes.get("B"));
        assertEquals(Set.of("C", "D"), reachableNodes.get("C"));
        assertEquals(Set.of("D"), reachableNodes.get("D"));
    }

    @Test
    public void testReachableNodesSingleNodeGraph() {
        String input = "A A";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Graph graph = Graph.readDirectedUnweightedGraph(stream);

        Map<String, Set<String>> reachableNodes = graph.getReachableNodes();

        assertEquals(Set.of("A"), reachableNodes.get("A"));
    }

    @Test
    public void testReachableNodesCyclicGraph() {
        String input = "A B\nB C\nC A";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Graph graph = Graph.readDirectedUnweightedGraph(stream);

        Map<String, Set<String>> reachableNodes = graph.getReachableNodes();

        assertEquals(Set.of("A", "B", "C"), reachableNodes.get("A"));
        assertEquals(Set.of("A", "B", "C"), reachableNodes.get("B"));
        assertEquals(Set.of("A", "B", "C"), reachableNodes.get("C"));
    }
}
