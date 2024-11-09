package graphlib;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestInverseGraph {

    // Method to convert a string to InputStream
    private InputStream stringToInputStream(String graphData) {
        return new ByteArrayInputStream(graphData.getBytes());
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

}
