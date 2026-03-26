package lld;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

// ==========================================
// Models & Enums
// ==========================================

enum RoomStyle { STANDARD, DELUXE, SUITE }

record Tourist(String id, String name) {}

record HotelBooking(String bookingId, Tourist tourist, String roomId, LocalDate checkIn, LocalDate checkOut, double totalPrice) {}

// ==========================================
// Strategy Pattern for Pricing
// ==========================================

interface HotelPricingStrategy {
    double calculatePrice(double basePrice, LocalDate date);
}

class WeekendSurgePricing implements HotelPricingStrategy {
    @Override
    public double calculatePrice(double basePrice, LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            return basePrice * 1.5; // 50% surge on weekends
        }
        return basePrice;
    }
}

// ==========================================
// Core Components (Thread-Safe)
// ==========================================

class Room {
    private final String roomId;
    private final RoomStyle style;
    private final double basePricePerNight;

    // Tracks specific dates this room is occupied
    private final Set<LocalDate> bookedDates;

    // Fine-grained lock for this specific room
    private final ReentrantLock roomLock;

    public Room(String roomId, RoomStyle style, double basePricePerNight) {
        this.roomId = roomId;
        this.style = style;
        this.basePricePerNight = basePricePerNight;
        this.bookedDates = new HashSet<>();
        this.roomLock = new ReentrantLock();
    }

    public String getRoomId() { return roomId; }
    public RoomStyle getStyle() { return style; }
    public double getBasePrice() { return basePricePerNight; }

    /**
     * Safely checks if the room is available for the requested range.
     * Note: This does NOT lock the room. It is for quick searches.
     */
    public boolean isAvailable(LocalDate checkIn, LocalDate checkOut) {
        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            if (bookedDates.contains(date)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Atomically locks the room, validates dates, and books it.
     */
    public boolean tryBookDates(LocalDate checkIn, LocalDate checkOut) {
        roomLock.lock();
        try {
            // 1. Double-check availability inside the lock (Crucial for thread safety)
            if (!isAvailable(checkIn, checkOut)) {
                return false;
            }

            // 2. Mark dates as booked
            for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
                bookedDates.add(date);
            }
            return true;
        } finally {
            roomLock.unlock();
        }
    }
}

class HotelBookingSystem {
    private final Map<String, Room> inventory = new ConcurrentHashMap<>();
    private final Queue<HotelBooking> hotelBookingLedger = new ConcurrentLinkedQueue<>();
    private final HotelPricingStrategy hotelPricingStrategy;

    public HotelBookingSystem(HotelPricingStrategy hotelPricingStrategy) {
        this.hotelPricingStrategy = hotelPricingStrategy;
    }

    public void addRoom(Room room) {
        inventory.put(room.getRoomId(), room);
    }

    /**
     * Searches for available rooms of a specific style.
     */
    public List<Room> searchAvailableRooms(RoomStyle style, LocalDate checkIn, LocalDate checkOut) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : inventory.values()) {
            if (room.getStyle() == style && room.isAvailable(checkIn, checkOut)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }

    /**
     * Handles the concurrent booking request.
     */
    public HotelBooking bookRoom(Tourist tourist, RoomStyle style, LocalDate checkIn, LocalDate checkOut) {
        // 1. Get potential rooms
        List<Room> candidates = searchAvailableRooms(style, checkIn, checkOut);

        if (candidates.isEmpty()) {
            System.out.println(Thread.currentThread().getName() + " -> No " + style + " rooms available for " + tourist.name());
            return null;
        }

        // 2. Iterate through candidates and try to lock/book one
        for (Room room : candidates) {
            if (room.tryBookDates(checkIn, checkOut)) {
                // Success! Calculate price and generate booking
                double totalPrice = calculateTotal(room.getBasePrice(), checkIn, checkOut);
                String bookingId = "BKG-" + UUID.randomUUID().toString().substring(0, 6);

                HotelBooking booking = new HotelBooking(bookingId, tourist, room.getRoomId(), checkIn, checkOut, totalPrice);
                hotelBookingLedger.offer(booking); // Lock-free append

                System.out.printf("%s -> SUCCESS: %s booked %s from %s to %s | Total: $%.2f%n",
                        Thread.currentThread().getName(), tourist.name(), room.getRoomId(), checkIn, checkOut, totalPrice);

                return booking;
            }
        }

        System.out.println(Thread.currentThread().getName() + " -> Failed: Rooms were snatched up by other users.");
        return null;
    }

    private double calculateTotal(double basePrice, LocalDate checkIn, LocalDate checkOut) {
        double total = 0.0;
        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
            total += hotelPricingStrategy.calculatePrice(basePrice, date);
        }
        return total;
    }
}

// ==========================================
// Main Execution / Concurrent Simulation
// ==========================================

public class HotelApp {
    public static void main(String[] args) throws InterruptedException {
        HotelBookingSystem system = new HotelBookingSystem(new WeekendSurgePricing());

        // Add 2 Standard Rooms
        system.addRoom(new Room("101", RoomStyle.STANDARD, 100.0));
        system.addRoom(new Room("102", RoomStyle.STANDARD, 100.0));

        // The target weekend
        LocalDate friday = LocalDate.of(2026, 5, 1);
        LocalDate monday = LocalDate.of(2026, 5, 4);

        System.out.println("---- Simulating Concurrent Hotel Bookings ----");

        // 3 Users trying to book a Standard room for the exact same weekend
        Tourist alice = new Tourist("U1", "Alice");
        Tourist bob = new Tourist("U2", "Bob");
        Tourist charlie = new Tourist("U3", "Charlie");

        ExecutorService users = Executors.newFixedThreadPool(3);

        users.submit(() -> system.bookRoom(alice, RoomStyle.STANDARD, friday, monday));
        users.submit(() -> system.bookRoom(bob, RoomStyle.STANDARD, friday, monday));
        users.submit(() -> system.bookRoom(charlie, RoomStyle.STANDARD, friday, monday));

        users.shutdown();
        users.awaitTermination(3, TimeUnit.SECONDS);

        /* Expected Outcome:
           Since there are only 2 rooms, 2 users will successfully book.
           The 3rd user will gracefully fail.
           Pricing will reflect the Friday/Saturday surge rates.
        */
    }
}