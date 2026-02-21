package dsa;

import java.util.*;

public class SerializeDeserializeBinaryTree {
    private static final String NULL = "null";

    public String serialize(TreeNode root) {
        List<String> result = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            if (node != null) {
                result.add(String.valueOf(node.val));
                queue.add(node.left);
                queue.add(node.right);
            } else {
                result.add(NULL);
            }
        }

        removeTrailingNulls(result);
        return "[" + String.join(",", result) + "]";
    }

    private void removeTrailingNulls(List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        while (!list.isEmpty()) {
            int lastIndex = list.size() - 1;
            String lastElement = list.get(lastIndex);

            if (lastElement != null && lastElement.equals(NULL)) {
                list.remove(lastIndex);
            } else {
                break;
            }
        }
    }


    public TreeNode deserialize(String data) {
        if (data == null || data.isEmpty() || data.equals("[]")) {
            return null;
        }

        String[] nodes = data.replace("[", "").replace("]", "").split(",");

        if (nodes[0].equals("null")) {
            return null;
        }

        TreeNode root = new TreeNode(Integer.parseInt(nodes[0].trim()));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);

        int i = 1;
        while (!queue.isEmpty() && i < nodes.length) {
            TreeNode current = queue.poll();

            if (!nodes[i].trim().equals(NULL)) {
                current.left = new TreeNode(Integer.parseInt(nodes[i].trim()));
                queue.add(current.left);
            }
            i++;

            if (i < nodes.length && !nodes[i].trim().equals(NULL)) {
                current.right = new TreeNode(Integer.parseInt(nodes[i].trim()));
                queue.add(current.right);
            }
            i++;
        }

        return root;
    }
}
