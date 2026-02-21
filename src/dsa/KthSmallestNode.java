package dsa;

public class KthSmallestNode {
    int currentCount = 0;
    int result = -1;

    public int kthSmallest(TreeNode root, int k) {

        inOrder(root, k);
        return result;

    }

    private boolean inOrder(TreeNode root, int k) {
        if (root == null) {
            return false;
        }
        if (inOrder(root.left, k)) {
            return true;
        }

        currentCount++;
        if (currentCount == k) {
            result = root.val;
            return true;
        }

        return inOrder(root.right, k);
    }
}
