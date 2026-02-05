package dsa;

import java.util.HashMap;
import java.util.Map;

public class TravellingSalesman {
    Map<String, Integer> memo = new HashMap<>();
    int n;
    int sourceVertex;
    int[][] distances;

    public int findMinimumDistance(int sourceVertex, int[][] distances) {
        this.distances = distances;
        this.n = distances.length;
        this.sourceVertex = sourceVertex;
        boolean[] visited = new boolean[n];
        int count = 1;
        visited[sourceVertex] = true;
        return findMinimumDistance(sourceVertex, distances, visited, count);

    }

    private int findMinimumDistance(int currentVertex, int[][] distances, boolean[] visited, int count) {
        if (count == n) {
            return distances[currentVertex][sourceVertex];
        }
        String key = formKey(currentVertex, visited);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        int min = Integer.MAX_VALUE;
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                visited[v] = true;
                int distance = distances[v][currentVertex] + findMinimumDistance(v, distances, visited, count + 1);
                if (distance < min) {
                    min = distance;
                }
                visited[v] = false;

            }

        }
        memo.put(key, min);
        return min;
    }

    private String formKey(int currentVertex, boolean[] visited) {
        StringBuilder sb = new StringBuilder(currentVertex + "-");
        for (boolean flag : visited) {
            if (flag) {
                sb.append(1);
            } else {
                sb.append(0);
            }
        }
        return sb.toString();
    }
}


