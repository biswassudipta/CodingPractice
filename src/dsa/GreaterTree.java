package dsa;

import java.util.ArrayList;
import java.util.List;

public class GreaterTree {
    public TreeNode convertBST(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inOrder(root, list);
        int[] sumArray = new int[list.size() + 1];
        for (int i = list.size() - 1; i >= 0; i--) {
            sumArray[i] = sumArray[i + 1] + list.get(i);
        }
        int[] index = new int[]{-1};
        inOrderBuild(root, sumArray, index);
        return root;
    }

    private void inOrder(TreeNode root, List<Integer> list) {
        if (root == null) return;
        inOrder(root.left, list);
        list.add(root.val);
        inOrder(root.right, list);

    }

    private void inOrderBuild(TreeNode root, int[] sumArray, int[] index) {
        if (root == null) return;

        inOrderBuild(root.left, sumArray, index);
        root.val = sumArray[++index[0]];
        inOrderBuild(root.right, sumArray, index);

    }
}
