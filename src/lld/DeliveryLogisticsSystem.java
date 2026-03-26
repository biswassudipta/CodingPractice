package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * WAYFAIR LLD: Furniture Delivery Logistics System
 * Focus: Bin-packing logic with thread-safe truck assignments.
 */

// ==========================================
// Models
// ==========================================

class DeliveryItem {
    private String id;
    private double weight;
    private double volume;

    public DeliveryItem(String id, double weight, double volume) {
        this.id = id;
        this.weight = weight;
        this.volume = volume;
    }

    public double getWeight() { return weight; }
    public double getVolume() { return volume; }
    public String getId() { return id; }
}

class DeliveryOrder {
    private String orderId;
    private List<DeliveryItem> items;

    public DeliveryOrder(String orderId, List<DeliveryItem> items) {
        this.orderId = orderId;
        this.items = items;
    }

    public String getOrderId() { return orderId; }

    public double getTotalWeight() {
        return items.stream().mapToDouble(DeliveryItem::getWeight).sum();
    }

    public double getTotalVolume() {
        return items.stream().mapToDouble(DeliveryItem::getVolume).sum();
    }
}

// ==========================================
// Core Components (Thread-Safe)
// ==========================================

class DeliveryTruck {
    private String truckId;
    private double maxWeightCapacity;
    private double maxVolumeCapacity;

    private double currentWeight = 0.0;
    private double currentVolume = 0.0;

    private List<DeliveryOrder> assignedOrders = new ArrayList<>();

    // Fine-grained lock for this specific truck
    private final ReentrantLock truckLock = new ReentrantLock();

    public DeliveryTruck(String truckId, double maxWeightCapacity, double maxVolumeCapacity) {
        this.truckId = truckId;
        this.maxWeightCapacity = maxWeightCapacity;
        this.maxVolumeCapacity = maxVolumeCapacity;
    }

    public String getTruckId() { return truckId; }

    /**
     * Attempts to assign an order to this truck safely.
     * Returns true if successful, false if capacity is exceeded.
     */
    public boolean tryAssignOrder(DeliveryOrder order) {
        double orderWeight = order.getTotalWeight();
        double orderVolume = order.getTotalVolume();

        truckLock.lock();
        try {
            // Double-check capacity inside the lock
            if (currentWeight + orderWeight <= maxWeightCapacity &&
                    currentVolume + orderVolume <= maxVolumeCapacity) {

                currentWeight += orderWeight;
                currentVolume += orderVolume;
                assignedOrders.add(order);

                System.out.printf("%s -> Assigned Order %s to %s (Cap: %.1f%% W, %.1f%% V)\n",
                        Thread.currentThread().getName(),
                        order.getOrderId(),
                        truckId,
                        (currentWeight/maxWeightCapacity)*100,
                        (currentVolume/maxVolumeCapacity)*100);
                return true;
            }
            return false; // Order won't fit
        } finally {
            truckLock.unlock();
        }
    }

    public void printManifest() {
        System.out.println("Truck " + truckId + " Manifest:");
        System.out.printf("  Weight: %.1f / %.1f | Volume: %.1f / %.1f\n",
                currentWeight, maxWeightCapacity, currentVolume, maxVolumeCapacity);
        System.out.print("  Orders: ");
        for (DeliveryOrder o : assignedOrders) System.out.print(o.getOrderId() + " ");
        System.out.println("\n");
    }
}

class DispatchManager {
    private List<DeliveryTruck> fleet = new CopyOnWriteArrayList<>();
    private Queue<DeliveryOrder> unassignedQueue = new ConcurrentLinkedQueue<>();

    public void registerTruck(DeliveryTruck truck) {
        fleet.add(truck);
    }

    /**
     * Processes an order concurrently. Looks for the first available truck.
     */
    public void dispatchOrder(DeliveryOrder order) {
        boolean assigned = false;

        for (DeliveryTruck truck : fleet) {
            if (truck.tryAssignOrder(order)) {
                assigned = true;
                break;
            }
        }

        if (!assigned) {
            System.out.println(Thread.currentThread().getName() + " -> Could not fit Order " + order.getOrderId() + " in fleet. Queuing.");
            unassignedQueue.offer(order);
        }
    }

    public void printFleetStatus() {
        System.out.println("\n--- Final Fleet Dispatch Status ---");
        for (DeliveryTruck truck : fleet) {
            truck.printManifest();
        }
        System.out.println("Pending Orders in Queue: " + unassignedQueue.size());
    }
}

// ==========================================
// Main Execution / Concurrent Simulation
// ==========================================

public class DeliveryLogisticsSystem {
    public static void main(String[] args) throws InterruptedException {
        DispatchManager dispatcher = new DispatchManager();

        // 1. Setup Fleet (2 Box Trucks)
        dispatcher.registerTruck(new DeliveryTruck("Truck-A", 1000.0, 500.0));
        dispatcher.registerTruck(new DeliveryTruck("Truck-B", 1000.0, 500.0));

        System.out.println("---- Wayfair Delivery Logistics System ----");

        // 2. Create concurrent tasks representing incoming orders
        ExecutorService system = Executors.newFixedThreadPool(6);

        Runnable order1 = () -> dispatcher.dispatchOrder(new DeliveryOrder("ORD-1", Arrays.asList(
                new DeliveryItem("Sofa1", 300, 200), new DeliveryItem("Lamp1", 10, 5)))); // W: 310, V: 205

        Runnable order2 = () -> dispatcher.dispatchOrder(new DeliveryOrder("ORD-2", Arrays.asList(
                new DeliveryItem("Bed1", 400, 250)))); // W: 400, V: 250

        Runnable order3 = () -> dispatcher.dispatchOrder(new DeliveryOrder("ORD-3", Arrays.asList(
                new DeliveryItem("DiningTable", 350, 150)))); // W: 350, V: 150

        Runnable order4 = () -> dispatcher.dispatchOrder(new DeliveryOrder("ORD-4", Arrays.asList(
                new DeliveryItem("MassiveWardrobe", 800, 450)))); // Extremely heavy and bulky

        // 3. Dispatch concurrently
        system.submit(order1);
        system.submit(order2);
        system.submit(order3);
        system.submit(order4);

        system.shutdown();
        system.awaitTermination(5, TimeUnit.SECONDS);

        // 4. Print results
        dispatcher.printFleetStatus();

        /* Expected outcome:
           Truck A and B will absorb ORD-1, ORD-2, and ORD-3.
           ORD-4 is so massive (800 W / 450 V) that if Trucks A and B already have *any* of the other orders, ORD-4 will fail to fit and be pushed to the pending queue.
        */
    }
}
