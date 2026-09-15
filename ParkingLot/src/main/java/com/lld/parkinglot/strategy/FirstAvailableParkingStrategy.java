package com.lld.parkinglot.strategy;

import com.lld.parkinglot.model.ParkingFloor;
import com.lld.parkinglot.model.ParkingLot;
import com.lld.parkinglot.model.ParkingSpot;
import com.lld.parkinglot.model.Vehicle;

public class FirstAvailableParkingStrategy implements ParkingStrategy{

    @Override
    public ParkingSpot findAndClaimSpot(Vehicle vehicle, ParkingLot parkingLot) {
        for (ParkingFloor floor : parkingLot.getFloors()) {
            for (ParkingSpot spot : floor.getSpots()) {
                if(spot.tryPark(vehicle)){
                    return spot;

                }
            }
        }
        return null;
    }
}
