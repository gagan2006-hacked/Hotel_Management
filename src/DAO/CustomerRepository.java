package DAO;

import DBConnection.ConnectionMangement;
import Data.BookingHistoryDTO;
import Data.Enums.BookingStatus;
import Data.Enums.PaymentMode;
import Data.Enums.PaymentStatus;
import Data.Enums.RoomType;
import Data.Insertion.Customer;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerRepository {
    private final ConnectionMangement mangement;

    public CustomerRepository() {
        this.mangement = new ConnectionMangement();
    }

    public boolean addCustomer(Customer customer){
        try {
            Connection connection=mangement.formConnection();
//          insert Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("INSERT INTO customer (name,email,phone,address) value(?,?,?,?);");
            statement.setString(1,customer.getName());
            statement.setString(2,customer.getEmail());
            statement.setString(3,customer.getPhone());
            statement.setString(4,customer.getAddress());

            int ack=statement.executeUpdate();
            if (ack==0){
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean updateCustomerName(String name,int customer_id,String email){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE customer set name=? where customer_id=? and email=?;");
            statement.setString(1,name);
            statement.setInt(2,customer_id);
            statement.setString(3,email);

            int ack=statement.executeUpdate();
            if (ack==0){
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean updateCustomerPhone(String phone,int customer_id,String email){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE customer set phone=? where customer_id=? and email=?;");
            statement.setString(1,phone);
            statement.setInt(2,customer_id);
            statement.setString(3,email);

            int ack=statement.executeUpdate();
            if (ack==0){
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean updateCustomerAddress(String address,int customer_id,String email){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE customer set address=? where customer_id=? and email=?;");
            statement.setString(1,address);
            statement.setInt(2,customer_id);
            statement.setString(3,email);

            int ack=statement.executeUpdate();
            if (ack==0){
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean updateCustomerEmail(String new_email,int customer_id,String email){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE customer set email=? where customer_id=? and email=?;");
            statement.setString(1,new_email);
            statement.setInt(2,customer_id);
            statement.setString(3,email);

            int ack=statement.executeUpdate();
            if (ack==0){
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

    public boolean deleteCustomer(int customer_id,String email){
        try {
            Connection connection=mangement.formConnection();
    //          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("DELETE from customer where customer_id=? and email=?;");
            statement.setInt(1,customer_id);
            statement.setString(2,email);

            int ack=statement.executeUpdate();
            if (ack==0){
                connection.rollback();
                return false;
            }
            connection.commit();
            connection.close();
            return true;
        } catch (SQLException e) {

        }
        return false;
    }

// Retrieval query
    public Customer getCustomerById(int customer_id){
        try {
            Connection connection=mangement.formConnection();
            //          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("SELECT * from customer where customer_id=?;");
            statement.setInt(1,customer_id);
            ResultSet set=statement.executeQuery();
            Customer customer=null;
            if (set.next()){
                int customerId= set.getInt("customer_id");
                String name= set.getString("name");
                String email= set.getString("email");
                String phone=set.getString("phone");
                String address= set.getString("address");
                Timestamp timestamp=set.getTimestamp("created_at");
                LocalDateTime created_at=timestamp.toLocalDateTime();
                customer=new Customer(customer_id,name,email,phone,address,created_at);
            }
            connection.commit();
            connection.close();
            return customer;
        } catch (SQLException e) {

        }
        return null;
    }

    public Customer getCustomerByEmail(String  email){
        try {
            Connection connection=mangement.formConnection();
            //          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("SELECT * from customer where email=?;");
            statement.setString(1,email);
            ResultSet set=statement.executeQuery();
            Customer customer=null;
            if (set.next()){
                int customerId= set.getInt("customer_id");
                String name= set.getString("name");

                String phone=set.getString("phone");
                String address= set.getString("address");
                Timestamp timestamp=set.getTimestamp("created_at");
                LocalDateTime created_at=timestamp.toLocalDateTime();
                customer=new Customer(customerId,name,email,phone,address,created_at);
            }

            connection.commit();
            connection.close();
            return customer;
        } catch (SQLException e) {

        }
        return null;
    }

    public boolean getCustomerByEmailConfirm(String  email){
        try {
            Connection connection=mangement.formConnection();
            //          Updation  Query
            PreparedStatement statement=connection.prepareStatement("SELECT * from customer where email=?;");
            statement.setString(1,email);
            ResultSet set=statement.executeQuery();
            Customer customer=null;
            if (set.next()){
                return true;
            }
        } catch (SQLException e) {

        }
        return false;
    }



    public List<Customer> getCustomerByName(String  name){
        List<Customer> list=new ArrayList<>();
        try {
            Connection connection=mangement.formConnection();
            PreparedStatement statement=connection.prepareStatement("SELECT * from customer where name=?;");
            statement.setString(1,name);
            ResultSet set=statement.executeQuery();
            while (set.next())
            {
                int customerId = set.getInt("customer_id");
                String name1 = set.getString("name");
                String email = set.getString("email");
                String phone = set.getString("phone");
                String address = set.getString("address");
                Timestamp timestamp = set.getTimestamp("created_at");
                LocalDateTime created_at = timestamp.toLocalDateTime();
                Customer customer = new Customer(customerId, name1, email, phone, address, created_at);
                list.add(customer);
            }
            connection.commit();
            connection.close();
            return list;
        } catch (SQLException e) {

        }
        return list;
    }
    public Customer getCustomerByPhone(String  phone){
        try {
            Connection connection=mangement.formConnection();
            //          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("SELECT * from customer where phone=?;");
            statement.setString(1,phone);
            ResultSet set=statement.executeQuery();
            Customer customer=null;
            if (set.next()){
                int customerId= set.getInt("customer_id");
                String name= set.getString("name");
                String email=set.getString("email");
                String address= set.getString("address");
                Timestamp timestamp=set.getTimestamp("created_at");
                LocalDateTime created_at=timestamp.toLocalDateTime();
                customer=new Customer(customerId,name,phone,phone,address,created_at);
            }

            connection.commit();
            connection.close();
            return customer;
        } catch (SQLException e) {

        }
        return null;
    }

    public List<Customer> getAllCustomer(){
        try {
            Connection connection=mangement.formConnection();
            //          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("SELECT * from customer ;");
            ResultSet set=statement.executeQuery();
            List<Customer>list=new ArrayList<>();
            if (set.next()){
                int customer_id= set.getInt("customer_id");
                String name= set.getString("name");
                String email= set.getString("email");
                String phone=set.getString("phone");
                String address= set.getString("address");
                Timestamp timestamp=set.getTimestamp("created_at");
                LocalDateTime created_at=timestamp.toLocalDateTime();
                Customer customer=new Customer(customer_id,name,email,phone,address,created_at);
                list.add(customer);
            }
            connection.commit();
            connection.close();
            return list;
        } catch (SQLException e) {

        }
        return null;
    }
    public List<BookingHistoryDTO> getCustomerBookingHistory(int userId){
        try {
            Connection connection=mangement.formConnection();
            //          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("SELECT \n" +
                    "    b.booking_id,\n" +
                    "    b.customer_id,\n" +
                    "    c.name AS customer_name,\n" +
                    "    r.room_number,\n" +
                    "    r.type AS room_type,\n" +
                    "    b.check_in,\n" +
                    "    b.check_out,\n" +
                    "    b.total_amount,\n" +
                    "    b.booking_status,\n" +
                    "    p.amount AS payment_amount,\n" +
                    "    p.payment_mode,\n" +
                    "    p.payment_status,\n" +
                    "    p.payment_date\n" +
                    "FROM booking b\n" +
                    "JOIN customer c ON b.customer_id = c.customer_id\n" +
                    "JOIN room r ON b.room_id = r.room_id\n" +
                    "LEFT JOIN payment p ON b.booking_id = p.booking_id\n" +
                    "WHERE b.customer_id = ? \n" +
                    "ORDER BY b.check_in DESC;");
            statement.setInt(1,userId);
            ResultSet set=statement.executeQuery();
            List<BookingHistoryDTO> bookingList=new ArrayList<>();
            while (set.next()){
                int booking_id=set.getInt("booking_id");
                int customer_id=set.getInt("customer_id");

                String customer_name=set.getString("customer_name");
                String room_number=set.getString("room_number");

                String type=set.getString("room_type");
                RoomType room_type=RoomType.setValue(type);

                Date check_in=set.getDate("check_in");
                Date  check_out=set.getDate("check_out");

                double total_amount=set.getDouble("total_amount");

                String booking_status=set.getString("booking_status");
                BookingStatus status=BookingStatus.setValue(booking_status);

                double payment_amount=set.getDouble("payment_amount");
                String payment_mode=set.getString("payment_mode");
                PaymentMode mode=PaymentMode.setValue(payment_mode);

                String payment_status=set.getString("payment_status");
                PaymentStatus paymentStatus=PaymentStatus.setValue(payment_status);

                Timestamp timestamp=set.getTimestamp("payment_date");
                LocalDateTime payment_date=timestamp.toLocalDateTime();

                BookingHistoryDTO booking=new BookingHistoryDTO(booking_id,customer_id,customer_name,room_number,room_type,check_in,check_out,total_amount,status,payment_amount,mode,paymentStatus,payment_date);
                bookingList.add(booking);
            }
            return bookingList;
        }
        catch (SQLException e){

        }
        return null;
    }


}
