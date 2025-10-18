package Data.Enums;

public enum RoomType {
    Single("single"),
    Double("double"),
    Suite("sutie"),
    Deluxe("deluxe");
    private String value;

    RoomType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
