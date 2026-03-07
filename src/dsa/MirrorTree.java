package dsa;

public class MirrorTree {
    public boolean isSymmetric(TreeNode root) {
        if (root == null) {
            return true;
        }
        if (root.left == null && root.right == null) {
            return true;
        }
        if (root.left == null || root.right == null) {
            return false;
        }

        return mirrorTree(root.left, root.right);

    }

    boolean mirrorTree(TreeNode root1, TreeNode root2) {
        if (root1 == null && root2 == null) {
            return true;
        }
        if (root1 == null || root2 == null) {
            return false;
        }
        if (root1.val == root2.val) {
            return mirrorTree(root1.right, root2.left) && mirrorTree(root1.left, root2.right);
        } else {
            return false;
        }

    }
}
