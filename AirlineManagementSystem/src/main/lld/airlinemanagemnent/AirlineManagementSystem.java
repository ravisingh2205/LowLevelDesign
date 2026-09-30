package AirlineManagementSystem.src.main.lld.airlinemanagemnent;

import AirlineManagementSystem.src.main.lld.airlinemanagemnent.booking.Booking;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.booking.BookingProcessor;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.flight.Flight;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.flight.FlightSearch;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.payment.Payment;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.payment.PaymentProcessor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AirlineManagementSystem {

    private final Map<String, Flight> flights;
    private final Map<String, Passenger> passengers;
    private final Map<String, Aircraft> aircrafts;
    private final FlightSearch flightSearch;
    private final BookingProcessor bookingProcessor;
    private final PaymentProcessor paymentProcessor;

    public AirlineManagementSystem() {
        flights = new HashMap<>();
        aircrafts = new HashMap<>();
        passengers = new HashMap<>();
        flightSearch = new FlightSearch();
        bookingProcessor = BookingProcessor.getInstance();
        paymentProcessor = PaymentProcessor.getGetInstance();
    }

    public Passenger addPassenger(String name, String email) {
        Passenger passenger = new Passenger(name, email);
        passengers.put(passenger.getPassengerId(), passenger);
        return passenger;
    }

    public Aircraft addAircraft(String model, String tailNumber, int totalSeats){
        Aircraft aircraft = new Aircraft(model, tailNumber, totalSeats);
        aircrafts.put(tailNumber,aircraft);
        return aircraft;
    }

    public Flight addFlights(String source, String destination, LocalDateTime departureTime,
                             LocalDateTime arrivalTime, String aircraftNumber) {
        Aircraft aircraft = aircrafts.get(aircraftNumber);
        Flight flight = new Flight(source, destination, departureTime, arrivalTime, aircraft);
        flights.put(flight.getFlightNumber(), flight);
        flightSearch.addFlight(flight);
        return flight;
    }

    public List<Flight> searchFlights(String source, String destination, LocalDate date){
        return flightSearch.searchFlight(source, destination, date);
    }

    public Booking bookFlight(String flightNumber, String passengerId, Seat seat, double price){
        Flight flight = flights.get(flightNumber);
        Passenger passenger = passengers.get(passengerId);
        return bookingProcessor.createBooking(flight, passenger, seat, price);
    }

    public void cancelBooking(String bookingNumber){
        bookingProcessor.cancelBooking(bookingNumber);
    }

    public void processPayment(Payment payment){
        paymentProcessor.processPayment(payment);
    }

}
