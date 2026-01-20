package dsa;

public class Node {
    public int data;
    public Node next;

    public Node(int new_data) {
        data = new_data;
        next = null;
    }

    public static void printList(Node node) {
        while (node != null) {
            System.out.print(node.data);
            if (node.next != null) System.out.print(" -> ");
            node = node.next;
        }
    }




}