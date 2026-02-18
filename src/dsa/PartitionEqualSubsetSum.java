package dsa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PartitionEqualSubsetSum {
    private boolean result = false;
    public boolean canPartition(int[] nums) {
        int total = 0;
        for (int i : nums) total += i;

        if ((total & 1) == 1) return false;

        int target = total / 2;
        Set<Integer> reachable = new HashSet<>();
        reachable.add(0);

        for (int num : nums) {
            if (num == target) return true;
            if (num > target) continue;

            List<Integer> newSums = new ArrayList<>();

            for (int s : reachable) {
                int currentSum = s + num;
                if (currentSum == target) return true;
                if (currentSum < target) {
                    newSums.add(currentSum);
                }
            }
            reachable.addAll(newSums);
        }
        return false;
    }
    public boolean canPartitionDP(int[] nums) {
        int sum = 0;
        for (int num : nums) sum += num;

        if ((sum % 2) != 0) return false;

        int target = sum / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;

        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                dp[i] = dp[i] || dp[i - num];
            }
        }

        return dp[target];
    }


    public boolean canPartitionBT(int[] nums) {

        int total = 0;
        int n = nums.length;

        for (int i : nums) {
            total = i + total;
        }
        if(total%2==1){
            return false;
        }
        generate(nums, 0, 0, total, n);
        return result;

    }

    private void generate(int[] nums, int index, int sum, int total, int n) {
        if ((total - sum) == sum ) {
            result = true;
            return;

        }
        if (index == n) {
            return;
        }
        if (total - sum < sum) {
            return;
        }
        generate(nums, index+1, sum + nums[index], total, n);
        generate(nums, index + 1, sum, total, n);

    }
}
