package dsa;

public class IslandsAndWater {
    private static final char visited = '2';
    private static final char water = '0';

    public int numIslands(char[][] grid) {
        int noOfIslands = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != visited && grid[i][j] != water) {
                    noOfIslands++;
                    dfs(grid, i, j);
                }

            }
        }
        return noOfIslands;

    }


    private void dfs(char[][] grid, int i, int j) {

        if (i < 0 || i > grid.length - 1 || j < 0 || j > grid[0].length - 1) {
            return;
        }
        if (grid[i][j] == visited) {
            return;
        }
        if (grid[i][j] == water) {
            return;
        }
        grid[i][j] = visited;

        int[][] dir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] d : dir) {
            int newR = i + d[0];
            int newC = j + d[1];
            dfs(grid, newR, newC);
        }


    }
}
