package dsa;

import java.util.ArrayList;
import java.util.List;

public class BSTToMaxHeap {
    public void convertBSTToMaxHeap(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        inOrder(root, list);
        int[] index = new int[]{-1};
        postOrder(root, list, index);
    }

    private void inOrder(TreeNode root, List<Integer> list) {
        if (root == null) return;
        inOrder(root.left, list);
        list.add(root.val);
        inOrder(root.right, list);

    }

    private void postOrder(TreeNode root, List<Integer> list, int[] index) {
        if (root == null) return;

        postOrder(root.left, list, index);
        postOrder(root.right, list, index);
        root.val = list.get(++index[0]);
    }
}
