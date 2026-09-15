# Low-Level Design (LLD) Practice Repository

A Java-focused repository for practicing **Low-Level Design (LLD)**
problems commonly asked in Senior/Staff Software Engineer interviews.

The goal is to solve each design problem using a consistent
interview-friendly approach:

> **Requirements → Entities → Class Design → Implementation →
> Extensibility**

Each LLD problem is maintained as an independent module/project so that
it can be understood, executed, and extended separately.

------------------------------------------------------------------------

## Repository Structure

``` text
LowLevelDesign/
│
├── README.md
│
├── ParkingLot/
│   ├── README.md
│   ├── pom.xml
│   └── src/
│       ├── main/java/...
│       └── test/java/...
│
├── ATM/
│   ├── README.md
│   ├── pom.xml
│   └── src/
│       ├── main/java/...
│       └── test/java/...
│
├── HotelBooking/
├── LibraryManagement/
├── NotificationSystem/
├── TicTacToe/
├── RateLimiter/
└── ...
```

Each directory represents one independent LLD exercise.

------------------------------------------------------------------------

## Standard Approach for Every LLD

### 1. Requirements

Start by identifying the core functional requirements and explicitly
stating assumptions.

Keep the first version small enough to implement during a **40--45
minute interview**. Separate must-have requirements from optional
extensions.

Example questions:

-   What are the primary use cases?
-   Who are the actors?
-   What operations must the system support?
-   What is intentionally out of scope?
-   Are there concurrency requirements?

### 2. Entities

Extract the important domain objects from the requirements.

For example, a Parking Lot may contain:

``` text
ParkingLot
ParkingFloor
ParkingSpot
Vehicle
ParkingTicket
```

Also identify:

-   Enums
-   Value objects
-   Entity relationships
-   State that belongs to each entity

### 3. Class Design

Define responsibilities before writing implementation code.

Focus on:

-   Encapsulation
-   Composition over inheritance where appropriate
-   Clear ownership of state
-   Interfaces for behavior that can vary
-   SOLID principles
-   Avoiding unnecessary design patterns

Example:

``` text
ParkingLot
    |
    +-- ParkingFloor
            |
            +-- ParkingSpot

Vehicle
    |
    +-- Car
    +-- Bike
    +-- Truck

ParkingStrategy
    |
    +-- FirstAvailableStrategy

PricingStrategy
    |
    +-- HourlyPricingStrategy
```

### 4. Implementation

Implement the main end-to-end use case first.

For example:

``` text
Vehicle enters
      ↓
Find available spot
      ↓
Claim spot
      ↓
Generate ticket
      ↓
Vehicle exits
      ↓
Calculate fee
      ↓
Release spot
```

Prefer working core logic over creating many incomplete classes.

After the basic flow works, add:

-   Validation
-   Exceptions
-   Thread safety
-   Unit tests
-   Edge cases

### 5. Extensibility

Finish each design by discussing how it would evolve.

Consider:

-   New entity types
-   New strategies
-   Concurrency
-   Persistence
-   Multiple application instances
-   APIs
-   Caching
-   Events
-   Observability
-   Failure handling

The objective is not to implement every extension, but to show that the
core design can accommodate change.

------------------------------------------------------------------------

## Suggested Modules

  Module                Important Concepts
  --------------------- ---------------------------------------------
  Parking Lot           Strategy, concurrency, allocation, pricing
  ATM                   State pattern, transactions, cash inventory
  Library Management    Entity relationships, search, lending
  Hotel Booking         Reservations, inventory, concurrency
  Tic-Tac-Toe           State, strategy, clean domain modeling
  Notification System   Strategy, factory, async processing
  Rate Limiter          Token bucket/sliding window, concurrency
  Elevator System       State, scheduling strategy
  Vending Machine       State pattern, inventory, payments
  Splitwise             Expense modeling, balances, strategy
  LRU Cache             HashMap, doubly linked list, concurrency
  Logging Framework     Chain of Responsibility, extensibility

------------------------------------------------------------------------

## Recommended Structure Inside Each Module

Each module should contain its own `README.md`.

Example:

``` text
ParkingLot/
│
├── README.md
├── pom.xml
└── src/
    ├── main/java/
    │   └── com/lld/parkinglot/
    │       ├── model/
    │       ├── service/
    │       ├── strategy/
    │       ├── exception/
    │       └── Main.java
    │
    └── test/java/
```

A module README should follow:

``` text
1. Problem Statement
2. Requirements
3. Assumptions
4. Entities
5. Class Diagram
6. Design Decisions
7. Implementation Flow
8. Design Patterns Used
9. Concurrency / Thread Safety
10. Edge Cases
11. Extensibility
12. How to Run
```

------------------------------------------------------------------------

## Design Principles

### SOLID

Use SOLID where it naturally improves the design:

-   **S --- Single Responsibility Principle**
-   **O --- Open/Closed Principle**
-   **L --- Liskov Substitution Principle**
-   **I --- Interface Segregation Principle**
-   **D --- Dependency Inversion Principle**

Do not introduce abstractions only to demonstrate SOLID.

### Prefer Composition

Prefer:

``` java
class ParkingLot {
    private ParkingStrategy parkingStrategy;
}
```

over creating deep inheritance hierarchies.

### Program to Interfaces

For behavior that can change:

``` java
public interface ParkingStrategy {
    ParkingSpot findSpot(Vehicle vehicle);
}
```

Possible implementations:

``` text
FirstAvailableStrategy
NearestSpotStrategy
LeastOccupiedFloorStrategy
```

### Keep State Changes Encapsulated

Prefer:

``` java
spot.park(vehicle);
spot.release();
```

instead of exposing state:

``` java
spot.setAvailable(false);
spot.setVehicle(vehicle);
```

The object owning the state should protect its own invariants.

------------------------------------------------------------------------

## Concurrency

Concurrency should be considered explicitly for designs where multiple
operations may happen simultaneously.

Examples:

``` text
Parking Lot:
Two entrance gates try to allocate the last available spot.

Hotel Booking:
Two customers try to reserve the last room.

ATM:
Two operations update the same account.

Rate Limiter:
Multiple threads update the same counter.
```

A design should distinguish between:

``` text
Single JVM concurrency
    → synchronized
    → ReentrantLock
    → Atomic classes
    → ConcurrentHashMap

Distributed concurrency
    → Database atomic operations
    → Optimistic/Pessimistic locking
    → Distributed locking where justified
    → Idempotency
```

------------------------------------------------------------------------

## Design Patterns

Patterns should solve a problem rather than become the starting point of
the design.

Common patterns used across these exercises:

``` text
Strategy
Factory
Observer
State
Builder
Chain of Responsibility
Repository
Adapter
```

For every pattern, be prepared to explain:

1.  What problem does it solve?
2.  Why is it appropriate here?
3.  What would happen without it?
4.  What trade-off does it introduce?

------------------------------------------------------------------------

## 40--45 Minute Interview Strategy

A useful time split:

  Time         Focus
  ------------ ------------------------------
  0--5 min     Requirements and assumptions
  5--10 min    Entities and relationships
  10--15 min   Class/interface design
  15--30 min   Core Java implementation
  30--35 min   End-to-end walkthrough
  35--40 min   Concurrency and edge cases
  40--45 min   Extensibility and trade-offs

The priority is:

``` text
Correctness
    ↓
Clean responsibilities
    ↓
Working core flow
    ↓
Extensibility
    ↓
Patterns / optimizations
```

Avoid spending most of the interview creating classes without completing
the main use case.

------------------------------------------------------------------------

## Java Guidelines

The examples in this repository primarily use modern Java.

Preferred practices:

-   Constructor injection
-   `private final` fields where possible
-   Immutable value objects where practical
-   Interfaces for variable behavior
-   Meaningful domain methods
-   Custom exceptions for domain failures
-   `java.time` APIs for date/time
-   Collections interfaces (`List`, `Map`, `Set`)
-   Thread-safe primitives only where concurrency requires them

------------------------------------------------------------------------

## Testing

Each module should test its important business flows.

For Parking Lot, examples include:

``` text
✓ Park a car successfully
✓ Correct spot type is selected
✓ Reject parking when full
✓ Generate ticket
✓ Calculate fee
✓ Release spot after exit
✓ Spot becomes available again
✓ Concurrent requests cannot claim the same spot
```

Tests should focus on behavior rather than implementation details.

------------------------------------------------------------------------

## Interview Checklist

Before considering an LLD complete, verify:

-   [ ] Requirements are clearly defined
-   [ ] Assumptions are documented
-   [ ] Core entities are identified
-   [ ] Responsibilities are separated
-   [ ] Relationships are clear
-   [ ] Main use case works end-to-end
-   [ ] Important edge cases are handled
-   [ ] Concurrency has been considered
-   [ ] Design patterns have a clear purpose
-   [ ] Design follows SOLID pragmatically
-   [ ] Extensibility is explained
-   [ ] Important flows have tests
-   [ ] Trade-offs can be explained

------------------------------------------------------------------------

## Philosophy

The objective of this repository is not to produce production-sized
implementations for every interview problem.

The objective is to demonstrate:

> **Simple design over unnecessary complexity.**\
> **Clear responsibilities over god classes.**\
> **Composition over deep inheritance.**\
> **Extensibility over hard-coded conditionals.**\
> **Correctness over pattern worship.**

A good LLD should be easy to explain, easy to test, and easy to change.
