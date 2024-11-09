package graphlib;

import java.io.InputStream;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Collection;
import java.util.HashMap;

public class Graph
{
    private Map<String, Node> nodes;

    public Graph()
    {
        nodes = new HashMap<>();
    }

    public Node getOrCreateNode(String name)
    {
        Node node = nodes.get(name);
        if (node == null)
        {
            node = new Node(name);
            nodes.put(name, node);
        }
        return node;
    }

    public boolean containsNode(String name)
    {
        return nodes.containsKey(name);
    }

    public Collection<Node> getAllNodes()
    {
        return nodes.values();
    }

    public void bfs(String startNodeName, NodeVisitor visitor)
    {
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        Node start = nodes.get(startNodeName);
        if (start == null)
        {
            throw new IllegalArgumentException("Node " + startNodeName + " not found");
        }
        queue.add(start);
        while (!queue.isEmpty())
        {
            Node node = queue.remove();
            if (visited.contains(node))
            {
                // skip nodes we have already visited
                continue;
            }
            // visit the node, and mark it as visited
            visitor.visit(node);
            visited.add(node);
            for (Node neighbor : node.getNeighbors())
            {
                if (!visited.contains(neighbor))
                {
                    queue.add(neighbor);
                }
            }
        }
    }

    public void dfs(String startNodeName, NodeVisitor visitor)
    {
        
        Node startNode = nodes.get(startNodeName);
        if (startNode == null)
        {
            throw new IllegalArgumentException("Node " + startNodeName + " not found");
        }
        Set<Node> visited = new HashSet<>();
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        while (!stack.isEmpty())
        {
            Node node = stack.pop();
            if (visited.contains(node))
            {
                // skip nodes we have already visited
                continue;
            }
            // visit the node, and mark it as visited
            visitor.visit(node);
            visited.add(node);
            for (Node neighbor : node.getNeighbors())
            {
                if (!visited.contains(neighbor))
                {
                    stack.push(neighbor);
                }
            }
        }
    }

    private static class Path implements Comparable<Path>
    {
        private Node node;
        private double weight;
        // TODO: include the path
        //private List<Node> path;

        public Path(Node node, double weight)
        {
            this.node = node;
            this.weight = weight;
        }

        public Node getNode()
        {
            return node;
        }

        public double getWeight()
        {
            return weight;
        }

        public int compareTo(Path other)
        {
            return Double.compare(weight, other.weight);
        }
    }

    public Map<Node, Double> dijkstra(String startNodeName)
    {
        Map<Node, Double> distances = new HashMap<>();
        
        Node start = nodes.get(startNodeName);
        PriorityQueue<Path> pq = new PriorityQueue<>();

        pq.add(new Path(start, 0.0));

        while (!pq.isEmpty() && distances.size() < nodes.size())
        {
            Path edge = pq.remove();
            Node node = edge.getNode();
            if (distances.containsKey(node)) continue;

            double distance = edge.getWeight();

            distances.put(node, distance);
            
            for (Node neighbor : node.getNeighbors())
            {
                if (!distances.containsKey(neighbor))
                {
                    double newDistance = distance + node.getWeight(neighbor);
                    pq.add(new Path(neighbor, newDistance));
                }
            }
        }
        
        return distances;
    }

    private static interface MyQueue
    {
        void add(Node node);
        Node remove();
        boolean isEmpty();
    }

    private void xfs(String startNodeName, NodeVisitor visitor, MyQueue queue)
    {
        Node startNode = nodes.get(startNodeName);
        if (startNode == null)
        {
            throw new IllegalArgumentException("Node " + startNodeName + " not found");
        }
        Set<Node> visited = new HashSet<>();
        queue.add(startNode);
        while (!queue.isEmpty())
        {
            Node node = queue.remove();
            if (visited.contains(node))
            {
                // skip nodes we have already visited
                continue;
            }
            // visit the node, and mark it as visited
            visitor.visit(node);
            visited.add(node);
            for (Node neighbor : node.getNeighbors())
            {
                if (!visited.contains(neighbor))
                {
                    queue.add(neighbor);
                }
            }
        }
    }

    public void bfs2(String startNodeName, NodeVisitor visitor)
    {
        xfs(startNodeName, visitor, new MyQueue()
        {
            private Queue<Node> queue = new LinkedList<>();

            public void add(Node node)
            {
                queue.add(node);
            }

            public Node remove()
            {
                return queue.remove();
            }

            public boolean isEmpty()
            {
                return queue.isEmpty();
            }
        });
    }

    public void dfs2(String startNodeName, NodeVisitor visitor)
    {
        xfs(startNodeName, visitor, new MyQueue()
        {
            private Stack<Node> stack = new Stack<>();

            public void add(Node node)
            {
                stack.push(node);
            }

            public Node remove()
            {
                return stack.pop();
            }

            public boolean isEmpty()
            {
                return stack.isEmpty();
            }
        });
    }

    /**
     * Returns a string representation of the graph in GraphViz format.
     * 
     * This is for an <b>undirected</b>, <b>weighted</b> graph
     * @return
     */
    public String toUndirectedWeightedGraphViz()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("graph G {\n");
        for (Node node : nodes.values())
        {
            for (Node neighbor : node.getNeighbors())
            {
                // make sure we only add each edge once
                if (node.getName().compareTo(neighbor.getName()) < 0)
                {
                    sb.append(String.format("  %s -- %s [label=\"%.1f\"];\n", node.getName(), neighbor.getName(), node.getWeight(neighbor)));
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
    /**
     * Returns a string representation of the graph in GraphViz format.
     * 
     * This is for an <b>directed</b>, <b>weighted</b> graph
     * @return
     */
    public String toDirectedWeightedGraphViz()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        for (Node node : nodes.values())
        {
            for (Node neighbor : node.getNeighbors())
            {
                // append using String.format
                sb.append(String.format("  %s -> %s [label=\"%.1f\"];\n", node.getName(), neighbor.getName(), node.getWeight(neighbor)));
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Returns a string representation of the graph in GraphViz format.
     * 
     * This is for an <b>directed</b>, <b>unweighted</b> graph
     * @return
     */
    public String toDirectedUnweightedGraphViz()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph G {\n");
        for (Node node : nodes.values())
        {
            for (Node neighbor : node.getNeighbors())
            {
                // append using String.format
                sb.append(String.format("  %s -> %s;\n", node.getName(), neighbor.getName()));
            }
        }
        sb.append("}\n");
        return sb.toString();
    }

    /**
     * Returns a string representation of the graph in GraphViz format.
     * 
     * This is for an <b>undirected</b>, <b>unweighted</b> graph
     * @return
     */
    public String toUndirectedUnweightedGraphViz()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("graph G {\n");
        for (Node node : nodes.values())
        {
            for (Node neighbor : node.getNeighbors())
            {
                // make sure we only add each edge once
                if (node.getName().compareTo(neighbor.getName()) < 0)
                {
                    sb.append("  " + node.getName() + " -- " + neighbor.getName() + ";\n");
                }
            }
        }
        sb.append("}\n");
        return sb.toString();
    }
    
    public static Graph readUndirectedUnweightedGraph(InputStream in)
    {
        Graph graph = new Graph();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext())
        {
            String nameA = scanner.next();
            String nameB = scanner.next();
            Node nodeA = graph.getOrCreateNode(nameA);
            Node nodeB = graph.getOrCreateNode(nameB);
            nodeA.addUnweightedUndirectedEdge(nodeB);
        }
        scanner.close();
        return graph;
    }

    public static Graph readDirectedUnweightedGraph(InputStream in)
    {
        Graph graph = new Graph();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext())
        {
            String nameA = scanner.next();
            String nameB = scanner.next();
            Node nodeA = graph.getOrCreateNode(nameA);
            Node nodeB = graph.getOrCreateNode(nameB);
            nodeA.addUnweightedDirectedEdge(nodeB);
        }
        scanner.close();
        return graph;
    }

    public static Graph readUndirectedWeightedGraph(InputStream in)
    {
        Graph graph = new Graph();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext())
        {
            String nameA = scanner.next();
            String nameB = scanner.next();
            double weight = scanner.nextDouble();
            Node nodeA = graph.getOrCreateNode(nameA);
            Node nodeB = graph.getOrCreateNode(nameB);
            nodeA.addUndirectedEdge(nodeB, weight);
        }
        scanner.close();
        return graph;
    }

    public static Graph readDirectedWeightedGraph(InputStream in)
    {
        Graph graph = new Graph();
        Scanner scanner = new Scanner(in);
        while (scanner.hasNext())
        {
            String nameA = scanner.next();
            String nameB = scanner.next();
            double weight = scanner.nextDouble();
            Node nodeA = graph.getOrCreateNode(nameA);
            Node nodeB = graph.getOrCreateNode(nameB);
            nodeA.addDirectedEdge(nodeB, weight);
        }
        scanner.close();
        return graph;
    }

    public int getNumComponents()
    {
        Set<Node> visited = new HashSet<>();
        int numComponents = 0;
        for (Node node : nodes.values())
        {
            if (!visited.contains(node))
            {
                numComponents++;
                dfs(node.getName(), new NodeVisitor()
                {
                    public void visit(Node node)
                    {
                        visited.add(node);
                    }
                });
            }
        }
        return numComponents;
    }

    public static Graph readIslandFile(InputStream in) 
    {
        Graph graph = new Graph();
        Scanner scanner = new Scanner(in);
        int numRows = scanner.nextInt();
        int numCols = scanner.nextInt();
        // TODO: pick up here

        while (scanner.hasNext())
        {
            String nameA = scanner.next();
            String nameB = scanner.next();
            Node nodeA = graph.getOrCreateNode(nameA);
            Node nodeB = graph.getOrCreateNode(nameB);
            nodeA.addUnweightedUndirectedEdge(nodeB);
        }
        scanner.close();
        return graph;
    }

    public static Graph readIslandFile2(InputStream in){
        Scanner scanner = new Scanner(in);
        int numRows = scanner.nextInt();
        int numCols = scanner.nextInt();
        scanner.nextLine(); // Consume the rest of the line

        int[][] island = new int[numRows][numCols];
        Graph graph = new Graph();

        // Read the island data
        for (int i = 0; i < numRows; i++) {
            String row = scanner.nextLine();
            for (int j = 0; j < numCols; j++) {
                island[i][j] = Character.getNumericValue(row.charAt(j));
            }
        }

        // Convert island to graph
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                if (island[i][j] == 1) {
                    String name = graph.nameMaker(i, j);
                    Node node = graph.getOrCreateNode(name);

                    // Check and add connections
                    if (i > 0 && island[i - 1][j] == 1) { // Up
                        String upName = graph.nameMaker(i - 1, j);
                        Node upNode = graph.getOrCreateNode(upName);
                        node.addUnweightedUndirectedEdge(upNode);
                    }
                    if (j > 0 && island[i][j - 1] == 1) { // Left
                        String leftName = graph.nameMaker(i, j - 1);
                        Node leftNode = graph.getOrCreateNode(leftName);
                        node.addUnweightedUndirectedEdge(leftNode);
                    }
                    if (i < numRows - 1 && island[i + 1][j] == 1) { // Down
                        String downName = graph.nameMaker(i + 1, j);
                        Node downNode = graph.getOrCreateNode(downName);
                        node.addUnweightedUndirectedEdge(downNode);
                    }
                    if (j < numCols - 1 && island[i][j + 1] == 1) { // Right
                        String rightName = graph.nameMaker(i, j + 1);
                        Node rightNode = graph.getOrCreateNode(rightName);
                        node.addUnweightedUndirectedEdge(rightNode);
                    }
                    if (i>0 && i< numRows-1 && j>0 &&j <numCols-1 && island[i-1][j-1]==1) {
                        String diagonalback = graph.nameMaker(i-1, j-1);
                        Node diagonalbackNode = graph.getOrCreateNode(diagonalback);
                        node.addUnweightedUndirectedEdge(diagonalbackNode);
                    }
                    if (i>0 && i< numRows-1 && j>0 &&j <numCols-1 && island[i+1][j+1]==1) {
                        String diagonalforward = graph.nameMaker(i+1, j+1);
                        Node diagonalforwardNode = graph.getOrCreateNode(diagonalforward);
                        node.addUnweightedUndirectedEdge(diagonalforwardNode);
                    }
                    if (i>0 && i< numRows-1 && j>0 &&j <numCols-1 && island[i-1][j+1]==1) {
                        String diagonalright = graph.nameMaker(i-1, j+1);
                        Node diagonalrightNode = graph.getOrCreateNode(diagonalright);
                        node.addUnweightedUndirectedEdge(diagonalrightNode);
                    }
                    if (i>0 && i< numRows-1 && j>0 &&j <numCols-1 && island[i+1][j-1]==1) {
                        String diagonalleft = graph.nameMaker(i+1, j-1);
                        Node diagonalleftNode = graph.getOrCreateNode(diagonalleft);
                        node.addUnweightedUndirectedEdge(diagonalleftNode);
                    }
                }
            }
        }

        scanner.close();
        return graph;
    }

  

    public String nameMaker(int i,int j){
        return i + "," + j;
    }

    
    public static Graph inverseGraph(InputStream in) {
        Graph originalGraph = readUndirectedUnweightedGraph(in);
        Graph invertedGraph = new Graph();
        for (Node node : originalGraph.getAllNodes()) {
            String nodeName = node.getName();
            if (!invertedGraph.containsNode(nodeName)) {
                invertedGraph.getOrCreateNode(nodeName);
            }
            for (Node neighbor : originalGraph.getAllNodes()) {
                String neighborName = neighbor.getName();
                if (!node.hasEdge(neighbor) && !nodeName.equals(neighborName)) {
                    invertedGraph.getOrCreateNode(nodeName).addUnweightedUndirectedEdge(invertedGraph.getOrCreateNode(neighborName));
                }
            }
        }
        return invertedGraph;
    }

    public Map<String, Set<String>> getReachableNodes() {
        Map<String, Set<String>> reachableNodes = new HashMap<>();

        for (Node node : nodes.values()) {
            Set<String> reachable = new HashSet<>();
            dfs(node.getName(), new NodeVisitor() {
                @Override
                public void visit(Node n) {
                    reachable.add(n.getName());
                }
            });
            reachableNodes.put(node.getName(), reachable);
        }

        return reachableNodes;
    }

   
}

