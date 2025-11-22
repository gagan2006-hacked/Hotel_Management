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

    public Booking( int customer_id, int room_id, Date check_in_date, Date check_out_date, BookingStatus booking_status, double total_amount) {
        this.customer_id = customer_id;
        this.room_id = room_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.booking_status = booking_status;
        this.total_amount = total_amount;
    }

    public Booking(int customer_id, int room_id, Date check_in_date, Date check_out_date, BookingStatus booking_status, double total_amount, LocalDateTime created_at) {
        this.customer_id = customer_id;
        this.room_id = room_id;
        this.check_in_date = check_in_date;
        this.check_out_date = check_out_date;
        this.booking_status = booking_status;
        this.total_amount = total_amount;
        this.created_at = created_at;
    }

    public int getBooking_id() {
        return booking_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public Date getCheck_in_date() {
        return check_in_date;
    }

    public Date getCheck_out_date() {
        return check_out_date;
    }

    public BookingStatus getBooking_status() {
        return booking_status;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setBooking_id(int booking_id) {
        this.booking_id = booking_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public void setCheck_in_date(Date check_in_date) {
        this.check_in_date = check_in_date;
    }

    public void setCheck_out_date(Date check_out_date) {
        this.check_out_date = check_out_date;
    }

    public void setBooking_status(BookingStatus booking_status) {
        this.booking_status = booking_status;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
    @Override
    public String toString() {
        return "Booking Details:\n" +
                "---------------------------\n" +
                "Booking ID      : " + booking_id + "\n" +
                "Customer ID     : " + customer_id + "\n" +
                "Room ID         : " + room_id + "\n" +
                "Check-In Date   : " + check_in_date + "\n" +
                "Check-Out Date  : " + check_out_date + "\n" +
                "Status          : " + booking_status + "\n" +
                "Total Amount    : " + total_amount + "\n" +
                "Created At      : " + (created_at != null ? created_at.toString() : "N/A") + "\n";
    }

}
