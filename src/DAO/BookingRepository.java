package DAO;

import DBConnection.ConnectionMangement;
import Data.Enums.BookingStatus;
import Data.Insertion.Booking;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
| Method                                                        | Description                                       |
| ------------------------------------------------------------- | ------------------------------------------------- |
| `addBooking(Booking booking)`                                 | Add a new booking record                          |
| `getBookingById(int bookingId)`                               | Fetch booking details by ID                       |
| `updateBookingStatus(int bookingId, BookingStatus newStatus)` | Change booking status                             |
| `cancelBooking(int bookingId)`                                | Mark booking as cancelled                         |
| `isRoomAvailable(int roomId, Date checkIn, Date checkOut)`    | Check room availability in a date range           |
| `getAllBookings()`                                            | Retrieve all bookings (admin overview)            |
| `getBookingsByCustomer(int customerId)`                       | Get all bookings for a specific customer          |
| `deleteBooking(int bookingId)`                                | Permanently delete a booking                      |
| `calculateTotalRevenue()`                                     | Compute total confirmed/completed booking revenue |
| `getTodayBookings()`                                          | List of all bookings made today                   |

* */
public class BookingRepository {
    private final ConnectionMangement management;

    public BookingRepository() {
        this.management = new ConnectionMangement();
    }

    // ==========================================================
    // 1️⃣ Add New Booking
    // ==========================================================
    public boolean addBooking(Booking booking) {
        String sql = "INSERT INTO booking (customer_id, room_id, check_in, check_out, total_amount, booking_status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, booking.getCustomer_id());
            ps.setInt(2, booking.getRoom_id());
            ps.setDate(3, new java.sql.Date(booking.getCheck_in_date().getTime()));
            ps.setDate(4, new java.sql.Date(booking.getCheck_out_date().getTime()));
            ps.setDouble(5, booking.getTotal_amount());
            ps.setString(6, booking.getBooking_status().getValue());
            ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            int result = ps.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean updateBooking(Booking booking) {
        String sql = "UPDATE booking SET customer_id=?, room_id=?, check_in=?, check_out=?, total_amount=?, booking_status=? WHERE booking_id=?";

        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, booking.getCustomer_id());
            ps.setInt(2, booking.getRoom_id());
            ps.setDate(3, new java.sql.Date(booking.getCheck_in_date().getTime()));
            ps.setDate(4, new java.sql.Date(booking.getCheck_out_date().getTime()));
            ps.setDouble(5, booking.getTotal_amount());
            ps.setString(6, booking.getBooking_status().getValue());

            // If you have updated_at in your model
//            ps.setTimestamp(7, Timestamp.valueOf(new Date(System.currentTimeMillis()).toString()));

            ps.setInt(7, booking.getBooking_id()); // WHERE condition

            int rows = ps.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    // ==========================================================
    // 2️⃣ Get Booking By ID
    // ==========================================================
    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id = ?";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int customerId = rs.getInt("customer_id");
                int roomId = rs.getInt("room_id");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                double totalAmount = rs.getDouble("total_amount");
                BookingStatus status = BookingStatus.setValue(rs.getString("booking_status"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Booking booking = new Booking(customerId, roomId, checkIn, checkOut, status, totalAmount, createdAt);
                booking.setBooking_id(bookingId);
                return booking;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================================
    // 3️⃣ Update Booking Status
    // ==========================================================
    public boolean updateBookingStatus(int bookingId, BookingStatus newStatus) {
        String sql = "UPDATE booking SET booking_status = ? WHERE booking_id = ?";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, newStatus.getValue());
            ps.setInt(2, bookingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 4️⃣ Cancel Booking
    // ==========================================================
    public boolean cancelBooking(int bookingId) {
        return updateBookingStatus(bookingId, BookingStatus.Cancelled);
    }

    // ==========================================================
    // 5️⃣ Check Room Availability (Between Dates)
    // ==========================================================
    public boolean isRoomAvailable(int roomId, Date checkIn, Date checkOut) {
        String sql = """
                SELECT COUNT(*) FROM booking
                WHERE room_id = ?
                AND booking_status IN ('confirmed', 'checked_in')
                AND (check_in <= ? AND check_out >= ?)
                """;
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, roomId);
            ps.setDate(2, new java.sql.Date(checkOut.getTime()));
            ps.setDate(3, new java.sql.Date(checkIn.getTime()));

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // available if count == 0
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 6️⃣ Get All Bookings (Admin View)
    // ==========================================================
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking ORDER BY created_at DESC";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int customerId = rs.getInt("customer_id");
                int roomId = rs.getInt("room_id");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                double totalAmount = rs.getDouble("total_amount");
                BookingStatus status = BookingStatus.setValue(rs.getString("booking_status"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Booking booking = new Booking(customerId, roomId, checkIn, checkOut, status, totalAmount, createdAt);
                booking.setBooking_id(bookingId);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // ==========================================================
    // 7️⃣ Get Bookings By Customer ID
    // ==========================================================
    public List<Booking> getBookingsByCustomer(int customerId) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE customer_id = ? ORDER BY created_at DESC";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int roomId = rs.getInt("room_id");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                double totalAmount = rs.getDouble("total_amount");
                BookingStatus status = BookingStatus.setValue(rs.getString("booking_status"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Booking booking = new Booking(customerId, roomId, checkIn, checkOut, status, totalAmount, createdAt);
                booking.setBooking_id(bookingId);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // ==========================================================
    // 8️⃣ Delete Booking
    // ==========================================================
    public boolean deleteBooking(int bookingId) {
        String sql = "DELETE FROM booking WHERE booking_id = ?";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==========================================================
    // 9️⃣ Calculate Total Revenue
    // ==========================================================
    public double calculateTotalRevenue() {
        String sql = "SELECT SUM(total_amount) AS total_revenue FROM booking WHERE booking_status IN ('confirmed','checked_out','completed')";
        try (Connection connection = management.formConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) return rs.getDouble("total_revenue");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    // ==========================================================
    // 🔟 Get Today's Bookings
    // ==========================================================
    public List<Booking> getTodayBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM booking WHERE DATE(created_at) = CURDATE()";
        try (Connection connection = management.formConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int bookingId = rs.getInt("booking_id");
                int customerId = rs.getInt("customer_id");
                int roomId = rs.getInt("room_id");
                Date checkIn = rs.getDate("check_in");
                Date checkOut = rs.getDate("check_out");
                double totalAmount = rs.getDouble("total_amount");
                BookingStatus status = BookingStatus.setValue(rs.getString("booking_status"));
                LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();

                Booking booking = new Booking(customerId, roomId, checkIn, checkOut, status, totalAmount, createdAt);
                booking.setBooking_id(bookingId);
                bookings.add(booking);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookings;
    }
}
