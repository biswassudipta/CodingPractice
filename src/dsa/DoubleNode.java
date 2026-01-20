package dsa;

public class DoubleNode {
    public int data;
    public DoubleNode next, prev;

    public DoubleNode(int d) {
        data = d;
        next = prev = null;
    }

    public static void printList(DoubleNode node) {
        while (node != null) {
            System.out.print(node.data);
            if (node.next != null) System.out.print(" -> ");
            node = node.next;
        }
    }
}
