package Data.Enums;

public enum RoomType {
    Single("single"),
    Double("double"),
    Suite("suite"),
    Deluxe("deluxe");
    private String value;

    RoomType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public static RoomType setValue(String  type){
        RoomType roomType;
        if (type.equalsIgnoreCase(RoomType.Single.getValue())||
                type.equalsIgnoreCase(RoomType.Double.getValue())||
                type.equalsIgnoreCase(RoomType.Suite.getValue())||
                type.equalsIgnoreCase(RoomType.Deluxe.getValue()))
        {
            if (type.equalsIgnoreCase(RoomType.Single.getValue())){
                roomType=RoomType.Single;
            } else if (type.equalsIgnoreCase(RoomType.Double.getValue())) {
                roomType=RoomType.Double;
            } else if (type.equalsIgnoreCase(RoomType.Suite.getValue())) {
                roomType=RoomType.Suite;
            }else {
                roomType=RoomType.Deluxe;
            }
            return roomType;
        }
        return null;
    }
}
