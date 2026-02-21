package dsa;

public class TrapRainWater {
    public int trap(int[] height) {
        int leftMax = 0;
        int n = height.length;
        int[] rightMax = new int[n];
        rightMax[n - 1] = height[n - 1];
        int water = 0;

        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(height[i], rightMax[i + 1]);

        }

        for (int i = 0; i < n; i++) {
            int left = Math.max(leftMax, height[i]);
            leftMax = left;
            int right = rightMax[i];
            int effectiveHeight = (Math.min(left, right) - height[i]);
            if (effectiveHeight < 0) {
                effectiveHeight = 0;
            }
            water = water + effectiveHeight;

        }
        return water;

    }
}
