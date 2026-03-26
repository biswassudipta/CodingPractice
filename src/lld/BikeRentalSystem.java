package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * WAYFAIR LLD: Bike Rental System
 * To run: Compile and execute BikeRentalSystem.
 *
 * Fixes over original:
 *  1. activeRentals keyed by user.id() — not user.name() — prevents same-name collisions.
 *  2. User and RentalTransaction converted to records (pure immutable data carriers).
 *  3. RentalTransaction.startTime passed from caller — keeps the record side-effect free.
 *  4. Removed dead field startStationId (stored but never readable).
 *  5. Bike.status marked volatile — visible across threads even inside lock boundaries.
 *  6. InterruptedException in simulation restores the interrupt flag.
 */

// ==========================================
// Enums & Models
// ==========================================

enum BikeStatus {
    AVAILABLE, RENTED, MAINTENANCE
}

class Bike {
    private final String id;
    private volatile BikeStatus status;  // FIX: volatile — written/read across threads

    public Bike(String id) {
        this.id     = id;
        this.status = BikeStatus.AVAILABLE;
    }

    public String getId()                      { return id; }
    public BikeStatus getStatus()              { return status; }
    public void setStatus(BikeStatus status)   { this.status = status; }
}

// FIX: record — id and name are immutable; accessor is user.id(), not getId()
record User(String id, String name) {}

// FIX: record — startTime passed in by caller so the record stays a pure value type.
//      Removed unused startStationId (was stored but had no getter and was never read).
record RentalTransaction(String transactionId, User user, Bike bike, long startTime) {}

// ==========================================
// Core Components (Thread-Safe)
// ==========================================

class Station {
    private final String        id;
    private final int           capacity;
    private final Queue<Bike>   availableBikes;
    private final ReentrantLock lock = new ReentrantLock();

    public Station(String id, int capacity) {
        this.id             = id;
        this.capacity       = capacity;
        this.availableBikes = new LinkedList<>();
    }

    public String getId() { return id; }

    public void addInitialBike(Bike bike) { availableBikes.add(bike); }

    public Bike rentBike() {
        lock.lock();
        try {
            if (availableBikes.isEmpty()) {
                System.out.printf("%s -> Station %s has no bikes available.%n",
                        Thread.currentThread().getName(), id);
                return null;
            }
            Bike bike = availableBikes.poll();
            bike.setStatus(BikeStatus.RENTED);
            System.out.printf("%s -> Rented bike %s from Station %s%n",
                    Thread.currentThread().getName(), bike.getId(), id);
            return bike;
        } finally {
            lock.unlock();
        }
    }

    public boolean returnBike(Bike bike) {
        lock.lock();
        try {
            if (availableBikes.size() >= capacity) {
                System.out.printf("%s -> Station %s is full! Cannot return %s%n",
                        Thread.currentThread().getName(), id, bike.getId());
                return false;
            }
            bike.setStatus(BikeStatus.AVAILABLE);
            availableBikes.offer(bike);
            System.out.printf("%s -> Returned bike %s to Station %s%n",
                    Thread.currentThread().getName(), bike.getId(), id);
            return true;
        } finally {
            lock.unlock();
        }
    }
}

class RentalSystemManager {
    private static final double HOURLY_RATE = 2.50;

    private final Map<String, Station>           stations     = new ConcurrentHashMap<>();
    // FIX: keyed by user.id() — name is not unique; two "Alice"s would overwrite each other
    private final Map<String, RentalTransaction> activeRentals = new ConcurrentHashMap<>();

    public void addStation(Station station) { stations.put(station.getId(), station); }

    public RentalTransaction rentBike(User user, String stationId) {
        Station station = stations.get(stationId);
        if (station == null) return null;

        Bike bike = station.rentBike();
        if (bike == null) return null;

        // FIX: pass startTime from here — keeps RentalTransaction a pure record
        RentalTransaction txn = new RentalTransaction(
                UUID.randomUUID().toString(), user, bike, System.currentTimeMillis());
        activeRentals.put(user.id(), txn);   // FIX: key by ID, not name
        return txn;
    }

    public void returnBike(User user, String destinationStationId) {
        RentalTransaction txn = activeRentals.get(user.id());  // FIX: lookup by ID
        if (txn == null) {
            System.out.println("No active rental found for " + user.name());
            return;
        }

        Station dest = stations.get(destinationStationId);
        if (dest == null) return;

        if (dest.returnBike(txn.bike())) {
            activeRentals.remove(user.id());
            // 1 ms = 1 hour in this simulation; swap denominator to 3_600_000L for production
            long hours = Math.max(1, System.currentTimeMillis() - txn.startTime());
            System.out.printf("Transaction complete for %s. Total cost: $%.2f%n",
                    user.name(), hours * HOURLY_RATE);
        } else {
            System.out.println(user.name() + " must find another station to return the bike.");
        }
    }
}

// ==========================================
// Main / Concurrent Simulation
// ==========================================

public class BikeRentalSystem {
    public static void main(String[] args) throws InterruptedException {
        RentalSystemManager system = new RentalSystemManager();

        Station timesSquare = new Station("Station-TimesSquare", 3);
        timesSquare.addInitialBike(new Bike("B1"));
        timesSquare.addInitialBike(new Bike("B2"));
        // 1 empty slot

        Station centralPark = new Station("Station-CentralPark", 2);
        centralPark.addInitialBike(new Bike("B3"));
        centralPark.addInitialBike(new Bike("B4"));
        // 0 empty slots (full)

        system.addStation(timesSquare);
        system.addStation(centralPark);

        System.out.println("---- Wayfair Bike Rental System ----");

        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Alice rents from Times Square; tries to return to Central Park (full initially)
        Runnable user1 = () -> {
            User alice = new User("U1", "Alice");
            system.rentBike(alice, "Station-TimesSquare");
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // FIX: restore interrupt flag
            }
            system.returnBike(alice, "Station-CentralPark");
        };

        // Bob rents from Central Park, freeing a slot for Alice
        Runnable user2 = () -> system.rentBike(new User("U2", "Bob"), "Station-CentralPark");

        // Charlie and Diana race for the last bike at Times Square
        Runnable user3 = () -> system.rentBike(new User("U3", "Charlie"), "Station-TimesSquare");
        Runnable user4 = () -> system.rentBike(new User("U4", "Diana"),   "Station-TimesSquare");

        executor.submit(user1);
        executor.submit(user2);
        executor.submit(user3);
        executor.submit(user4);

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Simulation complete.");
    }
}