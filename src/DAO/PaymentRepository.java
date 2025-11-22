package DAO;

import DBConnection.ConnectionMangement;
import Data.Enums.PaymentMode;
import Data.Enums.PaymentStatus;
import Data.Insertion.Payment;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
| #   | Method                                                        | Description                                             |
| --- | ------------------------------------------------------------- | ------------------------------------------------------- |
| 1️⃣ | `createPayment(Payment payment)`                              | Inserts a new payment record into the database.         |
| 2️⃣ | `getPaymentById(int paymentId)`                               | Fetches a single payment by its ID.                     |
| 3️⃣ | `getPaymentsByBookingId(int bookingId)`                       | Fetches all payments made for a particular booking.     |
| 4️⃣ | `updatePaymentStatus(int paymentId, PaymentStatus newStatus)` | Updates the status (Pending → Completed → Failed).      |
| 5️⃣ | `getCompletedPayments()`                                      | Returns a list of all successfully completed payments.  |
| 6️⃣ | `getTotalRevenue()`                                           | Returns the total revenue from completed payments.      |
| 7️⃣ | `getPaymentsByMode(PaymentMode mode)`                         | Filters payments by mode (Cash, Card, UPI, etc.).       |
| 8️⃣ | `deletePayment(int paymentId)`                                | Deletes a payment record from the database.             |
| 9️⃣ | `extractPayment(ResultSet rs)`                                | Internal helper to map DB rows into a `Payment` object. |

* */
public class PaymentRepository {
    private Connection connection;

    public PaymentRepository() {
        try {
            this.connection = new ConnectionMangement().formConnection();
        } catch (SQLException e) {

        }
    }

    // ===========================================================
    // 1️⃣  Create a new payment
    // ===========================================================
    public boolean createPayment(Payment payment) {
        String query = "INSERT INTO payment (booking_id, payment_mode, amount, payment_status, payment_date) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getBooking_id());
            ps.setString(2, payment.getPayment_mode().getValue());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getPayment_status().getValue());
            ps.setTimestamp(5, Timestamp.valueOf(payment.getPayment_date()));

            int rows = ps.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        payment.setPayment_id(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    // ===========================================================
    // 2️⃣  Get payment details by payment_id
    // ===========================================================
    public Payment getPaymentById(int paymentId) {
        String query = "SELECT * FROM payment WHERE payment_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return extractPayment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ===========================================================
    // 3️⃣  Get all payments for a specific booking
    // ===========================================================
    public List<Payment> getPaymentsByBookingId(int bookingId) throws SQLException {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payment WHERE booking_id = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(extractPayment(rs));
            }
        }
        return payments;
    }

    // ===========================================================
    // 4️⃣  Update payment status
    // ===========================================================
    public boolean updatePaymentStatus(int paymentId, PaymentStatus newStatus){
        String query = "UPDATE payment SET payment_status = ? WHERE payment_id = ?;";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, newStatus.getValue());
            ps.setInt(2, paymentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ===========================================================
    // 5️⃣  Get all completed payments
    // ===========================================================
    public List<Payment> getCompletedPayments() {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payment WHERE payment_status = 'completed';";

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                payments.add(extractPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // ===========================================================
    // 6️⃣  Get total revenue from completed payments
    // ===========================================================
    public double getTotalRevenue()  {
        String query = "SELECT SUM(amount) AS total_revenue FROM payment WHERE payment_status = 'completed';";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                return rs.getDouble("total_revenue");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ===========================================================
    // 7️⃣  Get payments filtered by mode (cash/card/upi)
    // ===========================================================
    public List<Payment> getPaymentsByMode(PaymentMode mode)  {
        List<Payment> payments = new ArrayList<>();
        String query = "SELECT * FROM payment WHERE payment_mode = ?;";

        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, mode.getValue());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                payments.add(extractPayment(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // ===========================================================
    // 8️⃣  Delete payment record
    // ===========================================================
    public boolean deletePayment(int paymentId) throws SQLException {
        String query = "DELETE FROM payment WHERE payment_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, paymentId);
            return ps.executeUpdate() > 0;
        }
    }

    // ===========================================================
    // 9️⃣  Helper function → Convert ResultSet → Payment object
    // ===========================================================
    private Payment extractPayment(ResultSet rs) throws SQLException {
        int payment_id = rs.getInt("payment_id");
        int booking_id = rs.getInt("booking_id");
        PaymentMode mode = PaymentMode.setValue(rs.getString("payment_mode"));
        double amount = rs.getDouble("amount");
        PaymentStatus status = PaymentStatus.setValue(rs.getString("payment_status"));
        Timestamp timestamp = rs.getTimestamp("payment_date");

        Payment payment = new Payment(booking_id, mode, amount, status,
                timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now());
        payment.setPayment_id(payment_id);

        return payment;
    }
    public List<Payment> getAllPayments(){
        try {
            List<Payment> payments = new ArrayList<>();
            String query = "SELECT * FROM payment ;";
            PreparedStatement statement=connection.prepareStatement(query);
            ResultSet set=statement.executeQuery();
            while (set.next()){
                int payment_id=set.getInt("payment_id");
                int booking_id=set.getInt("booking_id");
                PaymentMode payment_mode= PaymentMode.setValue(set.getString("payment_mode"));
                double amount=set.getInt("amount");
                PaymentStatus payment_status=PaymentStatus.setValue(set.getString("payment_status"));
                LocalDateTime payment_date=set.getTimestamp("payment_date").toLocalDateTime();
                Payment p=new Payment(payment_id,booking_id,payment_mode,amount,payment_status,payment_date);
                payments.add(p);
            }
            return payments;
        }catch (SQLException e){

        }
        return null;
    }
}
