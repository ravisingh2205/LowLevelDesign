package AirlineManagementSystem.src.main.lld.airlinemanagemnent.flight;

import AirlineManagementSystem.src.main.lld.airlinemanagemnent.Seat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FlightSearch {
    private final List<Flight> flights;

    public FlightSearch() {
        this.flights = new ArrayList<Flight>();
    }

    public void addFlight(Flight flight){
        flights.add(flight);
    }

    public List<Flight> searchFlight(String source, String destination, LocalDate date){
        return flights.stream().filter(flight -> flight.getSource().equalsIgnoreCase(source))
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> flight.getDepartureTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
}
