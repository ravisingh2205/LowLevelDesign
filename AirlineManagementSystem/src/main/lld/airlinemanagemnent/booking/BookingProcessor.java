package AirlineManagementSystem.src.main.lld.airlinemanagemnent.booking;

import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Passenger;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Seat;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.flight.Flight;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BookingProcessor {

    private static BookingProcessor instance;
    private final Map<String, Booking> bookings;
    private final Object lock = new Object();

    private BookingProcessor() {
        bookings = new HashMap<>();
    }

    public static BookingProcessor getInstance() {
        if(instance==null){
            instance =  new BookingProcessor();
        }
        return instance;
    }

    public Booking createBooking(Flight flight, Passenger passenger, Seat seat, double price){
        String bookingNumber = UUID.randomUUID().toString();
        Booking booking = new Booking( flight,  passenger,  seat,  price);
        synchronized (lock){
            bookings.put(bookingNumber,booking);
        }
        return booking;
    }

    public Booking cancelBooking(String bookingNumber){
        Booking booking;
        synchronized (lock){
            booking  = bookings.get(bookingNumber);
            if(booking!=null){
                //bookings.remove(booking);
                booking.cancel();
            }
        }
        return booking;
    }
}

