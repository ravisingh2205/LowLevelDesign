package com.lld.parkinglot.model;

import com.lld.parkinglot.enums.SpotType;
import com.lld.parkinglot.enums.TicketStatus;

import java.time.LocalDateTime;

public class ParkingTicket {
    private final String ticketId;
    private final ParkingSpot spot;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private TicketStatus status;

    public ParkingTicket(String ticketId, ParkingSpot spot, Vehicle vehicle) {
        this.ticketId = ticketId;
        this.spot = spot;
        this.vehicle = vehicle;
        this.entryTime = LocalDateTime.now();
        this.status = TicketStatus.ACTIVE;
    }

    public void close(){
        if(status == TicketStatus.CLOSED){
            throw new IllegalStateException("Ticket already closed!");
        }
        this.exitTime = LocalDateTime.now();
        this.status = TicketStatus.CLOSED;
    }

    public String getTicketId() {
        return ticketId;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public TicketStatus getStatus() {
        return status;
    }


}
