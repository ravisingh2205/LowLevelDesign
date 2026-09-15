package com.lld.parkinglot.model;

import com.lld.parkinglot.enums.VehicleType;

public class Car extends Vehicle{
    public Car(String licenseNumber, VehicleType type) {
        super(licenseNumber, type);
    }
}
