package dsa;

public class ArrayProduct {
    public int[] productExceptSelf(int[] nums) {
        int n = nums.length;
        int[] ans = new int[n];

        int rightProduct = 1;
        int leftProduct = 1;

        int[] rightVals = new int[n - 1];
        int j = n - 2;
        while (j >= 0) {
            rightVals[j] = nums[j + 1] * rightProduct;
            rightProduct = rightVals[j];
            j--;
        }

        for (int k = 0; k < n - 1; k++) {
            ans[k] = leftProduct * rightVals[k];
            leftProduct = leftProduct*nums[k];
        }
        ans[n-1] = leftProduct;

        return ans;

    }
}
