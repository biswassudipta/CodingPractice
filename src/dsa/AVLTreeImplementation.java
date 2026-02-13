package dsa;

import static java.lang.Math.max;

public class AVLTreeImplementation {
    private int balanceFactor(AVLTreeNode node) {
        return (node.left != null ? node.left.height : 0) - (node.right != null ? node.right.height : 0);
    }

    int height(AVLTreeNode node) {
        if (node == null) return 0;
        return node.height;
    }

    private AVLTreeNode rightRotate(AVLTreeNode y) {
        AVLTreeNode x = y.left;
        AVLTreeNode T2 = x.right;

        //noinspection SuspiciousNameCombination
        x.right = y;
        y.left = T2;

        y.height = max(height(y.left), height(y.right)) + 1;
        x.height = max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private AVLTreeNode leftRotate(AVLTreeNode x) {
        //noinspection SuspiciousNameCombination
        AVLTreeNode y = x.right;
        AVLTreeNode T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = max(height(x.left), height(x.right)) + 1;
        y.height = max(height(y.left), height(y.right)) + 1;

        //noinspection SuspiciousNameCombination
        return y;
    }

    public AVLTreeNode insert(AVLTreeNode node ,int key) {
        if (node == null)
            return (new AVLTreeNode(key));
        if (key < node.key)
            node.left = insert(node.left, key);
        else if (key > node.key)
            node.right = insert(node.right, key);
        else // Duplicate keys not allowed
            return node;

        node.height = 1 + max(height(node.left), height(node.right));
       int balance = balanceFactor(node);
        if (balance > 1 && key < node.left.key)  return rightRotate(node);

        if (balance < -1 && key > node.right.key) return leftRotate(node);

        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        if (balance < -1 && key < node.right.key) {
            //noinspection SuspiciousNameCombination
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }
        return node;

    }
}
