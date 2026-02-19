package dsa;

public class LongestPalindromicSubstring {
    public String longestPalindrome(String s) {
        int maxLen = 1;
        int start = 0;
        int end = 1;
        int n = s.length();
        int[][] dp = new int[n][n];
        int k = 0;
        while (k < n) {
            dp[k][k] = 1;
            k++;
        }

        for (int d = 1; d < n; d++) {
            for (int i = 0; i < n - d; i++) {
                int j = i + d;
                if (s.charAt(i) == s.charAt(j)) {
                    if (d == 1) {
                        dp[i][j] = 2;
                    } else if (dp[i + 1][j - 1] != 0) {
                        dp[i][j] = dp[i + 1][j - 1] + 2;
                    }
                    if (dp[i][j] > maxLen) {
                        maxLen = dp[i][j];
                        start = i;
                        end = j + 1;
                    }

                }

            }
        }
        return s.substring(start, end);

    }
}
