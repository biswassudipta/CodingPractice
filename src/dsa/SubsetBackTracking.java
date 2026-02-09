package dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SubsetBackTracking {
    public List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> resultWithDuplicates = new ArrayList<>();

    public List<List<Integer>> subsets(int[] nums) {

        List<Integer> subResult = new ArrayList<>();
        findSubsets(nums, 0, nums.length, subResult);
        return result;

    }

    public List<List<Integer>> subsetsWithDuplicates(int[] nums) {

        List<Integer> subResult = new ArrayList<>();
        Arrays.sort(nums);
        findSubsetsWithDuplicates(nums, 0, nums.length, subResult);
        return resultWithDuplicates;

    }

    private void findSubsets(int[] nums, int index, int n, List<Integer> subResult) {
        if (index == n) {
            result.add(new ArrayList<>(subResult));
            return;
        }
        subResult.add(nums[index]);
        findSubsets(nums, index + 1, n, subResult);
        subResult.removeLast();
        findSubsets(nums, index + 1, n, subResult);


    }

    private void findSubsetsWithDuplicates(int[] nums, int index, int n, List<Integer> subResult) {
        if (index == n) {
            resultWithDuplicates.add(new ArrayList<>(subResult));
            return;
        }
        subResult.add(nums[index]);
        findSubsetsWithDuplicates(nums, index + 1, n, subResult);
        subResult.removeLast();
        int idx = index + 1;
        while ((idx < n) && (nums[idx] == nums[idx - 1])) {
            idx++;
        }
        findSubsetsWithDuplicates(nums, idx, n, subResult);
    }
}
