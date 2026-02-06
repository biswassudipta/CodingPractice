package dsa;

public class LinkedList {


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

    public static NodeWithRandomPointer clone(NodeWithRandomPointer head) {
        if (head == null) return null;
        NodeWithRandomPointer current = head;
        while (current != null) {
            NodeWithRandomPointer nodeWithRandomPointer = new NodeWithRandomPointer(current.data);
            nodeWithRandomPointer.next = current.next;
            current.next = nodeWithRandomPointer;
            current = nodeWithRandomPointer.next;
        }
        NodeWithRandomPointer clonedCurrenHead = head.next;
        current = head;
        while (current != null) {
            if (current.random != null) {
                current.next.random = current.random.next;
            }
            current = current.next.next;
        }
        current = head;
        NodeWithRandomPointer clone = clonedCurrenHead;
        while (current.next.next != null) {
            current.next = current.next.next;
            clone.next = clone.next.next;
            current = current.next;
            clone = clone.next;
        }
        return clonedCurrenHead;
    }
}
