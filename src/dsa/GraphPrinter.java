package dsa;

import java.util.*;

public class GraphPrinter {

    /**
     * Prints the graph starting from the given node.
     * format: [Node Value] -> Neighbor1, Neighbor2, ...
     */
    public static void printGraph(GraphNode startNode) {
        if (startNode == null) {
            System.out.println("Graph is empty (node is null)");
            return;
        }

        // Use a Set to keep track of visited nodes based on their unique value/reference
        Set<Integer> visited = new HashSet<>();
        Queue<GraphNode> queue = new LinkedList<>();

        queue.offer(startNode);
        visited.add(startNode.val);

        System.out.println("--- Graph Structure ---");

        while (!queue.isEmpty()) {
            GraphNode curr = queue.poll();

            // Build list of neighbor values for printing
            List<String> neighborValues = new ArrayList<>();
            for (GraphNode neighbor : curr.neighbors) {
                neighborValues.add(String.valueOf(neighbor.val));

                // If we haven't visited this neighbor, add to queue
                if (!visited.contains(neighbor.val)) {
                    visited.add(neighbor.val);
                    queue.offer(neighbor);
                }
            }

            // Print format: [1] -> 2, 4
            System.out.printf("[%d] -> %s%n", curr.val, String.join(", ", neighborValues));
        }
        System.out.println("-----------------------");
    }

    // --- Helper to Create a Test Graph Quickly ---
    // Input: [[2,4],[1,3],[2,4],[1,3]] for 1-based index
    public static GraphNode buildTestGraph(int[][] adjList) {
        if (adjList.length == 0) return null;

        // Create all nodes first (1-indexed based on typical LeetCode problems)
        GraphNode[] nodes = new GraphNode[adjList.length + 1];
        for (int i = 1; i < nodes.length; i++) {
            nodes[i] = new GraphNode(i);
        }

        // Connect neighbors
        for (int i = 0; i < adjList.length; i++) {
            GraphNode curr = nodes[i + 1];
            for (int neighborVal : adjList[i]) {
                curr.neighbors.add(nodes[neighborVal]);
            }
        }
        return nodes[1]; // Return node 1
    }


}