package com.lld.parkinglot.strategy;

import com.lld.parkinglot.model.ParkingLot;
import com.lld.parkinglot.model.ParkingSpot;
import com.lld.parkinglot.model.Vehicle;

public interface ParkingStrategy {
    ParkingSpot findAndClaimSpot(Vehicle vehicle, ParkingLot parkingLot);
}
