package dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CriticalPathTarjanAlgo {
    private int timer = 0;

    private void findCriticalPath(int node, int parent, int[] disc, int[] low, List<List<Integer>> adj, boolean[] visited, List<List<Integer>> result) {
        visited[node] = true;
        low[node] = disc[node] = timer++;

        for (int child : adj.get(node)) {
            if (child == parent) {
                continue;
            }
            if (!visited[child]) {
                findCriticalPath(child, node, disc, low, adj, visited, result);
                low[node] = Math.min(low[node], low[child]);
                if (low[child]>disc[node]) {
                    result.add(Arrays.asList(node, child));
                }

            } else {
                low[node] = Math.min(low[node], disc[child]);
            }
        }
    }

    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<List<Integer>> adj = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (List<Integer> edge : connections) {
            int u = edge.get(0);
            int v = edge.get(1);
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        int[] disc = new int[n];
        int[] low = new int[n];
        boolean[] visited = new boolean[n];
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                findCriticalPath(i, -1, disc, low, adj, visited, result);
            }
        }
        return result;
    }
}
