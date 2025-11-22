package Data.Enums;

public enum RoomStatus {
    Available("available"),Booked("booked"),Maintenance("maintenance");
    private String value;

    RoomStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static RoomStatus setValue(String status){
       RoomStatus roomStatus;
        if (status.equalsIgnoreCase(RoomStatus.Available.getValue())||
                status.equalsIgnoreCase(RoomStatus.Booked.getValue())||
                status.equalsIgnoreCase(RoomStatus.Maintenance.getValue()))
        {
            if (status.equalsIgnoreCase(RoomStatus.Available.getValue())){
               roomStatus =RoomStatus.Available;
            } else if (status.equalsIgnoreCase(RoomStatus.Booked.getValue())) {
                roomStatus=RoomStatus.Booked;
            } else  {
                roomStatus=RoomStatus.Maintenance;
            }
            return roomStatus;
        }
        return null;
    }
}
