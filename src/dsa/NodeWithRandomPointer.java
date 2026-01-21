package dsa;

public class NodeWithRandomPointer{
    public int data;
    public NodeWithRandomPointer next;
    public NodeWithRandomPointer random;


    public NodeWithRandomPointer(int x) {
            this.data = x;
            this.next = null;
            this.random = null;
        }
    public static void printListWithRandomPointer(NodeWithRandomPointer head) {
        while (head != null) {
            System.out.print(head.data + "(");
            if (head.random != null) {
                System.out.print(head.random.data);
            } else {
                System.out.print("null");
            }
            System.out.print(")");

            if (head.next != null) {
                System.out.print(" -> ");
            }
            head = head.next;
        }
        System.out.println();
    }
}
