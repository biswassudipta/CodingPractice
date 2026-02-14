package dsa;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class LevelOrder {
    public List<List<Integer>> levelOrder(TreeNode root) {
        if (root == null) {
            return new ArrayList<>();
        }
        List<List<Integer>> path = new ArrayList<>();
        List<Integer> subPath = new ArrayList<>();
        Queue<TreeNode> queue1 = new ArrayDeque<>();
        Queue<TreeNode> queue2 = new ArrayDeque<>();
        queue1.offer(root);
        while (!queue1.isEmpty()) {

            TreeNode node = queue1.poll();
            subPath.add(node.val);

            if (node.left != null) {
                queue2.offer(node.left);
            }
            if (node.right != null) {
                queue2.offer(node.right);
            }
            if (queue1.isEmpty()) {
                path.add(subPath);
                queue1 = queue2;
                queue2 = new ArrayDeque<>();
                subPath = new ArrayList<>();

            }

        }
        return path;

    }
}
