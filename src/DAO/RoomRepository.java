package DAO;


import DBConnection.ConnectionMangement;
import Data.Enums.RoomStatus;
import Data.Enums.RoomType;
import Data.Insertion.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomRepository {
    private final ConnectionMangement mangement;
    public RoomRepository() {
        this.mangement = new ConnectionMangement();
    }

    public boolean addRoom(Room room){
        try {
            Connection connection=mangement.formConnection();
//          insert Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("INSERT INTO room (room_number,type,price_per_night,status) value(?,?,?,?);");
            statement.setString(1,room.getRoom_number());
            statement.setString(2,room.getType().getValue());
            statement.setDouble(3,room.getPrice());
            statement.setString(4,room.getStatus().getValue());
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

    public boolean updateRoomPrice(String room_number,int room_id,double price){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE room set price_per_night=? where room_id=? or room_number=? ;");
            statement.setDouble(1,price);
            statement.setString(3,room_number);
            statement.setInt(2,room_id);
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

    public boolean updateRoomNumber(String prev_room_number,int room_id,String new_Room_number){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE room set room_number=? where room_id=? or room_number=? ;");
            statement.setString(1,new_Room_number);
            statement.setInt(2,room_id);
            statement.setString(3,prev_room_number);
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
    public boolean updateRoomType(String room_number, int room_id, RoomType type){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE room set type=? where room_id=? or room_number=? ;");
            statement.setString(1,type.getValue());
            statement.setInt(2,room_id);
            statement.setString(3,room_number);
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
    public boolean updateRoomStatus(String room_number, int room_id, RoomStatus status){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("UPDATE room set status=? where room_id=? or room_number=? ;");
            statement.setString(1,status.getValue());
            statement.setInt(2,room_id);
            statement.setString(3,room_number);
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
    public boolean deleteRoom(String room_number,int room_id){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            connection.setAutoCommit(false);
            PreparedStatement statement=connection.prepareStatement("Delete from room where room_id=? and room_number=? ; ");
            statement.setInt(1,room_id);
            statement.setString(2,room_number);
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
//    Retrieval
    public Room getRoomById(int roomId){
        try {
            Connection connection=mangement.formConnection();
//          Retrieval  Query
            PreparedStatement statement=connection.prepareStatement("Select * from room where room_id=? ;");
            statement.setInt(1,roomId);
            ResultSet set= statement.executeQuery();
            Room room=null;
            if (set.next()) {
                int room_id = set.getInt("room_id");
                String room_number = set.getString("room_number");
                String type = set.getString("type");
                RoomType roomType=RoomType.setValue(type);
                double price = set.getDouble("price_per_night");
                String status = set.getString("status");
                RoomStatus roomStatus=RoomStatus.setValue(status);
                room=new Room(room_id,room_number,roomType,price,roomStatus);
            }
            connection.close();
            return room;
        } catch (SQLException e) {

        }
        return null;
    }

    public Room getRoomByRoomNumber(String room_number){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            PreparedStatement statement=connection.prepareStatement("Select * from room where room_number=? ;");
            statement.setString(1,room_number);
            ResultSet set= statement.executeQuery();
            Room room=null;
            if (set.next()) {
                int room_id = set.getInt("room_id");
                String roomnumber = set.getString("room_number");
                String type = set.getString("type");
                RoomType roomType=RoomType.setValue(type);
                double price = set.getDouble("price_per_night");
                String status = set.getString("status");
                RoomStatus roomStatus=RoomStatus.setValue(status);
                room=new Room(room_id,roomnumber,roomType,price,roomStatus);
            }
            connection.close();

            return room;
        } catch (SQLException e) {

        }
        return null;
    }

    public List<Room> getRoomByType(RoomType type){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            PreparedStatement statement=connection.prepareStatement("Select * from room  where type=? ;");
            statement.setString(1,type.getValue());

            ResultSet set= statement.executeQuery();
            List<Room> list=new ArrayList<>();
            while (set.next()) {
                int room_id = set.getInt("room_id");
                String roomnumber = set.getString("room_number");

                double price = set.getDouble("price_per_night");
                String status = set.getString("status");
                RoomStatus roomStatus=RoomStatus.setValue(status);
                Room room=new Room(room_id,roomnumber,type,price,roomStatus);
                list.add(room);
            }
            connection.close();

            return list;
        } catch (SQLException e) {

        }
        return new ArrayList<>();
    }
    public List<Room> getRoomByPrice(double price,int order){
        if(order<0 || order>1){
            order=0;
        }
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            PreparedStatement statement=connection.prepareStatement("Select * from room  where price_per_night>=? order by (price) ? ;");
            statement.setDouble(1,price);
            if (order==1){
                statement.setString(2,"DESC");
            }
            ResultSet set= statement.executeQuery();
            List<Room> list=new ArrayList<>();
            while (set.next()) {
                int room_id = set.getInt("room_id");
                String roomnumber = set.getString("room_number");
                String type = set.getString("type");
                RoomType roomType=RoomType.setValue(type);
                double r_price = set.getDouble("price_per_night");
                String status = set.getString("status");
                RoomStatus roomStatus=RoomStatus.setValue(status);
                Room room=new Room(room_id,roomnumber,roomType,r_price,roomStatus);
                list.add(room);
            }
            connection.close();

            return list;
        } catch (SQLException e) {

        }
        return new ArrayList<>();
    }

    public List<Room> getRoomByStatus(RoomStatus status){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            PreparedStatement statement=connection.prepareStatement("Select * from room where type=? ;");
            statement.setString(1,status.getValue());

            ResultSet set= statement.executeQuery();
            List<Room> list=new ArrayList<>();
            while (set.next()) {
                int room_id = set.getInt("room_id");
                String roomnumber = set.getString("room_number");
                String type=set.getString("type");
                RoomType roomType=RoomType.setValue(type);
                double price = set.getDouble("price_per_night");
                Room room=new Room(room_id,roomnumber,roomType,price,status);
                list.add(room);
            }
            connection.close();

            return list;
        } catch (SQLException e) {

        }
        return new ArrayList<>();
    }

    public List<Room> getAllRoom(){
        try {
            Connection connection=mangement.formConnection();
//          Updation  Query
            PreparedStatement statement=connection.prepareStatement("Select * from room order by (room_number);");
            ResultSet set= statement.executeQuery();
            List<Room> list=new ArrayList<>();
            while (set.next()) {
                int room_id = set.getInt("room_id");
                String roomnumber = set.getString("room_number");
                String type=set.getString("type");
                RoomType roomType=RoomType.setValue(type);
                double price = set.getDouble("price_per_night");
                RoomStatus status=RoomStatus.setValue(set.getString("status"));
                Room room=new Room(room_id,roomnumber,roomType,price,status);
                list.add(room);
            }
            connection.close();

            return list;
        } catch (SQLException e) {

        }
        return new ArrayList<>();
    }

}
