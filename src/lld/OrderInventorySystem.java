package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

record Order(String orderId, List<OrderItem> orderItems) {
}

record OrderItem(String productId, int qty) {
}

class InventoryManager {
    Map<String, Integer> inventory;
    Map<String, ReentrantLock> products;

    public InventoryManager() {
        inventory = new ConcurrentHashMap<>();
        products = new ConcurrentHashMap<>();
    }

    public void addInventory(String productId, int qty) {
        int currentStock = inventory.getOrDefault(productId, 0);
        inventory.put(productId, currentStock + qty);
        products.putIfAbsent(productId, new ReentrantLock());
    }


    public boolean processOrder(Order order) {
        List<OrderItem> productList = order.orderItems();
        List<String> productIdList = new ArrayList<>();
        for (OrderItem orderItem : productList) {
            productIdList.add(orderItem.productId());
        }
        Collections.sort(productIdList);

        for (String productId : productIdList) {

            ReentrantLock productLock = products.get(productId);

            if (productLock != null) {
                productLock.lock();
            } else {
                releaseProductLocks(productIdList);
                return false;
            }

        }
        try {
            for (OrderItem orderItem : productList) {
                int currentQty = inventory.getOrDefault(orderItem.productId(), 0);
                if (currentQty < orderItem.qty()) {
                    return false;
                }

            }

            for (OrderItem orderItem : productList) {
                int currentQty = inventory.get(orderItem.productId());
                currentQty= currentQty - orderItem.qty();
                inventory.put(orderItem.productId(),currentQty);
            }


            return true;


        } finally {

            releaseProductLocks(productIdList);


        }

    }

    private void releaseProductLocks(List<String> productIdList) {
        for (String productId : productIdList) {
            ReentrantLock productLock = products.get(productId);
            if (productLock != null && productLock.isHeldByCurrentThread()) {
                productLock.unlock();
            }

        }

    }

    public Integer getStock(String productId) {
        return inventory.getOrDefault(productId, 0);
    }
}

public class OrderInventorySystem {
    static void main() throws InterruptedException {
        InventoryManager im = new InventoryManager();

        System.out.println("---- Wayfair Multi-Threaded Inventory System ----");

        // Initial Stock: 10 beds, 5 lamps
        im.addInventory("bed-123", 10);
        im.addInventory("lamp-456", 5);

        System.out.println("Initial Stock -> Bed: 10, Lamp: 5");

        // Simulate multiple customers ordering simultaneously
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // Customer 1 wants 6 beds and 3 lamps
        Order o1 = new Order("Order-1", Arrays.asList(new OrderItem("bed-123", 6), new OrderItem("lamp-456", 3)));

        // Customer 2 wants 5 beds and 3 lamps
        Order o2 = new Order("Order-2", Arrays.asList(new OrderItem("bed-123", 5), new OrderItem("lamp-456", 3)));

        // Customer 3 wants 2 lamps
        Order o3 = new Order("Order-3", List.of(new OrderItem("lamp-456", 2)));

        // Submit to thread pool to simulate concurrency
        executor.submit(() -> im.processOrder(o1));
        executor.submit(() -> im.processOrder(o2));
        executor.submit(() -> im.processOrder(o3));

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        // Print final inventory state
        System.out.println("\nFinal Stock Levels:");
        System.out.println("Bed-123 stock: " + im.getStock("bed-123"));
        System.out.println("Lamp-456 stock: " + im.getStock("lamp-456"));

        /* Expected output logic:
           Because there are only 5 lamps total, and O1 wants 3, O2 wants 3.
           One of them will fail, the other will succeed. O3 wants 2, it might
           succeed depending on who grabbed the locks first.
           The total inventory will never go below 0!
        */
    }
}
