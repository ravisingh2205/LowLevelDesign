package AirlineManagementSystem.src.main.lld.airlinemanagemnent;

public class Aircraft {

    private final String model;
    private final String tailNumber;
    private final int totalSeats;

    public Aircraft(String model, String tailNumber, int totalSeats) {
        this.model = model;
        this.tailNumber = tailNumber;
        this.totalSeats = totalSeats;
    }

    public String getTailNumber() {
        return tailNumber;
    }
}
