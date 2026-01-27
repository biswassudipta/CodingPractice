package dsa;

import java.util.Arrays;

public class BellmanFordSingleSourceShortestPath {
    public int[] findBellmanFordSingleSourceShortestPaths(int V, int[][] edges, int src) {
        int[] distance = new int[V];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[src] = 0;
        int iteration = V - 1;

        while (iteration > 0) {
            for (int[] edge : edges) {
                int v1 = edge[0];
                int v2 = edge[1];
                int distanceFromV1ToV2 = edge[2];
                if (distance[v1] != Integer.MAX_VALUE)
                    distance[v2] = Math.min(distance[v2], distance[v1] + distanceFromV1ToV2);
            }
            iteration--;
        }
        return distance;
    }

}
