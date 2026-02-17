package dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InsertInterval {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;

        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }

        result.add(newInterval);

        while (i < n) {
            result.add(intervals[i]);
            i++;
        }

        return result.toArray(new int[result.size()][]);
    }

    public int[][] merge(int[][] intervals) {
        List<int[]> result = new ArrayList<>();

        int n = intervals.length;
        if (n == 1) {
            return intervals;
        }
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] == b[0]) {
                return Integer.compare(a[1], b[1]);
            }
            return Integer.compare(a[0], b[0]);
        });
        int start = 0;
        int end = 1;

        int previousStart = intervals[0][0];
        int previousEnd = intervals[0][1];
        for (int i = 1; i < n; i++) {
            if (previousEnd < intervals[i][start]) {
                result.add(new int[]{previousStart, previousEnd});
                previousStart = intervals[i][start];
                previousEnd = intervals[i][end];
            }

            if (previousEnd >= intervals[i][start]) {
                previousEnd = Math.max(intervals[i][end], previousEnd);
            }
            if (previousStart >= intervals[i][start]) {
                previousStart = Math.min(previousStart, intervals[i][start]);
            }
        }
        result.add(new int[]{previousStart, previousEnd});
        return result.toArray(new int[result.size()][]);

    }
}
