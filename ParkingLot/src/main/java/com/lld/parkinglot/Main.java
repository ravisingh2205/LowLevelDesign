package com.lld.parkinglot;

import com.lld.parkinglot.enums.SpotType;
import com.lld.parkinglot.enums.VehicleType;
import com.lld.parkinglot.model.*;
import com.lld.parkinglot.service.ParkingService;
import com.lld.parkinglot.strategy.FirstAvailableParkingStrategy;
import com.lld.parkinglot.strategy.HourlyPricingStrategy;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ParkingSpot bikeSpot = new ParkingSpot("B1", SpotType.BIKE);
        ParkingSpot carSpot = new ParkingSpot("C1", SpotType.CAR);
        ParkingSpot truckSpot = new ParkingSpot("T1", SpotType.TRUCK);

        ParkingFloor floor1 = new ParkingFloor(1, List.of(bikeSpot,carSpot,truckSpot));
        ParkingLot parkingLot = new ParkingLot("City Parking",List.of(floor1));

        ParkingService parkingService = new ParkingService(parkingLot,new FirstAvailableParkingStrategy(),new HourlyPricingStrategy(50));

        Vehicle car = new Car("KA01AB1234", VehicleType.CAR);

        //ENTRY
        ParkingTicket ticket = parkingService.park(car);
        System.out.println("Ticket : " + ticket.getTicketId());

        System.out.println("Parked at : " + ticket.getSpot().getSpotId());

        //EXIT
        double fee = parkingService.unpark(ticket);
        System.out.println("Parking Fee in INR: "+ fee);


    }
}
