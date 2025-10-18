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
}
