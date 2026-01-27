package dsa;

import static java.lang.IO.println;

public class MatrixChainMultiplication {
    public int findMinimumMultiplication(int[] dimensions) {
        int n = dimensions.length;

        int[][] dp = new int[n][n];

        for (int d = 1; d < n - 1; d++) {
            for (int i = 1; i < n - d; i++) {
                int j = i + d;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int min = dp[i][k] + dp[k + 1][j] + (dimensions[i - 1] * dimensions[k] * dimensions[j]);
                    if (min < dp[i][j]) {
                        dp[i][j] = min;
                    }
                }
            }
        }
        for (int[] ints : dp) {
            for (int j = 0; j < dp.length; j++) {
                IO.print(ints[j] + " ");
            }
            println();
        }
        return dp[1][n - 1];

    }
}
