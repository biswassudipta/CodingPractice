package dsa;

import java.util.ArrayDeque;
import java.util.Deque;

public class RottingOranges {
    public int orangesRotting(int[][] grid) {
        Deque<int[]> queue = new ArrayDeque<>();
        int time = 0;
        int fresh = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 2) {
                    grid[i][j] = 1;
                    queue.offer(new int[]{i, j, 0});
                }
                if (grid[i][j] == 1) {
                    fresh++;
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] orange = queue.poll();
            int i = orange[0];
            int j = orange[1];
            int timer = orange[2];

            if (i < 0 || j < 0 || i > grid.length - 1 || j > grid[0].length - 1 || grid[i][j] == 0 || grid[i][j] == 2) {
                continue;

            }
            grid[i][j] = 2;
            time = Math.max(time, timer);
            fresh--;

            int[][] dir = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
            for (int[] d : dir) {
                int newR = i + d[0];
                int newC = j + d[1];
                int newTimer = timer + 1;
                queue.offer(new int[]{newR, newC, newTimer});
            }

        }

        if (fresh > 0) {
            return -1;
        } else {
            return time;
        }
    }
}
