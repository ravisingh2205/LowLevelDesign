package AirlineManagementSystem.src.main.lld.airlinemanagemnent.booking;

import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Passenger;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Seat;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.flight.Flight;

import java.util.UUID;

public class Booking {

    private final String bookingid;
    private final Flight flight;
    private final Passenger passenger;
    private final Seat seat;
    private final double price;
    private BookingStatus bookingStatus;

    public Booking(Flight flight, Passenger passenger, Seat seat, double price) {
        this.bookingid = UUID.randomUUID().toString();
        this.flight = flight;
        this.passenger = passenger;
        this.seat = seat;
        this.price = price;
        this.bookingStatus = BookingStatus.CONFIRMED;
    }

    public void cancel(){
        bookingStatus = BookingStatus.CANCELLED;
        seat.release();
    }

    public String getBookingid() {
        return bookingid;
    }
}
