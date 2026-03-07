package lld;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinExample {

    // 1. Create the Task Class extending RecursiveTask
    static class SumArrayTask extends RecursiveTask<Long> {
        private static final int THRESHOLD = 10_000; // The threshold to stop splitting
        private final long[] array;
        private final int start;
        private final int end;

        public SumArrayTask(long[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            // Base Case: If the workload is small enough, compute it directly
            int length = end - start;
            if (length <= THRESHOLD) {
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            }

            // Recursive Case: Split the task in half
            int middle = start + (length / 2);
            SumArrayTask leftTask = new SumArrayTask(array, start, middle);
            SumArrayTask rightTask = new SumArrayTask(array, middle, end);

            // Fork the left task (pushes it to the queue for a thread to pick up)
            leftTask.fork();

            // Compute the right task directly in the current thread (Optimization)
            long rightResult = rightTask.compute();

            // Wait for the left task to finish and get its result
            long leftResult = leftTask.join();

            // Combine the results
            return leftResult + rightResult;
        }
    }

    static void main() {
        // Create an array with 100 million numbers
        long[] numbers = new long[100_000_000];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }

        // 2. Initialize the ForkJoinPool
        // By default, it uses the number of available CPU cores
        try (ForkJoinPool pool = new ForkJoinPool()) {

            // 3. Create the initial root task
            SumArrayTask rootTask = new SumArrayTask(numbers, 0, numbers.length);

            System.out.println("Starting calculation...");
            long startTime = System.currentTimeMillis();

            // 4. Submit the root task to the pool and get the result
            long result = pool.invoke(rootTask);

            long endTime = System.currentTimeMillis();

            System.out.println("Total Sum: " + result);
            System.out.println("Time taken: " + (endTime - startTime) + " ms");
        }
    }
}