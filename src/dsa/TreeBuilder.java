package dsa;

import java.util.LinkedList;
import java.util.Queue;

public class TreeBuilder {
    public TreeNode buildTree(Integer[] arr) {
        if (arr == null || arr.length == 0 || arr[0] == null) {
            return null;
        }

        // Create the root
        TreeNode root = new TreeNode(arr[0]);

        // Use a Queue to keep track of nodes that need children
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int i = 1; // Start from the second element (index 1)

        while (i < arr.length) {
            // Get the parent node from the queue
            TreeNode current = queue.poll();

            // --- Process Left Child ---
            if (arr[i] != null) {
                assert current != null;
                current.left = new TreeNode(arr[i]);
                queue.add(current.left);
            }
            i++; // Move to next index

            // --- Process Right Child ---
            if (i < arr.length && arr[i] != null) {
                assert current != null;
                current.right = new TreeNode(arr[i]);
                queue.add(current.right);
            }
            i++; // Move to next index
        }

        return root;
    }
}
