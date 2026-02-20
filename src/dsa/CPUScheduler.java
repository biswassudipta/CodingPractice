package dsa;

import java.util.*;

public class CPUScheduler {
    public int leastInterval(char[] tasks, int n) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        Queue<int[]> queue = new ArrayDeque<>();
        Map<Character, Integer> map = new HashMap<>();

        for (char c : tasks) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        maxHeap.addAll(map.values());
        int t = 0;
        while (!maxHeap.isEmpty() || !queue.isEmpty()) {
            if (!queue.isEmpty() && queue.peek()[1] == t) {
                int[] front = queue.poll();
                maxHeap.add(front[0]);
            }
            if (!maxHeap.isEmpty()) {
                int current = maxHeap.remove();
                current--;
                process();
                t++;
                if (current > 0) {
                    queue.add(new int[]{current, t + n});
                }
            }else{
                stayIdle();
                t++;
            }

        }

        return t;
    }

    private void process() {
        IO.println("task is being processed\n");
    }

    private void stayIdle() {
        IO.println("cpu is idle . No tasks to process\n");
    }
}
