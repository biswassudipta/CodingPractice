package lld;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

enum SeatStatus { AVAILABLE, BOOKED }

record Viewer(String id, String name) {}

record Booking(String id, Viewer viewer, Show show, List<String> seatIds) {}

class Seat {
    private String id;
    private SeatStatus status;

    public Seat(String id) {
        this.id = id;
        this.status = SeatStatus.AVAILABLE;
    }

    public String getId() { return id; }
    public SeatStatus getStatus() { return status; }
    public void setStatus(SeatStatus status) { this.status = status; }
}

class Show {
    private String id;
    private String movieName;
    private Map<String, Seat> seats;
    private final ReentrantLock showLock;

    public Show(String id, String movieName, List<String> seatIds) {
        this.id = id;
        this.movieName = movieName;
        this.seats = new HashMap<>();
        for (String seatId : seatIds) {
            this.seats.put(seatId, new Seat(seatId));
        }
        this.showLock = new ReentrantLock();
    }

    public String getId() { return id; }
    public String getMovieName() { return movieName; }
    public Map<String, Seat> getSeats() { return seats; }
    public ReentrantLock getShowLock() { return showLock; }
}

class TicketBookingSystem {
    private Map<String, Show> shows = new ConcurrentHashMap<>();
    private List<HotelBooking> hotelBookings = new CopyOnWriteArrayList<>();

    public void addShow(Show show) {
        shows.put(show.getId(), show);
    }

    public HotelBooking bookTicket(Viewer viewer, String showId, List<String> requestedSeatIds) {
        Show show = shows.get(showId);
        if (show == null) return null;

        ReentrantLock lock = show.getShowLock();
        lock.lock();

        try {
            Map<String, Seat> showSeats = show.getSeats();

            for (String seatId : requestedSeatIds) {
                Seat seat = showSeats.get(seatId);
                if (seat == null || seat.getStatus() == SeatStatus.BOOKED) {
                    return null;
                }
            }

            for (String seatId : requestedSeatIds) {
                showSeats.get(seatId).setStatus(SeatStatus.BOOKED);
            }

            String bookingId = UUID.randomUUID().toString().substring(0, 8);
            HotelBooking booking = new HotelBooking(bookingId, viewer, show, requestedSeatIds);
            hotelBookings.add(booking);

            return booking;

        } finally {
            lock.unlock();
        }
    }
}

public class BookMyShowApp {
    public static void main(String[] args) throws InterruptedException {
        TicketBookingSystem system = new TicketBookingSystem();

        Show avengers = new Show("SHOW-1", "Avengers", Arrays.asList("A1", "A2", "A3", "A4"));
        system.addShow(avengers);

        Viewer alice = new Viewer("U1", "Alice");
        Viewer bob = new Viewer("U2", "Bob");

        ExecutorService executor = Executors.newFixedThreadPool(2);

        Runnable task1 = () -> {
            HotelBooking b = system.bookTicket(alice, "SHOW-1", Arrays.asList("A1", "A2"));
            if (b != null) System.out.println("Alice booked successfully: " + b.id());
            else System.out.println("Alice booking failed.");
        };

        Runnable task2 = () -> {
            HotelBooking b = system.bookTicket(bob, "SHOW-1", Arrays.asList("A2", "A3"));
            if (b != null) System.out.println("Bob booked successfully: " + b.id());
            else System.out.println("Bob booking failed.");
        };

        executor.submit(task1);
        executor.submit(task2);

        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);
    }
}