package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * WAYFAIR LLD: Automated Parking Garage
 * To run: Compile and execute ParkingGarageSystem.
 * Key fixes over original:
 *  1. Ticket now holds its Level — processExit no longer iterates all levels.
 *  2. HourlyPricingStrategy uses a configurable ms-per-unit for correct fee math.
 *  3. canFitVehicle replaced with a single ordinal comparison.
 *  4. Singleton uses the initialization-on-demand holder (no synchronized overhead).
 *  5. InterruptedException in simulation properly restores the interrupt flag.
 *  6. car2Task saves and exits its ticket so the spot is actually freed.
 */

// ==========================================
// Enums & Models
// ==========================================

enum VehicleType {
    MOTORCYCLE, COMPACT, LARGE  // ordinal order matters: smaller fits inside larger
}

enum SpotType {
    MOTORCYCLE, COMPACT, LARGE  // ordinal must match VehicleType for fit-check shortcut
}

abstract class Vehicle {
    private final String licensePlate;
    private final VehicleType type;

    protected Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() { return licensePlate; }
    public VehicleType getType()    { return type; }
}

class Motorcycle extends Vehicle {
    public Motorcycle(String lp) { super(lp, VehicleType.MOTORCYCLE); }
}

class Car extends Vehicle {
    public Car(String lp) { super(lp, VehicleType.COMPACT); }
}

class Truck extends Vehicle {
    public Truck(String lp) { super(lp, VehicleType.LARGE); }
}

// ==========================================
// ParkingSpot
// ==========================================

class ParkingSpot {
    private final String   spotId;
    private final SpotType spotType;
    private volatile Vehicle currentVehicle;   // visible across threads

    public ParkingSpot(String spotId, SpotType spotType) {
        this.spotId   = spotId;
        this.spotType = spotType;
    }

    public boolean isAvailable() { return currentVehicle == null; }

    /**
     * A vehicle fits if the spot is at least as large as the vehicle.
     * Because VehicleType and SpotType share the same ordinal ordering
     * (MOTORCYCLE < COMPACT < LARGE), a single comparison suffices.
     */
    public boolean canFitVehicle(Vehicle v) {
        return isAvailable() && v.getType().ordinal() <= spotType.ordinal();
    }

    public void park(Vehicle v)  { this.currentVehicle = v; }
    public void removeVehicle()  { this.currentVehicle = null; }
    public String getSpotId()    { return spotId; }
}

// ==========================================
// Ticket  — record: immutable data carrier, Level included so exit is O(1)
// ==========================================

// entryTimeMs is passed in by the caller (System.currentTimeMillis() at creation time)
// so the record stays a pure value type with no hidden side effects in its constructor.
record Ticket(String ticketId, Vehicle vehicle, ParkingSpot spot, Level level, long entryTimeMs) {}

// ==========================================
// Strategy Pattern for Pricing
// ==========================================

interface PricingStrategy {
    double calculateFee(Ticket ticket);
}

/** @param msPerUnit  3_600_000 for production; 1 for simulation where 1 ms ≅ 1 hour */
record HourlyPricingStrategy(double hourlyRate, long msPerUnit) implements HotelPricingStrategy {
    @Override
    public double calculateFee(Ticket ticket) {
        long elapsed = System.currentTimeMillis() - ticket.entryTimeMs();
        long units   = Math.max(1, elapsed / msPerUnit);  // minimum 1 unit billed
        return units * hourlyRate;
    }
}

// ==========================================
// Level
// ==========================================

class Level {
    private final int             floorNumber;
    private final List<ParkingSpot> spots;

    public Level(int floorNumber, int numMotorcycle, int numCompact, int numLarge) {
        this.floorNumber = floorNumber;
        this.spots       = new ArrayList<>();

        for (int i = 0; i < numMotorcycle; i++)
            spots.add(new ParkingSpot("L" + floorNumber + "-M" + i, SpotType.MOTORCYCLE));
        for (int i = 0; i < numCompact; i++)
            spots.add(new ParkingSpot("L" + floorNumber + "-C" + i, SpotType.COMPACT));
        for (int i = 0; i < numLarge; i++)
            spots.add(new ParkingSpot("L" + floorNumber + "-L" + i, SpotType.LARGE));
    }

    /** Synchronized: prevents two threads assigning the same spot simultaneously. */
    public synchronized ParkingSpot findAndPark(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (spot.canFitVehicle(vehicle)) {
                spot.park(vehicle);
                return spot;
            }
        }
        return null;
    }

    /** Synchronized with findAndPark on the same monitor to avoid races on spot state. */
    public synchronized void freeSpot(ParkingSpot spot) {
        spot.removeVehicle();
    }

    public int getFloorNumber() { return floorNumber; }
}

// ==========================================
// ParkingGarage — Singleton via holder pattern
// ==========================================

class ParkingGarage {

    // Initialization-on-demand holder: lazy, thread-safe, no synchronization cost on hot path
    private static final class Holder {
        static final ParkingGarage INSTANCE = new ParkingGarage();
    }

    public static ParkingGarage getInstance() { return Holder.INSTANCE; }

    private final List<Level>      levels;
    private final HotelPricingStrategy pricingStrategy;
    private final AtomicInteger    ticketCounter;

    private ParkingGarage() {
        this.levels          = new ArrayList<>();
        this.ticketCounter   = new AtomicInteger(1000);
        // 1 ms = 1 hour for the simulation; swap to 3_600_000L for production
        this.pricingStrategy = new HourlyPricingStrategy(5.0, 1L);
    }

    public void addLevel(Level level) { levels.add(level); }

    public Ticket processEntry(Vehicle vehicle) {
        for (Level level : levels) {
            ParkingSpot spot = level.findAndPark(vehicle);
            if (spot != null) {
                String ticketId = "TKT-" + ticketCounter.getAndIncrement();
                Ticket ticket   = new Ticket(ticketId, vehicle, spot, level, System.currentTimeMillis());
                System.out.printf("%s -> Parked %s [%s] at %s%n",
                        Thread.currentThread().getName(),
                        vehicle.getType(), vehicle.getLicensePlate(), spot.getSpotId());
                return ticket;
            }
        }
        System.out.printf("%s -> Garage FULL for %s [%s]%n",
                Thread.currentThread().getName(),
                vehicle.getType(), vehicle.getLicensePlate());
        return null;
    }

    public void processExit(Ticket ticket) {
        if (ticket == null) return;

        // FIX: free the spot directly on its level — no iteration over all levels
        ticket.level().freeSpot(ticket.spot());

        double fee = pricingStrategy.calculateFee(ticket);
        System.out.printf("%s -> Unparked [%s]. Fee: $%.2f%n",
                Thread.currentThread().getName(),
                ticket.vehicle().getLicensePlate(), fee);
    }
}

// ==========================================
// Main / Concurrent Simulation
// ==========================================

public class ParkingGarageSystem {

    static void main() throws InterruptedException {
        ParkingGarage garage = ParkingGarage.getInstance();
        garage.addLevel(new Level(1, 1, 1, 1));  // 1 motorcycle, 1 compact, 1 large spot

        System.out.println("---- Wayfair Automated Parking Garage ----");

        ExecutorService gates = Executors.newFixedThreadPool(5);

        // car1: parks, waits 3 ms (≅ 3 hours in simulation), then exits
        Runnable car1Task = () -> {
            Ticket t = garage.processEntry(new Car("CAR-111"));
            try {
                Thread.sleep(3);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // FIX: restore interrupt flag
            }
            if (t != null) garage.processExit(t);
        };

        // FIX: car2 now saves its ticket and exits, so the spot is properly freed
        Runnable car2Task = () -> {
            Ticket t = garage.processEntry(new Car("CAR-222"));
            if (t != null) garage.processExit(t);
        };

        Runnable truckTask = () -> {
            Ticket t = garage.processEntry(new Truck("TRK-999"));
            if (t != null) garage.processExit(t);
        };

        Runnable motoTask = () -> {
            Ticket t = garage.processEntry(new Motorcycle("MOT-000"));
            if (t != null) garage.processExit(t);
        };

        gates.submit(car1Task);
        gates.submit(car2Task);
        gates.submit(truckTask);
        gates.submit(motoTask);

        gates.shutdown();
        gates.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("Simulation complete.");
    }
}