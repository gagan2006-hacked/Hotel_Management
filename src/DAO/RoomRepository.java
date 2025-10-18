package DAO;


import DBConnection.ConnectionMangement;
import Data.Insertion.Room;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoomRepository {
    private ConnectionMangement mangement;
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
            return true;
        } catch (SQLException e) {

        }
        return false;
    }
}
