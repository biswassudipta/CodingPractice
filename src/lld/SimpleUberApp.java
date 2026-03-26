package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

// ==========================================
// Models
// ==========================================

record Location(double x, double y) {
    public double distanceTo(Location other) {
        return Math.hypot(this.x - other.x, this.y - other.y);
    }
}

record Rider(String id, String name) {}

enum DriverStatus { AVAILABLE, ON_TRIP, OFFLINE }

class Driver {
    private String id;
    private String name;
    private Location location;
    private DriverStatus status;
    private final ReentrantLock lock;

    public Driver(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.status = DriverStatus.AVAILABLE;
        this.lock = new ReentrantLock();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Location getLocation() { return location; }
    public DriverStatus getStatus() { return status; }

    public void setStatus(DriverStatus status) { this.status = status; }
    public void setLocation(Location location) { this.location = location; }
    public ReentrantLock getLock() { return lock; }
}

class Ride {
    private String rideId;
    private Rider rider;
    private Driver driver;
    private double price;

    public Ride(String rideId, Rider rider, Driver driver, double price) {
        this.rideId = rideId;
        this.rider = rider;
        this.driver = driver;
        this.price = price;
    }

    public void printReceipt() {
        System.out.printf("Ride %s | Rider: %s | Driver: %s | Price: $%.2f%n",
                rideId, rider.name(), driver.getName(), price);
    }
}

// ==========================================
// Strategy Pattern for Surge Pricing
// ==========================================

interface PricingStrategyUber {
    double calculatePrice(Location source, Location dest, int availableDrivers, int activeRequests);
}

class DynamicSurgePricing implements PricingStrategyUber {
    private static final double BASE_RATE_PER_KM = 2.0;

    @Override
    public double calculatePrice(Location source, Location dest, int availableDrivers, int activeRequests) {
        double distance = source.distanceTo(dest);
        double basePrice = distance * BASE_RATE_PER_KM;

        // Simple Surge Logic: If requests outnumber available drivers, apply a 1.5x multiplier
        double surgeMultiplier = 1.0;
        if (availableDrivers == 0 || activeRequests > availableDrivers) {
            surgeMultiplier = 1.5;
            System.out.println(Thread.currentThread().getName() + " -> SURGE PRICING ACTIVE (1.5x)");
        }

        return basePrice * surgeMultiplier;
    }
}

// ==========================================
// Core Ride Dispatch System (Thread-Safe)
// ==========================================

class RideMatchingSystem {
    private final Map<String, Driver> drivers = new ConcurrentHashMap<>();
    private final PricingStrategyUber pricingStrategy;

    // Track active requests to calculate surge pricing
    private final AtomicInteger activeRequests = new AtomicInteger(0);

    public RideMatchingSystem(PricingStrategyUber pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void registerDriver(Driver driver) {
        drivers.put(driver.getId(), driver);
    }

    public Ride requestRide(Rider rider, Location source, Location destination) {
        activeRequests.incrementAndGet();
        try {
            List<Driver> availableDrivers = getAvailableDriversSortedByDistance(source);

            // Calculate dynamic price before assigning
            double price = pricingStrategy.calculatePrice(source, destination, availableDrivers.size(), activeRequests.get());

            // Attempt to assign the nearest driver using optimistic locking
            for (Driver driver : availableDrivers) {
                // tryLock() is non-blocking. If another thread is currently booking this driver,
                // it immediately returns false and we move to the next closest driver.
                if (driver.getLock().tryLock()) {
                    try {
                        // Double-check state inside the lock (in case they went offline a millisecond ago)
                        if (driver.getStatus() == DriverStatus.AVAILABLE) {
                            driver.setStatus(DriverStatus.ON_TRIP);
                            String rideId = "RIDE-" + UUID.randomUUID().toString().substring(0, 5);

                            System.out.println(Thread.currentThread().getName() + " -> Matched " + rider.name() + " with " + driver.getName());
                            return new Ride(rideId, rider, driver, price);
                        }
                    } finally {
                        driver.getLock().unlock();
                    }
                }
            }

            System.out.println(Thread.currentThread().getName() + " -> No drivers available right now for " + rider.name());
            return null; // System is at capacity

        } finally {
            activeRequests.decrementAndGet();
        }
    }

    public void completeRide(Driver driver, Location newLocation) {
        driver.getLock().lock();
        try {
            driver.setStatus(DriverStatus.AVAILABLE);
            driver.setLocation(newLocation);
            System.out.println("Driver " + driver.getName() + " is now AVAILABLE at " + newLocation);
        } finally {
            driver.getLock().unlock();
        }
    }

    private List<Driver> getAvailableDriversSortedByDistance(Location source) {
        List<Driver> available = new ArrayList<>();
        for (Driver d : drivers.values()) {
            if (d.getStatus() == DriverStatus.AVAILABLE) {
                available.add(d);
            }
        }
        available.sort(Comparator.comparingDouble(d -> d.getLocation().distanceTo(source)));
        return available;
    }
}

// ==========================================
// Main Execution / Concurrent Simulation
// ==========================================

public class SimpleUberApp {
    public static void main(String[] args) throws InterruptedException {
        RideMatchingSystem system = new RideMatchingSystem(new DynamicSurgePricing());

        // Register 2 Drivers
        Driver d1 = new Driver("D1", "Alice", new Location(0, 0));
        Driver d2 = new Driver("D2", "Bob", new Location(2, 2));
        system.registerDriver(d1);
        system.registerDriver(d2);

        // 3 Riders trying to book simultaneously
        Rider r1 = new Rider("R1", "Charlie");
        Rider r2 = new Rider("R2", "Diana");
        Rider r3 = new Rider("R3", "Eve");

        ExecutorService users = Executors.newFixedThreadPool(3);

        Runnable bookRide1 = () -> {
            Ride ride = system.requestRide(r1, new Location(1, 1), new Location(10, 10));
            if (ride != null) ride.printReceipt();
        };

        Runnable bookRide2 = () -> {
            Ride ride = system.requestRide(r2, new Location(1, 2), new Location(5, 5));
            if (ride != null) ride.printReceipt();
        };

        Runnable bookRide3 = () -> {
            Ride ride = system.requestRide(r3, new Location(0, 1), new Location(8, 8));
            if (ride != null) ride.printReceipt();
        };

        System.out.println("---- Simulating Concurrent Ride Requests ----");
        users.submit(bookRide1);
        users.submit(bookRide2);
        users.submit(bookRide3);

        users.shutdown();
        users.awaitTermination(3, TimeUnit.SECONDS);

        /* Expected Output:
           Because 3 riders are requesting rides but only 2 drivers exist,
           surge pricing will trigger for at least some of them.
           One rider will gracefully fail to find a ride.
           No double-booking will occur thanks to tryLock().
        */
    }
}