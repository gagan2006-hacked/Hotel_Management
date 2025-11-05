package Data.Insertion;

import Data.Enums.PaymentMode;
import Data.Enums.PaymentStatus;

import java.time.LocalDateTime;

/*
| Field           | Type                                   | Description     |
| --------------- | -------------------------------------- | --------------- |
| payment_id (PK) | INT                                    | Payment ID      |
| booking_id (FK) | INT                                    | Linked booking  |
| payment_mode    | ENUM('cash','card','upi','netbanking') | Payment type    |
| amount          | DECIMAL(10,2)                          | Amount paid     |
| payment_status  | ENUM('pending','completed','failed')   | Payment result  |
| payment_date    | DATETIME                               | Date of payment |
* */
public class Payment {
    private int payment_id;
    private int booking_id;
    private PaymentMode payment_mode;
    private double amount;
    private PaymentStatus payment_status;
    private LocalDateTime payment_date;

    public Payment(){

    }
    public Payment(int payment_id, int booking_id, PaymentMode payment_mode, double amount, PaymentStatus payment_status, LocalDateTime payment_date) {
        this.payment_id = payment_id;
        this.booking_id = booking_id;
        this.payment_mode = payment_mode;
        this.amount = amount;
        this.payment_status = payment_status;
        this.payment_date = payment_date;
    }

    public Payment(int booking_id, PaymentMode payment_mode, double amount, PaymentStatus payment_status, LocalDateTime payment_date) {
        this.booking_id = booking_id;
        this.payment_mode = payment_mode;
        this.amount = amount;
        this.payment_status = payment_status;
        this.payment_date = payment_date;
    }

    public void setPayment_id(int payment_id) {
        this.payment_id = payment_id;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public void setPayment_mode(PaymentMode payment_mode) {
        this.payment_mode = payment_mode;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setPayment_status(PaymentStatus payment_status) {
        this.payment_status = payment_status;
    }

    public void setPayment_date(LocalDateTime payment_date) {
        this.payment_date = payment_date;
    }

    public int getPayment_id() {
        return payment_id;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public PaymentMode getPayment_mode() {
        return payment_mode;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getPayment_status() {
        return payment_status;
    }

    public LocalDateTime getPayment_date() {
        return payment_date;
    }
}
