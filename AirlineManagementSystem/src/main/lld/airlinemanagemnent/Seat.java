package AirlineManagementSystem.src.main.lld.airlinemanagemnent;

public class Seat {
    private String seatNumber;
    private SeatType seatType;
    private SeatStatus seatStatus;

    public Seat(String seatNumber, SeatType seatType) {
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.seatStatus = SeatStatus.AVAILABLE;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void reserve(){
        seatStatus = SeatStatus.RESERVED;
    }

    public void release(){
        seatStatus = SeatStatus.AVAILABLE;
    }

    public synchronized boolean isBooked(){
        return seatStatus == SeatStatus.BOOKED;
    }
}
