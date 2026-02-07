package dsa;

public class ListNode {
    int val;
    public ListNode next;
    public ListNode(int val) { this.val = val; }

    public static void printList(ListNode node) {
        while (node != null) {
            System.out.print(node.val);
            if (node.next != null) System.out.print(" -> ");
            node = node.next;
        }
    }
}