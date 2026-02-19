package dsa;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ConstructBinaryTree {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (inorder.length == 0) {
            return null;
        }
        if (inorder.length == 1) {
            return new TreeNode(inorder[0]);
        }
        int rootValue = preorder[0];
        int splitIndex = search(inorder, rootValue);
        TreeNode root = new TreeNode(rootValue);
        int[] left = Arrays.copyOfRange(inorder, 0, splitIndex);
        int[] right = Arrays.copyOfRange(inorder, splitIndex + 1, inorder.length);

        int rightRootIndex = searchRoot(right, splitIndex + 1, preorder);
        if (rightRootIndex != -1) {
            int[] leftPre = Arrays.copyOfRange(preorder, 1, rightRootIndex);
            int[] rightPre = Arrays.copyOfRange(preorder, rightRootIndex, inorder.length);
            TreeNode leftNode = buildTree(leftPre, left);
            TreeNode rightNode = buildTree(rightPre, right);
            root.left = leftNode;
            root.right = rightNode;
        } else {
            int[] leftPre = Arrays.copyOfRange(preorder, 1, preorder.length);
            root.left = buildTree(leftPre, left);
        }

        return root;


    }

    private int searchRoot(int[] arr, int splitIndex, int[] preorder) {
        Set<Integer> set = new HashSet<>();
        for (int i : arr) {
            set.add(i);
        }
        while (splitIndex < preorder.length) {
            if (set.contains(preorder[splitIndex])) {
                return splitIndex;
            }
            splitIndex++;
        }
        return -1;

    }

    private int search(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1;
    }
}
