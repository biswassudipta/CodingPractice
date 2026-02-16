package dsa;

public class MinStack {

    private static class Node {
        int val;
        int min;
        Node next;

        Node(int val, int min, Node next) {
            this.val = val;
            this.min = min;
            this.next = next;
        }
    }

    private Node head;

    public MinStack() {
        head = null;
    }

    public void push(int val) {
        if (head == null) {
            head = new Node(val, val, null);
        } else {
            int currentMin = Math.min(val, head.min);
            head = new Node(val, currentMin, head);
        }
    }

    public void pop() {
        if (head != null) {
            head = head.next;
        }
    }

    public int top() {
        if (head != null) {
            return head.val;
        }
        return -1;
    }

    public int getMin() {
        if (head != null) {
            return head.min;
        }
        return -1;
    }
}
