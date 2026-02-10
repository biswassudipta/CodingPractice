package dsa;

import java.util.ArrayList;
import java.util.List;

public class PermutationBackTracking {
    public List<List<Integer>> result = new ArrayList<>();
    public List<List<Integer>> uniqueResult = new ArrayList<>();

    public List<List<Integer>> permute(int[] nums) {
        List<Integer> subResult = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        findTotalPermutations(nums, visited, nums.length, subResult);
        return result;

    }

    private void findTotalPermutations(int[] nums, boolean[] visited, int length, List<Integer> subResult) {
        if (subResult.size() == length) {
            result.add(new ArrayList<>(subResult));
        }
        for (int i = 0; i < length; i++) {
            if (!visited[i]) {
                visited[i] = true;
                subResult.add(nums[i]);
                findTotalPermutations(nums, visited, length, subResult);
                subResult.removeLast();
                visited[i] = false;
            }

        }
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<Integer> subResult = new ArrayList<>();
        boolean[] visited = new boolean[nums.length];
        findTotalUniquePermutations(nums, visited, nums.length, subResult);
        return uniqueResult;

    }

    private void findTotalUniquePermutations(int[] nums, boolean[] visited, int length, List<Integer> subResult) {
        if (subResult.size() == length) {
            uniqueResult.add(new ArrayList<>(subResult));
        }
        for (int i = 0; i < length; i++) {
            if ((i > 0 && nums[i] == nums[i - 1] && !visited[i - 1]) || visited[i]) continue;

            visited[i] = true;
            subResult.add(nums[i]);
            findTotalUniquePermutations(nums, visited, length, subResult);
            subResult.removeLast();
            visited[i] = false;


        }
    }
}
