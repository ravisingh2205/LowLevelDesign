package AirlineManagementSystem.src.main.lld.airlinemanagemnent.payment;

public class Payment {
    private final String paymentId;
    private final String paymentType;
    private PaymentStatus paymentStatus;
    private final double amount;

    public Payment(String paymentId, String paymentType, double amount) {
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.paymentStatus = PaymentStatus.PENDING;
        this.amount = amount;
    }

    public void processPayment(){
        paymentStatus = PaymentStatus.COMPLETED;
    }
}
