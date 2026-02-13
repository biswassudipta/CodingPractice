package dsa;

public class AVLTreeNode{
    int key;
    AVLTreeNode left;
    AVLTreeNode right;
    int height;

    public AVLTreeNode(int key) {
        this.key = key;
        this.height=1;
    }

}
