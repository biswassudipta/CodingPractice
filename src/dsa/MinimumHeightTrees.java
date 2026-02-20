package dsa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class MinimumHeightTrees {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        List<Integer> result = new ArrayList<>();
        List<List<Integer>> adj = new ArrayList<>();
        if (n == 1) {
            result.add(0);
            return result;
        }

        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());

        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }
        int[] inDegree = new int[n];
        for (int i = 0; i < n; i++) {
            for (int v : adj.get(i)) {
                inDegree[v]++;
            }
        }
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 1) {
                queue.add(i);
            }
        }
        while (n > 2) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {

                int u =queue.remove();
                n--;
                for (int v : adj.get(u)) {
                    inDegree[v]--;
                    if (inDegree[v] == 1) {
                        queue.add(v);
                    }

                }
            }
        }
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        return result;

    }
}
