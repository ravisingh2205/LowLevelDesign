package AirlineManagementSystem.src.main.lld.airlinemanagemnent.payment;

public class PaymentProcessor {
    private static PaymentProcessor instance;

    private PaymentProcessor(){

    }

    public static PaymentProcessor getGetInstance() {
        if(instance == null){
            instance = new PaymentProcessor();
        }

        return instance;
    }

    public void processPayment(Payment payment){
        payment.processPayment();
    }
}
