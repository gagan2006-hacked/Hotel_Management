package Data.Insertion;

import Data.Enums.RoomStatus;
import Data.Enums.RoomType;

/*
| Field           | Type                                     | Description     |
| --------------- | ---------------------------------------- | --------------- |
| room_id (PK)    | INT                                      | Room ID         |
| room_number     | VARCHAR(10)                              | Room number     |
| type            | ENUM('single','double','suite','deluxe') | Type of room    |
| price_per_night | DECIMAL(10,2)                            | Price per night |
| status          | ENUM('available','booked','maintenance') | Room status     |
* */
public class Room {
    private int roomId;
    private String room_number;
    private RoomType type;
    private double price ;
    private RoomStatus status;

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public void setType(RoomType type) {
        this.type = type;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public Room(String room_number, RoomType type, double price, RoomStatus status) {
        this.room_number = room_number;
        this.type = type;
        this.price = price;
        this.status=status;
    }

    public Room(int roomId, String room_number, RoomType type, double price, RoomStatus status) {
        this.roomId = roomId;
        this.room_number = room_number;
        this.type = type;
        this.price = price;
        this.status = status;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getRoom_number() {
        return room_number;
    }

    public RoomType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public RoomStatus getStatus() {
        return status;
    }
}
