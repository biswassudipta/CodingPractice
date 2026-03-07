package lld;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

// ─────────────────────────────────────────────
//  Shared Bounded Buffer
// ─────────────────────────────────────────────
class BoundedBuffer {
    private final Queue<Integer> buffer = new ArrayDeque<>();
    private final int capacity;

    public BoundedBuffer(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Called by a Producer.
     * Waits if the buffer is FULL, then adds the item and notifies all waiting threads.
     */
    public synchronized void produce(int item, String producerName) throws InterruptedException {
        // Wait while buffer is full
        while (buffer.size() == capacity) {
            System.out.println(producerName + " waiting — buffer FULL (" + buffer.size() + "/" + capacity + ")");
            wait();
        }

        buffer.add(item);
        System.out.println(producerName + " produced: " + item
                + "  | Buffer size: " + buffer.size() + "/" + capacity);

        // Wake up ALL waiting threads (both producers and consumers)
        // Consumers waiting on "buffer empty" will now proceed
        notifyAll();
    }

    /**
     * Called by a Consumer.
     * Waits if the buffer is EMPTY, then removes an item and notifies all waiting threads.
     */
    public synchronized void consume(String consumerName) throws InterruptedException {
        // Wait while buffer is empty
        while (buffer.isEmpty()) {
            System.out.println(consumerName + " waiting — buffer EMPTY");
            wait();
        }

        int item = buffer.poll();
        System.out.println(consumerName + " consumed: " + item
                + "  | Buffer size: " + buffer.size() + "/" + capacity);

        // Wake up ALL waiting threads (both consumers and producers)
        // Producers waiting on "buffer full" will now proceed
        notifyAll();

    }

    public synchronized int size() {
        return buffer.size();
    }
}

// ─────────────────────────────────────────────
//  Producer Thread
// ─────────────────────────────────────────────
class Producer implements Runnable {
    private final BoundedBuffer buffer;
    private final String name;
    private final int itemsToProduce;

    // Each producer starts from a different offset so items are distinguishable
    private static final AtomicInteger globalCounter = new AtomicInteger(0);

    public Producer(BoundedBuffer buffer, String name, int itemsToProduce) {
        this.buffer = buffer;
        this.name = name;
        this.itemsToProduce = itemsToProduce;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemsToProduce; i++) {
                int item;
                synchronized (Producer.class) {
                    item = globalCounter.incrementAndGet();
                }
                buffer.produce(item, name);
                Thread.sleep((long) (Math.random() * 300)); // simulate work
            }
            System.out.println(name + " finished producing.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " interrupted.");
        }
    }
}

// ─────────────────────────────────────────────
//  Consumer Thread
// ─────────────────────────────────────────────
class Consumer implements Runnable {
    private final BoundedBuffer buffer;
    private final String name;
    private final int itemsToConsume;

    public Consumer(BoundedBuffer buffer, String name, int itemsToConsume) {
        this.buffer = buffer;
        this.name = name;
        this.itemsToConsume = itemsToConsume;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < itemsToConsume; i++) {
                buffer.consume(name);
                Thread.sleep((long) (Math.random() * 500)); // simulate work
            }
            System.out.println(name + " finished consuming.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(name + " interrupted.");
        }
    }
}

// ─────────────────────────────────────────────
//  Main — Configure M Producers and N Consumers
// ─────────────────────────────────────────────
public class ProducerConsumer {

    static void main() throws InterruptedException {
        CompletableFuture<Void> future = new CompletableFuture<>();

        final int M              = 3;   // Number of producers
        final int N              = 2;   // Number of consumers
        final int BUFFER_SIZE    = 5;   // Max buffer capacity
        final int ITEMS_PER_PROD = 4;   // Items each producer will produce

        // Total items produced = M * ITEMS_PER_PROD
        // Distribute evenly among consumers
        final int totalItems          = M * ITEMS_PER_PROD;
        final int itemsPerConsumer    = totalItems / N;
        final int extraItems          = totalItems % N; // give remainder to last consumer

        System.out.println("=== Producer-Consumer Demo ===");
        System.out.printf("Producers: %d | Consumers: %d | Buffer Capacity: %d%n%n",
                M, N, BUFFER_SIZE);

        BoundedBuffer sharedBuffer = new BoundedBuffer(BUFFER_SIZE);

        // Create and start M producer threads
        Thread[] producers = new Thread[M];
        for (int i = 0; i < M; i++) {
            producers[i] = new Thread(
                    new Producer(sharedBuffer, "Producer-" + (i + 1), ITEMS_PER_PROD));
        }

        // Create and start N consumer threads
        Thread[] consumers = new Thread[N];
        for (int i = 0; i < N; i++) {
            int items = itemsPerConsumer + (i == N - 1 ? extraItems : 0);
            consumers[i] = new Thread(
                    new Consumer(sharedBuffer, "Consumer-" + (i + 1), items));
        }

        // Start all threads
        for (Thread p : producers) p.start();
        for (Thread c : consumers) c.start();

        // Wait for all threads to finish
        for (Thread p : producers) p.join();
        for (Thread c : consumers) c.join();

        System.out.println("\n=== All producers and consumers finished. ===");
        System.out.println("Remaining items in buffer: " + sharedBuffer.size());
    }
}
