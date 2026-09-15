# Parking Lot --- Low-Level Design

## 1. Problem Statement

Design a Parking Lot system that supports multiple floors and different
vehicle/parking-spot types. A vehicle should be assigned a compatible
available spot on entry, receive a parking ticket, pay a fee on exit,
and release the spot.

The implementation is scoped so the core design can be explained and
coded in roughly **40--45 minutes** in an LLD interview.

## 2. Requirements

### Functional Requirements

1.  A parking lot can contain multiple parking floors.
2.  Each floor contains multiple parking spots.
3.  Support `BIKE`, `CAR`, and `TRUCK`.
4.  Each parking spot has a supported `SpotType`.
5.  A vehicle can be parked only in a compatible available spot.
6.  Generate a ticket when a vehicle is parked.
7.  Record entry and exit time.
8.  Calculate the parking fee on exit.
9.  Release the parking spot after exit.
10. Prevent two concurrent requests from claiming the same spot.

### Out of Scope for V1

-   Online reservations
-   Lost-ticket handling
-   Payment gateway integration
-   EV charging
-   Valet parking
-   Persistent database storage
-   Distributed locking across application instances

## 3. Assumptions

-   One vehicle occupies one spot.
-   Bike, car, and truck use matching spot types in V1.
-   First available compatible spot is selected.
-   Pricing is hourly with a minimum charge of one hour.
-   The implementation is in-memory.
-   Multiple entrance threads may call the parking service concurrently.

## 4. Entities

``` text
ParkingLot
ParkingFloor
ParkingSpot
Vehicle
  ├── Bike
  ├── Car
  └── Truck
ParkingTicket

VehicleType
SpotType
TicketStatus
```

Variable behavior is separated behind:

``` text
ParkingStrategy
  └── FirstAvailableParkingStrategy

PricingStrategy
  └── HourlyPricingStrategy
```

## 5. Class Design

``` text
                         ParkingLot
                             |
                             | 1..*
                             v
                       ParkingFloor
                             |
                             | 1..*
                             v
                       ParkingSpot
                             |
                             v
                          Vehicle
                       /     |     \
                    Bike    Car    Truck

ParkingService
     |
     +------ ParkingStrategy
     |
     +------ PricingStrategy
     |
     +------ ParkingTicket
```

  -----------------------------------------------------------------------
Class                               Responsibility
  ----------------------------------- -----------------------------------
`ParkingLot`                        Owns floors

`ParkingFloor`                      Owns spots

`ParkingSpot`                       Protects occupancy state and
atomically claims/releases a spot

`Vehicle`                           Base vehicle representation

`ParkingTicket`                     Represents one parking session

`ParkingStrategy`                   Spot allocation behavior

`PricingStrategy`                   Fee calculation behavior

`ParkingService`                    Orchestrates park/unpark
-----------------------------------------------------------------------

## 6. Core Flow

### Entry

``` text
Vehicle
   ↓
ParkingService.park()
   ↓
ParkingStrategy.findAndClaimSpot()
   ↓
ParkingSpot.tryPark()
   ↓
Generate ParkingTicket
```

### Exit

``` text
ParkingTicket
   ↓
ParkingService.unpark()
   ↓
Close ticket
   ↓
PricingStrategy.calculateFee()
   ↓
Release ParkingSpot
```

## 7. Design Decisions

### Strategy Pattern

Allocation and pricing can vary independently:

``` java
public interface ParkingStrategy {
    ParkingSpot findAndClaimSpot(Vehicle vehicle, ParkingLot parkingLot);
}
```

Future allocation strategies can include nearest available spot or least
occupied floor. Pricing can later support vehicle-based, weekend, or
dynamic rates.

### Encapsulation

The spot owns its state transition:

``` java
spot.tryPark(vehicle);
spot.release();
```

instead of exposing setters for availability and vehicle state.

### No Unnecessary Singleton

`ParkingLot` is not a Singleton because the requirements do not say only
one parking lot can exist. Normal construction is also easier to test.

## 8. Concurrency / Thread Safety

A naïve check-then-act flow can race:

``` text
Thread A: isAvailable() -> true
Thread B: isAvailable() -> true
Thread A: assign
Thread B: assign
```

`ParkingSpot.tryPark()` makes checking and claiming atomic inside one
JVM:

``` java
public synchronized boolean tryPark(Vehicle vehicle) {
    if (this.vehicle != null || !canFit(vehicle)) {
        return false;
    }
    this.vehicle = vehicle;
    return true;
}
```

For multiple application servers, JVM synchronization is insufficient.
Concurrency control should move to a shared authoritative store using an
atomic update, optimistic/pessimistic locking, or another justified
distributed mechanism.

## 9. Project Structure

``` text
ParkingLot/
├── README.md
├── pom.xml
└── src/
    ├── main/java/com/lld/parkinglot/
    │   ├── Main.java
    │   ├── model/
    │   ├── strategy/
    │   ├── service/
    │   └── exception/
    └── test/java/com/lld/parkinglot/
        └── ParkingServiceTest.java
```

## 10. Edge Cases

-   Parking lot is full.
-   No compatible spot exists.
-   Two threads try to claim the final spot.
-   Invalid or already closed ticket is submitted.
-   Vehicle attempts an incompatible spot.
-   Minimum one-hour fee.
-   Spot becomes available after exit.

## 11. Extensibility

Potential extensions include:

-   New vehicle/spot types such as EV or accessible parking.
-   `EntranceGate` and `ExitGate`.
-   Observer/event-based display boards.
-   `PaymentService` with UPI/card/cash strategies.
-   Repository/database persistence.
-   Atomic DB claims for multi-server deployments.
-   More advanced allocation and pricing strategies.

Example distributed spot claim:

``` sql
UPDATE parking_spot
SET occupied = true
WHERE id = ?
  AND occupied = false;
```

Only the request that successfully updates one row owns the spot.

## 12. SOLID

-   **SRP:** allocation, pricing, spot state, and orchestration are
    separate.
-   **OCP:** new strategies can be added without changing
    `ParkingService`.
-   **LSP:** concrete vehicles are usable through `Vehicle`.
-   **ISP:** strategy contracts are focused.
-   **DIP:** service code depends on strategy abstractions.

## 13. Patterns

Implemented:

-   **Strategy** --- allocation and pricing.

Possible future patterns:

-   **Observer** --- display board.
-   **Repository** --- persistence.
-   **State** --- richer ticket/payment lifecycle.
-   **Factory** --- when object creation becomes complex.

Patterns are introduced only when they solve a real problem.

## 14. 40--45 Minute Interview Plan

Time         Focus
  ------------ ------------------------------
0--5 min     Requirements and assumptions
5--10 min    Entities and relationships
10--15 min   Classes/interfaces
15--30 min   Core Java implementation
30--35 min   End-to-end walkthrough
35--40 min   Concurrency and edge cases
40--45 min   Extensibility and trade-offs

## 15. How to Run

Requires Java 17+ and Maven.

``` bash
mvn clean test
mvn exec:java -Dexec.mainClass=com.lld.parkinglot.Main
```

## 16. Interview Checklist

-   [x] Requirements and assumptions
-   [x] Entities and relationships
-   [x] Core class design
-   [x] Park/unpark implementation
-   [x] Strategy pattern
-   [x] Thread-safe spot claim
-   [x] Pricing
-   [x] Edge cases
-   [x] Tests
-   [x] Extensibility
-   [x] Multi-server concurrency discussion

> **Get the simplest correct design working first; then demonstrate how
> it can evolve without rewriting the core.**
