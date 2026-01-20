package dsa;

public class HuffmanNode implements Comparable<HuffmanNode> {
    private final char data;
    private final int frequency;
    private HuffmanNode left, right;

    public HuffmanNode(char data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    public HuffmanNode(char data, int frequency, HuffmanNode right, HuffmanNode left) {
        this.data = data;
        this.frequency = frequency;
        this.right = right;
        this.left = left;
    }

    public char getData() {
        return data;
    }

    public int getFrequency() {
        return frequency;
    }


    public HuffmanNode getLeft() {
        return left;
    }


    public HuffmanNode getRight() {
        return right;
    }


    @Override
    public int compareTo(HuffmanNode other) {
        return (this.frequency - other.getFrequency());
    }
}
