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

    public Payment(int booking_id, PaymentMode payment_mode, double amount, PaymentStatus payment_status, LocalDateTime payment_date) {
        this.booking_id = booking_id;
        this.payment_mode = payment_mode;
        this.amount = amount;
        this.payment_status = payment_status;
        this.payment_date = payment_date;
    }
}
