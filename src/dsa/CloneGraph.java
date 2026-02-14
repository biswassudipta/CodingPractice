package dsa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;

public class CloneGraph {
    public GraphNode cloneGraph(GraphNode graphNode) {
        if (graphNode == null) {
            return null;
        }

        GraphNode[] visited = new GraphNode[101];
        Queue<GraphNode> q1 = new ArrayDeque<>();

        q1.offer(graphNode);
        GraphNode clone = new GraphNode(graphNode.val, new ArrayList<>());
        visited[graphNode.val] = clone;

        while (!q1.isEmpty()) {

            GraphNode currentGraphNode = q1.poll();
            GraphNode cloneGraphNode = visited[currentGraphNode.val];

            for (GraphNode child : currentGraphNode.neighbors) {
                if (visited[child.val] == null) {

                    GraphNode cloneChild = new GraphNode(child.val, new ArrayList<>());

                    visited[child.val] = cloneChild;

                    q1.offer(child);

                }
                cloneGraphNode.neighbors.add(visited[child.val]);

            }

        }
        return clone;

    }
}
