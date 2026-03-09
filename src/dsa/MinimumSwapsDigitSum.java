package dsa;

import java.util.*;

public class MinimumSwapsDigitSum {
    public int minSwaps(int[] nums) {
        int n = nums.length;
        boolean[] visited = new boolean[nums.length];
        Map<Integer, Integer> mapDigitSum = new HashMap<>();
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = digitSum(nums[i], mapDigitSum);
        }

        Map<Integer, Queue<Integer>> mapPos = new HashMap<>();
        for (int i = 0; i < n; i++) {
            mapPos.computeIfAbsent(arr[i], _ -> new PriorityQueue<>(
                    Comparator.comparingInt(idx -> nums[idx]))).add(i);
        }
        Arrays.sort(arr);
        int[] permutation = new int[n];
        for (int i = 0; i < n; i++) {
            permutation[i] = Objects.nonNull(mapPos.get(arr[i]))?mapPos.get(arr[i]).size(): 0;
        }
        int swaps = 0;
        for (int i = 0; i < nums.length; i++) {
            if (visited[i] || permutation[i] == i) {
                continue;
            }
            int j = i;
            int cycles = 0;
            while (!visited[j]) {
                visited[j] = true;
                j = permutation[j];
                cycles++;
            }
            if (cycles > 0) {
                swaps = swaps + (cycles - 1);
            }

        }
        return swaps;

    }

    private int digitSum(int i, Map<Integer, Integer> mapDigitSum) {

        if (mapDigitSum.containsKey(i)) {
            return mapDigitSum.get(i);
        } else {
            int result = 0;
            if (i != 0) {
                int val = i % 10;
                result = val + digitSum(i / 10, mapDigitSum);
            }

            mapDigitSum.put(i, result);
            return result;
        }

    }
}
