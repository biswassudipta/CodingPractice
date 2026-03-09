package dsa;

import java.util.*;

public class TopKFrequentElements {
    public int[] topKFrequent(int[] numbs, int k) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : numbs) {
            map.merge(num, 1, Integer::sum);
        }
        Queue<Map.Entry<Integer, Integer>> pq = new PriorityQueue<>(Map.Entry.comparingByValue());
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            pq.add(entry);
            if (pq.size() > k) {
                pq.poll();
            }
        }
        int[] result = new int[k];
        while (k > 0) {
            result[--k] = Objects.requireNonNull(pq.poll()).getKey();

        }
        return result;
    }
}
