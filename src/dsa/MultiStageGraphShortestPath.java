package dsa;

import java.util.Arrays;

public class MultiStageGraphShortestPath {

    public int findShortestPath(int[][] graph) {
        int N = graph.length;
        int[] distance = new int[N];
        Arrays.fill(distance, Integer.MAX_VALUE);

        distance[N - 1] = 0;

        for (int i = N - 2; i >= 0; i--) {
            for (int j = i + 1; j < N; j++) {
                if (graph[i][j] == Integer.MAX_VALUE) continue;

                int min = graph[i][j] + distance[j];
                if (min < distance[i]) {
                    distance[i] = min;
                }
            }
        }

        return distance[0];

    }
}
