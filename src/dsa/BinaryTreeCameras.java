package dsa;

import java.util.HashSet;
import java.util.Set;

public class BinaryTreeCameras {
    Set<TreeNode> covered;
    int cameras;

    public int minCameraCover(TreeNode root) {
        if (root == null) {
            return 0;
        }
        this.cameras = 0;
        this.covered = new HashSet<>();
        covered.add(null);
        dfs(root, null);
        return cameras;
    }

    public void dfs(TreeNode node, TreeNode parent) {
        if (node == null) {
            return;
        }
        dfs(node.left, node);
        dfs(node.right, node);

        if (parent == null && !covered.contains(node) || !covered.contains(node.left) || !covered.contains(node.right)) {
            cameras++;
            covered.add(node);
            covered.add(node.left);
            covered.add(node.right);
            covered.add(parent);
        }
    }
}
