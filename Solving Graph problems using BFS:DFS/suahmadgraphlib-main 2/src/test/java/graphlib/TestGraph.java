package graphlib;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGraph {

    // Method to convert a string to InputStream
    private InputStream stringToInputStream(String graphData) {
        return new ByteArrayInputStream(graphData.getBytes());
    }

    @Test
    public void testGetOrCreateNode() {
        Graph graph = new Graph();
        Node a = graph.getOrCreateNode("A");
        Node b = graph.getOrCreateNode("B");
        a.addUndirectedEdge(b, 1);
        assert a.getNeighbors().contains(b);
        assert b.getNeighbors().contains(a);
        assert a.getWeight(b) == 1;
        assert b.getWeight(a) == 1;
    }

    @Test
    public void testNoDuplicates() {
        Graph graph = new Graph();
        Node a = graph.getOrCreateNode("A");
        Node b = graph.getOrCreateNode("A");
        assert a == b;
    }

    @Test
    public void testStaticFactoryMethod() throws Exception {
        Graph g = Graph.readUndirectedUnweightedGraph(new FileInputStream("datafiles/graph1.txt"));
        Node zero = g.getOrCreateNode("0");
        Node one = g.getOrCreateNode("1");
        Node two = g.getOrCreateNode("2");
        Node three = g.getOrCreateNode("3");
        Node four = g.getOrCreateNode("4");
        assert zero.hasEdge(one);
        assert one.hasEdge(two);
        assert three.hasEdge(four);
        assert four.hasEdge(three);
    }

    @Test
    public void testSingleGraph() throws Exception {
        Graph g = Graph.readUndirectedUnweightedGraph(new FileInputStream("datafiles/singlegraph.txt"));
        assertEquals(1, g.getNumComponents());
    }

    @Test
    public void testTwoDisconnectedGraph() throws Exception {
        Graph g = Graph.readUndirectedUnweightedGraph(new FileInputStream("datafiles/twodisconnectedgraph.txt"));
        assertEquals(2, g.getNumComponents());
    }

    @Test
    public void testThreeDisconnectedGraphs() throws Exception {
        Graph g = Graph.readUndirectedUnweightedGraph(new FileInputStream("datafiles/threedisconnectedgraphs.txt"));
        assertEquals(3, g.getNumComponents());
    }

    static class CountingVisitor implements NodeVisitor {
        private int count = 0;

        public void visit(Node node) {
            count += 1;
        }

        public int getCount() {
            return count;
        }
    }

    @Test
    public void testLargestIsland() throws Exception {
        Graph g = Graph.readIslandFile2(new FileInputStream("datafiles/island1.txt"));
        Collection<Node> nodes = g.getAllNodes();
        int max = 0;

        for (Node node : nodes) {
            CountingVisitor countingVisitor = new CountingVisitor();
            g.bfs(node.getName(), countingVisitor);
            if (countingVisitor.getCount() > max) {
                max = countingVisitor.getCount();
            }
        }
        System.out.printf("max: %d\n", max);
        assertEquals(5, max);
    }

    @Test
    public void testLargestIsland2() throws Exception {
        Graph g = Graph.readIslandFile2(new FileInputStream("datafiles/islands2.txt"));
        Collection<Node> nodes = g.getAllNodes();
        int max = 0;

        for (Node node : nodes) {
            CountingVisitor countingVisitor = new CountingVisitor();
            g.bfs(node.getName(), countingVisitor);
            if (countingVisitor.getCount() > max) {
                max = countingVisitor.getCount();
            }
        }
        System.out.printf("max: %d\n", max);
        assertEquals(12, max);
    }

    @Test
    public void testLargestIsland3() throws Exception {
        String islandData = "4 5\n" +
        "11000\n" +
        "11010\n" +
        "00100\n" +
        "00000\n";
        InputStream inputStream = new ByteArrayInputStream(islandData.getBytes());
        Graph g = Graph.readIslandFile2(inputStream);
        Collection<Node> nodes = g.getAllNodes();
        int max = 0;

        for (Node node : nodes) {
            CountingVisitor countingVisitor = new CountingVisitor();
            g.bfs(node.getName(), countingVisitor);
            if (countingVisitor.getCount() > max) {
                max = countingVisitor.getCount();
            }
        }
        System.out.printf("max: %d\n", max);
        assertEquals(6, max);
    }

    @Test
    public void testLargestIslandWithNoConnections() throws Exception {
        String islandData = "5 5\n" +
                            "10001\n" +
                            "00100\n" +
                            "00000\n" +
                            "10001\n" +
                            "00100\n";

        InputStream inputStream = new ByteArrayInputStream(islandData.getBytes());
        Graph g = Graph.readIslandFile2(inputStream);
        Collection<Node> nodes = g.getAllNodes();
        int max = 0;

        for (Node node : nodes) {
            CountingVisitor countingVisitor = new CountingVisitor();
            g.bfs(node.getName(), countingVisitor);
            if (countingVisitor.getCount() > max) {
                max = countingVisitor.getCount();
            }
        }
        System.out.printf("max: %d\n", max);
        assertEquals(1, max);
    }



    @Test
    public void testCompleteGraphInverted() {
        String graphData = "A B\nA C\nB C"; 
        InputStream in = stringToInputStream(graphData);
        Graph invertedGraph = Graph.inverseGraph(in);

        Node a = invertedGraph.getOrCreateNode("A");
        Node b = invertedGraph.getOrCreateNode("B");
        Node c = invertedGraph.getOrCreateNode("C");

        assert a != null && b != null && c != null : "Nodes A, B, and C should exist";
        assert !a.hasEdge(b) && !a.hasEdge(c) : "Node A should have no edges";
        assert !b.hasEdge(a) && !b.hasEdge(c) : "Node B should have no edges";
        assert !c.hasEdge(a) && !c.hasEdge(b) : "Node C should have no edges";
    }
    
    @Test
    public void testGraphInverted() {
        String graphData = "A B\nB C"; 
        InputStream in = stringToInputStream(graphData);
        Graph invertedGraph = Graph.inverseGraph(in);
    
        Node a = invertedGraph.getOrCreateNode("A");
        Node b = invertedGraph.getOrCreateNode("B");
        Node c = invertedGraph.getOrCreateNode("C");
    
        assert a != null && b != null && c != null : "Nodes A, B, and C should exist";
        assert a.hasEdge(c) && !a.hasEdge(b) : "Node A should have an edge to C but not to B";
        assert c.hasEdge(a) && !c.hasEdge(b) : "Node C should have an edge to A but not to B";
        assert !b.hasEdge(a) && !b.hasEdge(c) : "Node B should not have edges to A and C";
        assert b.getNeighbors().isEmpty() : "Node B should have no edges";
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
