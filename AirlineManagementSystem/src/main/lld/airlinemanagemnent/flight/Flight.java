package AirlineManagementSystem.src.main.lld.airlinemanagemnent.flight;

import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Aircraft;
import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Seat;

import java.time.LocalDateTime;
import java.util.*;

public class Flight {
    private String flightNumber;
    private String source;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private FlightStatus flightStatus;
    private Aircraft aircraft;
    private Map<String, Seat> seatMap;
    private List<Seat> availableSeats;


    public Flight(String source, String destination, LocalDateTime departureTime,
                  LocalDateTime arrivalTime, Aircraft aircraft) {
        this.flightNumber = UUID.randomUUID().toString();
        this.source = source;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.flightStatus = FlightStatus.ON_TIME;
        this.aircraft = aircraft;
        this.seatMap = new HashMap<>();
        this.availableSeats = new ArrayList<>();
    }


    public String getFlightNumber() {
        return flightNumber;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public Map<String, Seat> getSeatMap() {
        return seatMap;
    }

    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    public synchronized boolean isSeatAvailable(String seatNo){
        Seat seat = seatMap.get(seatNo);
        return  seat!= null && !seat.isBooked();
    }
    public synchronized void reserveSeat(String seatNo){
        Seat seat = seatMap.get(seatNo);
        if(seat==null){
            throw  new IllegalArgumentException("Invalid seat Number");
        }
        seat.reserve();
    }

    public synchronized void releaseSeat(String seatNo){
        Seat seat = seatMap.get(seatNo);
        if(seat==null){
            throw  new IllegalArgumentException("Invalid seat number");
        }
        seat.release();
    }
}
