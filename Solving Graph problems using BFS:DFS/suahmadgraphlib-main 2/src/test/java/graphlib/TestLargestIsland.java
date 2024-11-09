package graphlib;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestLargestIsland {

    // Method to convert a string to InputStream
    private InputStream stringToInputStream(String graphData) {
        return new ByteArrayInputStream(graphData.getBytes());
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

}
