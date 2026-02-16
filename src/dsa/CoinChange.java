package dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinChange {
    int total = 0;
    int min = Integer.MAX_VALUE;

    public int findMinimumCoinsDP(int[] coins, int amount) {
        if (amount == 0) {
            return 0;
        }
        int len = coins.length;
        int[][] dp = new int[len + 1][amount + 1];
        dp[0][0] = 0;
        Arrays.fill(dp[0], Integer.MAX_VALUE - 1);
        for (int i = 1; i <= len; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j - coins[i - 1] >= 0) {
                    dp[i][j] = Math.min(dp[i - 1][j], 1 + dp[i][j - coins[i - 1]]);
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[len][amount] == Integer.MAX_VALUE - 1 ? -1 : dp[len][amount];

    }

    public int findTotalCombinationsDP(int[] coins, int amount) {
        if (amount == 0) {
            return 1;
        }
        int len = coins.length;
        int[][] dp = new int[len + 1][amount + 1];
        for (int i = 0; i < len + 1; i++) {
            dp[i][0] = 1;
        }
        for (int i = 1; i <= len; i++) {
            for (int j = 1; j <= amount; j++) {
                if (j - coins[i - 1] >= 0) {

                    dp[i][j] = dp[i - 1][j] + dp[i][j - coins[i - 1]];
                } else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }
        return dp[len][amount];

    }

    public int findMinimumCoinsBT(int[] coins, int amount) {
        generateTotalCombinationOfMinimumLength(coins, amount, 0, 0, new ArrayList<>());
        return min;

    }

    public int findTotalCombinationsBT(int[] coins, int amount) {

        generateTotalCombinations(coins, amount, 0, 0);
        return total;
    }

    private void generateTotalCombinationOfMinimumLength(int[] coins, int amount, int sum, int index, List<Integer> combination) {
        if (sum == amount) {
            min = Math.min(combination.size(), min);
            return;
        }
        if (sum > amount) {
            return;
        }
        if (index == coins.length) {
            return;
        }
        combination.add(coins[index]);
        generateTotalCombinationOfMinimumLength(coins, amount, sum + coins[index], index, combination);
        combination.removeLast();
        generateTotalCombinationOfMinimumLength(coins, amount, sum, index + 1, combination);

    }

    private void generateTotalCombinations(int[] coins, int amount, int sum, int index) {
        if (sum == amount) {
            total++;
            return;
        }
        if (sum > amount) {
            return;
        }
        if (index == coins.length) {
            return;
        }
        generateTotalCombinations(coins, amount, sum + coins[index], index);
        generateTotalCombinations(coins, amount, sum, index + 1);

    }
}
