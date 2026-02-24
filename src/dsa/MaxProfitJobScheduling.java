package dsa;

import java.util.Arrays;
import java.util.Comparator;

public class MaxProfitJobScheduling {
    int[] dp;

    public int jobScheduling(int[] startTime, int[] endTime, int[] profit) {
        dp = new int[startTime.length];
        Arrays.fill(dp, -1);

        int[][] intervals = new int[startTime.length][3];
        for (int i = 0; i < startTime.length; i++) {
            intervals[i] = new int[]{startTime[i], endTime[i], profit[i]};
        }

        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        return generate(0, intervals);
    }

    private int generate(int i, int[][] intervals) {
        if (i >= intervals.length) {
            return 0;
        }
        if (dp[i] != -1) {
            return dp[i];
        }

        int maxProfitWithOutIncluding = generate(i + 1, intervals);

        int j = findLatestNonOverlappingJob(intervals, i);
        int maxProfitWithIncluding = generate(j, intervals);

        dp[i] = Math.max(maxProfitWithIncluding + intervals[i][2], maxProfitWithOutIncluding);
        return dp[i];
    }

    private int findLatestNonOverlappingJob(int[][] intervals, int i) {
        int pivot = intervals[i][1];
        int left = i + 1;
        int right = intervals.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (intervals[mid][0] >= pivot) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        if (left < intervals.length && intervals[left][0] >= pivot) {
            return left;
        }

        return intervals.length;
    }
}