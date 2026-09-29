# Splitwise --- Low-Level Design

A Java-focused Low-Level Design for a simplified Splitwise
expense-sharing system.

This module follows the repository's standard interview approach:

> **Requirements → Entities → Class Design → Implementation →
> Extensibility**

The scope is intentionally designed so the core solution can be
explained and implemented in approximately **40--45 minutes**.

------------------------------------------------------------------------

## 1. Problem Statement

Design an expense-sharing system where users can create groups, add
shared expenses, divide expenses among participants, track balances, and
settle amounts owed.

Example:

``` text
Ravi pays ₹3000 for dinner.

Participants:
Ravi
Amit
John

Equal split = ₹1000 each

Result:
Amit owes Ravi ₹1000
John owes Ravi ₹1000
```

------------------------------------------------------------------------

## 2. Functional Requirements

1.  Create users.
2.  Create groups.
3.  Add users to a group.
4.  Add an expense.
5.  One user can pay on behalf of multiple users.
6.  Split an expense among participants.
7.  Support:
    -   Equal split
    -   Exact split
    -   Percentage split
8.  Maintain balances between users.
9.  Show how much one user owes another.
10. Allow users to settle an outstanding balance.

### V1 Scope

Implement first:

-   Users
-   Groups
-   Expenses
-   Equal split
-   Balance tracking
-   Balance lookup
-   Basic settlement

Exact and Percentage splitting are extensions if interview time permits.

------------------------------------------------------------------------

## 3. Out of Scope for V1

-   Authentication
-   Multiple currencies/conversion
-   Payment gateway integration
-   Notifications
-   Recurring expenses
-   Receipt uploads
-   Database persistence
-   Distributed deployment
-   Advanced debt simplification

------------------------------------------------------------------------

## 4. Assumptions

-   Users and groups have unique IDs.
-   One user is the payer for an expense in V1.
-   Participants may include the payer.
-   Payer does not owe themselves.
-   Equal expenses are divided equally.
-   Exact splits must total the expense amount.
-   Percentage splits must total 100%.
-   Balances should represent net debt.
-   Use `BigDecimal` for production money calculations.
-   V1 is in-memory.

------------------------------------------------------------------------

## 5. Entities

``` text
User
Group
Expense
Split
BalanceSheet
```

Variable behavior:

``` text
SplitStrategy
      |
      ├── EqualSplitStrategy
      ├── ExactSplitStrategy
      └── PercentageSplitStrategy
```

Supporting enum:

``` text
SplitType
```

------------------------------------------------------------------------

## 6. Class Responsibilities

  -----------------------------------------------------------------------
Class                               Responsibility
  ----------------------------------- -----------------------------------
`User`                              Represents a participant

`Group`                             Groups users and expenses

`Expense`                           Represents a shared expense

`Split`                             Represents one user's share

`BalanceSheet`                      Tracks who owes whom

`SplitStrategy`                     Calculates individual shares

`SplitwiseService`                  Orchestrates expense creation and
balance updates

`SettlementService`                 Handles repayment
-----------------------------------------------------------------------

------------------------------------------------------------------------

## 7. Class Design

``` text
                           Group
                             |
                    +--------+--------+
                    |                 |
                   User             Expense
                                      |
                         +------------+------------+
                         |                         |
                      paidBy                     splits
                         |                         |
                        User                    Split
                                                  |
                                                 User


                         SplitStrategy
                       /       |        \
                    Equal     Exact    Percentage


                       SplitwiseService
                              |
                              v
                         BalanceSheet
```

------------------------------------------------------------------------

## 8. Core Expense Flow

``` text
Add Expense
     |
     v
SplitwiseService.addExpense()
     |
     v
Validate request
     |
     v
SplitStrategy.calculate()
     |
     v
Create Split objects
     |
     v
Create Expense
     |
     v
Update BalanceSheet
```

Example:

``` text
Ravi pays ₹3000
        |
        v
EqualSplitStrategy
        |
        v
Ravi = ₹1000
Amit = ₹1000
John = ₹1000
        |
        v
Amit -> Ravi = ₹1000
John -> Ravi = ₹1000
```

------------------------------------------------------------------------

## 9. Split Strategy

``` java
public interface SplitStrategy {

    List<Split> calculate(
            BigDecimal amount,
            List<User> participants);
}
```

### Equal

``` text
₹3000 / 3 = ₹1000 each
```

### Exact

``` text
Ravi = ₹500
Amit = ₹1000
John = ₹1500

500 + 1000 + 1500 = 3000
```

### Percentage

``` text
Ravi = 20%
Amit = 30%
John = 50%

20 + 30 + 50 = 100%
```

The Strategy pattern keeps splitting logic outside the main service and
makes new algorithms easy to add.

------------------------------------------------------------------------

## 10. Balance Representation

A simple representation:

``` java
Map<String, Map<String, BigDecimal>> balances;
```

Example:

``` text
Amit
  └── Ravi -> ₹1000

John
  └── Ravi -> ₹1000
```

Meaning:

``` text
Amit owes Ravi ₹1000
John owes Ravi ₹1000
```

------------------------------------------------------------------------

## 11. Reverse Balance Netting

Suppose:

``` text
Amit owes Ravi ₹1000
```

Later:

``` text
Ravi owes Amit ₹600
```

Avoid storing both directions:

``` text
Amit -> Ravi = ₹1000
Ravi -> Amit = ₹600
```

Prefer the net balance:

``` text
Amit -> Ravi = ₹400
```

------------------------------------------------------------------------

## 12. Settlement

Suppose:

``` text
Amit owes Ravi ₹1000
```

Amit pays Ravi ₹400:

``` text
Amit owes Ravi ₹600
```

Flow:

``` text
settle(Amit, Ravi, ₹400)
        |
        v
Validate debt
        |
        v
Reduce balance
        |
        v
Record settlement
```

If the full amount is settled, remove or zero the balance.

------------------------------------------------------------------------

## 13. Validation

Handle:

-   Amount \<= 0
-   Empty participants
-   Duplicate participants
-   Invalid payer
-   User not belonging to group
-   Exact amounts not totaling expense
-   Percentages not totaling 100%
-   Settlement greater than outstanding debt
-   Duplicate expense submission

------------------------------------------------------------------------

## 14. Money

Production code should use:

``` java
BigDecimal amount =
        new BigDecimal("3000.00");
```

rather than `double`.

Also define currency, scale, and rounding rules in a production system.

------------------------------------------------------------------------

## 15. Concurrency

Two threads may update the same balance simultaneously:

``` text
Thread A ----              ---> Amit -> Ravi balance
Thread B ----/
```

The balance update must be atomic.

Single-JVM options include:

-   `synchronized`
-   `ConcurrentHashMap.compute()`
-   Locks

For multiple servers, JVM locks are insufficient. Use database
transactions, atomic updates, optimistic/pessimistic locking, and
idempotency where appropriate.

------------------------------------------------------------------------

## 16. Idempotency

Retries must not create the same expense twice.

``` text
idempotencyKey = EXPENSE-123

First request -> create expense
Retry         -> return existing expense
```

A production system should enforce uniqueness of the idempotency key.

------------------------------------------------------------------------

## 17. SOLID

-   **SRP:** expense data, splitting, balances, and orchestration are
    separated.
-   **OCP:** new split strategies can be added without changing core
    service logic.
-   **LSP:** strategy implementations satisfy the same split contract.
-   **ISP:** strategy interfaces remain focused.
-   **DIP:** service code depends on abstractions such as
    `SplitStrategy`.

------------------------------------------------------------------------

## 18. Design Patterns

### Implement

**Strategy Pattern**

``` text
SplitStrategy
      ├── EqualSplitStrategy
      ├── ExactSplitStrategy
      └── PercentageSplitStrategy
```

### Discuss as Extensions

-   Repository --- persistence
-   Factory --- selecting strategy by `SplitType`
-   Observer/events --- notifications

Do not introduce patterns unless they solve a real problem.

------------------------------------------------------------------------

## 19. Edge Cases

Consider:

-   Expense involving only payer
-   Rounding differences
-   Negative/zero expense
-   Empty participant list
-   Invalid exact/percentage splits
-   Settlement exceeding debt
-   Duplicate expenses
-   Concurrent balance updates

------------------------------------------------------------------------

## 20. Extensibility

Future extensions:

``` text
Multiple currencies
      -> Money / Currency

Notifications
      -> NotificationService

Payments
      -> SettlementService / PaymentService

Persistence
      -> Repositories

Recurring expenses
      -> RecurringExpenseService

Debt simplification
      -> DebtSimplificationStrategy
```

Example debt simplification:

``` text
A owes B ₹100
B owes C ₹100
```

could potentially become:

``` text
A owes C ₹100
```

Keep this outside the initial implementation.

------------------------------------------------------------------------

## 21. Suggested Project Structure

``` text
Splitwise/
├── README.md
├── pom.xml
└── src/
    ├── main/java/com/lld/splitwise/
    │   ├── Main.java
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Group.java
    │   │   ├── Expense.java
    │   │   ├── Split.java
    │   │   └── SplitType.java
    │   ├── strategy/
    │   │   ├── SplitStrategy.java
    │   │   ├── EqualSplitStrategy.java
    │   │   ├── ExactSplitStrategy.java
    │   │   └── PercentageSplitStrategy.java
    │   ├── service/
    │   │   ├── SplitwiseService.java
    │   │   ├── BalanceSheet.java
    │   │   └── SettlementService.java
    │   └── exception/
    │       ├── InvalidExpenseException.java
    │       ├── InvalidSplitException.java
    │       └── InvalidSettlementException.java
    └── test/java/com/lld/splitwise/
        └── SplitwiseServiceTest.java
```

------------------------------------------------------------------------

## 22. Classes to Prioritize in the Interview

Implement these first:

``` text
User
Split
Expense
SplitStrategy
EqualSplitStrategy
BalanceSheet
SplitwiseService
```

Then, if time permits:

``` text
SettlementService
ExactSplitStrategy
PercentageSplitStrategy
Group
```

The core flow must work before adding optional features.

------------------------------------------------------------------------

## 23. 40--45 Minute Interview Plan

Time         Focus
  ------------ -----------------------------------------
0--5 min     Requirements and assumptions
5--10 min    Entities and relationships
10--15 min   Class/interface design
15--30 min   Expense + balance implementation
30--35 min   End-to-end example
35--40 min   Settlement, validation, edge cases
40--45 min   Concurrency, persistence, extensibility

A useful opening statement:

> "I'll model users, expenses, splits and balances first. I'll implement
> Equal Split and the end-to-end balance update while keeping split
> calculation behind a strategy. Once that works, I'll cover settlement,
> Exact/Percentage splits, concurrency and persistence."

------------------------------------------------------------------------

## 24. Interview Checklist

-   [x] Requirements
-   [x] Assumptions
-   [x] Entities
-   [x] Class design
-   [x] Core expense flow
-   [x] Strategy pattern
-   [x] Balance tracking
-   [x] Reverse-balance netting
-   [x] Settlement
-   [x] Validation
-   [x] Money precision
-   [x] Concurrency
-   [x] Idempotency
-   [x] Edge cases
-   [x] Extensibility
-   [x] 40--45 minute plan

------------------------------------------------------------------------

## Design Principle

> **Get expense creation, splitting, and balance tracking working first.
> Then demonstrate how the design evolves to settlement, additional
> strategies, persistence, concurrency, and debt simplification without
> rewriting the core.**
