package com.lld.parkinglot.model;

import java.util.List;

public class ParkingLot {
    private final String name;
    private final List<ParkingFloor> floors;

    public ParkingLot(String name, List<ParkingFloor> floors) {
        this.name = name;
        this.floors = List.copyOf(floors);
    }

    public String getName() {
        return name;
    }

    public List<ParkingFloor> getFloors() {
        return floors;
    }
}
