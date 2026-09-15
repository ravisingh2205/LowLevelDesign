package com.lld.parkinglot.model;

import com.lld.parkinglot.enums.SpotType;

public class ParkingSpot {
    private final String spotId;
    private final SpotType type;
    private Vehicle vehicle;

    public ParkingSpot(String spotId, SpotType type) {
        this.spotId = spotId;
        this.type = type;

    }

    public String getSpotId() {
        return spotId;
    }

    public SpotType getType() {
        return type;
    }

    public boolean isAvailable(){
        return vehicle==null;
    }

    public boolean canFit(Vehicle vehicle){
        return (type.name().equals(vehicle.getType().name()));
    }

    public synchronized boolean tryPark(Vehicle vehicle){
        if(!isAvailable() || !canFit(vehicle)){
            return false;
        }
        this.vehicle = vehicle;
        return true;
    }

    public synchronized void release(){
        this.vehicle=null;
    }
}
