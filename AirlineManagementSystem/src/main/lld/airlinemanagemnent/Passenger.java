package AirlineManagementSystem.src.main.lld.airlinemanagemnent;

import java.util.UUID;

public class Passenger {

    private String passengerId;
    private String name;
    private String email;

    public Passenger( String name, String email) {
        this.passengerId = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
    }

    public String getPassengerId() {
        return passengerId;
    }

}
