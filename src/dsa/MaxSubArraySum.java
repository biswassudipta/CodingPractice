package dsa;

import java.util.HashMap;
import java.util.Map;

public class MaxSubArraySum {
    public int subarraySum(int[] numbs, int k) {
        int currSum = 0;
        int count = 0;
        Map<Integer, Integer> map = new HashMap<>();

        for (int num : numbs) {
            currSum += num;
            if (currSum == k) {
                count++;

            }
            if (map.containsKey(currSum - k)) {
                count += map.get(currSum - k);
            }
            map.put(currSum, map.getOrDefault(currSum, 0) + 1);
        }
        return count;

    }
}
