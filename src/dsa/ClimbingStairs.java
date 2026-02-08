package dsa;

import java.math.BigInteger;

public class ClimbingStairs {
    public void climbStairsUsingCombinations(int n) {
        long startTime = System.currentTimeMillis();
        BigInteger[] dp = new BigInteger[n + 1];
        dp[0] = BigInteger.ONE;
        int k = 1;
        while (k <= n) {
            dp[k] = dp[k - 1].multiply(BigInteger.valueOf(k));
            k++;
        }
        int i = n;
        int j = 0;
        BigInteger combinations = BigInteger.ZERO;
        while (i >= 0) {
            combinations = combinations.add(dp[i + j].divide(dp[i].multiply(dp[j])));
            i = i - 2;
            j++;
        }
        long endTime = System.currentTimeMillis();
        IO.println("time taken using combinations: " + (endTime - startTime) + " and result is " + combinations + "\n");

    }

    public void climbStairsUsingDP(int n) {
        long startTime = System.currentTimeMillis();
        if (n <= 1) {
            long endTime = System.currentTimeMillis();
            IO.println("time taken using dp: " + (endTime - startTime) + " and result is " + BigInteger.ONE + "\n");
        } else {

            BigInteger prev1 = BigInteger.ONE;
            BigInteger prev2 = BigInteger.ONE;
            BigInteger current = BigInteger.ZERO;

            for (int i = 2; i <= n; i++) {
                current = prev1.add(prev2);
                prev2 = prev1;
                prev1 = current;
            }
            long endTime = System.currentTimeMillis();
            IO.println("time taken using dp: " + (endTime - startTime) + " and result is " + current + "\n");
        }

    }

    public void climbStairsUsingBruteForce(int n) {
        long startTime = System.currentTimeMillis();
        int i = n;
        int j = 0;
        BigInteger combinations = BigInteger.ZERO;
        while (i >= 0) {
            combinations = combinations.add(factorial(i + j).divide(factorial(i).multiply(factorial(j))));
            i = i - 2;
            j++;
        }
        long endTime = System.currentTimeMillis();
        IO.println("time taken using bruteforce: " + (endTime - startTime) + " and result is " + combinations + "\n");

    }

    private BigInteger factorial(int n) {
        if (n <= 1) {
            return BigInteger.ONE;
        } else {
            return factorial(n - 1).multiply(BigInteger.valueOf(n));
        }
    }
}
