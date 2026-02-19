package dsa;

public class BinaryTreePrinter {

    public static void print(TreeNode root) {
        if (root == null) {
            System.out.println("(Empty Tree)");
            return;
        }
        printHelper(root, "", true, "Root");
    }

    private static void printHelper(TreeNode node, String prefix, boolean isLast, String type) {
        if (node == null) return;

        System.out.println(prefix + (isLast ? "└── " : "├── ") + type + ": " + node.val);

        String childPrefix = prefix + (isLast ? "    " : "│   ");

        boolean hasRight = node.right != null;
        boolean hasLeft = node.left != null;

        if (hasLeft) {
            printHelper(node.left, childPrefix, !hasRight, "L");
        }

        if (hasRight) {
            printHelper(node.right, childPrefix, true, "R");
        }
    }
}