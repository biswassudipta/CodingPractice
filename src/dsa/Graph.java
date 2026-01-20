package dsa;

import java.util.*;


public class Graph {

    public int findMSTCostPrims(int v, List<List<int[]>> adj) {

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        boolean[] visited = new boolean[v];
        int res = 0;
        pq.add(new int[]{0, 0});
        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int u = top[0];
            if (visited[u]) continue;

            visited[u] = true;
            int wt = top[1];
            res = res + wt;
            for (int[] edge : adj.get(u)) {
                if (!visited[edge[0]]) pq.offer(edge);
            }
        }

        return res;

    }

    public int findMSTCostKruskal(int v, List<List<int[]>> adj) {
        List<int[]> edges = new ArrayList<>();
        int res = 0, count = 0;
        for (int i = 0; i < v; i++) {
            for (int[] edge : adj.get(i)) {
                edges.add(new int[]{i, edge[0], edge[1]});
            }
        }

        edges.sort(Comparator.comparingInt(a -> a[2]));
        DSU dsu = new DSU(v);
        for (int[] edge : edges) {
            if (dsu.find(edge[0]) != dsu.find(edge[1])) {
                dsu.union(edge[0], edge[1]);
                res = res + edge[2];
                if (++count == v - 1) break;
            }

        }

        return res;
    }

    public List<Integer> findShortestPathDijkstra(int src, int v, List<List<int[]>> adj) {
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        int[] distance = new int[v];
        Arrays.fill(distance, Integer.MAX_VALUE);

        pq.add(new int[]{src, 0});
        distance[src] = 0;


        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int u = top[0];
            int d = top[1];
            if (distance[u] < d) continue;
            for (int[] edge : adj.get(u)) {
                int nextVertex = edge[0];
                int distanceFromPreviousVertex = edge[1];
                int distanceAtCurrentVertex = distanceFromPreviousVertex + distance[u];
                if (distanceAtCurrentVertex < distance[nextVertex]) {
                    distance[nextVertex] = distanceAtCurrentVertex;
                    pq.offer(new int[]{nextVertex, distanceAtCurrentVertex});
                }

            }
        }

        ArrayList<Integer> result = new ArrayList<>();
        for (int d : distance)
            result.add(d);
        return result;
    }
}
