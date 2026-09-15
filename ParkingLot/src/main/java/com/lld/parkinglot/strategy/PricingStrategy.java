package com.lld.parkinglot.strategy;

import com.lld.parkinglot.model.ParkingTicket;

public interface PricingStrategy {
    double calculateFee(ParkingTicket ticket);
}
