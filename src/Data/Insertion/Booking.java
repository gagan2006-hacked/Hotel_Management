package Data.Insertion;

import Data.Enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Date;

/*
| Field            | Type                                                     | Description     |
| ---------------- | -------------------------------------------------------- | --------------- |
| booking_id (PK)  | INT                                                      | Booking ID      |
| customer_id (FK) | INT                                                      | Linked customer |
| room_id (FK)     | INT                                                      | Linked room     |
| check_in_date    | DATE                                                     | Check-in date   |
| check_out_date   | DATE                                                     | Check-out date  |
| booking_status   | ENUM('confirmed','checked_in','checked_out','cancelled') | Current status  |
| total_amount     | DECIMAL(10,2)                                            | Amount billed   |
| created_at       | DATETIME                                                 | Created time    |
* */
public class Booking {
    private int  booking_id;
    private int customer_id;
    private int room_id;
    private Date check_in_date;
    private Date check_out_date;
    private BookingStatus booking_status;
    private double total_amount;
    private LocalDateTime created_at;

    public Booking(int customer_id, int room_id, Date check_in_date, Date check_out_date, BookingStatus booking_status, double total_amount, LocalDateTime created_at) {
        this.customer_id = customer_id;
        this.room_id = room_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.booking_status = booking_status;
        this.total_amount = total_amount;
        this.created_at = created_at;
    }

}
