package dsa;

public class ZeroOneKnapsack {

    public int findMaxProfit(int[] p, int[] wt, int W) {
        int[][] dp = new int[wt.length + 1][W + 1];
        for (int i = 1; i <= wt.length; i++) {
            for (int j = 1; j <= W; j++) {
                if (j - wt[i - 1] >= 0) dp[i][j] = Math.max(dp[i - 1][j], p[i - 1] + dp[i - 1][j - wt[i - 1]]);
                else dp[i][j] = dp[i - 1][j];
            }
        }
        return dp[wt.length][W];
    }
}
