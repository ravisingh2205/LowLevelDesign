package com.lld.parkinglot.strategy;

import com.lld.parkinglot.model.ParkingTicket;

import java.time.Duration;

public class HourlyPricingStrategy implements PricingStrategy{

    private final double hourlyRate;

    public HourlyPricingStrategy(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    @Override
    public double calculateFee(ParkingTicket ticket) {
        long minutes = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toMinutes();
        long hours = Math.max(1,(long) Math.ceil(minutes/60.0));
        return hours*hourlyRate;
    }
}
