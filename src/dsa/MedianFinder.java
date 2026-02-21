package dsa;

import java.util.Collections;
import java.util.PriorityQueue;

@SuppressWarnings("DataFlowIssue")
public class MedianFinder {
    public PriorityQueue<Integer> minHeap;
    public PriorityQueue<Integer> maxHeap;


    public MedianFinder() {
        this.minHeap = new PriorityQueue<>();
        this.maxHeap = new PriorityQueue<>(Collections.reverseOrder());
    }

    public void addNum(int num) {
        maxHeap.add(num);
        minHeap.add(maxHeap.poll());
        if (minHeap.size() > maxHeap.size()) {
            maxHeap.add(minHeap.poll());
        }
    }

    public double findMedian() {
        if (minHeap.size() == maxHeap.size()) {
            return ((double) minHeap.peek() + maxHeap.peek()) / 2.0d;
        } else {
            return (double) maxHeap.peek();
        }
    }
}
