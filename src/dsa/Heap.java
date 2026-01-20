package dsa;


import java.util.ArrayList;
import java.util.List;

public class Heap {
    private static final int MAX_SIZE = 100;
    private int size;

    public List<Integer> getHeapList() {
        return heap;
    }

    private final List<Integer> heap;


    public Heap(List<Integer> input) {
        size = input.size();
        if (size > MAX_SIZE) {
            throw new IllegalArgumentException("Heap size exceeded");
        }
        this.heap = new ArrayList<>(List.copyOf(input));

    }

    private int findParentIndex(int index) {
        return (index - 1) / 2;
    }

    private int findParent(int index) {
        return index >= 0 ? heap.get(index) : Integer.MAX_VALUE;
    }

    private int findLeftChildIndex(int index) {
        return (2 * index) + 1;
    }

    private int findRightChildIndex(int index) {
        return (2 * index) + 2;
    }

    private int findLeftChild(int index) {
        return index < size ? heap.get(index) : Integer.MIN_VALUE;
    }

    private int findRightChild(int index) {
        return index < size ? heap.get(index) : Integer.MIN_VALUE + 1;
    }

    private void swap(int i, int j) {
        int valueI = heap.get(i);
        int valueJ = heap.get(j);
        heap.set(j, valueI);
        heap.set(i, valueJ);
    }

    public void insert(int value) {
        if (size + 1 > MAX_SIZE) {
            throw new IllegalArgumentException("Heap size exceeded");
        }
        heap.add(value);
        size++;
        int index = size - 1;
        int parent = findParent(findParentIndex(size - 1));
        int node = heap.get(size - 1);
        while (index >= 0 && node > parent) {
            swap(index, findParentIndex(index));
            index = findParentIndex(index);
            parent = findParent(findParentIndex(index));
        }
    }

    public int delete() {
        if (size == 0) {
            throw new IllegalArgumentException("No element to delete");
        }
        int elementDeleted = heap.getFirst();
        swap(0, size - 1);
        heap.remove(size - 1);
        size--;
        if (size > 1) {
            int index = 0;
            topToDownTraversal(index);
        }

        return elementDeleted;
    }

    public void heapify() {
        if (size == 0) {
            throw new IllegalArgumentException("No element present");
        }

        int heapifyStartIndex = (size / 2) - 1;
        for (; heapifyStartIndex >= 0; heapifyStartIndex--) {
            topToDownTraversal(heapifyStartIndex);

        }

    }

    public void resetSize() {
        size = heap.size();
    }

    private void topToDownTraversal(int index) {
        int node = heap.get(index);
        int nextIndex = findLeftChild(findLeftChildIndex(index)) >= findRightChild(findRightChildIndex(index)) ? findLeftChildIndex(index) : findRightChildIndex(index);
        while (nextIndex < size && node < heap.get(nextIndex)) {
            swap(index, nextIndex);
            node = heap.get(nextIndex);
            index = nextIndex;
            nextIndex = findLeftChild(findLeftChildIndex(index)) >= findRightChild(findRightChildIndex(index)) ? findLeftChildIndex(index) : findRightChildIndex(index);
        }
    }

    public void sort() {
        heapify();
        int count = size;
        while (count > 0) {
            int highestElement = delete();
            heap.add(size, highestElement);

            count--;
        }
        IO.println("heap sort \n");
        for (Integer integer : heap) IO.print(integer + " ");

    }


}
