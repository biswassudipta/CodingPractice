package dsa;

import java.util.ArrayList;
import java.util.List;

public class CourseComplete {

    public boolean canFinish(int numCourses, int[][] prerequisites) {

        List<List<Integer>> adj = constructAdj(numCourses, prerequisites);

        int[] color = new int[numCourses];
        for (int i = 0; i < numCourses; i++) {
            if (color[i] == 0) {
                if (dfsUtil(i, adj, color))
                    return false;
            }
        }

        return true;
    }

    private List<List<Integer>> constructAdj(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>(V);

        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
        }

        return adj;
    }

    private boolean dfsUtil(int u, List<List<Integer>> adj, int[] color) {
        final int unVisited = 0;
        final int grey = 1;
        final int black = 2;

        color[u] = black;

        for (int v : adj.get(u)) {
            if (color[v] == black) {
                return true;
            }
            if (color[v] == unVisited && dfsUtil(v, adj,
                    color)) {
                return true;

            }

        }
        color[u] = grey;
        return false;
    }
}
