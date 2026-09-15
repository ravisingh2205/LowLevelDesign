package com.lld.parkinglot.service;

import com.lld.parkinglot.enums.TicketStatus;
import com.lld.parkinglot.exception.InvalidTicketException;
import com.lld.parkinglot.exception.ParkingFullException;
import com.lld.parkinglot.model.ParkingLot;
import com.lld.parkinglot.model.ParkingSpot;
import com.lld.parkinglot.model.ParkingTicket;
import com.lld.parkinglot.model.Vehicle;
import com.lld.parkinglot.strategy.ParkingStrategy;
import com.lld.parkinglot.strategy.PricingStrategy;

import java.util.UUID;

public class ParkingService {
    private final ParkingLot parkingLot;
    private final ParkingStrategy parkingStrategy;
    private final PricingStrategy pricingStrategy;

    public ParkingService(ParkingLot parkingLot, ParkingStrategy parkingStrategy, PricingStrategy pricingStrategy) {
        this.parkingLot = parkingLot;
        this.parkingStrategy = parkingStrategy;
        this.pricingStrategy = pricingStrategy;
    }

    public ParkingTicket park(Vehicle vehicle){
        ParkingSpot spot =parkingStrategy.findAndClaimSpot(vehicle,parkingLot);
        if(spot==null){
            throw new ParkingFullException("No  compatible  parking spot available");
        }
        return new ParkingTicket(UUID.randomUUID().toString(),spot,vehicle);
    }

    public double unpark(ParkingTicket ticket){
        if(ticket==null || ticket.getStatus() != TicketStatus.ACTIVE){
            throw new InvalidTicketException("Invalid or already closed ticket");
        }
        ticket.close();

        double fee = pricingStrategy.calculateFee(ticket);
        ticket.getSpot().release();
        return fee;
    }
}
