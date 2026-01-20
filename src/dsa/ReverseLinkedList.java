package dsa;

public class ReverseLinkedList {


    public static Node reverseLinkedIstIteratively(Node head) {
        Node prev = null;
        Node next;
        Node current = head;

        while (current != null) {
            next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        return prev;
    }

    public static Node reverseLinkedIstIRecursively(Node head) {
        if (head == null || head.next == null) {
            return head;
        }
        Node rest = reverseLinkedIstIRecursively(head.next);
        head.next.next = head;
        head.next = null;

        return rest;
    }

    public static DoubleNode reverseDoublyLinkedList(DoubleNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        DoubleNode prev = null;
        DoubleNode current = head;
        while (current != null) {
            prev = current.prev;
            current.prev = current.next;
            current.next = prev;

            current = current.prev;
        }

        return prev.prev;

    }
}
