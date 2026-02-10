package dsa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationBackTracking {
    public List<List<Integer>> combinationSum1 = new ArrayList<>();
    public List<List<Integer>> combinationSum2 = new ArrayList<>();
    public List<List<Integer>> combinationSum3 = new ArrayList<>();
    public int result = 0;

    /*    Given an array of distinct integers candidates and a target integer target, return a list of all unique combinations of candidates where the chosen numbers sum to target. You may return the combinations in any order.

        The same number may be chosen from candidates an unlimited number of times. Two combinations are unique if the frequency of at least one of the chosen numbers is different.

        The test cases are generated such that the number of unique combinations that sum up to target is less than 150 combinations for the given input.



                Example 1:

        Input: candidates = [2,3,6,7], target = 7
        Output: [[2,2,3],[7]]
        Explanation:
                2 and 3 are candidates, and 2 + 2 + 3 = 7. Note that 2 can be used multiple times.
    7 is a candidate, and 7 = 7.
        These are the only two combinations.*/
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<Integer> list = new ArrayList<>();
        findCombinationSum1(candidates, 0, target, 0, list);
        return combinationSum1;


    }

    private void findCombinationSum1(int[] candidates, int index, int target, int sum, List<Integer> subList) {
        if (sum == target) {
            combinationSum1.add(new ArrayList<>(subList));
            return;
        }
        if (sum > target || index == candidates.length) {
            return;
        }
        subList.add(candidates[index]);
        findCombinationSum1(candidates, index, target, sum + candidates[index], subList);
        subList.removeLast();
        findCombinationSum1(candidates, index + 1, target, sum, subList);
    }

   /* Given a collection of candidate numbers (candidates) and a target number (target), find all unique combinations in candidates where the candidate numbers sum to target.

    Each number in candidates may only be used once in the combination.

    Note: The solution set must not contain duplicate combinations.



    Example 1:

    Input: candidates = [10,1,2,7,6,1,5], target = 8
    Output:
            [
            [1,1,6],
            [1,2,5],
            [1,7],
            [2,6]
            ]
    Example 2:

    Input: candidates = [2,5,2,1,2], target = 5
    Output:
            [
            [1,2,2],
            [5]
            ]*/

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<Integer> list = new ArrayList<>();
        Arrays.sort(candidates);
        findCombinationSum2(candidates, 0, target, 0, list);
        return combinationSum2;

    }

    private void findCombinationSum2(int[] candidates, int start, int target, int sum, List<Integer> subList) {
        if (sum == target) {
            combinationSum2.add(new ArrayList<>(subList));
            return;
        }
        if (sum > target || start == candidates.length || subList.size() == candidates.length) {
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i - 1]) continue;

            if (candidates[i] > target) break;
            subList.add(candidates[i]);
            findCombinationSum2(candidates, i + 1, target, sum + candidates[i], subList);
            subList.removeLast();
        }

    }

    /*
    * Find all valid combinations of k numbers that sum up to n such that the following conditions are true:

Only numbers 1 through 9 are used.
Each number is used at most once.
Return a list of all possible valid combinations. The list must not contain the same combination twice, and the combinations may be returned in any order.



Example 1:

Input: k = 3, n = 7
Output: [[1,2,4]]
Explanation:
1 + 2 + 4 = 7
There are no other valid combinations.
Example 2:

Input: k = 3, n = 9
Output: [[1,2,6],[1,3,5],[2,3,4]]
Explanation:
1 + 2 + 6 = 9
1 + 3 + 5 = 9
2 + 3 + 4 = 9
There are no other valid combinations.
Example 3:

Input: k = 4, n = 1
Output: []
Explanation: There are no valid combinations.
Using 4 different numbers in the range [1,9], the smallest sum we can get is 1+2+3+4 = 10 and since 10 > 1, there are no valid combination.
*/
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<Integer> list = new ArrayList<>();
        int[] candidates = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9};
        findCombinationSum3(candidates, 0, k, 0, n, list);
        return combinationSum3;

    }

    private void findCombinationSum3(int[] candidates, int index, int len, int sum, int target, List<Integer> subList) {
        if (sum == target && len == subList.size()) {
            combinationSum3.add(new ArrayList<>(subList));
            return;
        }
        if (sum > target || index == candidates.length || subList.size() == len) {
            return;
        }
        subList.add(candidates[index]);
        findCombinationSum3(candidates, index + 1, len, sum + candidates[index], target, subList);
        subList.removeLast();
        findCombinationSum3(candidates, index + 1, len, sum, target, subList);
    }

    /*
    * Given an array of distinct integers nums and a target integer target, return the number of possible combinations that add up to target.

The test cases are generated so that the answer can fit in a 32-bit integer.



Example 1:

Input: nums = [1,2,3], target = 4
Output: 7
Explanation:
The possible combination ways are:
(1, 1, 1, 1)
(1, 1, 2)
(1, 2, 1)
(1, 3)
(2, 1, 1)
(2, 2)
(3, 1)
Note that different sequences are counted as different combinations.
Example 2:

Input: nums = [9], target = 3
Output: 0


Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= 1000
All the elements of nums are unique.
1 <= target <= 1000
*/

    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target + 1];
        Arrays.fill(dp, -1);
        List<Integer> list = new ArrayList<>();
        findCombinationSum4(nums,0,target,0, list,dp);
        return result;

    }

    private void findCombinationSum4(int[] candidates, int start, int target, int sum,List<Integer> subList,int[] dp) {
        if (sum == target) {
            result++;
            return;
        }
        if (sum > target || start == candidates.length ) {
            return;
        }
        if(dp[sum]!=-1){
            result+=dp[sum];
            return;
        }
        int before=result;
        for (int i = 0; i < candidates.length; i++) {


            subList.add(candidates[i]);
            findCombinationSum4(candidates, 0,target, sum + candidates[i],subList,dp);
            subList.removeLast();
        }
        dp[sum] = result - before;

    }

}
