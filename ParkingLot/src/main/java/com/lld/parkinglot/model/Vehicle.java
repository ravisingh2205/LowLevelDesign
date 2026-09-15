package com.lld.parkinglot.model;

import com.lld.parkinglot.enums.VehicleType;

public abstract class Vehicle {

    private final String licenseNumber; //vehicleNumber
    private final VehicleType type;

    public Vehicle(String licenseNumber, VehicleType type) {
        this.licenseNumber = licenseNumber;
        this.type = type;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public VehicleType getType() {
        return type;
    }
}
